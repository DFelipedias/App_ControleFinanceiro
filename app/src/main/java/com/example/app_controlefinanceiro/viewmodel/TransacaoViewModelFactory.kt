package com.example.app_controlefinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_controlefinanceiro.data.repository.CategoriaRepository
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository

class TransacaoViewModelFactory(
    private val transacaoRepository: TransacaoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransacaoViewModel::class.java)) {
            return TransacaoViewModel(transacaoRepository, categoriaRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
