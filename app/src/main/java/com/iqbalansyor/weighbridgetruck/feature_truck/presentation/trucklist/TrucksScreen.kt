package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.component.OrderSection
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.component.TruckItem
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.util.Screen
import com.iqbalansyor.weighbridgetruck.util.TestTags
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun TrucksScreen(
    navController: NavController,
    state: TrucksState,
    viewModel: TrucksViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Fab scroll auto hide
    val fabHeight = 72.dp
    val fabHeightPx = with(LocalDensity.current) { fabHeight.roundToPx().toFloat() }
    val fabOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = fabOffsetHeightPx.floatValue + delta
                fabOffsetHeightPx.floatValue = newOffset.coerceIn(-fabHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    Scaffold(
        Modifier
            .nestedScroll(nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset { IntOffset(x = 0, y = -fabOffsetHeightPx.floatValue.roundToInt()) },
                onClick = {
                    navController.navigate(Screen.AddEditTruckScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add truck")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    end = 16.dp,
                    start = 16.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weigh Bridge Truck",
                    style = MaterialTheme.typography.h5
                )
            }
            if (state.trucks.isNotEmpty()) {
                AnimatedVisibility(
                    visible = true
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .testTag(TestTags.ORDER_SECTION),
                        truckOrder = state.truckOrder,
                        onOrderChange = {
                            viewModel.onEvent(TrucksEvent.Order(it))
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (state.trucks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No record so far, add with + button below",
                        style = MaterialTheme.typography.h6
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.trucks) { truck ->
                        TruckItem(
                            truck = Truck(
                                licenseNumber = truck.licenseNumber,
                                driver = truck.driver,
                                timestamp = truck.timestamp,
                                inboundWeight = truck.inboundWeight,
                                outboundWeight = truck.outboundWeight,
                                id = truck.id
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screen.AddEditTruckScreen.route +
                                                "?truckId=${truck.id}"
                                    )
                                },
                            onDeleteClick = {
                                viewModel.onEvent(TrucksEvent.DeleteTruck(truck))
                                scope.launch {
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        message = "Truck deleted",
                                        actionLabel = "restore"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(TrucksEvent.RestoreTruck)
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}