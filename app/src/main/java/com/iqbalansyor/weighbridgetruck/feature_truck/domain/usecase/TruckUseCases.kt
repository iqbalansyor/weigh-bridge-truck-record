package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

data class TruckUseCases(
    val getTrucks: GetTrucks,
    val deleteTruck: DeleteTruck,
    val addTruck: AddTruck,
    val getTruck: GetTruck
)