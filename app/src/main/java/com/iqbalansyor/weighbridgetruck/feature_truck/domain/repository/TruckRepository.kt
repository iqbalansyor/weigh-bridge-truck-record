package com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import kotlinx.coroutines.flow.Flow

interface TruckRepository {

    fun getTrucks(): Flow<List<Truck>>

    suspend fun getTruckById(id: Int): Truck?

    suspend fun insertTruck(truck: Truck)

    suspend fun deleteTruck(truck: Truck)

}