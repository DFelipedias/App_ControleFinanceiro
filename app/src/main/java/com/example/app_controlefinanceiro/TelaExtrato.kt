package com.example.app_controlefinanceiro

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
fun TelaExtrato(navController: NavHostController, financeiro: FinanceiroViewModel) {
    var pesquisa by remember { mutableStateOf("") }
    var categoriaId by remember { mutableStateOf<Int?>(null) }
    val resultado = financeiro.transacoes.filter {
        (it.descricao.contains(pesquisa, true) || dinheiro(it.valor).contains(
            pesquisa,
            true
        )) && (categoriaId == null || it.categoriaId == categoriaId)
    }
    Scaffold(containerColor = Fundo, bottomBar = { BottomNavBar(navController) }) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "Extrato de Contas",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Texto
                )
            }
            item { Campo("Buscar por nome ou valor", pesquisa) { pesquisa = it } }
            item {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { categoriaId = null },
                        colors = ButtonDefaults.buttonColors(containerColor = if (categoriaId == null) Azul else Color.Gray)
                    ) { Text("Todas") }
                    for (categoria in financeiro.categorias) {
                        Button(
                            onClick = { categoriaId = categoria.id },
                            colors = ButtonDefaults.buttonColors(containerColor = if (categoriaId == categoria.id) Azul else Color.Gray)
                        ) { Text(categoria.nome) }
                    }
                }
            }
            item { Botao("NOVA TRANSAÇÃO") { navController.navigate(Rotas.NOVA_TRANSACAO) } }
            item { Text("${resultado.size} transações encontradas", color = Color.Gray) }
            items(resultado.reversed(), key = { it.id }) { item ->
                CardTransacao(
                    item,
                    financeiro
                ) { navController.navigate(Rotas.transacao(item.id)) }
            }
            if (resultado.isEmpty()) item {
                Text(
                    "Nenhuma transação encontrada.",
                    color = Color.Gray
                )
            }
        }
    }
}
