package com.example.app_controlefinanceiro.data.repository

import com.example.app_controlefinanceiro.data.local.Transacao
import com.example.app_controlefinanceiro.data.local.TransacaoDao

class TransacaoRepository(private val dao: TransacaoDao) {

    suspend fun buscarTransacao(): List<Transacao> = dao.buscarTransacao()

    suspend fun inserir(transacao: Transacao) = dao.inserir(transacao)

    suspend fun atualizarTransacao(transacao: Transacao) = dao.atualizarTransacao(transacao)

    suspend fun removerTransacao(transacao: Transacao) = dao.removerTransacao(transacao)
}
