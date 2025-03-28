package com.example.fn_mobile_01

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            Home(navController)
        }
        composable(Screen.MyPage1.route) {
            MyPage1(navController)
        }
        composable(Screen.MyPage2.route) {
            MyPage2(navController)
        }
    }
}
