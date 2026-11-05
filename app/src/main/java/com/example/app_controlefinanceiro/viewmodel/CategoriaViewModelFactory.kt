package com.example.app_controlefinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_controlefinanceiro.data.repository.CategoriaRepository
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository

class CategoriaViewModelFactory(
    private val categoriaRepository: CategoriaRepository,
    private val transacaoRepository: TransacaoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
            return CategoriaViewModel(categoriaRepository, transacaoRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
