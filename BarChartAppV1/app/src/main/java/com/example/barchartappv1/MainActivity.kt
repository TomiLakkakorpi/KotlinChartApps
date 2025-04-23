package com.example.barchartappv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.barchartappv1.ui.theme.BarChartAppV1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarChartAppV1Theme {
                Box() {
                    DrawBarChart()
                }
            }
        }
    }
}

@Composable
fun DrawBarChart() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val maxRange = 700000F
        val yStepSize = 7

        val list = arrayListOf(
            BarData(
                point = Point(1F, 681802F),
                Color.Cyan,
                label = "Helsinki",
            ),

            BarData(
                point = Point(2F, 318507F),
                Color.Blue,
                label = "Espoo",
            ),

            BarData(
                point = Point(3F, 258770F),
                Color.Green,
                label = "Tampere",
            ),

            BarData(
                point = Point(4F, 250073F),
                Color.Yellow,
                label = "Vantaa",
            ),

            BarData(
                point = Point(5F, 215530F),
                Color.Red,
                label = "Oulu",
            ),

            BarData(
                point = Point(6F, 204618F),
                Color.Magenta,
                label = "Turku",
            ),
        )

        val xAxisData = AxisData.Builder()
            .axisStepSize(30.dp)
            .steps(list.size - 1)
            .bottomPadding(40.dp)
            .axisLabelAngle(20f)
            .startDrawPadding(25.dp)
            .labelData { index -> list[index].label }
            .build()

        val yAxisData = AxisData.Builder()
            .steps(yStepSize)
            .labelAndAxisLinePadding(20.dp)
            .axisOffset(10.dp)
            .labelData { index -> (index * (maxRange / yStepSize)).toString() }
            .build()

        val barChartData = BarChartData(
            chartData = list,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                paddingBetweenBars = 25.dp,
                barWidth = 20.dp
            ),
            showYAxis = true,
            showXAxis = true,
            horizontalExtraSpace = 50.dp
        )
        BarChart(modifier = Modifier.height(350.dp), barChartData = barChartData)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BarChartAppV1Theme {
        DrawBarChart()
    }
}