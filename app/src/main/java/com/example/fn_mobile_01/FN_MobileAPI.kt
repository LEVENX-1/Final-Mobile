package com.example.fn_mobile_01

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FN_MobileAPI {
    @GET("AllOrder")
    fun retrieveAllOrder(): Call<List<Order>>

    @GET("OneOrder")
    fun retrieveOneOrder(
        @Query("id") id: Int
    ): Call<Order>

    @POST("InsertOrder")
    fun insertOrder(@Body order: Order): Call<Order>

    companion object {
        fun create(): FN_MobileAPI {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FN_MobileAPI::class.java)
        }
    }
}
