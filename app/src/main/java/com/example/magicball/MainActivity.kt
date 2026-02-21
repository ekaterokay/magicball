package com.example.magicball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.magicball.ui.theme.MagicBallTheme
import ui.AppRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagicBallTheme {
                AppRoot(
                    onExitApp = { finish() }
                )
            }
        }
    }
}