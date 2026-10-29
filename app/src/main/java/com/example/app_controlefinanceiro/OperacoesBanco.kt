package com.example.app_controlefinanceiro

import android.util.Log

suspend fun inserirTransacao(descricao: String, valor: Double, categoriaId: Int, data: String, despesa: Boolean, transacaoDao: TransacaoDao) {
    try {
        transacaoDao.inserir(Transacao(0, descricao, valor, categoriaId, data, despesa))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun buscarTransacao(transacaoDao: TransacaoDao): List<Transacao> {
    try {
        return transacaoDao.buscarTransacao()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun deletarTransacao(transacao: Transacao, transacaoDao: TransacaoDao) {
    try {
        transacaoDao.removerTransacao(transacao)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "Msg ${e.message}")
    }
}

suspend fun atualizarTransacao(transacao: Transacao, transacaoDao: TransacaoDao) {
    try {
        transacaoDao.atualizarTransacao(transacao)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Msg ${e.message}")
    }
}
