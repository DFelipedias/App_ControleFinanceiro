package com.example.app_controlefinanceiro

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FinanceiroViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val transacaoDao = db.transacaoDao()
    private val categoriaDao = db.categoriaDao()

    val categorias = mutableStateListOf<Categoria>()
    val transacoes = mutableStateListOf<Transacao>()

    init {
        carregarDados()
    }

    fun carregarDados() {
        viewModelScope.launch {
            val categoriasBanco = categoriaDao.buscarTodas()
            if (categoriasBanco.isEmpty()) {
                val padrao = listOf(
                    Categoria(nome = "Alimentação", descricao = "Refeições e compras de mercado"),
                    Categoria(nome = "Transporte", descricao = "Combustível, ônibus e aplicativos"),
                    Categoria(nome = "Saúde", descricao = "Farmácia e cuidados pessoais"),
                    Categoria(nome = "Renda", descricao = "Salário e outras entradas")
                )
                padrao.forEach { categoriaDao.inserir(it) }
                categorias.clear()
                categorias.addAll(categoriaDao.buscarTodas())
            } else {
                categorias.clear()
                categorias.addAll(categoriasBanco)
            }

            val transacoesBanco = transacaoDao.buscarTransacao()
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
                padraoTransacoes.forEach { transacaoDao.inserir(it) }
                transacoes.clear()
                transacoes.addAll(transacaoDao.buscarTransacao())
            } else {
                transacoes.clear()
                transacoes.addAll(transacoesBanco)
            }
        }
    }

    fun adicionarTransacao(descricao: String, valor: Double, categoriaId: Int, data: String, despesa: Boolean) {
        viewModelScope.launch {
            val nova = Transacao(descricao = descricao, valor = valor, categoriaId = categoriaId, data = data, despesa = despesa)
            transacaoDao.inserir(nova)
            transacoes.clear()
            transacoes.addAll(transacaoDao.buscarTransacao())
        }
    }

    fun editarTransacao(item: Transacao, descricao: String, valor: Double, data: String) {
        viewModelScope.launch {
            val atualizada = item.copy(descricao = descricao, valor = valor, data = data)
            transacaoDao.atualizarTransacao(atualizada)
            transacoes.clear()
            transacoes.addAll(transacaoDao.buscarTransacao())
        }
    }

    fun removerTransacao(item: Transacao) {
        viewModelScope.launch {
            transacaoDao.removerTransacao(item)
            transacoes.clear()
            transacoes.addAll(transacaoDao.buscarTransacao())
        }
    }

    fun adicionarCategoria(nome: String, descricao: String) {
        viewModelScope.launch {
            val nova = Categoria(nome = nome, descricao = descricao)
            categoriaDao.inserir(nova)
            categorias.clear()
            categorias.addAll(categoriaDao.buscarTodas())
        }
    }

    fun removerCategoria(item: Categoria): Boolean {
        if (transacoes.any { it.categoriaId == item.id }) return false
        viewModelScope.launch {
            categoriaDao.removerCategoria(item)
            categorias.clear()
            categorias.addAll(categoriaDao.buscarTodas())
        }
        return true
    }

    fun nomeCategoria(id: Int) = categorias.find { it.id == id }?.nome ?: "Sem categoria"
    fun receitas() = transacoes.filter { !it.despesa }.sumOf { it.valor }
    fun despesas() = transacoes.filter { it.despesa }.sumOf { it.valor }
    fun saldo() = receitas() - despesas()
}
