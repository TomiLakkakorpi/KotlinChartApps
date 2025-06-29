package com.example.chartapplications

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
                text = "Kaavio esimerkit",
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

                    navController.navigate(route = Screen.BarChartAppV1Screen.route)
                }
            ) {
                Text("Pylväskaavio")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.LineChartAppV1Screen.route)
                }
            ) {
                Text("Viivakaavio")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.WaveChartAppV1Screen.route)
                }
            ) {
                Text("Aaltokaavio")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.PieChartAppV1Screen.route)
                }
            ) {
                Text("Piirakkakaavio V1")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {

                    navController.navigate(route = Screen.PieChartAppV2Screen.route)
                }
            ) {
                Text("Piirakkakaavio V2")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {
                    navController.navigate(route = Screen.PieChartAppV3Screen.route)
                }
            ) {
                Text("Piirakkakaavio V3")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {
                    navController.navigate(route = Screen.DonutChartAppV1Screen.route)
                }
            ) {
                Text("Donitsikaavio")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {
                    navController.navigate(route = Screen.BubbleChartAppV1Screen.route)
                }
            ) {
                Text("Kuplakaavio")
            }

            Button(
                shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                onClick = {
                    navController.navigate(route = Screen.CombinedChartAppV1Screen.route)
                }
            ) {
                Text("Yhdistetty Kaavio")
            }
        }
    }
}