package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucks

import app.cash.turbine.test
import com.iqbalansyor.weighbridgetruck.MainDispatcherRule
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.TrucksEvent
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.TrucksState
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.TrucksViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TrucksViewModelTest {

    // Mocks
    @RelaxedMockK
    private lateinit var truckUseCases: TruckUseCases

    // ViewModel
    private lateinit var viewModel: TrucksViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = TrucksViewModel(truckUseCases)
    }

    @Test
    fun `getTrucks should update state when invoked`() = runTest {
        val truckOrder = TruckOrder.Date(OrderType.Descending)
        val trucks = listOf(
            Truck(licenseNumber = "license1", driver = "driver1", timestamp = 1, outboundWeight = 1.0f, inboundWeight = 1.0f, id = 1),
            Truck(licenseNumber = "license2", driver = "driver2", timestamp = 2, outboundWeight = 1.0f, inboundWeight = 1.0f, id = 2),
            Truck(licenseNumber = "license3", driver = "driver3", timestamp = 3, outboundWeight = 1.0f, inboundWeight = 1.0f, id = 3),
        )
        coEvery { truckUseCases.getTrucks(truckOrder) } returns flowOf(trucks)

        viewModel.state.test {
            assertFalse(awaitItem().isOrderSectionVisible)
            viewModel.getTrucks(truckOrder)
            assertTrue(awaitItem().trucks == trucks)
            cancelAndIgnoreRemainingEvents()
        }

    }

    // TODO: Fix test
//    @Test
//    fun `onEvent with TrucksEvent Order should invoke getTrucks`() {
//        val truckOrder = TruckOrder.Title(OrderType.Ascending)
//        val currentState = TrucksState(truckOrder = TruckOrder.Date(OrderType.Descending))
//        val trucks = listOf(
//            Truck(licenseNumber = "license1", driver = "driver1", timestamp = 1, outboundWeight = 1.0f, inboundWeight = 1.0f, id = 1),
//            Truck(licenseNumber = "license2", driver = "driver2", timestamp = 2, outboundWeight = 1.0f, inboundWeight = 1.0f, id = 2),
//            Truck(licenseNumber = "license3", driver = "driver3", timestamp = 3, outboundWeight = 1.0f, inboundWeight = 1.0f, id = 3),
//        )
//        coEvery { truckUseCases.getTrucks(truckOrder) } returns flowOf(trucks)
//
//        viewModel.setState(currentState)
//
//        viewModel.onEvent(TrucksEvent.Order(truckOrder))
//
//        verify { viewModel.getTrucks(truckOrder) }
//    }
//
//    @Test
//    fun `onEvent with TrucksEvent Order should not invoke getTrucks if order is the same`() {
//        val truckOrder = TruckOrder.Date(OrderType.Descending)
//        val currentState = TrucksState(truckOrder = truckOrder)
//        viewModel.setState(currentState)
//
//        viewModel.onEvent(TrucksEvent.Order(truckOrder))
//
//        verify { viewModel.getTrucks(truckOrder) }
//    }

    @Test
    fun `onEvent with TrucksEvent DeleteTruck should delete truck and update recentlyDeletedTruck`() =
        runTest {
            val truck = Truck(
                licenseNumber = "license",
                driver = "driver",
                timestamp = 1,
                inboundWeight = 1.0f,
                outboundWeight = 2.0f,
                id = 1
            )
            coEvery { truckUseCases.deleteTruck(truck) } just Runs
            val event = TrucksEvent.DeleteTruck(truck)

            viewModel.onEvent(event)

            coVerify { truckUseCases.deleteTruck(truck) }
            assert(viewModel.recentlyDeletedTruck == truck)
        }

    @Test
    fun `onEvent with TrucksEvent RestoreTruck should add truck and clear recentlyDeletedTruck`() =
        runTest {
            val truck = Truck(
                licenseNumber = "license1",
                driver = "driver",
                timestamp = 1,
                inboundWeight = 1.0f,
                outboundWeight = 2.0f,
                id = 1
            )
            coEvery { truckUseCases.addTruck(truck) } just Runs
            viewModel.recentlyDeletedTruck = truck
            val event = TrucksEvent.RestoreTruck

            viewModel.onEvent(event)

            coVerify { truckUseCases.addTruck(truck) }
            assert(viewModel.recentlyDeletedTruck == null)
        }

    @Test
    fun `onEvent with TrucksEvent ToggleOrderSection should toggle isOrderSectionVisible`() {
        val currentState = TrucksState(isOrderSectionVisible = false)
        viewModel.setState(currentState)
        val event = TrucksEvent.ToggleOrderSection

        viewModel.onEvent(event)

        assert(viewModel.state.value.isOrderSectionVisible)
    }
}