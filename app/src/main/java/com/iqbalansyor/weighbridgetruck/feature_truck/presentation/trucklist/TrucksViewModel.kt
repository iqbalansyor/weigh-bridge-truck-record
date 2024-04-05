package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.reflect.TypeToken
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
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
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("/list")

    var recentlyDeletedTruck: Truck? = null

    private var getTrucksJob: Job? = null

    lateinit var order: TruckOrder

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

                // TODO: Move to another repository
                ref.removeValue()
                    .addOnSuccessListener {
                        Log.d(TAG, "Item deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error deleting item", e)
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

            else -> {}
        }
    }

    fun getTrucks(order: TruckOrder) {
        getTrucksJob?.cancel()

        this.order = order
        getTrucksJob = truckUseCases.getTrucks(order)
            .onEach { trucks ->
                _state.value = state.value.copy(
                    trucks = trucks,
                    truckOrder = order
                )
            }
            .launchIn(viewModelScope)

        getRemoteTruck()
    }

    private fun getRemoteTruck() {
        // TODO: Move to another repository
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataArray = mutableListOf<Truck>()

                for (childSnapshot in dataSnapshot.children) {
                    val data = childSnapshot.getValue(Truck::class.java)
                    data?.let {
                        dataArray.add(it)
                        Log.d(TAG, "Value is: $it")

                        viewModelScope.launch(Dispatchers.IO) {
                            truckUseCases.addTruck(it)
                        }
                    }
                }
                _state.value = state.value.copy(
                    trucks = dataArray,
                    truckOrder = order
                )

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun setState(state: TrucksState) {
        _state.value = state
    }
}