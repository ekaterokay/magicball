package com.example.magicball.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magicball.ui.theme.*

private val MagicRadius = 18.dp

// ---------- Background (gradient + subtle glow) ----------
@Composable
fun MagicBackground(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GradBg)
    ) {
        GlowBlob(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopStart)
                .offset((-80).dp, (-90).dp),
            brush = GradGlowLavender
        )

        GlowBlob(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .offset((70).dp, (90).dp),
            brush = GradGlowCyan
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
private fun GlowBlob(modifier: Modifier, brush: Brush) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(brush)
    )
}

// ---------- Magic Ball Hero (pure Compose) ----------
@Composable
fun MagicBallHero(
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    title: String? = null,
    subtitle: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .drawBehind {
                    // outer glow rings (NO unresolved refs)
                    this.drawCircle(
                        color = Color(0x33B388FF),
                        radius = size.toPx() * 0.62f
                    )
                    this.drawCircle(
                        color = Color(0x1A63E6FF),
                        radius = size.toPx() * 0.72f
                    )
                }
                .clip(CircleShape)
                .background(GradBall)
        ) {
            // inner highlight
            Box(
                modifier = Modifier
                    .size(size * 0.62f)
                    .align(Alignment.TopStart)
                    .offset(20.dp, 26.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0x66FFFFFF), Color(0x00FFFFFF))
                        )
                    )
            )
        }

        if (title != null) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
        }

        if (subtitle != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
    }
}

// ---------- Premium Buttons ----------
@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    PrimaryButton(text = text, enabled = true, onClick = onClick)
}

@Composable
fun PrimaryButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(MagicRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = Color(0xFF0B0611),
            disabledContentColor = TextTertiary
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        val brush = if (enabled) {
            GradPrimary
        } else {
            Brush.horizontalGradient(listOf(Color(0xFF3A2B55), Color(0xFF2C2042)))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(MagicRadius))
                .background(brush)
                .drawBehind {
                    // glow without maxDimension
                    val r = if (size.width > size.height) size.width else size.height
                    this.drawCircle(color = Color(0x1AB388FF), radius = r)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (enabled) Color(0xFF0B0611) else TextTertiary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(MagicRadius),
        border = BorderStroke(1.dp, StrokeGlow),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = TextPrimary
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Medium)
    }
}

// ---------- Field ----------
@Composable
fun Field(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        placeholder = { Text(placeholder, color = TextTertiary) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Surface1,
            unfocusedContainerColor = Surface0,
            disabledContainerColor = Surface0,

            focusedBorderColor = AccentLavender,
            unfocusedBorderColor = StrokeSoft,
            cursorColor = AccentNeon,

            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,

            focusedPlaceholderColor = TextTertiary,
            unfocusedPlaceholderColor = TextTertiary
        )
    )
}