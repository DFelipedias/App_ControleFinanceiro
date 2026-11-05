package com.example.app_controlefinanceiro.data.repository

import kotlinx.coroutines.flow.Flow
import com.example.app_controlefinanceiro.data.local.Categoria
import com.example.app_controlefinanceiro.data.local.CategoriaDao

class CategoriaRepository(private val dao: CategoriaDao) {

    fun buscarTodas(): Flow<List<Categoria>> = dao.buscarTodas()

    suspend fun inserir(categoria: Categoria) = dao.inserir(categoria)

    suspend fun atualizarCategoria(categoria: Categoria) = dao.atualizarCategoria(categoria)

    suspend fun removerCategoria(categoria: Categoria) = dao.removerCategoria(categoria)
}
