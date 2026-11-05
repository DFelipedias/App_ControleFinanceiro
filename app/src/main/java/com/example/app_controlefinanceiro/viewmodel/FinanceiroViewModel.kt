package com.example.app_controlefinanceiro.viewmodel

import com.example.app_controlefinanceiro.data.local.AppDatabase
import com.example.app_controlefinanceiro.data.local.Categoria
import com.example.app_controlefinanceiro.data.local.Transacao
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository
import com.example.app_controlefinanceiro.data.repository.CategoriaRepository

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FinanceiroViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val transacaoRepository = TransacaoRepository(db.transacaoDao())
    private val categoriaRepository = CategoriaRepository(db.categoriaDao())

    val categorias = mutableStateListOf<Categoria>()
    val transacoes = mutableStateListOf<Transacao>()

    init {
        carregarDados()
    }

    fun carregarDados() {
        viewModelScope.launch {
            val categoriasBanco = categoriaRepository.buscarTodas()
            if (categoriasBanco.isEmpty()) {
                val padrao = listOf(
                    Categoria(nome = "Alimentação", descricao = "Refeições e compras de mercado"),
                    Categoria(nome = "Transporte", descricao = "Combustível, ônibus e aplicativos"),
                    Categoria(nome = "Saúde", descricao = "Farmácia e cuidados pessoais"),
                    Categoria(nome = "Renda", descricao = "Salário e outras entradas")
                )
                padrao.forEach { categoriaRepository.inserir(it) }
                categorias.clear()
                categorias.addAll(categoriaRepository.buscarTodas())
            } else {
                categorias.clear()
                categorias.addAll(categoriasBanco)
            }

            val transacoesBanco = transacaoRepository.buscarTransacao()
            if (transacoesBanco.isEmpty() && categorias.isNotEmpty()) {
                val catRenda = categorias.find { it.nome == "Renda" }?.id ?: categorias.first().id
                val catAlim = categorias.find { it.nome == "Alimentação" }?.id ?: categorias.first().id
                val catTransp = categorias.find { it.nome == "Transporte" }?.id ?: categorias.first().id
                val catSaude = categorias.find { it.nome == "Saúde" }?.id ?: categorias.first().id

                val padraoTransacoes = listOf(
                    Transacao(descricao = "Salário", valor = 3500.0, categoriaId = catRenda, data = "01/10/2026", despesa = false),
                    Transacao(descricao = "Supermercado", valor = 450.0, categoriaId = catAlim, data = "02/10/2026", despesa = true),
                    Transacao(descricao = "Combustível", valor = 180.0, categoriaId = catTransp, data = "03/10/2026", despesa = true),
                    Transacao(descricao = "Almoço Restaurante", valor = 38.50, categoriaId = catAlim, data = "04/10/2026", despesa = true),
                    Transacao(descricao = "Farmácia", valor = 27.90, categoriaId = catSaude, data = "04/10/2026", despesa = true)
                )
                padraoTransacoes.forEach { transacaoRepository.inserir(it) }
                transacoes.clear()
                transacoes.addAll(transacaoRepository.buscarTransacao())
            } else {
                transacoes.clear()
                transacoes.addAll(transacoesBanco)
            }
        }
    }

    fun adicionarTransacao(descricao: String, valor: Double, categoriaId: Int, data: String, despesa: Boolean) {
        viewModelScope.launch {
            val nova = Transacao(descricao = descricao, valor = valor, categoriaId = categoriaId, data = data, despesa = despesa)
            transacaoRepository.inserir(nova)
            transacoes.clear()
            transacoes.addAll(transacaoRepository.buscarTransacao())
        }
    }

    fun editarTransacao(item: Transacao, descricao: String, valor: Double, data: String) {
        viewModelScope.launch {
            val atualizada = item.copy(descricao = descricao, valor = valor, data = data)
            transacaoRepository.atualizarTransacao(atualizada)
            transacoes.clear()
            transacoes.addAll(transacaoRepository.buscarTransacao())
        }
    }

    fun removerTransacao(item: Transacao) {
        viewModelScope.launch {
            transacaoRepository.removerTransacao(item)
            transacoes.clear()
            transacoes.addAll(transacaoRepository.buscarTransacao())
        }
    }

    fun adicionarCategoria(nome: String, descricao: String) {
        viewModelScope.launch {
            val nova = Categoria(nome = nome, descricao = descricao)
            categoriaRepository.inserir(nova)
            categorias.clear()
            categorias.addAll(categoriaRepository.buscarTodas())
        }
    }

    fun removerCategoria(item: Categoria): Boolean {
        if (transacoes.any { it.categoriaId == item.id }) return false
        viewModelScope.launch {
            categoriaRepository.removerCategoria(item)
            categorias.clear()
            categorias.addAll(categoriaRepository.buscarTodas())
        }
        return true
    }

    fun nomeCategoria(id: Int) = categorias.find { it.id == id }?.nome ?: "Sem categoria"
    fun receitas() = transacoes.filter { !it.despesa }.sumOf { it.valor }
    fun despesas() = transacoes.filter { it.despesa }.sumOf { it.valor }
    fun saldo() = receitas() - despesas()
}
