package com.example.combinedchartappv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.combinedchartappv1.ui.theme.CombinedChartAppV1Theme

import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
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
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CombinedChartAppV1Theme {
                DrawCombinedChart()
            }
        }
    }
}

@Composable
fun DrawCombinedChart() {
    val maxRange = 100
    val yStepSize = 10

    val lineData: List<Point> =
        listOf(
            Point(1f, 25f),
            Point(2f, 27f),
            Point(3f, 33f),
            Point(4f, 22f),
            Point(5f, 14f),
            Point(6f, 30f),
            Point(7f, 4f),
            Point(8f, 15f),
            Point(9f, 18f),
            Point(10f, 22f)
        )

    val groupBarData: List<GroupBar> = listOf(
        GroupBar(
            label = "1",
            barList = arrayListOf(
                BarData(
                    point = Point(1F, 18f)
                ),
                BarData(
                    point = Point(2F, 20f)
                ),
                BarData(
                    point = Point(3F, 24f)
                )
            )
        ),

        GroupBar(
            label = "2",
            barList = arrayListOf(
                BarData(
                    point = Point(1F, 18f)
                ),
                BarData(
                    point = Point(2F, 20f)
                ),
                BarData(
                    point = Point(3F, 24f)
                )
            )
        ),
    )

    val xAxisData = AxisData.Builder()
        //.startDrawPadding((-20).dp)
        .axisStepSize(30.dp)
        .bottomPadding(5.dp)
        .labelData { index -> index.toString() }
        .build()

    val yAxisData = AxisData.Builder()
        //.startDrawPadding((-20).dp)
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    val linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = lineData,
                lineStyle = LineStyle(color = Color.Blue),
                intersectionPoint = IntersectionPoint(),
                selectionHighlightPoint = SelectionHighlightPoint(),
                selectionHighlightPopUp = SelectionHighlightPopUp()
            )
        )
    )

    val colorPaletteList = DataUtils.getColorPaletteList(3)
    val legendsConfig = LegendsConfig(
        legendLabelList = DataUtils.getLegendsLabelData(colorPaletteList),
        gridColumnCount = 3
    )
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