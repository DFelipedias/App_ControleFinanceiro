package com.example.app_controlefinanceiro.ui

object Rotas {
    const val INICIO = "inicio"
    const val EXTRATO = "extrato"
    const val NOVA_TRANSACAO = "nova_transacao"
    const val DETALHES_TRANSACAO = "transacao/{id}"
    const val CATEGORIAS = "categorias"
    const val NOVA_CATEGORIA = "nova_categoria"
    const val DETALHES_CATEGORIA = "categoria/{id}"
    fun transacao(id: Int) = "transacao/$id"
    fun categoria(id: Int) = "categoria/$id"
}
