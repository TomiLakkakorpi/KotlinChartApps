package com.example.graphingcalculators

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object UserInputExample1Screen: Screen(route = "userinputexample1_screen")
    object GraphingCalculatorScreen1: Screen(route = "graphingcalculator1_screen")
    object GraphingCalculatorScreen2: Screen(route = "graphingcalculator2_screen")
    object GraphingCalculatorScreen3: Screen(route = "graphingcalculator3_screen")
    object GraphingCalculatorScreen4: Screen(route = "graphingcalculator4_screen")
    object GraphingCalculatorScreen5: Screen(route = "graphingcalculator5_screen")
    object GraphingCalculatorScreen6: Screen(route = "graphingcalculator6_screen")
    object GraphingCalculatorScreen7: Screen(route = "graphingcalculator7_screen")
}