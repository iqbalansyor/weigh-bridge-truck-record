package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder

sealed class TrucksEvent {
    data class Order(val truckOrder: TruckOrder) : TrucksEvent()
    data class DeleteTruck(val truck: Truck) : TrucksEvent()
    object RestoreNote : TrucksEvent()
    object ToggleOrderSection : TrucksEvent()
}