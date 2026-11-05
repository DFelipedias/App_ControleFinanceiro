package com.example.app_controlefinanceiro.viewmodel

import com.example.app_controlefinanceiro.data.local.Categoria
import com.example.app_controlefinanceiro.data.local.Transacao
import java.text.NumberFormat
import java.util.Locale

data class TransacaoUiState(
    val transacoes: List<Transacao> = emptyList(),
    val categorias: List<Categoria> = emptyList(),
    val carregando: Boolean = true,
    val valor: String = "",
    val descricao: String = "",
    val data: String = "",
    val despesa: Boolean = true,
    val categoriaId: Int? = null,
    val pesquisa: String = "",
    val categoriaFiltroId: Int? = null,
    val transacaoId: Int? = null,
    val editando: Boolean = false,
    val descricaoEdicao: String = "",
    val valorEdicao: String = "",
    val dataEdicao: String = "",
    val mensagem: String? = null
) {
    val receitas: Double get() = transacoes.filter { !it.despesa }.sumOf { it.valor }
    val despesas: Double get() = transacoes.filter { it.despesa }.sumOf { it.valor }
    val saldo: Double get() = receitas - despesas
    val ultimasTransacoes: List<Transacao> get() = transacoes.reversed().take(3)
    val transacaoSelecionada: Transacao? get() = transacoes.find { it.id == transacaoId }

    val resultado: List<Transacao>
        get() {
            val formato = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            return transacoes.filter {
                (it.descricao.contains(pesquisa, true) || formato.format(it.valor).contains(pesquisa, true)) &&
                    (categoriaFiltroId == null || it.categoriaId == categoriaFiltroId)
            }.reversed()
        }

    fun nomeCategoria(id: Int) = categorias.find { it.id == id }?.nome ?: "Sem categoria"
}
