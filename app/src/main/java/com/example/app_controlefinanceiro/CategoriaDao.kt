package com.example.app_controlefinanceiro

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoriaDao {

    @Insert
    suspend fun inserir(categoria: Categoria)

    @Query("SELECT * FROM categorias")
    suspend fun buscarTodas(): List<Categoria>

    @Update
    suspend fun atualizarCategoria(categoria: Categoria)

    @Delete
    suspend fun removerCategoria(categoria: Categoria)
}
