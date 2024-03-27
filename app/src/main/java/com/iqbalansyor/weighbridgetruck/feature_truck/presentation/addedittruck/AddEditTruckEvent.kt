package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck

import androidx.compose.ui.focus.FocusState

sealed class AddEditTruckEvent {
    data class EnteredTruckLicense(val value: String) : AddEditTruckEvent()
    data class ChangeTruckLicenseFocus(val focusState: FocusState) : AddEditTruckEvent()
    data class EnteredDriver(val value: String) : AddEditTruckEvent()
    data class ChangeDriverFocus(val focusState: FocusState) : AddEditTruckEvent()
    data class EnteredInboundWeight(val value: String) : AddEditTruckEvent()
    data class ChangeInboundWeightFocus(val focusState: FocusState) : AddEditTruckEvent()
    data class EnteredOutboundWeight(val value: String) : AddEditTruckEvent()
    data class ChangeOutboundWeightFocus(val focusState: FocusState) : AddEditTruckEvent()
    object SaveNote : AddEditTruckEvent()
}