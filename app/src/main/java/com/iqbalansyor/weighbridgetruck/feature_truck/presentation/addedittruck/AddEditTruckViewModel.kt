package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.InvalidTruckException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTruckViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _truckLicense = mutableStateOf(
        TruckTextFieldState(
            hint = "Truck License Number"
        )
    )
    val truckLicense: State<TruckTextFieldState> = _truckLicense

    private val _driver = mutableStateOf(
        TruckTextFieldState(
            hint = "Driver name"
        )
    )
    val driver: State<TruckTextFieldState> = _driver

    private val _inboundWeight = mutableStateOf(
        TruckTextFieldState(
            hint = "Inbound Weight"
        )
    )
    val inboundWeight: State<TruckTextFieldState> = _inboundWeight

    private val _outboundWeight = mutableStateOf(
        TruckTextFieldState(
            hint = "Outbound Weight"
        )
    )
    val outboundWeight: State<TruckTextFieldState> = _outboundWeight

    private val _netWeight = mutableStateOf(
        TruckTextFieldState(
            hint = "Net Weight"
        )
    )
    val netWeight: State<TruckTextFieldState> = _netWeight

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    // todo handle
                }
            }
        }
    }

    fun onEvent(event: AddEditTruckEvent) {
        when (event) {
            is AddEditTruckEvent.EnteredTruckLicense -> {
                _truckLicense.value = truckLicense.value.copy(
                    text = event.value
                )
            }

            is AddEditTruckEvent.ChangeTruckLicenseFocus -> {
                _truckLicense.value = _truckLicense.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _truckLicense.value.text.isBlank()
                )
            }

            is AddEditTruckEvent.EnteredDriver -> {
                _driver.value = _driver.value.copy(
                    text = event.value
                )
            }

            is AddEditTruckEvent.ChangeDriverFocus -> {
                _driver.value = _driver.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _driver.value.text.isBlank()
                )
            }

            is AddEditTruckEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        // todo
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidTruckException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Somethings wrong"
                            )
                        )
                    }
                }
            }

            is AddEditTruckEvent.EnteredInboundWeight -> {
                _inboundWeight.value = _inboundWeight.value.copy(
                    text = event.value
                )
                calculateNetWeight()
            }

            is AddEditTruckEvent.ChangeInboundWeightFocus -> {
                _inboundWeight.value = _inboundWeight.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _inboundWeight.value.text.isBlank()
                )
                calculateNetWeight()
            }

            is AddEditTruckEvent.EnteredOutboundWeight -> {
                _outboundWeight.value = _outboundWeight.value.copy(
                    text = event.value
                )
                calculateNetWeight()
            }

            is AddEditTruckEvent.ChangeOutboundWeightFocus -> {
                _outboundWeight.value = _outboundWeight.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _outboundWeight.value.text.isBlank()
                )
                calculateNetWeight()
            }
        }
    }

    private fun calculateNetWeight() {
        val inboundWeight = _inboundWeight.value.text.toFloatOrNull() ?: 0.0f
        val outboundWeight = _outboundWeight.value.text.toFloatOrNull() ?: 0.0f
        if (outboundWeight < inboundWeight) {
            _netWeight.value = _netWeight.value.copy(
                text = "outbound wieght should greater than inbound",
                isError = true
            )
        } else {
            _netWeight.value = _netWeight.value.copy(
                text = (outboundWeight - inboundWeight).toString(),
                isError = false
            )
        }
    }
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}