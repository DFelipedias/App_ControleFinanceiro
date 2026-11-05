package com.example.app_controlefinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_controlefinanceiro.data.local.Categoria
import com.example.app_controlefinanceiro.data.local.Transacao
import com.example.app_controlefinanceiro.data.repository.CategoriaRepository
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransacaoViewModel(
    private val transacaoRepository: TransacaoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransacaoUiState())
    val uiState: StateFlow<TransacaoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            carregarDadosIniciais()
            combine(transacaoRepository.buscarTransacao(), categoriaRepository.buscarTodas()) { transacoes, categorias ->
                transacoes to categorias
            }.collect { (transacoes, categorias) ->
                _uiState.update { estado ->
                    val atualizado = estado.copy(transacoes = transacoes, categorias = categorias, carregando = false)
                    val selecionada = atualizado.transacaoSelecionada
                    if (estado.transacaoSelecionada == null && selecionada != null) {
                        atualizado.copy(
                            descricaoEdicao = selecionada.descricao,
                            valorEdicao = selecionada.valor.toString(),
                            dataEdicao = selecionada.data
                        )
                    } else atualizado
                }
            }
        }
    }

    private suspend fun carregarDadosIniciais() {
        var categoriasBanco = categoriaRepository.buscarTodas().first()
        if (categoriasBanco.isEmpty()) {
            val padrao = listOf(
                Categoria(nome = "Alimentação", descricao = "Refeições e compras de mercado"),
                Categoria(nome = "Transporte", descricao = "Combustível, ônibus e aplicativos"),
                Categoria(nome = "Saúde", descricao = "Farmácia e cuidados pessoais"),
                Categoria(nome = "Renda", descricao = "Salário e outras entradas")
            )
            padrao.forEach { categoriaRepository.inserir(it) }
            categoriasBanco = categoriaRepository.buscarTodas().first()
        }

        val transacoesBanco = transacaoRepository.buscarTransacao().first()
        if (transacoesBanco.isEmpty() && categoriasBanco.isNotEmpty()) {
            val catRenda = categoriasBanco.find { it.nome == "Renda" }?.id ?: categoriasBanco.first().id
            val catAlim = categoriasBanco.find { it.nome == "Alimentação" }?.id ?: categoriasBanco.first().id
            val catTransp = categoriasBanco.find { it.nome == "Transporte" }?.id ?: categoriasBanco.first().id
            val catSaude = categoriasBanco.find { it.nome == "Saúde" }?.id ?: categoriasBanco.first().id
            val padraoTransacoes = listOf(
                Transacao(descricao = "Salário", valor = 3500.0, categoriaId = catRenda, data = "01/10/2026", despesa = false),
                Transacao(descricao = "Supermercado", valor = 450.0, categoriaId = catAlim, data = "02/10/2026", despesa = true),
                Transacao(descricao = "Combustível", valor = 180.0, categoriaId = catTransp, data = "03/10/2026", despesa = true),
                Transacao(descricao = "Almoço Restaurante", valor = 38.50, categoriaId = catAlim, data = "04/10/2026", despesa = true),
                Transacao(descricao = "Farmácia", valor = 27.90, categoriaId = catSaude, data = "04/10/2026", despesa = true)
            )
            padraoTransacoes.forEach { transacaoRepository.inserir(it) }
        }
    }

    fun iniciarNovaTransacao() {
        _uiState.update {
            it.copy(valor = "", descricao = "", data = "", despesa = true, categoriaId = it.categorias.firstOrNull()?.id, mensagem = null)
        }
    }

    fun iniciarExtrato() {
        _uiState.update { it.copy(pesquisa = "", categoriaFiltroId = null) }
    }

    fun mudarValor(valor: String) { _uiState.update { it.copy(valor = valor) } }
    fun mudarDescricao(descricao: String) { _uiState.update { it.copy(descricao = descricao) } }
    fun mudarData(data: String) { _uiState.update { it.copy(data = data) } }
    fun mudarDespesa(despesa: Boolean) { _uiState.update { it.copy(despesa = despesa) } }
    fun mudarCategoria(id: Int) { _uiState.update { it.copy(categoriaId = id) } }
    fun mudarPesquisa(pesquisa: String) { _uiState.update { it.copy(pesquisa = pesquisa) } }
    fun mudarFiltroCategoria(id: Int?) { _uiState.update { it.copy(categoriaFiltroId = id) } }

    fun adicionarTransacao(): Boolean {
        val estado = _uiState.value
        val numero = estado.valor.replace(",", ".").toDoubleOrNull()
        val categoriaId = estado.categoriaId
        if (estado.descricao.isBlank() || numero == null || !numero.isFinite() || numero <= 0 || categoriaId == null ||
            estado.categorias.none { it.id == categoriaId } || !estado.data.matches(Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}"))) {
            _uiState.update { it.copy(mensagem = "Preencha a descrição, o valor positivo, a categoria e a data no formato indicado.") }
            return false
        }
        _uiState.update { it.copy(mensagem = null) }
        viewModelScope.launch {
            transacaoRepository.inserir(
                Transacao(descricao = estado.descricao.trim(), valor = numero, categoriaId = categoriaId, data = estado.data.trim(), despesa = estado.despesa)
            )
        }
        return true
    }

    fun selecionarTransacao(id: Int?) {
        _uiState.update {
            val item = it.transacoes.find { transacao -> transacao.id == id }
            it.copy(transacaoId = id, editando = false, descricaoEdicao = item?.descricao ?: "", valorEdicao = item?.valor?.toString() ?: "", dataEdicao = item?.data ?: "", mensagem = null)
        }
    }

    fun alternarEdicao() { _uiState.update { it.copy(editando = !it.editando) } }
    fun mudarDescricaoEdicao(descricao: String) { _uiState.update { it.copy(descricaoEdicao = descricao) } }
    fun mudarValorEdicao(valor: String) { _uiState.update { it.copy(valorEdicao = valor) } }
    fun mudarDataEdicao(data: String) { _uiState.update { it.copy(dataEdicao = data) } }

    fun editarTransacao(): Boolean {
        val estado = _uiState.value
        val item = estado.transacaoSelecionada ?: return false
        val numero = estado.valorEdicao.replace(",", ".").toDoubleOrNull()
        if (estado.descricaoEdicao.isBlank() || numero == null || !numero.isFinite() || numero <= 0 ||
            !estado.dataEdicao.matches(Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}"))) {
            _uiState.update { it.copy(mensagem = "Confira a descrição, o valor e a data.") }
            return false
        }
        viewModelScope.launch {
            transacaoRepository.atualizarTransacao(item.copy(descricao = estado.descricaoEdicao.trim(), valor = numero, data = estado.dataEdicao.trim()))
        }
        _uiState.update { it.copy(editando = false, mensagem = null) }
        return true
    }

    fun removerTransacao(item: Transacao) {
        viewModelScope.launch { transacaoRepository.removerTransacao(item) }
    }
}
