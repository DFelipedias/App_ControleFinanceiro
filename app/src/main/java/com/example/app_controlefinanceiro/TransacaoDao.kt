package com.example.app_controlefinanceiro

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Delete
import androidx.room3.Update

@Dao
interface TransacaoDao {

    @Insert
    suspend fun inserir(transacao: Transacao)

    @Query("SELECT * FROM transacoes")
    suspend fun buscarTransacao() : List<Transacao>

    @Update
    suspend fun atualizarTransacao(transacao: Transacao)

    @Delete
    suspend fun removerTransacao(transacao: Transacao)

}