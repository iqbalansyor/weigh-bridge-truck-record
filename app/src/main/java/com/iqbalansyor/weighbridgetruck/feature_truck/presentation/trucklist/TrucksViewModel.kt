package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrucksViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(TrucksState())
    val state = _state.asStateFlow()

    var recentlyDeletedTruck: Truck? = null

    private var getTrucksJob: Job? = null

    init {
        getNotes(TruckOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: TrucksEvent) {
        when (event) {
            is TrucksEvent.Order -> {
                if (state.value.truckOrder::class == event.truckOrder::class &&
                    state.value.truckOrder.orderType == event.truckOrder.orderType
                ) {
                    return
                }
                getNotes(event.truckOrder)
            }

            is TrucksEvent.DeleteTruck -> {
                viewModelScope.launch {

                }
            }

            is TrucksEvent.RestoreNote -> {
                viewModelScope.launch {

                }
            }

            is TrucksEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    fun getNotes(order: TruckOrder) {
        getTrucksJob?.cancel()
//        getTrucksJob = truckUseCase.getNotes(order)
//            .onEach { truck ->
//                _state.value = state.value.copy(
//                    truck = truck,
//                    truckOrder = order
//                )
//            }
//            .launchIn(viewModelScope)

        GlobalScope.launch {
            println("Before delay")
            delay(3000) // Simulate a delay of 3 seconds
            _state.value = state.value.copy(
                trucks = listOf(
                    "A", "B", "C"
                ),
                truckOrder = order
            )
        }
    }

    fun setState(state: TrucksState) {
        _state.value = state
    }
}