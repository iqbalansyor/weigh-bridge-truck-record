package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder

data class TrucksState(
    val trucks: List<Truck> = emptyList(),
    val truckOrder: TruckOrder = TruckOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)