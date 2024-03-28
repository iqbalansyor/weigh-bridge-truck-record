package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck

data class TruckTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = ""
)