package com.example.app_controlefinanceiro.ui

import com.example.app_controlefinanceiro.viewmodel.FinanceiroViewModel
import com.example.app_controlefinanceiro.data.local.Transacao

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

val Azul = Color(0xFF2F6FE4)
val Fundo = Color(0xFFF7F8FA)
val Texto = Color(0xFF172033)
val Vermelho = Color(0xFFB52323)
val Verde = Color(0xFF16834A)
fun dinheiro(valor: Double) = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("pt", "BR")).format(valor)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val entrada by navController.currentBackStackEntryAsState()
    val rota = entrada?.destination?.route
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(selected = rota == Rotas.INICIO, onClick = { navController.navigate(Rotas.INICIO) { launchSingleTop = true; popUpTo(Rotas.INICIO) } }, icon = { Icon(Icons.Default.Home, "Início") }, label = { Text("Início") })
        NavigationBarItem(selected = rota == Rotas.EXTRATO, onClick = { navController.navigate(Rotas.EXTRATO) { launchSingleTop = true; popUpTo(Rotas.INICIO) } }, icon = { Icon(Icons.Default.List, "Extrato") }, label = { Text("Extrato") })
        NavigationBarItem(selected = rota == Rotas.CATEGORIAS, onClick = { navController.navigate(Rotas.CATEGORIAS) { launchSingleTop = true; popUpTo(Rotas.INICIO) } }, icon = { Icon(Icons.Default.Category, "Categorias") }, label = { Text("Categorias") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTopo(titulo: String, navController: NavHostController) {
    TopAppBar(title = { Text(titulo, fontWeight = FontWeight.Bold) }, navigationIcon = {
        IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") }
    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Fundo, titleContentColor = Texto))
}

@Composable
fun CardResumo(titulo: String, valor: String, escuro: Boolean = false) {
    Card(colors = CardDefaults.cardColors(containerColor = if (escuro) Color(0xFF263238) else Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text(titulo, color = if (escuro) Color.LightGray else Texto, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Text(valor, color = if (escuro) Color.White else Azul, fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }
    }
}

@Composable
fun CardTransacao(item: Transacao, financeiro: FinanceiroViewModel, abrir: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = abrir), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFDCE1E7))) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(item.descricao, color = Texto, fontWeight = FontWeight.Bold)
                Text("${financeiro.nomeCategoria(item.categoriaId)} • ${item.data}", fontSize = 12.sp, color = Color.Gray)
                Text((if (item.despesa) "− " else "+ ") + dinheiro(item.valor), color = if (item.despesa) Vermelho else Verde, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { financeiro.removerTransacao(item) }) { Icon(Icons.Default.Delete, "Remover ${item.descricao}", tint = Vermelho) }
        }
    }
}

@Composable
fun Campo(rotulo: String, valor: String, mudar: (String) -> Unit) {
    OutlinedTextField(value = valor, onValueChange = mudar, label = { Text(rotulo) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
}

@Composable
fun Botao(texto: String, acao: () -> Unit) {
    Button(onClick = acao, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Azul)) { Text(texto) }
}
