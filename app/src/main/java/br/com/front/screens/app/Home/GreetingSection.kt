package br.com.front.screens.app.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun GreetingSection(name: String) {
    Column {
        Text(
            text = "Olá, $name",
            color = Color(0xFF2D6CDF),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Explore, avalie e compartilhe",
            color = Color.Gray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
