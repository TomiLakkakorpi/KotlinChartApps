package com.example.graphingcalculators

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }

        //
        composable(
            route = Screen.UserInputExample1Screen.route
        ) {
            UserInputExample1Screen(navController)
        }

        //
        composable(
            route = Screen.GraphingCalculatorScreen1.route
        ) {
            GraphingCalculatorScreen1(navController)
        }

        //
        composable(
            route = Screen.GraphingCalculatorScreen2.route
        ) {
            GraphingCalculatorScreen2(navController)
        }

        composable(
            route = Screen.GraphingCalculatorScreen3.route
        ) {
            GraphingCalculatorScreen3(navController)
        }

        composable(
            route = Screen.GraphingCalculatorScreen4.route
        ) {
            GraphingCalculatorScreen4(navController)
        }

        composable(
            route = Screen.GraphingCalculatorScreen5.route
        ) {
            GraphingCalculatorScreen5(navController)
        }

        composable(
            route = Screen.GraphingCalculatorScreen6.route
        ) {
            GraphingCalculatorScreen6(navController)
        }

        composable(
            route = Screen.GraphingCalculatorScreen7.route
        ) {
            GraphingCalculatorScreen7(navController)
        }

        composable(
            route = Screen.GraphingCalculatorScreen8.route
        ) {
            GraphingCalculatorScreen8(navController)
        }
    }
}