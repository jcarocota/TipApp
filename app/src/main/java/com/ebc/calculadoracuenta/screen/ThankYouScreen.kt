package com.ebc.calculadoracuenta.screen

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.ebc.calculadoracuenta.R
import com.ebc.calculadoracuenta.videmodel.TipViewModel

@Composable
fun ThankYouScreen(navController: NavController, viewModel: TipViewModel = viewModel()) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.thanks));

    val total by viewModel.totalToPay.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gracias por su compra", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // Aquí mostramos el total pagado
        Text("Total pagado: $$total", style = MaterialTheme.typography.titleMedium)

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(400.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.reset()
            navController.navigate("main") {
                popUpTo("main") { inclusive = true }
            }
        }) {
            Text("Iniciar nuevamente")
        }
    }
}
