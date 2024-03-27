package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.util

sealed class Screen(val route: String) {
    object TruckListScreen: Screen("truck_list_screen")
    object AddEditTruckScreen: Screen("add_edit_truck_screen")
}