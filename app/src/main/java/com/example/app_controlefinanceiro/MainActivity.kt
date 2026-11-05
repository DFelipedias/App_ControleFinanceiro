package com.example.app_controlefinanceiro

import com.example.app_controlefinanceiro.ui.AppNavigation
import com.example.app_controlefinanceiro.ui.Azul

import com.example.app_controlefinanceiro.data.local.AppDatabase
import com.example.app_controlefinanceiro.data.repository.CategoriaRepository
import com.example.app_controlefinanceiro.data.repository.TransacaoRepository
import com.example.app_controlefinanceiro.viewmodel.CategoriaViewModelFactory
import com.example.app_controlefinanceiro.viewmodel.TransacaoViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val banco = AppDatabase.getDatabase(applicationContext)
        val transacaoRepository = TransacaoRepository(banco.transacaoDao())
        val categoriaRepository = CategoriaRepository(banco.categoriaDao())
        val transacaoFactory = TransacaoViewModelFactory(transacaoRepository, categoriaRepository)
        val categoriaFactory = CategoriaViewModelFactory(categoriaRepository, transacaoRepository)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Azul)) {
                AppNavigation(transacaoFactory, categoriaFactory)
            }
        }
    }
}
