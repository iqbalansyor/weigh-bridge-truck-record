package com.iqbalansyor.weighbridgetruck.feature_truck.data.repository

import com.iqbalansyor.weighbridgetruck.feature_truck.data.datasource.TruckDao
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow

class TruckRepositoryImpl(
    private val dao: TruckDao
) : TruckRepository {

    override fun getTrucks(): Flow<List<Truck>> {
        return dao.getTrucks()
    }

    override suspend fun getTruckById(id: Int): Truck? {
        return dao.getTruckById(id)
    }

    override suspend fun insertTruck(truck: Truck) {
        dao.insertTruck(truck)
    }

    override suspend fun deleteTruck(truck: Truck) {
        dao.deleteTruck(truck)
    }
}