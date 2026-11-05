package com.example.app_controlefinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_controlefinanceiro.data.local.Categoria
import com.example.app_controlefinanceiro.data.repository.CategoriaRepository
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriaViewModel(
    private val categoriaRepository: CategoriaRepository,
    private val transacaoRepository: TransacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaUiState())
    val uiState: StateFlow<CategoriaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(categoriaRepository.buscarTodas(), transacaoRepository.buscarTransacao()) { categorias, transacoes ->
                categorias to transacoes
            }.collect { (categorias, transacoes) ->
                _uiState.update { it.copy(categorias = categorias, transacoes = transacoes, carregando = false) }
            }
        }
    }

    fun iniciarNovaCategoria() {
        _uiState.update { it.copy(nome = "", descricao = "", mensagem = null) }
    }

    fun mudarNome(nome: String) { _uiState.update { it.copy(nome = nome) } }
    fun mudarDescricao(descricao: String) { _uiState.update { it.copy(descricao = descricao) } }
    fun selecionarCategoria(id: Int?) { _uiState.update { it.copy(categoriaId = id) } }

    fun adicionarCategoria(): Boolean {
        val estado = _uiState.value
        val mensagem = when {
            estado.nome.isBlank() || estado.descricao.isBlank() -> "Preencha o nome e a descrição."
            estado.categorias.any { it.nome.equals(estado.nome.trim(), true) } -> "Essa categoria já existe."
            else -> null
        }
        _uiState.update { it.copy(mensagem = mensagem) }
        if (mensagem != null) return false
        viewModelScope.launch {
            categoriaRepository.inserir(Categoria(nome = estado.nome.trim(), descricao = estado.descricao.trim()))
        }
        return true
    }

    fun removerCategoria(item: Categoria): Boolean {
        if (_uiState.value.transacoes.any { it.categoriaId == item.id }) {
            _uiState.update { it.copy(mensagem = "Remova primeiro as transações desta categoria.") }
            return false
        }
        _uiState.update { it.copy(mensagem = null) }
        viewModelScope.launch { categoriaRepository.removerCategoria(item) }
        return true
    }
}
