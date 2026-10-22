package com.example.app_controlefinanceiro

import android.util.Log

suspend fun inserirTransacao(nome: String, desc: String, valor: Double, trasacoesDao: TransacoesDao) {
    try {
        TransacoesDao.inserir(Transacao(0, nome, valor, ))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun buscarFilmes(trasacoesDao: TransacoesDao): List<Transacao> {
    try {
        return TransacoesDao.buscarTransacao()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "Msg: ${e.message}")
        return emptyList()
    }
}

suspend fun deletarFilme(filme: Transacao, filmesDao: FilmesDAO){
    try {
        filmesDao.removerFilme(filme)
    }catch (e: Exception){
        Log.e("Erro ao deletar", "Msg ${e.message}")
    }
}

suspend fun atualizarFilme(filme: Filmes, filmesDao: FilmesDAO){
    try {
        filmesDao.atualizarFilmes(filme)
    }catch (e: Exception){
        Log.e("Erro ao atualizar", "Msg ${e.message}")
    }
}