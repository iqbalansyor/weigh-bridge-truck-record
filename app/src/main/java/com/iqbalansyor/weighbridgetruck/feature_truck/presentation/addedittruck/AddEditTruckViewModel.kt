package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.InvalidTruckException
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase.TruckUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditTruckViewModel @Inject constructor(
    private val truckUseCases: TruckUseCases,
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
            hint = "Inbound Weight (Ton)"
        )
    )
    val inboundWeight: State<TruckTextFieldState> = _inboundWeight

    private val _outboundWeight = mutableStateOf(
        TruckTextFieldState(
            hint = "Outbound Weight (Ton)"
        )
    )
    val outboundWeight: State<TruckTextFieldState> = _outboundWeight

    private val _netWeight = mutableStateOf(
        TruckTextFieldState(
            hint = "Net Weight (Ton)"
        )
    )
    val netWeight: State<TruckTextFieldState> = _netWeight

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentTruckId: Int? = null

    init {
        savedStateHandle.get<Int>("truckId")?.let { truckId ->
            if (truckId != -1) {
                viewModelScope.launch {
                    truckUseCases.getTruck(truckId)?.also { truck ->
                        currentTruckId = truck.id
                        _truckLicense.value = _truckLicense.value.copy(
                            text = truck.licenseNumber,
                            isHintVisible = false
                        )
                        _driver.value = _driver.value.copy(
                            text = truck.driver,
                            isHintVisible = false
                        )
                        _inboundWeight.value = _inboundWeight.value.copy(
                            text = truck.inboundWeight.toString(),
                            isHintVisible = false
                        )
                        _outboundWeight.value = _outboundWeight.value.copy(
                            text = truck.outboundWeight.toString(),
                            isHintVisible = false
                        )
                        _netWeight.value = _outboundWeight.value.copy(
                            text = (truck.outboundWeight - truck.inboundWeight).toString(),
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditTruckEvent) {
        when (event) {
            is AddEditTruckEvent.EnteredTruckLicense -> {
                val eventText = event.value.toUpperCase(Locale.ROOT)
                validateLicenseNumberFormat(eventText)
            }

            is AddEditTruckEvent.ChangeTruckLicenseFocus -> {
                _truckLicense.value = _truckLicense.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _truckLicense.value.text.isBlank()
                )
            }

            is AddEditTruckEvent.EnteredDriver -> {
                if (event.value.length < 4) {
                    _driver.value = driver.value.copy(
                        isError = true,
                        errorMessage = "Driver name should be more than 5 char"
                    )
                } else {
                    _driver.value = driver.value.copy(
                        isError = false,
                        errorMessage = ""
                    )
                }
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

            is AddEditTruckEvent.SaveTruck -> {
                viewModelScope.launch {
                    try {
                        if (_truckLicense.value.isError || _truckLicense.value.text.isBlank()
                            || _driver.value.isError || _driver.value.text.isBlank()
                            || inboundWeight.value.isError || inboundWeight.value.text.isBlank()
                            || outboundWeight.value.isError || outboundWeight.value.text.isBlank()
                            || netWeight.value.isError
                        ) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = "Please check your input"
                                )
                            )
                        } else {
                            truckUseCases.addTruck(
                                Truck(
                                    licenseNumber = _truckLicense.value.text,
                                    driver = _driver.value.text,
                                    timestamp = System.currentTimeMillis(),
                                    inboundWeight = _inboundWeight.value.text.toFloatOrNull()
                                        ?: 0.0f,
                                    outboundWeight = _outboundWeight.value.text.toFloatOrNull()
                                        ?: 0.0f,
                                    id = if (currentTruckId == null) System.currentTimeMillis()
                                        .toInt() else currentTruckId
                                )
                            )

                            _eventFlow.emit(UiEvent.SaveTruck)
                        }
                    } catch (e: InvalidTruckException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Please check your input"
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
                isError = true,
                errorMessage = "outbound wieght should greater than inbound"
            )
        } else {
            _netWeight.value = _netWeight.value.copy(
                text = (outboundWeight - inboundWeight).toString(),
                isError = false,
                errorMessage = ""
            )
        }
    }

    private fun validateLicenseNumberFormat(eventText: String) {
        // check for 1 char
        val firstChar = eventText.firstOrNull()
        val isOnlyOne = eventText.length == 1
        if (isOnlyOne && !Regex("[A-Z]").matches(firstChar.toString())) {
            _truckLicense.value = truckLicense.value.copy(
                isError = true,
                errorMessage = "License should follow B1234XZ format"
            )
            return
        }

        // check for 3 char
        val isTwo = eventText.length == 2
        if (isTwo) {
            val secondChar = eventText[1]
            if (!Regex("[A-Z]").matches(firstChar.toString())
                || !Regex("[0-9]").matches(secondChar.toString())) {
                _truckLicense.value = truckLicense.value.copy(
                    isError = true,
                    errorMessage = "License should follow B1234XZ format"
                )
                return
            }
        }

        // check for 3 char
        val isThree = eventText.length == 3
        if (isThree) {
            val secondChar = eventText[1]
            val thirdChar = eventText[2]
            if (!Regex("[A-Z]").matches(firstChar.toString())
                || !Regex("[0-9]").matches(secondChar.toString())
                || !Regex("[0-9]").matches(thirdChar.toString())) {
                _truckLicense.value = truckLicense.value.copy(
                    isError = true,
                    errorMessage = "License should follow B1234XZ format"
                )
                return
            }
        }

        // check for 4 char
        val isFour = eventText.length == 4
        if (isFour) {
            val secondChar = eventText[1]
            val thirdChar = eventText[2]
            val fourthChar = eventText[3]
            if (!Regex("[A-Z]").matches(firstChar.toString())
                || !Regex("[0-9]").matches(secondChar.toString())
                || !Regex("[0-9]").matches(thirdChar.toString())
                || !Regex("[0-9]").matches(fourthChar.toString())) {
                _truckLicense.value = truckLicense.value.copy(
                    isError = true,
                    errorMessage = "License should follow B1234XZ format"
                )
                return
            }
        }

        // check for 5 char
        val isFive = eventText.length == 5
        if (isFive) {
            val secondChar = eventText[1]
            val thirdChar = eventText[2]
            val fourthChar = eventText[3]
            val fifthChar = eventText[4]
            if (!Regex("[A-Z]").matches(firstChar.toString())
                || !Regex("[0-9]").matches(secondChar.toString())
                || !Regex("[0-9]").matches(thirdChar.toString())
                || !Regex("[0-9]").matches(fourthChar.toString())
                || !Regex("[0-9]").matches(fifthChar.toString())) {
                _truckLicense.value = truckLicense.value.copy(
                    isError = true,
                    errorMessage = "License should follow B1234XZ format"
                )
                return
            }
        }

        // check for 6 char
        val isSix = eventText.length == 6
        if (isSix) {
            val secondChar = eventText[1]
            val thirdChar = eventText[2]
            val fourthChar = eventText[3]
            val fifthChar = eventText[4]
            val sixthChar = eventText[5]
            if (!Regex("[A-Z]").matches(firstChar.toString())
                || !Regex("[0-9]").matches(secondChar.toString())
                || !Regex("[0-9]").matches(thirdChar.toString())
                || !Regex("[0-9]").matches(fourthChar.toString())
                || !Regex("[0-9]").matches(fifthChar.toString())
                || !Regex("[A-Z]").matches(sixthChar.toString())) {
                _truckLicense.value = truckLicense.value.copy(
                    isError = true,
                    errorMessage = "License should follow B1234XZ format"
                )
                return
            }
        }

        // check for 7 char
        val isSeven = eventText.length == 7
        if (isSeven) {
            val secondChar = eventText[1]
            val thirdChar = eventText[2]
            val fourthChar = eventText[3]
            val fifthChar = eventText[4]
            val sixthChar = eventText[5]
            val seventhChar = eventText[6]
            if (!Regex("[A-Z]").matches(firstChar.toString())
                || !Regex("[0-9]").matches(secondChar.toString())
                || !Regex("[0-9]").matches(thirdChar.toString())
                || !Regex("[0-9]").matches(fourthChar.toString())
                || !Regex("[0-9]").matches(fifthChar.toString())
                || !Regex("[A-Z]").matches(sixthChar.toString())
                || !Regex("[A-Z]").matches(seventhChar.toString())) {
                _truckLicense.value = truckLicense.value.copy(
                    isError = true,
                    errorMessage = "License should follow B1234XZ format"
                )
                return
            } else {
                _truckLicense.value = truckLicense.value.copy(
                    isError = false,
                    errorMessage = ""
                )
            }
        }

        if(eventText.length > 7) return

        if(eventText.length != 7) {
            _truckLicense.value = truckLicense.value.copy(
                isError = true,
                errorMessage = "License should follow B1234XZ format"
            )
        }

        _truckLicense.value = truckLicense.value.copy(
            text = eventText
        )
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveTruck : UiEvent()
    }
}