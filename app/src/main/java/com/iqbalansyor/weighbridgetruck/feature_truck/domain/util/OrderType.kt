package com.iqbalansyor.weighbridgetruck.feature_truck.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}