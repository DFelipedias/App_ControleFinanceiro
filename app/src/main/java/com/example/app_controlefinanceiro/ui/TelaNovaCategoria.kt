package com.example.app_controlefinanceiro.ui

import com.example.app_controlefinanceiro.viewmodel.CategoriaViewModel

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
fun TelaNovaCategoria(navController: NavHostController, categorias: CategoriaViewModel) {
    val estado by categorias.uiState.collectAsState()
    LaunchedEffect(Unit) { categorias.iniciarNovaCategoria() }
    val nome = estado.nome
    val descricao = estado.descricao
    val contexto = LocalContext.current
    Scaffold(
        containerColor = Fundo,
        topBar = { BarraTopo("Nova Categoria", navController) }) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { CardResumo("Sua nova categoria", nome.ifBlank { "Organize seus gastos" }, true) }
            item {
                Text(
                    "Agrupe movimentações parecidas para entender melhor seu dinheiro.",
                    color = Color.Gray
                )
            }
            item { Campo("Nome da categoria", nome) { categorias.mudarNome(it) } }
            item {
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { categorias.mudarDescricao(it) },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
            item {
                Botao("SALVAR CATEGORIA") {
                    if (categorias.adicionarCategoria()) {
                        navController.popBackStack()
                    } else {
                        Toast.makeText(contexto, categorias.uiState.value.mensagem, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
