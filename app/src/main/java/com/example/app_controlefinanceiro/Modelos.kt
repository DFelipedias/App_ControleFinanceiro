package com.example.app_controlefinanceiro

import androidx.room3.Entity
import androidx.room3.PrimaryKey
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

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val nome: String,
    val descricao: String
)
