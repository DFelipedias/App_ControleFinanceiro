package com.example.app_controlefinanceiro.ui

import com.example.app_controlefinanceiro.viewmodel.FinanceiroViewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val financeiro: FinanceiroViewModel = viewModel()
    NavHost(navController, startDestination = Rotas.INICIO) {
        composable(Rotas.INICIO) { TelaVisaoGeral(navController, financeiro) }
        composable(Rotas.EXTRATO) { TelaExtrato(navController, financeiro) }
        composable(Rotas.NOVA_TRANSACAO) { TelaNovaTransacao(navController, financeiro) }
        composable(Rotas.CATEGORIAS) { TelaCategorias(navController, financeiro) }
        composable(Rotas.NOVA_CATEGORIA) { TelaNovaCategoria(navController, financeiro) }
        composable(Rotas.DETALHES_TRANSACAO) { entrada ->
            val id = entrada.arguments?.getString("id")?.toIntOrNull()
            TelaDetalhesTransacao(navController, financeiro, id)
        }
        composable(Rotas.DETALHES_CATEGORIA) { entrada ->
            val id = entrada.arguments?.getString("id")?.toIntOrNull()
            TelaDetalhesCategoria(navController, financeiro, id)
        }
    }
}
