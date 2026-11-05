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
fun TelaDetalhesTransacao(
    navController: NavHostController,
    transacoes: TransacaoViewModel,
    id: Int?
) {
    val estado by transacoes.uiState.collectAsState()
    LaunchedEffect(id) { transacoes.selecionarTransacao(id) }
    val item = estado.transacaoSelecionada
    val contexto = LocalContext.current
    val editar = estado.editando
    val descricao = estado.descricaoEdicao
    val valor = estado.valorEdicao
    val data = estado.dataEdicao
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
                            Text("Categoria: ${estado.nomeCategoria(item.categoriaId)}")
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
                        transacoes.alternarEdicao()
                    }
                }
                if (editar) {
                    item { Campo("Descrição", descricao) { transacoes.mudarDescricaoEdicao(it) } }
                    item { Campo("Valor", valor) { transacoes.mudarValorEdicao(it) } }
                    item { Campo("Data (dia/mês/ano)", data) { transacoes.mudarDataEdicao(it) } }
                    item {
                        Botao("SALVAR ALTERAÇÕES") {
                            if (!transacoes.editarTransacao()) {
                                Toast.makeText(contexto, transacoes.uiState.value.mensagem, Toast.LENGTH_SHORT).show()
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
