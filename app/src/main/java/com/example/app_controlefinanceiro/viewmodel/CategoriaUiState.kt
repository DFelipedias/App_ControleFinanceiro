package com.example.app_controlefinanceiro.viewmodel

import com.example.app_controlefinanceiro.data.local.Categoria
import com.example.app_controlefinanceiro.data.local.Transacao

data class CategoriaUiState(
    val categorias: List<Categoria> = emptyList(),
    val transacoes: List<Transacao> = emptyList(),
    val carregando: Boolean = true,
    val nome: String = "",
    val descricao: String = "",
    val categoriaId: Int? = null,
    val mensagem: String? = null
) {
    val categoriaSelecionada: Categoria? get() = categorias.find { it.id == categoriaId }
    val movimentacoes: List<Transacao> get() = transacoes.filter { it.categoriaId == categoriaId }.reversed()
    val despesas: Double get() = movimentacoes.filter { it.despesa }.sumOf { it.valor }
    val receitas: Double get() = movimentacoes.filter { !it.despesa }.sumOf { it.valor }
    val resultado: Double get() = receitas - despesas

    fun quantidadeTransacoes(id: Int) = transacoes.count { it.categoriaId == id }
    fun nomeCategoria(id: Int) = categorias.find { it.id == id }?.nome ?: "Sem categoria"
}
