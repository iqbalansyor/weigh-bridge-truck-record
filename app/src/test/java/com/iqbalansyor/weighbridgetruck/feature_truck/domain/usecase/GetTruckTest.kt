package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.iqbalansyor.weighbridgetruck.feature_truck.data.repository.FakeTruckRepository
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTruckTest {
    private lateinit var getTruckTest: GetTruck
    private lateinit var fakeRepository: FakeTruckRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTruckRepository()
        getTruckTest = GetTruck(fakeRepository)
    }

    @Test
    fun `GIVEN a truck WHEN search THEN found is correct`() = runBlocking {

        val truck = Truck("license", "driver", 1, 1.0f, 1.0f, 1)
        fakeRepository.insertTruck(truck)

        val findTruck = getTruckTest(1)

        assertThat(findTruck?.id).isEqualTo(truck.id)

    }

    @Test
    fun `GIVEN a truck WHEN search THEN not found is correct`() = runBlocking {

        val truck = Truck("Title", "Content", 1, 1.0f, 1.0f, 1)
        fakeRepository.insertTruck(truck)

        val findTruck = getTruckTest(0)

        assertThat(findTruck).isNull()
    }

}