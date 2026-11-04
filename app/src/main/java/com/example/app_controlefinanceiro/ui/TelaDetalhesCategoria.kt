package com.example.app_controlefinanceiro.ui

import com.example.app_controlefinanceiro.viewmodel.FinanceiroViewModel
import com.example.app_controlefinanceiro.data.local.Categoria

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TelaDetalhesCategoria(navController: NavHostController, financeiro: FinanceiroViewModel, id: Int?) {
    val categoria = financeiro.categorias.find { it.id == id }
    val movimentacoes = financeiro.transacoes.filter { it.categoriaId == id }
    val despesas = movimentacoes.filter { it.despesa }.sumOf { it.valor }
    val receitas = movimentacoes.filter { !it.despesa }.sumOf { it.valor }
    Scaffold(containerColor = Fundo, topBar = { BarraTopo("Detalhes da Categoria", navController) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (categoria == null) item { Text("Categoria não encontrada.") }
            else {
                item { CardResumo(categoria.nome, dinheiro(despesas), true) }
                item { Text(categoria.descricao, color = Texto, fontSize = 18.sp) }
                item { Text("O card mostra o total de despesas desta categoria.", color = Color.Gray) }
                item { CardResumo("Receitas da categoria", dinheiro(receitas)) }
                item { Text("${movimentacoes.size} transações • Resultado: ${dinheiro(receitas - despesas)}", fontWeight = FontWeight.Bold, color = Texto) }
                item { Text("Movimentações relacionadas", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                items(movimentacoes.reversed(), key = { it.id }) { item -> CardTransacao(item, financeiro) { navController.navigate(Rotas.transacao(item.id)) } }
                if (movimentacoes.isEmpty()) item { Text("Esta categoria ainda não tem transações.", color = Color.Gray) }
                item { Botao("REGISTRAR TRANSAÇÃO") { navController.navigate(Rotas.NOVA_TRANSACAO) } }
            }
        }
    }
}
