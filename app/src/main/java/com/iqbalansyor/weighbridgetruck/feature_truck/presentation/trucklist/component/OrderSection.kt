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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.OrderType
import com.iqbalansyor.weighbridgetruck.feature_truck.domain.util.TruckOrder

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    truckOrder: TruckOrder = TruckOrder.Date(OrderType.Descending),
    onOrderChange: (TruckOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DefaultRadioButton(
                text = "Título",
                selected = truckOrder is TruckOrder.Title,
                onSelect = { onOrderChange(TruckOrder.Title(truckOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Fecha",
                selected = truckOrder is TruckOrder.Date,
                onSelect = { onOrderChange(TruckOrder.Date(truckOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))

        }
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colors.primary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DefaultRadioButton(
                text = "Ascendente",
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
                text = "Descendente",
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