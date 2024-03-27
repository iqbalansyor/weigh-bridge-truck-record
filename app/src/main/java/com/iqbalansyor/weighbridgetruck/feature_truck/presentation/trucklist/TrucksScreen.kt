package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.iqbalansyor.weighbridgetruck.util.TestTags
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.component.OrderSection
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.component.TruckItem
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.util.Screen
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
    val fabHeight = 72.dp // FabSize+Padding
    val fabHeightPx = with(LocalDensity.current) { fabHeight.roundToPx().toFloat() }
    val fabOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = fabOffsetHeightPx.value + delta
                fabOffsetHeightPx.value = newOffset.coerceIn(-fabHeightPx, 0f)

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
                    .offset { IntOffset(x = 0, y = -fabOffsetHeightPx.value.roundToInt()) },
                onClick = {
                    navController.navigate(Screen.AddEditTruckScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir nota")
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
                IconButton(
                    onClick = {
                        viewModel.onEvent(TrucksEvent.ToggleOrderSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Ordenar"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .testTag(TestTags.ORDER_SECTION),
                    truckOrder = state.truckOrder,
                    onOrderChange = {
                        viewModel.onEvent(TrucksEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.trucks) { truck ->
                    TruckItem(
                        truck = Truck(
                            licenseNumber = "BA 5555 II",
                            driver = "Driver jon",
                            timestamp = System.currentTimeMillis(),
                            inboundWeight = 0.0f,
                            outboundWeight = 1.0f,
                            id = 1
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditTruckScreen.route +
                                            "?truckId=${1}"
                                )
                            },
                        onEditClick = {
                            viewModel.onEvent(
                                TrucksEvent.DeleteTruck(
                                    Truck(
                                        licenseNumber = "license",
                                        driver = "driver",
                                        timestamp = System.currentTimeMillis(),
                                        inboundWeight = 0.0f,
                                        outboundWeight = 1.0f,
                                        id = 1
                                    )
                                )
                            )
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Nota eliminada",
                                    actionLabel = "Deshacer"
                                )
                                if (result == SnackbarResult.ActionPerformed) {

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