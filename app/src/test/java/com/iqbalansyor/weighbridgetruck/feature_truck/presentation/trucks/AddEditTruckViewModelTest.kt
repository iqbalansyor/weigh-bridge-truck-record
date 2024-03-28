package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucks

import androidx.lifecycle.SavedStateHandle
import com.iqbalansyor.weighbridgetruck.MainDispatcherRule
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck.AddEditTruckEvent
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck.AddEditTruckViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
}