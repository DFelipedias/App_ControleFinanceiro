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
fun TelaNovaTransacao(navController: NavHostController, financeiro: FinanceiroViewModel) {
    var valor by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var despesa by remember { mutableStateOf(true) }
    var categoriaId by remember { mutableStateOf<Int?>(financeiro.categorias.firstOrNull()?.id) }
    val contexto = LocalContext.current
    Scaffold(
        containerColor = Fundo,
        topBar = { BarraTopo("Nova Transação", navController) }) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CardResumo("Valor informado", "R$ " + valor.ifBlank { "0,00" }, true) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { despesa = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (despesa) Vermelho else Color.Gray)
                    ) { Text("Despesa (−)") }
                    Button(
                        onClick = { despesa = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (!despesa) Verde else Color.Gray)
                    ) { Text("Receita (+)") }
                }
            }
            item { Campo("Valor (ex.: 38,50)", valor) { valor = it } }
            item { Campo("Descrição", descricao) { descricao = it } }
            item { Campo("Data (dia/mês/ano)", data) { data = it } }
            item { Text("Categoria", fontWeight = FontWeight.Bold) }
            item {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (categoria in financeiro.categorias) {
                        Button(
                            onClick = { categoriaId = categoria.id },
                            colors = ButtonDefaults.buttonColors(containerColor = if (categoriaId == categoria.id) Azul else Color.Gray)
                        ) { Text(categoria.nome) }
                    }
                }
            }
            item { TextButton(onClick = { navController.navigate(Rotas.NOVA_CATEGORIA) }) { Text("Cadastrar uma categoria") } }
            item {
                Botao("SALVAR TRANSAÇÃO") {
                    val numero = valor.replace(",", ".").toDoubleOrNull()
                    if (descricao.isBlank() || numero == null || !numero.isFinite() || numero <= 0 || categoriaId == null || financeiro.categorias.none { it.id == categoriaId } || !data.matches(
                            Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}")
                        )) {
                        Toast.makeText(
                            contexto,
                            "Preencha a descrição, o valor positivo, a categoria e a data no formato indicado.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        financeiro.adicionarTransacao(
                            descricao.trim(),
                            numero,
                            categoriaId!!,
                            data.trim(),
                            despesa
                        )
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
