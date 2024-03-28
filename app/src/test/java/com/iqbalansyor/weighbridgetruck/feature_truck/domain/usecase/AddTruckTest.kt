package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.iqbalansyor.weighbridgetruck.feature_truck.data.repository.FakeTruckRepository
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.InvalidTruckException
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddTruckTest {

    private lateinit var addTruck: AddTruck
    private lateinit var fakeRepository: FakeTruckRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTruckRepository()
        addTruck = AddTruck(fakeRepository)
    }

    @Test(expected = InvalidTruckException::class)
    fun `GIVEN a truck WHEN insert into db with no license THEN throws exception`() = runBlocking {
        val truck = Truck("", "driver", 1, 1.0f, 1.0f)
        addTruck(truck)
    }

    @Test(expected = InvalidTruckException::class)
    fun `GIVEN a truck WHEN insert into db with no driver THEN throws exception`() = runBlocking {
        val truck = Truck("license", "", 1, 1.0f, 1.0f)
        addTruck(truck)
    }

    @Test
    fun `GIVEN a truck WHEN insert into db THEN inserted`() = runBlocking {
        val truck = Truck("license", "driver", 1, 1.0f, 1.0f, id = 1)
        addTruck(truck)
        val insertedTruck = fakeRepository.getTruckById(1)
        assertThat(insertedTruck).isEqualTo(truck)
    }
}