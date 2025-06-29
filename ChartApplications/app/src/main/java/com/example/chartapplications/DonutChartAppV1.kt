package com.example.chartapplications

import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData

import com.example.chartapplications.ui.theme.color1
import com.example.chartapplications.ui.theme.color2
import com.example.chartapplications.ui.theme.color4
import com.example.chartapplications.ui.theme.color6
import com.example.chartapplications.ui.theme.color7
import com.example.chartapplications.ui.theme.color9
import com.example.chartapplications.ui.theme.color10
import com.example.chartapplications.ui.theme.color13
import com.example.chartapplications.ui.theme.color14
import com.example.chartapplications.ui.theme.color15
import com.example.chartapplications.ui.theme.color16

@Composable
fun DonutChartAppV1Screen(navController: NavController) {
//Luodaan dynaaminen lista, johon asetetaan arvoksi lista suomen kaupunkeja, niiden väkiluvut sekä siivulle haluttu väri.
    val dataList = PieChartData(
        slices = listOf(
            //Lisätään kaavioon siivut, lisätään siivuille otsikko, arvo ja väri
            PieChartData.Slice("Helsinki", 684018f, color = color1),
            PieChartData.Slice("Espoo", 320931f, color = color2),
            PieChartData.Slice("Tampere", 260180f, color = color4),
            PieChartData.Slice("Vantaa", 251269f, color = color6),
            PieChartData.Slice("Oulu", 216152f, color = color7),
            PieChartData.Slice("Turku", 206073f, color = color9),
            PieChartData.Slice("Jyväskylä", 149194f, color = color10),
            PieChartData.Slice("Kuopio", 125666f, color = color13),
            PieChartData.Slice("Lahti", 121337f, color = color14),
            PieChartData.Slice("Pori", 83305f, color = color15),
            PieChartData.Slice("Muu Suomi", 3129089f, color = color16)
        ),
        //Määritellään minkälaisen kaavion haluamme
        plotType = PlotType.Donut
    )

    // Luodaan konfigurointi arvo, jossa määritellään kaaviolle eri parametreja
    val donutChartConfig = PieChartConfig(
        labelVisible = true,
        strokeWidth = 120f,
        labelColor = Color.Black,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
        labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
        isAnimationEnable = true,
        chartPadding = 25,
        labelFontSize = 42.sp,
    )

    Column() {
        Text(
            text = "Suomen väkiluku kaupungittain",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(0.dp, 50.dp),
            fontSize = 20.sp
        )

        // Luodaan sarake, jonka korkeudeksi määritetään 500dp
        Column() {
            //Luodaan sarakkeiden väliin 20dp rako
            Spacer(modifier = Modifier.height(20.dp))

            //Muodostetaan selitteiden luettelo ruudukkomuotoon
            Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = dataList, 3))

            //Kutsutaan funktiota joka piirtää kaavion
            //Annetaan funktiolle luomamme data ja konfiguroinnit
            DonutPieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                dataList,
                donutChartConfig
            ) {}
        }

        //Näppäin päävalikkoon palaamiseen
        Button(
            modifier = Modifier
                .padding(5.dp, 1.dp, 5.dp, 1.dp)
                .width(200.dp),
            shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.Black
            ),
            onClick = {
                navController.navigateUp()
            }
        ) {
            Text("Takaisin päävalikkoon")
        }
    }
}