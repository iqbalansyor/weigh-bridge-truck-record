package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iqbalansyor.weighbridgetruck.core.util.TestTags
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.addedittruck.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditTruckScreen(
    navController: NavController,
    viewModel: AddEditTruckViewModel = hiltViewModel()
) {
    val truckLicenseState = viewModel.truckLicense.value
    val driverState = viewModel.driver.value
    val inboundWeightState = viewModel.inboundWeight.value
    val outboundWeightState = viewModel.outboundWeight.value
    val netWeightState = viewModel.netWeight.value

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditTruckViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is AddEditTruckViewModel.UiEvent.SaveTruck -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditTruckEvent.SaveTruck)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Truck Form",
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = truckLicenseState.text,
                hint = truckLicenseState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTruckEvent.EnteredTruckLicense(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditTruckEvent.ChangeTruckLicenseFocus(it))
                },
                singleLine = true,
                testTag = TestTags.LICENSE_TEXT_FIELD
            )
            TransparentHintTextField(
                text = driverState.text,
                hint = driverState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTruckEvent.EnteredDriver(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditTruckEvent.ChangeDriverFocus(it))
                },
                testTag = TestTags.DRIVER_TEXT_FIELD,
            )
            TransparentHintTextField(
                text = inboundWeightState.text,
                hint = inboundWeightState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTruckEvent.EnteredInboundWeight(it))
                },
                placeholder = "0",
                onFocusChange = {
                    viewModel.onEvent(AddEditTruckEvent.ChangeInboundWeightFocus(it))
                },
                testTag = TestTags.INBOUND_WEIGHT_TEXT_FIELD,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            TransparentHintTextField(
                text = outboundWeightState.text,
                hint = outboundWeightState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditTruckEvent.EnteredOutboundWeight(it))
                },
                placeholder = "0",
                onFocusChange = {
                    viewModel.onEvent(AddEditTruckEvent.ChangeOutboundWeightFocus(it))
                },
                testTag = TestTags.OUTBOUND_WEIGHT_TEXT_FIELD,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            TransparentHintTextField(
                text = netWeightState.text,
                hint = netWeightState.hint,
                errorMessage = if (netWeightState.isError) "error" else null,
                onValueChange = {},
                placeholder = "0",
                onFocusChange = {},
                testTag = TestTags.OUTBOUND_WEIGHT_TEXT_FIELD,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                readOnly = true,
            )
        }
    }
}