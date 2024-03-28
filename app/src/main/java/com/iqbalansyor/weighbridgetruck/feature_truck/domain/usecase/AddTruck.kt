package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.InvalidTruckException
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository


class AddTruck(
    private val repository: TruckRepository
) {

    @Throws(InvalidTruckException::class)
    suspend operator fun invoke(truck: Truck) {
        if (truck.licenseNumber.isNullOrBlank()) {
            throw InvalidTruckException("License number is empty")
        }
        if (truck.driver.isNullOrBlank()) {
            throw InvalidTruckException("Driver is empty")
        }
        repository.insertTruck(truck)
    }

}