package com.example.app_controlefinanceiro.ui

import com.example.app_controlefinanceiro.viewmodel.TransacaoViewModel

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
fun TelaExtrato(navController: NavHostController, transacoes: TransacaoViewModel) {
    val estado by transacoes.uiState.collectAsState()
    LaunchedEffect(Unit) { transacoes.iniciarExtrato() }
    val pesquisa = estado.pesquisa
    val categoriaId = estado.categoriaFiltroId
    val resultado = estado.resultado
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
            item { Campo("Buscar por nome ou valor", pesquisa) { transacoes.mudarPesquisa(it) } }
            item {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { transacoes.mudarFiltroCategoria(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = if (categoriaId == null) Azul else Color.Gray)
                    ) { Text("Todas") }
                    for (categoria in estado.categorias) {
                        Button(
                            onClick = { transacoes.mudarFiltroCategoria(categoria.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (categoriaId == categoria.id) Azul else Color.Gray)
                        ) { Text(categoria.nome) }
                    }
                }
            }
            item { Botao("NOVA TRANSAÇÃO") { navController.navigate(Rotas.NOVA_TRANSACAO) } }
            item { Text("${resultado.size} transações encontradas", color = Color.Gray) }
            items(resultado, key = { it.id }) { item ->
                CardTransacao(
                    item,
                    estado.nomeCategoria(item.categoriaId),
                    { transacoes.removerTransacao(item) }
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
