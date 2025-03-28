package com.example.fn_mobile_01

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import retrofit2.Call

@Composable
fun MyPage2(navController: NavController) {
    val orderId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("orderId")
    println(orderId)
    // ตรวจสอบว่าได้รับ `orderId` หรือไม่
    if (orderId == null) {
        Text(text = "Order ID is missing", color = Color.Red)
        return
    }

    // ใช้ ViewModel หรือ State เพื่อจัดการข้อมูล
    val context = LocalContext.current
    val order = remember { mutableStateOf<Order?>(null) }
    val isLoading = remember { mutableStateOf(true) } // สถานะการโหลดข้อมูล

    // ดึงข้อมูลออเดอร์จาก API เมื่อ `orderId` ถูกส่งมา
    LaunchedEffect(orderId) {
        val apiService = FN_MobileAPI.create() // Retrofit Client
        // เรียก API โดยใช้ enqueue
        apiService.retrieveOneOrder(orderId).enqueue(object : retrofit2.Callback<Order> { // เปลี่ยนจาก List<Order> เป็น Order
            override fun onResponse(
                call: Call<Order>,
                response: retrofit2.Response<Order>
            ) {
                if (response.isSuccessful) {
                    order.value = response.body() // ดึงข้อมูลจาก body และเก็บใน order
                    isLoading.value = false // อัปเดตสถานะการโหลดเมื่อเสร็จ
                } else {
                    order.value = null
                    isLoading.value = false
                    Log.e("API Error", "Failed to fetch order: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                order.value = null
                isLoading.value = false
                Log.e("API Error", "Error: ${t.message}")
            }
        })
    }

    // ตรวจสอบสถานะของข้อมูล
    val data = order.value ?: Order(0, "", "", 0, "","","")
    val toppingSelect = data.topping
    val sizeSelect = data.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(
            modifier = Modifier.size(100.dp),
            onClick = {
//                navController.navigateUp()
            }
        ) {
//            Icon(
//                Icons.AutoMirrored.Filled.ArrowBack,
//                contentDescription = "",
//                tint = Color.Magenta
//            )
        }

        Spacer(modifier = Modifier.height(height = 20.dp))
        Text(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            text = "Check Information",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )

        // ตรวจสอบว่าได้ข้อมูลออเดอร์มาแล้วหรือยัง
        if (isLoading.value) {
            // กรณีที่ยังไม่ได้รับข้อมูลหรือกำลังโหลด
            Text(text = "Loading order...", fontSize = 20.sp, color = Color.Gray)
        } else if (order.value != null) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = """
                    Order ID: ${data.id}
                    
                    Customer Name: ${data.name}
                    
                    Cup Size: ${sizeSelect}
                    
                    Topping: $toppingSelect
                    
                    Number of Cups: ${data.number}
                    
                    Sweetness: ${data.sweetness}
                    
                    Price: ${data.price}

                """.trimIndent(),
                fontSize = 20.sp
            )
        } else {
            // กรณีที่ไม่ได้รับข้อมูลออเดอร์หรือเกิดข้อผิดพลาดในการดึงข้อมูล
            Text(text = "No matching order found", fontSize = 20.sp, color = Color.Red)
        }

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { navController.navigateUp() }
        ) {
            Text(text = "Close")
        }
    }
}
