package com.iqbalansyor.weighbridgetruck.feature_truck.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Truck(
    val licenseNumber: String,
    val driver: String,
    val timestamp: Long,
    val inboundWeight: Float,
    val outboundWeight: Float,
    @PrimaryKey val id: Int? = null
) {
    fun getDisplayDate() : String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(date)
    }
}

class InvalidTruckException(message: String) : Exception(message)