package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder

data class TrucksState(
    val trucks: List<String> = emptyList(),
    val truckOrder: TruckOrder = TruckOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)