package com.example.app_controlefinanceiro.data.local

import android.util.Log
import kotlinx.coroutines.flow.first
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository

suspend fun inserirTransacao(descricao: String, valor: Double, categoriaId: Int, data: String, despesa: Boolean, transacaoRepository: TransacaoRepository) {
    try {
        transacaoRepository.inserir(Transacao(0, descricao, valor, categoriaId, data, despesa))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun buscarTransacao(transacaoRepository: TransacaoRepository): List<Transacao> {
    try {
        return transacaoRepository.buscarTransacao().first()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun deletarTransacao(transacao: Transacao, transacaoRepository: TransacaoRepository) {
    try {
        transacaoRepository.removerTransacao(transacao)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "Msg ${e.message}")
    }
}

suspend fun atualizarTransacao(transacao: Transacao, transacaoRepository: TransacaoRepository) {
    try {
        transacaoRepository.atualizarTransacao(transacao)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Msg ${e.message}")
    }
}
