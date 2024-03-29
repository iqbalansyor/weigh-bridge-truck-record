package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucks

import androidx.lifecycle.SavedStateHandle
import com.iqbalansyor.weighbridgetruck.MainDispatcherRule
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck.AddEditTruckEvent
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck.AddEditTruckViewModel
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.TrucksEvent
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class AddEditTruckViewModelTest {

    // Mocks
    @RelaxedMockK
    private lateinit var truckUseCases: TruckUseCases

    // ViewModel
    private lateinit var viewModel: AddEditTruckViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = AddEditTruckViewModel(truckUseCases, SavedStateHandle())
    }

    @Test
    fun `onEvent with EnteredTruckLicense should set license number with uppercase`() = run {
        val testValue = "b1234xz"
        viewModel.onEvent(AddEditTruckEvent.EnteredTruckLicense(testValue))
        assert(viewModel.truckLicense.value.text == testValue.uppercase(Locale.ROOT))
    }

    @Test
    fun `onEvent with EnteredDriver should set driver value`() = run {
        val testValue = "test"
        viewModel.onEvent(AddEditTruckEvent.EnteredDriver("test"))
        assert(viewModel.driver.value.text == testValue)
    }

    @Test
    fun `onEvent with EnteredInboundWeight should set inbound weight value`() = run {
        val testValue = 10.toString()
        viewModel.onEvent(AddEditTruckEvent.EnteredInboundWeight(testValue))
        assert(viewModel.inboundWeight.value.text == testValue)
    }

    @Test
    fun `onEvent with EnteredOutbounddWeight should set outbound weight value`() = run {
        val testValue = 10.toString()
        viewModel.onEvent(AddEditTruckEvent.EnteredOutboundWeight(testValue))
        assert(viewModel.outboundWeight.value.text == testValue)
    }

    @Test
    fun `onEvent with EnteredOutbounddWeight & EnteredOutbounddWeight should set net weight value`() = run {
        val inboundTestValue = 10.toString()
        val outboundTestValue = 20.toString()
        viewModel.onEvent(AddEditTruckEvent.EnteredOutboundWeight(outboundTestValue))
        viewModel.onEvent(AddEditTruckEvent.EnteredInboundWeight(inboundTestValue))
        val inboundWeight = inboundTestValue.toFloatOrNull() ?: 0.0f
        val outboundWeight = outboundTestValue.toFloatOrNull() ?: 0.0f
        assert(viewModel.netWeight.value.text == (outboundWeight - inboundWeight).toString())
    }

    // TODO: Fix test
//    @Test
//    fun `onEvent with TrucksEvent SaveTruck should invoke usecase SaveTruck`() =
//        runTest {
//            val licenseTestValue = "b1234xz"
//            val driverTestValue = "driver"
//            val timestampTestValue = 1.toLong()
//            val inboundTestValue = 1.0f
//            val outboundTestValue = 2.0f
//            val idTestValue = 1
//            val truck = Truck(
//                licenseNumber = licenseTestValue,
//                driver = driverTestValue,
//                timestamp = timestampTestValue,
//                inboundWeight = inboundTestValue,
//                outboundWeight = outboundTestValue,
//                id = idTestValue
//            )
//            coEvery { truckUseCases.addTruck(truck) } just Runs
//
//            viewModel.onEvent(AddEditTruckEvent.EnteredTruckLicense(licenseTestValue))
//            viewModel.onEvent(AddEditTruckEvent.EnteredDriver(driverTestValue))
//            viewModel.onEvent(AddEditTruckEvent.EnteredInboundWeight(inboundTestValue.toString()))
//            viewModel.onEvent(AddEditTruckEvent.EnteredOutboundWeight(outboundTestValue.toString()))
//
//            val event = AddEditTruckEvent.SaveTruck
//            viewModel.onEvent(event)
//
//            coVerify { truckUseCases.addTruck(truck) }
//        }
}