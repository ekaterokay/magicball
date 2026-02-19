package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Вариант 1: PrimaryButton("Text") { ... }
@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) { Text(text) }
}

// Вариант 2: PrimaryButton("Text", enabled) { ... }  ✅ для твоих текущих вызовов
@Composable
fun PrimaryButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) { Text(text) }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) { Text(text) }
}

@Composable
fun ClickText(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun Field(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = true
    )
}
