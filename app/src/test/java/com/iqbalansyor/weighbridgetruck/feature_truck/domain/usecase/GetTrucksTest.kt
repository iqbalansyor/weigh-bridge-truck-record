package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.iqbalansyor.weighbridgetruck.feature_truck.data.repository.FakeTruckRepository
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTrucksTest {

    private lateinit var getTrucks: GetTrucks
    private lateinit var fakeRepository: FakeTruckRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTruckRepository()
        getTrucks = GetTrucks(fakeRepository)

        val trucksToInsert = mutableListOf<Truck>()
        ('a'..'z').forEachIndexed { index, c ->
            trucksToInsert.add(
                Truck(
                    licenseNumber = c.toString(),
                    driver = c.toString(),
                    timestamp = index.toLong(),
                    outboundWeight = index.toFloat(),
                    inboundWeight = index.toFloat(),
                    id = index
                )
            )
        }
        trucksToInsert.shuffle()
        runBlocking {
            trucksToInsert.forEach { fakeRepository.insertTruck(it) }
        }
    }

    @Test
    fun `WHEN order trucks by id ascending THEN correct order`() = runBlocking {
        val trucks = getTrucks(TruckOrder.Id(OrderType.Ascending)).first()

        for (i in 0..trucks.size - 2) {
            assertThat(trucks[i].id).isLessThan(trucks[i + 1].id)
        }

    }

    @Test
    fun `WHEN order trucks by id descending THEN correct order`() = runBlocking {
        val trucks = getTrucks(TruckOrder.Date(OrderType.Descending)).first()

        for (i in 0..trucks.size - 2) {
            assertThat(trucks[i].id).isGreaterThan(trucks[i + 1].id)
        }

    }

    @Test
    fun `WHEN order trucks by date ascending THEN correct order`() = runBlocking {
        val trucks = getTrucks(TruckOrder.Date(OrderType.Ascending)).first()

        for (i in 0..trucks.size - 2) {
            assertThat(trucks[i].timestamp).isLessThan(trucks[i + 1].timestamp)
        }

    }

    @Test
    fun `WHEN order trucks by date descending THEN correct order`() = runBlocking {
        val trucks = getTrucks(TruckOrder.Date(OrderType.Descending)).first()

        for (i in 0..trucks.size - 2) {
            assertThat(trucks[i].timestamp).isGreaterThan(trucks[i + 1].timestamp)
        }

    }
}