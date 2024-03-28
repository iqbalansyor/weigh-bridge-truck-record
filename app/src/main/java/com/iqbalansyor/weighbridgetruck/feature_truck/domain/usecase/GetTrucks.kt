package com.iqbalansyor.weighbridgetruck.feature_truck.domain.usecase

import com.iqbalansyor.weighbridgetruck.feature_truck.domain.model.Truck
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.repository.TruckRepository
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrucks(
    private val repository: TruckRepository
) {

    operator fun invoke(
        truckOrder: TruckOrder = TruckOrder.Date(OrderType.Descending)
    ): Flow<List<Truck>> {
        return repository.getTrucks().map { trucks ->
            when (truckOrder.orderType) {
                is OrderType.Ascending -> {
                    when (truckOrder) {
                        is TruckOrder.Id -> trucks.sortedBy { it.id }
                        is TruckOrder.Date -> trucks.sortedBy { it.timestamp }
                    }
                }

                is OrderType.Descending -> {
                    when (truckOrder) {
                        is TruckOrder.Id -> trucks.sortedByDescending { it.id }
                        is TruckOrder.Date -> trucks.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}