package com.iqbalansyor.weighbridgetruck.feature_truck.domain.util

sealed class TruckOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): TruckOrder(orderType)
    class Date(orderType: OrderType): TruckOrder(orderType)

    fun copy(orderType: OrderType): TruckOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}