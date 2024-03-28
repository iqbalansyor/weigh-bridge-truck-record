package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository

class GetTruck(
    private val repository: TruckRepository
) {

    suspend operator fun invoke(id: Int): Truck? {
        return repository.getTruckById(id)
    }
}