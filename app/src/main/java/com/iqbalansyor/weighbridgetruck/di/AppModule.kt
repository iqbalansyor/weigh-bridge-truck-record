package com.iqbalansyor.weighbridgetruck.di

import android.content.Context
import androidx.room.Room
import com.iqbalansyor.weighbridgetruck.feature_truck.data.datasource.TruckDatabase
import com.iqbalansyor.weighbridgetruck.feature_truck.data.repository.TruckRepositoryImpl
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.AddTruck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.DeleteTruck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.GetTruck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.GetTrucks
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): TruckDatabase {
        return Room.databaseBuilder(
            context,
            TruckDatabase::class.java,
            TruckDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: TruckDatabase): TruckRepository {
        return TruckRepositoryImpl(db.truckDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: TruckRepository): TruckUseCases {
        return TruckUseCases(
            getTrucks = GetTrucks(repository),
            deleteTruck = DeleteTruck(repository),
            addTruck = AddTruck(repository),
            getTruck = GetTruck(repository)
        )
    }
}