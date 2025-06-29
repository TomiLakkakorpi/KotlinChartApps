package com.example.chartapplications

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object BarChartAppV1Screen: Screen(route = "barchart1screen")
    object LineChartAppV1Screen: Screen(route = "linechart1screen")
    object WaveChartAppV1Screen: Screen(route = "wavechart1screen")
    object PieChartAppV1Screen: Screen(route = "piechart1screen")
    object PieChartAppV2Screen: Screen(route = "piechart2screen")
    object PieChartAppV3Screen: Screen(route = "piechart3screen")
    object DonutChartAppV1Screen: Screen(route = "donutchart1screen")
    object BubbleChartAppV1Screen: Screen(route = "bubblechart1screen")
    object CombinedChartAppV1Screen: Screen(route = "combinedchart1screen")
}