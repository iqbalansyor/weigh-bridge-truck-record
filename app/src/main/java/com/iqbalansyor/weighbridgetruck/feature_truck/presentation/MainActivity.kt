package com.iqbalansyor.weighbridgetruck.feature_truck.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.TrucksScreen
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.TrucksViewModel
import com.iqbalansyor.weighbridgetruck.feature_truck.presentation.util.Screen
import com.iqbalansyor.weighbridgetruck.ui.theme.WTruckTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WTruckTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: TrucksViewModel = hiltViewModel()
                    val state = viewModel.state.collectAsState().value
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TruckListScreen.route
                    ) {
                        composable(route = Screen.TruckListScreen.route) {
                            TrucksScreen(navController = navController, state = state)
                        }
                        // TODO: Add Edit Truck here
                    }
                }
            }
        }
    }
}