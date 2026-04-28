package com.tecsup.restaurante_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecsup.restaurante_app.navigation.AppNavigation
import com.tecsup.restaurante_app.ui.theme.Restaurante_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Restaurante_appTheme {
                AppNavigation()
            }
        }
    }
}