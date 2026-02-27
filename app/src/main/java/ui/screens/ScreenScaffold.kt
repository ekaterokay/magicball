package ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import com.example.magicball.ui.MagicBackground
import com.example.magicball.ui.theme.AccentNeon
import com.example.magicball.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    showBack: Boolean,
    onBack: () -> Unit,
    onOpenHistory: (() -> Unit)? = null,
    onExit: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        // ✅ ВАЖНО: не Transparent!
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, color = TextPrimary) },

                navigationIcon = {
                    when {
                        onExit != null -> {
                            IconButton(onClick = onExit) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Exit",
                                    tint = TextPrimary
                                )
                            }
                        }
                        showBack -> {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextPrimary
                                )
                            }
                        }
                    }
                },

                actions = {
                    if (onOpenHistory != null) {
                        IconButton(onClick = onOpenHistory) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "History",
                                tint = AccentNeon
                            )
                        }
                    }
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        MagicBackground(contentPadding = padding) {
            content(padding)
        }
    }
}