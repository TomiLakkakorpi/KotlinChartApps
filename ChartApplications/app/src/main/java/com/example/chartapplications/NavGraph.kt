package com.example.chartapplications

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
            route = Screen.BarChartAppV1Screen.route
        ) {
            BarChartAppV1Screen(navController)
        }

        //
        composable(
            route = Screen.LineChartAppV1Screen.route
        ) {
            LineChartAppV1Screen(navController)
        }

        composable(
            route = Screen.WaveChartAppV1Screen.route
        ) {
            WaveChartAppV1Screen(navController)
        }

        composable(
            route = Screen.PieChartAppV1Screen.route
        ) {
            PieChartAppV1Screen(navController)
        }

        composable(
            route = Screen.PieChartAppV2Screen.route
        ) {
            PieChartAppV2Screen(navController)
        }

        composable(
            route = Screen.PieChartAppV3Screen.route
        ) {
            PieChartAppV3Screen(navController)
        }

        composable(
            route = Screen.DonutChartAppV1Screen.route
        ) {
            DonutChartAppV1Screen(navController)
        }

        composable(
            route = Screen.BubbleChartAppV1Screen.route
        ) {
            BubbleChartAppV1Screen(navController)
        }

        composable(
            route = Screen.CombinedChartAppV1Screen.route
        ) {
            CombinedChartAppV1Screen(navController)
        }
    }
}