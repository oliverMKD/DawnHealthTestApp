package com.oliver.dawnhealthtestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.oliver.dawnhealthtestapp.presentation.navigation.AppNavigation
import com.oliver.dawnhealthtestapp.presentation.theme.DawnHealthTestAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var keepSplash = true
        installSplashScreen().setKeepOnScreenCondition {
            keepSplash
        }
        setContent {
            LaunchedEffect(Unit) {
                delay(3000)
                keepSplash = false
            }
            DawnHealthTestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        activity = this,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}