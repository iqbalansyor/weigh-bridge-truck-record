package com.iqbalansyor.weighbridgetruck.feature_truck.presentation.trucklist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    truckOrder: TruckOrder = TruckOrder.Date(OrderType.Descending),
    onOrderChange: (TruckOrder) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DefaultRadioButton(
                text = "Older",
                selected = truckOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(truckOrder.copy(OrderType.Ascending))
                },
                textStyle = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic,
                image = Icons.Default.ArrowUpward,
                imageDescription = "Asc"
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Newest",
                selected = truckOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(truckOrder.copy(OrderType.Descending))
                },
                textStyle = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic,
                image = Icons.Default.ArrowDownward,
                imageDescription = "Des"
            )
        }
    }
}