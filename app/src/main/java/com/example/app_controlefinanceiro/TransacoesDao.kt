package com.example.app_controlefinanceiro

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Delete
import androidx.room3.Update

@Dao
interface TransacoesDao {

    @Insert
    fun inserir(transacao: Transacao)

    @Query("SELECT * FROM transacoes")
    fun buscarTransacao() : List<Transacoes>

    @Update
    fun atualizarTransacao(transacao: Transacoes)

    @Delete
    fun removerTransacao(transacao: Transacoes)

}