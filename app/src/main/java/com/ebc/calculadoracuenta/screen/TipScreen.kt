package com.ebc.calculadoracuenta.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ebc.calculadoracuenta.R
import com.ebc.calculadoracuenta.component.TipPercentageSelector
import com.ebc.calculadoracuenta.videmodel.TipViewModel

@Composable
fun TipScreen(navController: NavController, viewModel: TipViewModel = viewModel()) {
    val bill by viewModel.billAmount.collectAsState()
    val tipPercent by viewModel.tipPercent.collectAsState()
    val customTip by viewModel.customTipAmount.collectAsState()
    val total by viewModel.totalToPay.collectAsState()

    val isBillValid = bill.toDoubleOrNull() != null && bill.toDoubleOrNull()!! > 0

    val tipOptions = listOf(5, 10, 15, 20)

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bill));

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Total de la cuenta")
        TextField(
            value = bill,
            onValueChange = viewModel::setBillAmount,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona porcentaje de propina")
        //DropdownMenuTipSelector(tipOptions, tipPercent, viewModel::setTipPercent)
        TipPercentageSelector (tipOptions, tipPercent, viewModel::setTipPercent, enabled = isBillValid)

        Text("O ingresa una cantidad personalizada")
        TextField(
            value = customTip,
            //onValueChange = { newValue -> viewModel.setCustomTipAmount(newValue) }
            onValueChange = viewModel::setCustomTipAmount,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = isBillValid
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Total a pagar: $total", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.reset() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Reiniciar")
            }
            Button(
                onClick = { navController.navigate("thankyou") },
                enabled = isBillValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Pagar")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun DropdownMenuTipSelector(
    options: List<Int>,
    selected: Int?,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected?.let { "$it%" } ?: "Selecciona porcentaje")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text("$option%") },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}
