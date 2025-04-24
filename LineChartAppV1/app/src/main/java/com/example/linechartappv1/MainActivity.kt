package com.example.linechartappv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.linechartappv1.ui.theme.LineChartAppV1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LineChartAppV1Theme {

                //Luodaan laatikko komponentti
                Box(
                    modifier = Modifier

                        //Asetetaan laatikon kooksi koko näyttö
                        .fillMaxSize()

                        //Asetetaan laatikon reunoille 20dp tyhjä reuna
                        .padding(20.dp)
                ) {
                    //Kutsutaan funktiota, jossa muodostamme diagrammin
                    DrawLineChart()
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
    val pointsData: List<Point> =
        listOf(
            Point(1f, 25f),
            Point(2f, 73f),
            Point(3f, 68f),
            Point(4f, 100f),
            Point(5f, 17f),
            Point(6f, 44f),
            Point(7f, 31f),
            Point(8f, 2f),
            Point(9f, 49f),
            Point(10f, 15f)
        )

    val steps = 10

    val xAxisData = AxisData.Builder()
        .axisStepSize(35.dp)
        .topPadding(105.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData[i].x.toInt().toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMax = 100.0f
            val yMin = 0.0f
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

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

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LineChartAppV1Theme {
        DrawLineChart()
    }
}