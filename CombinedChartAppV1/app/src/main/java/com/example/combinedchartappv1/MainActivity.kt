package com.example.combinedchartappv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.combinedchartappv1.ui.theme.CombinedChartAppV1Theme

import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.combinedchart.CombinedChart
import co.yml.charts.ui.combinedchart.model.CombinedChartData
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import androidx.compose.material3.Text
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import com.example.combinedchartappv1.ui.theme.color1
import com.example.combinedchartappv1.ui.theme.color2
import com.example.combinedchartappv1.ui.theme.color3

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CombinedChartAppV1Theme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column() {
                        Text(
                            modifier = Modifier.padding(10.dp, 50.dp, 10.dp, 10.dp),
                            text = "Lämpötila ja sademäärä ennuste 23.5. - 1.6.2025"
                        )
                        DrawCombinedChart()
                    }
                }
            }
        }
    }
}

@Composable
fun DrawCombinedChart() {

    val maxRange = 20
    val yStepSize = 10

    // Data 10pv sääennuste Oulu Pe 23.5. - Su 1.6.
    val lineData = arrayListOf(
            Point(1f, 1.9f),
            Point(2f, 1.7f),
            Point(3f, 0.8f),
            Point(4f, 3.3f),
            Point(5f, 2.8f),
            Point(6f, 3.7f),
            Point(7f, 3.7f),
            Point(8f, 9.0f),
            Point(9f, 7.2f),
            Point(10f, 0.2f)
        )

    val groupBarData = arrayListOf(
        GroupBar(label = "23.5.", barList = arrayListOf(
            BarData(point = Point(1F, 11f)),
            BarData(point = Point(2F, 18f))
        )),

        GroupBar(label = "24.5.", barList = arrayListOf(
            BarData(point = Point(1F, 5f)),
            BarData(point = Point(2F, 16f))
        )),

        GroupBar(label = "25.5.", barList = arrayListOf(
            BarData(point = Point(1F, 8f)),
            BarData(point = Point(2F, 11f))
        )),

        GroupBar(label = "26.5.", barList = arrayListOf(
            BarData(point = Point(1F, 7f)),
            BarData(point = Point(2F, 15f))
        )),

        GroupBar(label = "27.5.", barList = arrayListOf(
            BarData(point = Point(1F, 9f)),
            BarData(point = Point(2F, 15f))
        )),

        GroupBar(label = "28.5.", barList = arrayListOf(
            BarData(point = Point(1F, 9f)),
            BarData(point = Point(2F, 14f))
        )),

        GroupBar(label = "29.5.", barList = arrayListOf(
            BarData(point = Point(1F, 9f)),
            BarData(point = Point(2F, 16f))
        )),

        GroupBar(label = "30.5.", barList = arrayListOf(
            BarData(point = Point(1F, 10f)),
            BarData(point = Point(2F, 16f))
        )),

        GroupBar(label = "31.5.", barList = arrayListOf(
            BarData(point = Point(1F, 8f)),
            BarData(point = Point(2F, 14f))
        )),

        GroupBar(label = "1.6.", barList = arrayListOf(
            BarData(point = Point(1F, 8f)),
            BarData(point = Point(2F, 14f))
        )),
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .bottomPadding(5.dp)
        .labelData { index -> index.toString() }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    val linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = lineData,
                lineStyle = LineStyle(color = color3),
                intersectionPoint = IntersectionPoint(),
                selectionHighlightPoint = SelectionHighlightPoint(),
                selectionHighlightPopUp = SelectionHighlightPopUp()
            )
        )
    )

    val legendsConfig = LegendsConfig(
        legendLabelList = arrayListOf(
            LegendLabel(
                color = color1,
                name = "Alin lämpötila °C"
            ),
            LegendLabel(
                color = color2,
                name = "Ylin lämpötila °C"
            ),
            LegendLabel(
                color = color3,
                name = "Sademäärä mm"
            )
        ),
        gridColumnCount = 2
    )

    val colorPaletteList = listOf(color1, color2,)

    val barPlotData = BarPlotData(
        groupBarList = groupBarData,
        barStyle = BarStyle(barWidth = 35.dp),
        barColorPaletteList = colorPaletteList
    )

    val combinedChartData = CombinedChartData(
        combinedPlotDataList = listOf(barPlotData, linePlotData),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    Column(
        Modifier
            .height(500.dp)
    ) {
        CombinedChart(
            modifier = Modifier
                .height(400.dp),
            combinedChartData = combinedChartData
        )
        Legends(
            legendsConfig = legendsConfig
        )
    }
}