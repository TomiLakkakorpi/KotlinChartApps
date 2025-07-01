package com.example.graphingcalculators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(
                text = "Graafiset laskimet",
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp)
            )

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.UserInputExample1Screen.route)
                }
            ) {
                Text("Yksittäisten arvojen syöttö")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.GraphingCalculatorScreen1.route)
                }
            ) {
                Text("Laskin 1: Yksi Kaava")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.GraphingCalculatorScreen2.route)
                }
            ) {
                Text("Laskin 2: Yksi Kaava, mukautettu piirtoalue")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.GraphingCalculatorScreen3.route)
                }
            ) {
                Text("Laskin 3: Kaksi Kaavaa")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.GraphingCalculatorScreen4.route)
                }
            ) {
                Text("Laskin 4: Ympyrän piirto")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.GraphingCalculatorScreen5.route)
                }
            ) {
                Text("Laskin 5: Datan muunnos")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {
                    navController.navigate(route = Screen.GraphingCalculatorScreen6.route)
                }
            ) {
                Text("Laskin 6: Ominaisuudet yhdistetty")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {
                    navController.navigate(route = Screen.GraphingCalculatorScreen7.route)
                }
            ) {
                Text("Laskin 7: Parametrikäyrä kolmella argumentilla")
            }
        }
    }
}