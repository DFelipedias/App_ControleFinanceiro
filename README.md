# Controle Financeiro

Aplicativo de controle financeiro feito em Kotlin com Jetpack Compose. Permite registrar receitas e despesas, organizar os registros por categoria e acompanhar o saldo.

## Integrantes

Daniel, Leonardo e Marcelo.

Na primeira entrega, Daniel ficou com a Visão Geral, Leonardo com o Extrato e Marcelo com Nova Transação.

## Como abrir e rodar

1. Clone este repositório e abra no Android Studio a pasta que contém `settings.gradle.kts`.
2. Para acompanhar o Trabalho 4 antes da integração na principal, selecione a branch `trabalho4-mvvm`.
3. Use o JDK 21 para o Gradle, conforme a configuração de `gradle/gradle-daemon-jvm.properties`.
4. Instale o SDK solicitado pelo projeto e aguarde a sincronização. O projeto usa AGP 9.0.1 e compile SDK 36.1.
5. Selecione um emulador ou celular com Android 7.0 (API 24) ou superior e execute o módulo `app`.

Também é possível compilar pelo terminal:

- Windows: `.\gradlew.bat :app:assembleDebug`
- Linux/macOS: `./gradlew :app:assembleDebug`

## Telas

| Tela | Função |
|---|---|
| Visão Geral | Mostra saldo, receitas, despesas e últimas transações |
| Extrato | Lista transações, com busca, filtro por categoria e remoção |
| Nova Transação | Cadastra receita ou despesa |
| Detalhes da Transação | Mostra o registro e permite editar descrição, valor e data |
| Categorias | Lista categorias e permite remover as que não têm transações |
| Nova Categoria | Cadastra nome e descrição |
| Detalhes da Categoria | Mostra totais e transações relacionadas |

Os dados ficam salvos no banco local com Room e continuam disponíveis depois de fechar o app. Os registros iniciais são exemplos. O aplicativo não tem integração bancária.

## Trabalho 4 — MVVM

O Trabalho 4 foi feito no mesmo repositório do Trabalho 3. A mudança foi na organização do código. As telas, os campos e as funções do aplicativo foram mantidos.

### Como estava e o que mudou

No Trabalho 3, o `FinanceiroViewModel` concentrava as categorias e as transações e acessava os DAOs diretamente. As consultas retornavam listas, então era necessário buscar os dados novamente depois de cadastrar, editar ou remover um registro. Parte do estado e das validações dos formulários ficava nas telas, em variáveis com `remember`.

A reorganização foi feita por etapas, com um commit para cada mudança e teste no Android Studio antes de continuar.

| Antes | Depois |
|---|---|
| Arquivos de tela e banco misturados no pacote principal | Arquivos separados por responsabilidade |
| Um `FinanceiroViewModel` para as duas entidades | `TransacaoViewModel` e `CategoriaViewModel` |
| ViewModel acessando os DAOs | Acesso aos dados pelos Repositories |
| Consultas retornando `List` | Consultas retornando `Flow<List<...>>` |
| Campos e validações dentro das telas | Estado agrupado em UiState e validações nos ViewModels |
| ViewModel criado sem Factory própria | Repositories recebidos pelo construtor, usando Factories |

### Divisão dos pacotes

| Pacote | Arquivos e responsabilidade |
|---|---|
| `ui` | Telas, componentes, navegação e rotas. Exibe o estado e chama as ações dos ViewModels |
| `data.local` | Entities `Transacao` e `Categoria`, os dois DAOs e `AppDatabase`. Faz a persistência com Room |
| `data.repository` | `TransacaoRepository` e `CategoriaRepository`. Repassam as operações para os DAOs |
| `viewmodel` | Os dois ViewModels, seus UiStates e suas Factories |

A `MainActivity` prepara os Repositories e as Factories antes de abrir a navegação. O tema continua em `ui.theme`.

O ViewModel antigo foi substituído pelos dois novos. As funções auxiliares de `OperacoesBanco`, que não tinham chamadas no aplicativo, foram retiradas. As operações usadas pelo app ficaram nos Repositories e ViewModels.

### Por que usar Repository

O Repository separa o acesso ao banco do restante do aplicativo. Cada um recebe o DAO da sua entidade e oferece as operações de consulta, cadastro, atualização e remoção.

Assim, as telas não precisam conhecer o DAO, e os ViewModels não fazem chamadas diretas a ele. Se a origem dos dados mudar no futuro, essa alteração fica concentrada no Repository.

No cadastro de uma transação, por exemplo, a tela chama o `TransacaoViewModel`, que chama o `TransacaoRepository`. O Repository usa o `TransacaoDao` para gravar no Room.

### Consultas com Flow

As consultas de listagem ficaram assim:

```kotlin
fun buscarTransacao(): Flow<List<Transacao>>
fun buscarTodas(): Flow<List<Categoria>>
```

Essas duas funções não usam mais `suspend`. Inserir, atualizar e remover continuam sendo funções suspensas.

Os Repositories repassam os Flows e os ViewModels observam os dados com `collect`. Quando o banco muda, o estado recebe as listas atualizadas. Com isso, não é mais necessário buscar tudo novamente depois de cada operação.

### O que fica em cada UiState

O `TransacaoUiState` reúne o estado usado na Visão Geral, no Extrato, no cadastro e nos detalhes de transação.

| Campos | Motivo |
|---|---|
| `transacoes` e `categorias` | Preencher as listas, os nomes das categorias e os resumos |
| `valor`, `descricao`, `data`, `despesa` e `categoriaId` | Guardar os campos que já existiam no cadastro |
| `pesquisa` e `categoriaFiltroId` | Manter a busca e o filtro do Extrato |
| `transacaoId`, `editando`, `descricaoEdicao`, `valorEdicao` e `dataEdicao` | Identificar o registro aberto e controlar sua edição |
| `carregando` e `mensagem` | Acompanhar a leitura inicial e guardar as mensagens de validação |

Saldo, receitas, despesas, últimas transações e o resultado da busca são calculados a partir desse estado.

O `CategoriaUiState` reúne o estado da listagem, do cadastro e dos detalhes de categoria.

| Campos | Motivo |
|---|---|
| `categorias` e `transacoes` | Listar as categorias, contar os registros e calcular os totais relacionados |
| `nome` e `descricao` | Guardar os campos do formulário |
| `categoriaId` | Identificar a categoria aberta nos detalhes |
| `carregando` e `mensagem` | Acompanhar a leitura inicial e guardar as mensagens de validação |

Os dois ViewModels usam `_uiState` privado com `MutableStateFlow` e expõem `uiState` como `StateFlow`, somente para leitura. As telas usam `collectAsState()` e chamam métodos do ViewModel quando o usuário interage. As gravações ficam dentro de `viewModelScope.launch`.

Os dois ViewModels recebem os dois Repositories porque as informações se relacionam. A transação precisa das categorias, e a categoria usa as transações para mostrar totais e impedir a exclusão de uma categoria em uso.

### Factories

Foram criadas `TransacaoViewModelFactory` e `CategoriaViewModelFactory`, implementando `ViewModelProvider.Factory`.

Cada Factory entrega os Repositories ao seu ViewModel. No `AppNavigation`, eles são obtidos com `viewModel(factory = ...)`. A criação do banco e dos Repositories fica fora dos Composables.

### Testes e dificuldades

Os testes foram feitos no Android Studio após cada etapa. Não foram encontradas funcionalidades quebradas durante a reorganização.

| Verificação | Resultado |
|---|---|
| Compilar e abrir o aplicativo | Funcionou |
| Cadastrar, listar, editar e excluir transações | Funcionou |
| Cancelar a edição de uma transação | Funcionou |
| Pesquisar por nome ou valor e filtrar por categoria | Funcionou |
| Cadastrar categoria e abrir seus detalhes | Funcionou |
| Remover categoria sem transações | Funcionou |
| Bloquear a remoção de categoria com transações | Funcionou |
| Validar campos obrigatórios | Funcionou |
| Atualizar listas e totais após alterações | Funcionou |
| Fechar e reabrir o app mantendo os dados | Funcionou |
| Manter o layout e a navegação existentes | Funcionou |

Na parte de código, os testes passaram sem problemas e não houve dificuldade específica a registrar. Durante a preparação do ambiente, o emulador não iniciava por falta de espaço no disco. Os arquivos do dispositivo virtual foram movidos para outro disco e, após reiniciar o computador, foi possível executar o app.

### Prints e vídeos

Os registros visuais desta etapa ainda precisam ser anexados. Os resultados acima correspondem aos testes realizados, mas esta parte da documentação ainda está pendente.

- [ ] Print dos pacotes expandidos no Android Studio.
- [ ] Prints dos Repositories, DAOs com Flow, UiStates e Factories.
- [ ] Vídeo curto mostrando cadastro, edição, exclusão, busca e filtro de transações.
- [ ] Vídeo curto mostrando cadastro de categoria e bloqueio de exclusão quando ela tem transações.
- [ ] Vídeo mostrando um registro salvo antes e depois de fechar e reabrir o app.

Os arquivos em `docs/PROCESSO.md`, `docs/TESTES.md` e `docs/Documentacao_ControleFinanceiro_V2.docx` são materiais da etapa anterior. O relato do Trabalho 4 está neste README.
