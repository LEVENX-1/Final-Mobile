package com.example.fn_mobile_01

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage1(navController: NavHostController) {
    // ประกาศตัวแปรสำหรับเก็บสถานะต่างๆ ของออเดอร์
    var customerName by remember { mutableStateOf("") } // ชื่อลูกค้า
    var numberOfCups by remember { mutableStateOf("1") } // จำนวนแก้ว
    var selectedToppings by remember { mutableStateOf(listOf<String>()) } // หน้าท็อปปิ้งที่เลือก
    var selectedSize by remember { mutableStateOf("S") } // ขนาดแก้ว
    var selectedSweetness by remember { mutableStateOf("Normal") } // ระดับความหวาน
    var calculatedPrice by remember { mutableStateOf(0.0) } // ราคาที่คำนวณได้
    var expanded by remember { mutableStateOf(false) }

    // ฟังก์ชันคำนวณราคา
    fun calculatePrice(): Double {
        // กำหนดราคาฐานตามขนาดแก้ว
        val sizePrices = mapOf(
            "S" to 35.0, // ขนาดเล็ก 35 บาท
            "M" to 45.0, // ขนาดกลาง 45 บาท
            "L" to 55.0  // ขนาดใหญ่ 55 บาท
        )

        // กำหนดราคาท็อปปิ้ง
        val toppingPrices = mapOf(
            "Brownie" to 10.0, // บราวนี่ 10 บาท
            "Jelly" to 5.0     // เยลลี่ 5 บาท
        )

        // คำนวณราคาฐานจากขนาดแก้ว
        val basePrice = sizePrices[selectedSize] ?: 35.0

        // คำนวณราคาท็อปปิ้ง
        val toppingPrice = selectedToppings.sumOf { toppingPrices[it] ?: 0.0 }

        // คำนวณราคารวมตามจำนวนแก้ว
        val cups = numberOfCups.toIntOrNull() ?: 1
        return (basePrice + toppingPrice) * cups
    }

    // คำนวณราคาใหม่ทุกครั้งที่มีการเปลี่ยนแปลงพารามิเตอร์
    calculatedPrice = calculatePrice()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // แถบหัวด้านบน
        CenterAlignedTopAppBar(
            title = { Text(text = "กรอกออเดอร์", color = Color.Black) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ช่องกรอกชื่อลูกค้า
        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("ชื่อลูกค้า") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // ช่องกรอกจำนวนแก้ว
        OutlinedTextField(
            value = numberOfCups,
            onValueChange = {
                // กรองให้รับเฉพาะตัวเลข และจำกัดความยาวไม่เกิน 2 หลัก
                numberOfCups = it.filter { it.isDigit() }.take(2)
            },
            label = { Text("จำนวนแก้ว") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // เลือกท็อปปิ้ง
        Text(
            text = "ท็อปปิ้ง:",
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row {
            val toppingOptions = listOf("Brownie", "Jelly")
            toppingOptions.forEach { topping ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    // ช่องเลือกท็อปปิ้ง
                    Checkbox(
                        checked = selectedToppings.contains(topping),
                        onCheckedChange = {
                            selectedToppings = if (it) {
                                selectedToppings + topping // เพิ่มท็อปปิ้ง
                            } else {
                                selectedToppings - topping // ลบท็อปปิ้ง
                            }
                        }
                    )
                    Text(text = topping)
                }
            }
        }

        // เลือกขนาดแก้ว
        Text(
            text = "ขนาดแก้ว: $selectedSize",
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val sizeOptions = listOf("S", "M", "L")
            sizeOptions.forEach { size ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    // ปุ่มเลือกขนาดแก้ว
                    RadioButton(
                        selected = selectedSize == size,
                        onClick = { selectedSize = size },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Magenta
                        )
                    )
                    Text(text = size)
                }
            }
        }

        // เลือกระดับความหวาน
        Text(
            text = "ความหวาน: $selectedSweetness",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded, // ใช้สถานะที่เปิด/ปิดเมนู
                onExpandedChange = { expanded = !expanded }, // เปลี่ยนสถานะเมื่อกดที่ช่อง
                modifier = Modifier.fillMaxWidth(0.8f) // จำกัดขนาดให้พอดี
            ) {
                // ช่องกรอก
                OutlinedTextField(
                    value = selectedSweetness,
                    onValueChange = { selectedSweetness = it },
                    label = { Text("เลือกความหวาน") },
                    readOnly = true, // กำหนดให้ไม่สามารถพิมพ์ได้
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                    },
                    modifier = Modifier.menuAnchor()
                )

                // เมนูตัวเลือก
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false } // ปิดเมนูเมื่อคลิกนอกเมนู
                ) {
                    val sweetnessOptions = listOf("Less", "Normal", "More")
                    sweetnessOptions.forEach { sweetness ->
                        DropdownMenuItem(
                            text = { Text(text = sweetness) }, // เพิ่ม text parameter
                            onClick = {
                                selectedSweetness = sweetness
                                expanded = false // ปิดเมนูหลังเลือก
                            }
                        )
                    }
                }
            }
        }

        // แสดงราคารวม
        Text(
            text = "ราคารวม: ${String.format("%.2f", calculatedPrice)} ฿",
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Magenta
        )

        Spacer(modifier = Modifier.height(16.dp))

        // แถวปุ่มส่งออเดอร์และยกเลิก
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // ปุ่มยกเลิก
            OutlinedButton(
                onClick = {
                    navController.navigateUp()  // ย้อนกลับไปหน้าก่อน
                },
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, Color.Red), // กรอบปุ่มสีแดง
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Red // พื้นหลังสีขาว
                )
            ) {
                Text("ยกเลิก", color = Color.White) // ตัวหนังสือสีแดง
            }

            // ปุ่มส่งออเดอร์
            Button(
                onClick = {
                    val createClient = FN_MobileAPI.create()
                    // สร้างออบเจ็กต์ออเดอร์เพื่อส่งไปยัง API
                    val orderData = Order(
                        id = 0,
                        name = customerName,
                        size = selectedSize,
                        number = numberOfCups.toIntOrNull() ?: 1,
                        topping = selectedToppings.joinToString(","),
                        sweetness = selectedSweetness,
                        price = calculatedPrice.toString()
                    )
                    // ส่งข้อมูลออเดอร์ไปยัง API
                    createClient.insertOrder(orderData).enqueue(object : retrofit2.Callback<Order> {
                        override fun onResponse(call: Call<Order>, response: retrofit2.Response<Order>) {
                            if (response.isSuccessful) {
                                // บันทึก ID ออเดอร์เพื่อใช้ในหน้าถัดไป
                                val orderId = response.body()?.id ?: 0
                                println("ส่งออเดอร์สำเร็จ, รหัสออเดอร์: $orderId")

                                navController.currentBackStackEntry?.savedStateHandle?.set("orderId", orderId)

                                // ไปยังหน้าถัดไป
                                navController.navigate(Screen.MyPage2.route)
                            } else {
                                println("เกิดข้อผิดพลาด: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<Order>, t: Throwable) {
                            println("ล้มเหลว: ${t.message}")
                        }
                    })
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green) // กรอบปุ่มสีเขียว
            ) {
                Text("ส่งออเดอร์", color = Color.White) // ตัวหนังสือสีขาว
            }
        }
    }
}

