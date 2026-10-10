package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MainAppScaffold
import com.example.ui.theme.CareerBridgeTheme
import com.example.ui.viewmodel.CareerBridgeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CareerBridgeTheme {
                val viewModel: CareerBridgeViewModel = viewModel()
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}
