# Controle Financeiro

Continuação do app desenvolvido no Trabalho 1, em Kotlin com Jetpack Compose. O objetivo é registrar receitas e despesas e acompanhar o saldo com navegação real entre as telas.

## Integrantes

Daniel, Leonardo e Marcelo.

Na primeira entrega, Daniel fez a Visão Geral (Main2), Leonardo fez o Extrato e Marcelo fez Nova Transação.

## Como abrir e rodar

1. Clone este repositório ou baixe o ZIP.
2. No Android Studio, escolha Open e selecione a pasta que contém settings.gradle.kts.
3. Use JDK 17 para o Gradle. A configuração foi mantida da primeira entrega: AGP 9.0.1 e SDK 36.1 (API 36, extensão minor 1).
4. Instale o SDK solicitado e aguarde a sincronização do Gradle.
5. Selecione um emulador ou celular com Android 7.0 (API 24) ou superior e execute o módulo app.

Também é possível compilar com `./gradlew :app:assembleDebug` ou, no Windows, `gradlew.bat :app:assembleDebug`.

## Telas

| Tela | Função |
|---|---|
| Visão Geral | Saldo calculado, receitas, despesas e últimas transações |
| Extrato | Lista com busca, filtro por categoria, remoção e acesso aos detalhes |
| Nova Transação | Cadastro de receita ou despesa |
| Detalhes da Transação | Dados do item selecionado e edição de descrição, valor e data |
| Categorias | Lista de categorias com acesso aos detalhes e remoção |
| Nova Categoria | Cadastro de nome e descrição |
| Detalhes da Categoria | Totais calculados e transações relacionadas |

Os dados iniciais são exemplos. As alterações ficam apenas na memória; não há banco de dados nem integração bancária.

## Organização

- `MainActivity.kt`: abre a navegação.
- `AppNavigation.kt`: único NavHost central, com sete destinos.
- `Rotas.kt`: constantes e rotas dos detalhes por ID.
- `FinanceiroViewModel.kt`: listas reativas e operações sobre os dados.
- `Modelos.kt`: data classes Transacao e Categoria.
- `Componentes.kt`: barra inferior, barra superior, cards e campos compartilhados.
- Arquivos `Tela*.kt`: conteúdo de cada tela.

A navegação e o ViewModel seguem o exemplo da Aula 15. As listas reativas e as operações de inclusão/remoção seguem a ideia da Aula 12. LazyColumn e data classes atendem aos requisitos expressos no Trabalho 2.

## Regras

Valores devem ser positivos; receita/despesa define o efeito no saldo. A data é um campo de texto validado pelo formato dd/mm/aaaa, sem validação completa do calendário. Categorias com movimentações vinculadas não podem ser removidas. Nomes de categorias não podem se repetir. Remover uma transação atualiza os resumos.

## Processo e validação

Consulte [docs/PROCESSO.md](docs/PROCESSO.md), [a documentação em Word](docs/Documentacao_ControleFinanceiro_V2.docx) e [docs/TESTES.md](docs/TESTES.md).

**Pendente antes da entrega:** compilar e testar no Android Studio, anexar prints ou vídeo reais das etapas e completar os relatos do trio. Não considerar a documentação concluída enquanto essas evidências estiverem ausentes.
