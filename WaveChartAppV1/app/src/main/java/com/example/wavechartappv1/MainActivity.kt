package com.example.wavechartappv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.wavechartappv1.ui.theme.WaveChartAppV1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaveChartAppV1Theme {
                DrawWaveChart()
            }
        }
    }
}

@Composable
fun DrawWaveChart() {

    val dataList = arrayListOf(
        Point(1f, 10.0f),
        Point(2f, 6.9f),
        Point(3f, 9.2f),
        Point(4f, 7.7f),
        Point(5f, 6.1f),
        Point(6f, 1.5f),
        Point(7f, 3.8f),
        Point(8f, 8.4f),
        Point(9f, 6.3f),
        Point(10f, 3.8f),
        Point(11f, 1.2f),
        Point(12f, 5.6f)
    )

    val yAxisSteps = 10

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .startDrawPadding(48.dp)
        .steps(dataList.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yAxisSteps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = dataList.minOf { it.y }
            val yMax = dataList.maxOf { it.y }
            val yScale = (yMax - yMin) / yAxisSteps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

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

    WaveChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        waveChartData = data
    )
}