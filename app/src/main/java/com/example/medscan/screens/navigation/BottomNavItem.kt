package com.example.medscan.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Главная", Icons.Default.Home)
    object Upload : BottomNavItem("upload", "Загрузка", Icons.Default.CloudUpload)
    object Analysis : BottomNavItem("analysis", "Анализ", Icons.Default.Analytics)
    object Monitoring : BottomNavItem("monitoring", "Мониторинг", Icons.Default.MonitorHeart)
    object Symptoms : BottomNavItem("symptoms", "Симптомы", Icons.Default.Sick)

}
