package com.example.fn_mobile_01

sealed class Screen (val route: String, val name: String) {
    data object Home : Screen("Home","Home")
    data object MyPage1 : Screen("MyPage1","หน้า 1")
    data object MyPage2 : Screen("MyPage2","หน้า 2")
}