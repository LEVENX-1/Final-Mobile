package com.example.fn_mobile_01

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("name") val name: String,

    @Expose
    @SerializedName("size") val size: String,

    @Expose
    @SerializedName("number") val number: Int,

    @Expose
    @SerializedName("topping") val topping: String,

    @Expose
    @SerializedName("sweetness") val sweetness: String,

    @Expose
    @SerializedName("price") val price: String
) : Parcelable
