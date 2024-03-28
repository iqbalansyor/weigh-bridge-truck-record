package com.iqbalansyor.weighbridgetruck.feature_truck.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck

@Database(
    entities = [Truck::class],
    version = 1,
    exportSchema = false
)
abstract class TruckDatabase : RoomDatabase() {

    abstract val truckDao: TruckDao

    companion object {
        const val DATABASE_NAME = "trucks_db"
    }

}