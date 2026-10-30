package com.example.app_controlefinanceiro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val categoriaId: Int,
    val data: String,
    val despesa: Boolean
)