package com.ebc.calculadoracuenta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.ebc.calculadoracuenta.screen.ThankYouScreen
import com.ebc.calculadoracuenta.screen.TipScreen
import com.ebc.calculadoracuenta.videmodel.TipViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: TipViewModel = viewModel ()

    NavHost(navController, startDestination = "main") {
        composable("main") { TipScreen(navController, viewModel) }
        composable("thankyou") { ThankYouScreen(navController, viewModel) }
    }
}
