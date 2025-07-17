package com.example.medscan

import UserViewModel
import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.medscan.data.DocumentRepository
import com.example.medscan.screens.forUI.AnalysisScreen
import com.example.medscan.screens.forUI.DetailsScreen
import com.example.medscan.screens.forUI.HealthCharts
import com.example.medscan.screens.forUI.HomeScreen
import com.example.medscan.screens.forUI.LoginScreen
import com.example.medscan.screens.forUI.MonitoringScreen
import com.example.medscan.screens.forUI.SymptomScreen
import com.example.medscan.screens.forUI.UploadScreen
import com.example.medscan.screens.navigation.BottomNavItem
import com.example.medscan.screens.navigation.BottomNavigationBar
import com.example.medscan.ui.theme.MedscanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedscanTheme {
                MedScanApp()
            }
        }
    }
}

@Composable
fun MedScanApp() {
    val context = LocalContext.current
    val repository = remember { DocumentRepository(context) }
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Upload,
        BottomNavItem.Analysis,
        BottomNavItem.Monitoring,
        BottomNavItem.Symptoms
    )

    // ✅ Получаем текущий маршрут (экран)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    // ✅ Определим, показывать ли нижнюю навигацию
    val showBottomBar = currentDestination !in listOf("login", "register")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController, items = items)
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(navController, userViewModel)
                }
                composable("register") {
                    RegisterScreen(navController, userViewModel)
                }
                composable(BottomNavItem.Home.route) { HomeScreen(navController) }
                composable(BottomNavItem.Upload.route) { UploadScreen(navController) }
                composable(BottomNavItem.Analysis.route) { AnalysisScreen(navController) }
                composable(BottomNavItem.Monitoring.route) { HealthCharts(navController) }
                composable(BottomNavItem.Symptoms.route) { SymptomScreen(navController) }
                composable("details/{documentId}") { backStackEntry ->
                    val documentId = backStackEntry.arguments?.getString("documentId")?.toLongOrNull()
                    documentId?.let {
                        DetailsScreen(
                            documentId = it,
                            navController = navController,
                            repository = repository
                        )
                    }
                }
            }
        }
    )
}