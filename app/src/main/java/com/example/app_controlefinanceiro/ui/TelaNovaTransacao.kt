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
fun TelaNovaTransacao(navController: NavHostController, transacoes: TransacaoViewModel) {
    val estado by transacoes.uiState.collectAsState()
    LaunchedEffect(Unit) { transacoes.iniciarNovaTransacao() }
    val valor = estado.valor
    val descricao = estado.descricao
    val data = estado.data
    val despesa = estado.despesa
    val categoriaId = estado.categoriaId
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
                        onClick = { transacoes.mudarDespesa(true) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (despesa) Vermelho else Color.Gray)
                    ) { Text("Despesa (−)") }
                    Button(
                        onClick = { transacoes.mudarDespesa(false) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (!despesa) Verde else Color.Gray)
                    ) { Text("Receita (+)") }
                }
            }
            item { Campo("Valor (ex.: 38,50)", valor) { transacoes.mudarValor(it) } }
            item { Campo("Descrição", descricao) { transacoes.mudarDescricao(it) } }
            item { Campo("Data (dia/mês/ano)", data) { transacoes.mudarData(it) } }
            item { Text("Categoria", fontWeight = FontWeight.Bold) }
            item {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (categoria in estado.categorias) {
                        Button(
                            onClick = { transacoes.mudarCategoria(categoria.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (categoriaId == categoria.id) Azul else Color.Gray)
                        ) { Text(categoria.nome) }
                    }
                }
            }
            item { TextButton(onClick = { navController.navigate(Rotas.NOVA_CATEGORIA) }) { Text("Cadastrar uma categoria") } }
            item {
                Botao("SALVAR TRANSAÇÃO") {
                    if (transacoes.adicionarTransacao()) {
                        navController.popBackStack()
                    } else {
                        Toast.makeText(contexto, transacoes.uiState.value.mensagem, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
