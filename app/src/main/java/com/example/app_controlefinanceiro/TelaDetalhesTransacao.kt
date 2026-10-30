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
fun TelaDetalhesTransacao(
    navController: NavHostController,
    financeiro: FinanceiroViewModel,
    id: Int?
) {
    val item = financeiro.transacoes.find { it.id == id }
    val contexto = LocalContext.current
    var editar by remember { mutableStateOf(false) }
    var descricao by remember(id) { mutableStateOf(item?.descricao ?: "") }
    var valor by remember(id) { mutableStateOf(item?.valor?.toString() ?: "") }
    var data by remember(id) { mutableStateOf(item?.data ?: "") }
    Scaffold(
        containerColor = Fundo,
        topBar = { BarraTopo("Detalhes da Transação", navController) }) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (item == null) item { Text("Transação não encontrada.") }
            else {
                item {
                    CardResumo(
                        if (item.despesa) "Despesa registrada" else "Receita registrada",
                        dinheiro(item.valor),
                        true
                    )
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(item.descricao, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text("Categoria: ${financeiro.nomeCategoria(item.categoriaId)}")
                            Text("Data: ${item.data}")
                            Text(
                                "Impacto no saldo: " + (if (item.despesa) "− " else "+ ") + dinheiro(
                                    item.valor
                                ), color = if (item.despesa) Vermelho else Verde
                            )
                        }
                    }
                }
                item {
                    Botao(if (editar) "CANCELAR EDIÇÃO" else "EDITAR TRANSAÇÃO") {
                        editar = !editar
                    }
                }
                if (editar) {
                    item { Campo("Descrição", descricao) { descricao = it } }
                    item { Campo("Valor", valor) { valor = it } }
                    item { Campo("Data (dia/mês/ano)", data) { data = it } }
                    item {
                        Botao("SALVAR ALTERAÇÕES") {
                            val numero = valor.replace(",", ".").toDoubleOrNull()
                            if (descricao.isBlank() || numero == null || !numero.isFinite() || numero <= 0 || !data.matches(
                                    Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}")
                                )
                            ) Toast.makeText(
                                contexto,
                                "Confira a descrição, o valor e a data.",
                                Toast.LENGTH_SHORT
                            ).show()
                            else {
                                financeiro.editarTransacao(
                                    item,
                                    descricao.trim(),
                                    numero,
                                    data.trim()
                                ); editar = false
                            }
                        }
                    }
                }
                item {
                    TextButton(onClick = { navController.navigate(Rotas.categoria(item.categoriaId)) }) {
                        Text(
                            "Ver detalhes da categoria"
                        )
                    }
                }
            }
        }
    }
}
