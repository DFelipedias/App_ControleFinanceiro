package com.example.app_controlefinanceiro

import com.example.app_controlefinanceiro.ui.AppNavigation
import com.example.app_controlefinanceiro.ui.Azul

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
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Azul)) {
                AppNavigation()
            }
        }
    }
}
