package com.example.scenic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scenic.R

val SupermercadoOne = FontFamily(
    Font(R.font.supermercado_one, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = SupermercadoOne, fontSize = 57.sp),
    displayMedium = TextStyle(fontFamily = SupermercadoOne, fontSize = 45.sp),
    displaySmall = TextStyle(fontFamily = SupermercadoOne, fontSize = 36.sp),
    headlineLarge = TextStyle(fontFamily = SupermercadoOne, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = SupermercadoOne, fontSize = 28.sp),
    headlineSmall = TextStyle(fontFamily = SupermercadoOne, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = SupermercadoOne, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = SupermercadoOne, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = SupermercadoOne, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = SupermercadoOne, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = SupermercadoOne, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = SupermercadoOne, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontFamily = SupermercadoOne, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = SupermercadoOne, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = SupermercadoOne, fontSize = 11.sp)
)
