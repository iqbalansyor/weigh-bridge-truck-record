package com.iqbalansyor.weighbridgetruck.feature_truck.data.repository

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTruckRepository : TruckRepository {

    private val trucks = mutableListOf<Truck>()

    override fun getTrucks(): Flow<List<Truck>> {
        return flow { emit(trucks) }
    }

    override suspend fun getTruckById(id: Int): Truck? {
        return trucks.find { it.id == id }
    }

    override suspend fun insertTruck(truck: Truck) {
        trucks.add(truck)
    }

    override suspend fun deleteTruck(truck: Truck) {
        trucks.remove(truck)
    }

}