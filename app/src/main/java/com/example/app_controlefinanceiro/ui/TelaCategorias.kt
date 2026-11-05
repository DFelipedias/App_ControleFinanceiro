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
fun TelaCategorias(navController: NavHostController, categorias: CategoriaViewModel) {
    val estado by categorias.uiState.collectAsState()
    val contexto = LocalContext.current
    Scaffold(containerColor = Fundo, bottomBar = { BottomNavBar(navController) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { Text("Categorias", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Texto) }
            item { CardResumo("Organize suas movimentações", "${estado.categorias.size} categorias", true) }
            item { Botao("NOVA CATEGORIA") { navController.navigate(Rotas.NOVA_CATEGORIA) } }
            items(estado.categorias, key = { it.id }) { categoria ->
                Card(Modifier.fillMaxWidth().clickable { navController.navigate(Rotas.categoria(categoria.id)) }, colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFDCE1E7))) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Category, null, tint = Azul)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(categoria.nome, fontWeight = FontWeight.Bold, color = Texto)
                            Text(categoria.descricao, color = Color.Gray, fontSize = 13.sp)
                            Text("${estado.quantidadeTransacoes(categoria.id)} transações", color = Azul, fontSize = 12.sp)
                        }
                        IconButton(onClick = {
                            if (!categorias.removerCategoria(categoria)) Toast.makeText(contexto, categorias.uiState.value.mensagem, Toast.LENGTH_LONG).show()
                        }) { Icon(Icons.Default.Delete, "Remover ${categoria.nome}", tint = Vermelho) }
                    }
                }
            }
            if (estado.categorias.isEmpty()) item { Text("Cadastre uma categoria para organizar suas transações.") }
        }
    }
}
