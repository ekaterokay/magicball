package com.example.magicball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.magicball.data.HistoryStore
import com.example.magicball.ui.theme.MagicBallTheme
import ui.AppRoot

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()   // ← ВАЖНО

        super.onCreate(savedInstanceState)

        // ✅ чтобы история сохранялась между запусками
        HistoryStore.init(applicationContext)

        setContent {
            MagicBallTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0C0F1C))
                ) {
                    AppRoot(
                        onExitApp = { finish() }
                    )
                }
            }
        }
    }
}