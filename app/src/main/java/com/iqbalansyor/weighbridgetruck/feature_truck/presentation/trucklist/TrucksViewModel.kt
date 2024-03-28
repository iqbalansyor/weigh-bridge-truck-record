package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrucksViewModel @Inject constructor(
    private val truckUseCases: TruckUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TrucksState())
    val state = _state.asStateFlow()

    var recentlyDeletedTruck: Truck? = null

    private var getTrucksJob: Job? = null

    init {
        getTrucks(state.value.truckOrder)
    }

    fun onEvent(event: TrucksEvent) {
        when (event) {
            is TrucksEvent.Order -> {
                if (state.value.truckOrder::class == event.truckOrder::class &&
                    state.value.truckOrder.orderType == event.truckOrder.orderType
                ) {
                    return
                }
                _state.value = state.value.copy(
                    truckOrder = event.truckOrder
                )
                getTrucks(event.truckOrder)
            }

            is TrucksEvent.DeleteTruck -> {
                viewModelScope.launch {
                    truckUseCases.deleteTruck(event.truck)
                    recentlyDeletedTruck = event.truck
                }
            }

            is TrucksEvent.RestoreTruck -> {
                viewModelScope.launch {
                    truckUseCases.addTruck(recentlyDeletedTruck ?: return@launch)
                    recentlyDeletedTruck = null
                }
            }

            is TrucksEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    fun getTrucks(order: TruckOrder) {
        getTrucksJob?.cancel()
        getTrucksJob = truckUseCases.getTrucks(order)
            .onEach { trucks ->
                _state.value = state.value.copy(
                    trucks = trucks,
                    truckOrder = order
                )
            }
            .launchIn(viewModelScope)
    }

    fun setState(state: TrucksState) {
        _state.value = state
    }
}