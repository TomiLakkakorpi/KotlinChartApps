package com.example.chartapplications

import androidx.compose.foundation.layout.Column
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

import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

@Composable
fun LineChartAppV1Screen(navController: NavController) {
//Luodaan dynaaminen lista, johon lisätään halutut arvot
    //Esimerkissä käytetään datana Nokian osakkeen arvoa Tammikuu 2024 - Joulukuu 2024 välillä.
    val dataList = arrayListOf(
        Point(1f, 3.332f),
        Point(2f, 3.260f),
        Point(3f, 3.291f),
        Point(4f, 3.412f),
        Point(5f, 3.591f),
        Point(6f, 3.558f),
        Point(7f, 3.621f),
        Point(8f, 3.978f),
        Point(9f, 3.924f),
        Point(10f, 4.325f),
        Point(11f, 3.980f),
        Point(12f, 4.274f)
    )

    val monthList = arrayListOf(
        "Tammi",
        "Helmi",
        "Maalis",
        "Huhti",
        "Touko",
        "Kesä",
        "Heinä",
        "Elo",
        "Syys",
        "Loka",
        "Marras",
        "Joulu",
        ""
    )

    //Määritellään montako "askelta" haluamme taulukolle y akselille
    val yAxisSteps = 10

    //Luodaan xAxisData arvo jossa konfiguroidaan x akselille eri parametreja.
    val xAxisData = AxisData.Builder()
        .axisStepSize(50.dp)
        .topPadding(105.dp)
        .steps(dataList.size - 1)
        .axisLabelFontSize(15.sp)
        //.labelData { i -> "." + dataList[i].x.toInt().toString() }
        .labelData { i -> monthList[i].toString() }
        .labelAndAxisLinePadding(25.dp)
        .build()

    //Luodaan yAxisData arvo jossa konfiguroidaan y akselille eri parametreja.
    val yAxisData = AxisData.Builder()
        .steps(yAxisSteps)
        .labelAndAxisLinePadding(30.dp)
        .labelData { i ->
            val yMin = dataList.minOf{it.y}       //Asetetaan taulukon minimiarvoksi listan pienin arvo
            val yMax = dataList.maxOf{it.y}       //Asetetaan taulukon maksimiarvoksi listan suurin arvo
            val yScale = (yMax - yMin) / yAxisSteps
            ((i * yScale) + yMin).formatToSinglePrecision() + " €"
        }.build()

    //Luodaan arvo johon lisätään datalistamme sekä x ja y akselien konfiguraatiot
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = dataList,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    Column() {
        Text(
            modifier = Modifier
                .padding(10.dp, 50.dp, 10.dp, 0.dp),
            textAlign = TextAlign.Center,
            text = "Nokian osake Tammikuu 2024 - Joulukuu 2024",
            fontSize = 15.sp
        )

        //Kutsutaan YCharts funktiota joka piirtää diagrammin
        //Syötetään funktiolle lisäämämme data sekä x- ja y-akselien konfiguraatiot
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = data
        )

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