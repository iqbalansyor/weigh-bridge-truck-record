package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository

class DeleteTruck(
    private val repository: TruckRepository
) {

    suspend operator fun invoke(truck: Truck) {
        repository.deleteTruck(truck)
    }
}