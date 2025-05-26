package com.example.userinputexample1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.userinputexample1.ui.theme.UserInputExample1Theme

//YCharts imports
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

var lineChartListIndex = 0F
var lineChartList = mutableListOf<Point>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserInputExample1Theme {
                val context = LocalContext.current

                var text by remember {
                    mutableStateOf("")
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val steps = lineChartList.size

                        val xAxisData = AxisData.Builder()
                            .axisStepSize(35.dp)
                            .topPadding(105.dp)
                            .steps(lineChartList.size - 1)
                            .labelData { i -> lineChartList[i].x.toInt().toString() }
                            .labelAndAxisLinePadding(15.dp)
                            .build()

                        val yAxisData = AxisData.Builder()
                            .steps(steps)
                            .labelAndAxisLinePadding(20.dp)
                            .labelData { i ->
                                val yMin = lineChartList.minOf { it.y }
                                val yMax = lineChartList.maxOf { it.y }
                                val yScale = (yMax - yMin) / steps
                                ((i * yScale) + yMin).formatToSinglePrecision()
                            }.build()

                        val data = LineChartData(
                            linePlotData = LinePlotData(
                                lines = listOf(
                                    Line(
                                        dataPoints = lineChartList,
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

                        Box(
                            modifier = Modifier
                                .width(370.dp)
                                .height(300.dp)
                        ) {
                            if(lineChartList.isNotEmpty()){
                                LineChart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    lineChartData = data
                                )
                            }
                        }

                        TextField(
                            value = text,
                            onValueChange = { newText ->
                                text = newText
                            },
                            label = {
                                Text(text = "Syötä arvo")
                            },
                        )

                        Button(
                            onClick = {
                                if(text.isNotEmpty()){
                                    lineChartList.add(Point(lineChartListIndex, text.toFloat(), ""))
                                    text = ""
                                    lineChartListIndex++
                                } else {
                                    Toast.makeText(context, "Syötä arvo!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text("Lisää arvo kaavioon")
                        }

                        Button(
                            onClick = {
                                if (lineChartList.isNotEmpty()) {

                                    while(lineChartList.isNotEmpty()) {
                                        lineChartList.removeAt(lineChartList.size -1)
                                    }

                                    lineChartListIndex = 0f

                                    text = " "
                                    text = ""
                                } else {
                                    Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text("Tyhjennä taulukko")
                        }
                    }
                }
            }
        }
    }
}