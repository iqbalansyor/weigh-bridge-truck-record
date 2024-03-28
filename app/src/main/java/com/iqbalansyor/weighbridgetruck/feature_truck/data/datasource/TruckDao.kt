package com.iqbalansyor.weighbridgetruck.feature_truck.data.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {

    @Query("SELECT * FROM truck")
    fun getTrucks(): Flow<List<Truck>>

    @Query("SELECT * FROM truck WHERE id = :id")
    suspend fun getTruckById(id: Int): Truck?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: Truck)

    @Delete
    suspend fun deleteTruck(truck: Truck)
}