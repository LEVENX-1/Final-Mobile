package com.example.fn_mobile_01

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val orderItemsList = remember { mutableStateListOf<Order>() }
    var searchText by remember { mutableStateOf("") }
    val contextForToast = LocalContext.current
    val filteredList by remember {
        derivedStateOf {
            if (searchText.isBlank()) {
                orderItemsList
            } else {
                orderItemsList.filter {
                    it.name.contains(searchText, ignoreCase = true) ||
                            it.id.toString().contains(searchText)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        showAllOrders(orderItemsList, contextForToast)
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Orders",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search Bar with Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE0E0E0),
                                    Color(0xFFF5F5F5)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFBDBDBD),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color(0xFF757575),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            textStyle = TextStyle(
                                color = Color(0xFF212121),
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.weight(1f),
                            decorationBox = { innerTextField ->
                                if (searchText.isEmpty()) {
                                    Text(
                                        text = "Search orders...",
                                        color = Color(0xFF9E9E9E),
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                // Orders Grid with Improved Card Design
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(filteredList) { order ->
                        OrderCard(
                            order = order,
                            navController = navController  // Pass the navController here
                        )
                    }
                }
            }

            // Floating Action Button with Shadow
            FloatingActionButton(
                onClick = {
                    // Navigate to MyPage1
                    navController.navigate(Screen.MyPage1.route)
                },
                containerColor = Color(0xFF2ECC71),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        clip = true
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Order",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                // ส่งข้อมูล orderId ไปยังหน้าถัดไป
                navController.currentBackStackEntry?.savedStateHandle?.set("orderId", order.id)
                // นำทางไปยังหน้าถัดไป
                navController.navigate(Screen.MyPage2.route)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Order #${order.id}",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OrderDetailRow("ชื่อ", order.name)
            OrderDetailRow("ขนาดแก้ว", order.size)
            OrderDetailRow("ท็อปปิ้ง", order.topping)
            OrderDetailRow("จำนวน", "${order.number} แก้ว")
        }
    }
}



@Composable
fun OrderDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF455A64)
            ),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF37474F)
            ),
            modifier = Modifier.weight(0.6f)
        )
    }
}

fun showAllOrders(OrderItemsList: MutableList<Order>, contextForToast: android.content.Context) {
    val createClient = FN_MobileAPI.create()
    createClient.retrieveAllOrder().enqueue(object : Callback<List<Order>> {
        override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    OrderItemsList.clear()
                    OrderItemsList.addAll(data)
                }
            } else {
                Toast.makeText(contextForToast, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<List<Order>>, t: Throwable) {
            Toast.makeText(contextForToast, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
