package com.example.linechartappv1

import com.example.linechartappv1.ui.theme.LineChartAppV1Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

//YCharts importit
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LineChartAppV1Theme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Column() {
                        Text(
                            modifier = Modifier
                                .padding(10.dp, 20.dp, 10.dp, 0.dp),
                            textAlign = TextAlign.Center,
                            text = "Nokian osake Tammikuu 2024 - Joulukuu 2024",
                            fontSize = 15.sp
                        )
                        DrawLineChart()     //Kutsutaan funktiota, jossa muodostamme diagrammin
                    }
                }
            }
        }
    }
}

//Luodaan funktio, joka rakentaa ja piirtää viivadiagrammin
//Kotlinissa käyttöliittymäfunktiot merkitään @Composable merkinnällä
@Composable
fun DrawLineChart() {

    //Luodaan pointsData lista, johon lisätään halutut arvot
    //Esimerkissä käytetään datana Nokian osakkeen arvoa Tammikuu 2024 - Joulukuu 2024 välillä.

    val pointsData = arrayListOf(
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

    //Asetetaan montako "askelta" haluamme taulukolle y akselille
    val steps = 10

    //Luodaan xAxisData arvo jossa konfiguroidaan x akselille eri ominaisuuksia.
    val xAxisData = AxisData.Builder()
        .axisStepSize(35.dp)
        .topPadding(105.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData[i].x.toInt().toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    //Luodaan yAxisData arvo jossa konfiguroidaan y akselille eri ominaisuuksia.
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(30.dp)
        .labelData { i ->
            val yMin = pointsData.minOf{it.y}       //Asetetaan taulukon minimiarvoksi listan pienin arvo
            val yMax = pointsData.maxOf{it.y}       //Asetetaan taulukon maksimiarvoksi listan suurin arvo
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

    // Luodaan arvo johon lisätään datalistamme sekä x ja y akselien konfiguraatiot
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
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

    //Kutsutaan YCharts funktiota joka piirtää diagrammin
    //Annetaan funktiolle lisäämämme data sekä x ja y akselien konfiguraatiot
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}