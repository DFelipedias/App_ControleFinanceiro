package com.example.app_controlefinanceiro

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransacaoDao {

    @Insert
    suspend fun inserir(transacao: Transacao)

    @Query("SELECT * FROM transacoes")
    suspend fun buscarTransacao(): List<Transacao>

    @Update
    suspend fun atualizarTransacao(transacao: Transacao)

    @Delete
    suspend fun removerTransacao(transacao: Transacao)
}
