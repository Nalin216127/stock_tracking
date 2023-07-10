package com.example.task_02.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "smart_phone")
data class SmartPhone(
    @PrimaryKey
    @SerializedName("productCode")
    val productCode:String,
    @SerializedName("productName")
    val productName:String,
    @SerializedName("price")
    val price:Int,
    @SerializedName("quantity")
    var quantity:Int = 0
)