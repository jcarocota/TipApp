package com.ebc.calculadoracuenta.videmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.NumberFormat
import java.util.Locale

// Esta clase maneja toda la lógica del formulario y el cálculo del total
class TipViewModel: ViewModel() {

    // Estado interno (privado) para el monto de la cuenta (como texto)
    private val _billAmount = MutableStateFlow("");
    // Estado público, solo de lectura, para que la UI observe cambios
    val billAmount: StateFlow<String> = _billAmount;

    // Estado interno para el porcentaje de propina seleccionado (puede ser null si hay propina personalizada)
    private val _tipPercent = MutableStateFlow<Int?>(null);
    val tipPercent: StateFlow<Int?> = _tipPercent;

    // Estado interno para una propina personalizada en cantidad (como texto)
    private val _customTipAmount = MutableStateFlow("");
    val customTipAmount: StateFlow<String> = _customTipAmount;

    // StateFlow que contiene el total calculado (cuenta + propina), y se actualiza automáticamente
    val totalToPay: StateFlow<String> = combine(
        _billAmount,        // flujo 1: cantidad total ingresada
        _tipPercent,        // flujo 2: porcentaje seleccionado (opcional)
        _customTipAmount    // flujo 3: propina personalizada
    ) { billStr, tipPercent, customTipStr ->
        val bill = billStr.toDoubleOrNull() ?: 0.0;
        val customTip = customTipStr.toDoubleOrNull();
        val tip = if (customTipStr.isNotBlank() && customTip != null && customTip > 0) {
            customTip
        } else {
            bill * ((tipPercent ?: 0) / 100.0)
        }

        // ESTE VALOR SE EMITE como resultado del combine
        val formatted = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        formatted.format(bill + tip)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0.00")

    fun setBillAmount(value: String) {
        _billAmount.value = value
    }

    fun setTipPercent(value: Int?) {
        _tipPercent.value = value
        _customTipAmount.value = ""
    }

    fun setCustomTipAmount(value: String) {
        _customTipAmount.value = value
        _tipPercent.value = null
    }

    fun reset() {
        _billAmount.value = ""
        _tipPercent.value = null
        _customTipAmount.value = ""
    }
}