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
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.wavechart.WaveChart
import co.yml.charts.ui.wavechart.model.Wave
import co.yml.charts.ui.wavechart.model.WaveChartData
import co.yml.charts.ui.wavechart.model.WaveFillColor
import co.yml.charts.ui.wavechart.model.WavePlotData

@Composable
fun WaveChartAppV1Screen(navController: NavController) {
//Luodaan dynaaminen lista, johon lisätään halutut arvot
    val dataList = arrayListOf(
        Point(0f, -10f),
        Point(2f, 10f),
        Point(4f, -10f),
        Point(6f, 10f),
        Point(8f, -10f),
        Point(10f, 10f),
        Point(12f, -10f),
        Point(14f, 10f),
        Point(16f, -10f),
        Point(18f, 10f),
        Point(20f, -10f),
        Point(22f, 10f)
    )

    //Määritellään montako "askelta" haluamme taulukolle y akselille
    val yAxisSteps = 10

    //Luodaan xAxisData arvo jossa konfiguroidaan x akselille eri parametreja.
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .startDrawPadding(48.dp)
        .steps(dataList.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    //Luodaan yAxisData arvo jossa konfiguroidaan y akselille eri parametreja.
    val yAxisData = AxisData.Builder()
        .steps(yAxisSteps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = dataList.minOf { it.y }
            val yMax = dataList.maxOf { it.y }
            val yScale = (yMax - yMin) / yAxisSteps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

    //Luodaan arvo johon lisätään datalistamme sekä x ja y akselien konfiguraatiot
    val data = WaveChartData(
        wavePlotData = WavePlotData(
            lines = listOf(
                Wave(
                    dataPoints = dataList,
                    waveStyle = LineStyle(color = Color.Black),
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp(),
                    waveFillColor = WaveFillColor(topColor = Color.Green, bottomColor = Color.Red),
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    Column() {
        //Kutsutaan YCharts funktiota joka piirtää diagrammin
        //Syötetään funktiolle lisäämämme data sekä x- ja y-akselien konfiguraatiot
        WaveChart(
            modifier = Modifier
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .height(300.dp),
            waveChartData = data
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