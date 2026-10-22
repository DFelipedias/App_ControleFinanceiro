package com.example.app_controlefinanceiro

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.util.Date

@Entity(tableName = "transacoes")
data class Transacoes (

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val valor: Double,
    val descricao: String,
    val categoria: String,
    val data: Date
)