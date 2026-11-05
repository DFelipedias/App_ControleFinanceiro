package com.example.app_controlefinanceiro.ui

import com.example.app_controlefinanceiro.viewmodel.TransacaoViewModel
import com.example.app_controlefinanceiro.viewmodel.CategoriaViewModel
import com.example.app_controlefinanceiro.viewmodel.TransacaoViewModelFactory
import com.example.app_controlefinanceiro.viewmodel.CategoriaViewModelFactory

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    transacaoFactory: TransacaoViewModelFactory,
    categoriaFactory: CategoriaViewModelFactory
) {
    val navController = rememberNavController()
    val transacoes: TransacaoViewModel = viewModel(factory = transacaoFactory)
    val categorias: CategoriaViewModel = viewModel(factory = categoriaFactory)
    NavHost(navController, startDestination = Rotas.INICIO) {
        composable(Rotas.INICIO) { TelaVisaoGeral(navController, transacoes) }
        composable(Rotas.EXTRATO) { TelaExtrato(navController, transacoes) }
        composable(Rotas.NOVA_TRANSACAO) { TelaNovaTransacao(navController, transacoes) }
        composable(Rotas.CATEGORIAS) { TelaCategorias(navController, categorias) }
        composable(Rotas.NOVA_CATEGORIA) { TelaNovaCategoria(navController, categorias) }
        composable(Rotas.DETALHES_TRANSACAO) { entrada ->
            val id = entrada.arguments?.getString("id")?.toIntOrNull()
            TelaDetalhesTransacao(navController, transacoes, id)
        }
        composable(Rotas.DETALHES_CATEGORIA) { entrada ->
            val id = entrada.arguments?.getString("id")?.toIntOrNull()
            TelaDetalhesCategoria(navController, categorias, transacoes, id)
        }
    }
}
