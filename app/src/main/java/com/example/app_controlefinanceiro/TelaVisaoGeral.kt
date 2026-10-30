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
fun TelaVisaoGeral(navController: NavHostController, financeiro: FinanceiroViewModel) {
    Scaffold(
        containerColor = Fundo,
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Rotas.NOVA_TRANSACAO) },
                containerColor = Azul
            ) { Icon(Icons.Default.Add, "Nova transação", tint = Color.White) }
        }) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Visão Geral",
                    color = Texto,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item { CardResumo("Saldo consolidado", dinheiro(financeiro.saldo()), true) }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Receitas",
                                color = Color.Gray
                            ); Text(
                            dinheiro(financeiro.receitas()),
                            color = Verde,
                            fontWeight = FontWeight.Bold
                        )
                        }
                        Column {
                            Text(
                                "Despesas",
                                color = Color.Gray
                            ); Text(
                            dinheiro(financeiro.despesas()),
                            color = Vermelho,
                            fontWeight = FontWeight.Bold
                        )
                        }
                    }
                }
            }
            item { Botao("REGISTRAR TRANSAÇÃO") { navController.navigate(Rotas.NOVA_TRANSACAO) } }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Últimas transações", fontWeight = FontWeight.Bold, color = Texto)
                    TextButton(onClick = { navController.navigate(Rotas.EXTRATO) }) { Text("Ver todas") }
                }
            }
            items(
                financeiro.transacoes.reversed().take(3),
                key = { it.id }) { item ->
                CardTransacao(item, financeiro) {
                    navController.navigate(
                        Rotas.transacao(
                            item.id
                        )
                    )
                }
            }
            if (financeiro.transacoes.isEmpty()) item { Text("Registre sua primeira transação para acompanhar o saldo.") }
            item { Spacer(Modifier.height(64.dp)) }
        }
    }
}
