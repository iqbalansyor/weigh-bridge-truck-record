package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.iqbalansyor.weighbridgetruck.feature_truck.data.repository.FakeTruckRepository
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class DeleteTruckTest {

    private lateinit var deleteTruck: DeleteTruck
    private lateinit var fakeRepository: FakeTruckRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTruckRepository()
        deleteTruck = DeleteTruck(fakeRepository)
    }

    @Test
    fun `GIVEN a truck WHEN delete existing truck THEN correct`() = runBlocking {

        val truck = Truck("license", "driver", 1, 1.0f, 1.0f, 1)
        fakeRepository.insertTruck(truck)

        deleteTruck(truck)

        assertThat(fakeRepository.getTruckById(1)).isNotEqualTo(truck)

    }

    @Test
    fun `GIVEN a truck WHEN delete non existing truck THEN correct`() = runBlocking {

        val truck = Truck("license", "driver", 1, 1.0f, 1.0f, 1)
        fakeRepository.deleteTruck(truck)

        deleteTruck(truck)

        assertThat(fakeRepository.getTruckById(1)).isNotEqualTo(truck)

    }

}