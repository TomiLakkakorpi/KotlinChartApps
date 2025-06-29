package com.example.chartapplications

import androidx.compose.foundation.layout.Column
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
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.Point
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

import com.example.chartapplications.ui.theme.color1
import com.example.chartapplications.ui.theme.color3
import com.example.chartapplications.ui.theme.color8

@Composable
fun CombinedChartAppV1Screen(navController: NavController) {
//Määritellään maksimiarvo ja askelten määrä
    val maxRange = 20
    val yStepSize = 10

    // Luodaan dynaaminen lista, johon lisätään halutut arvot. Tämä lista sisältää viivan datan.
    // Esimerkissä Datana 10pv sääennuste ajalle 23.5. - 1.6. Oulussa
    val lineData = arrayListOf(
        Point(0f, 1.9f),
        Point(1f, 1.7f),
        Point(2f, 0.8f),
        Point(3f, 3.3f),
        Point(4f, 2.8f),
        Point(5f, 3.7f),
        Point(6f, 3.7f),
        Point(7f, 9.0f),
        Point(8f, 7.2f),
        Point(9f, 0.2f),
    )

    val dateList = arrayListOf(
        "23.5.",
        "24.5.",
        "25.5.",
        "26.5.",
        "27.5.",
        "28.5.",
        "29.5.",
        "30.5.",
        "31.5.",
        "1.6.",
        "", //Tyhjä elementti listan lopussa "index out of bounds" virheviestin välttämiseksi
    )

    // Luodaan dynaaminen lista, johon lisätään halutut arvot. Tämä lista sisältää pylväiden datan
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

    //Luodaan xAxisData arvo jossa konfiguroidaan x akselille eri parametreja.
    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .bottomPadding(5.dp)
        .labelData { index -> dateList[index].toString() }
        .build()

    //Luodaan yAxisData arvo jossa konfiguroidaan y akselille eri parametreja.
    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() /*+ "°C/mm"*/ }
        .startDrawPadding((-50).dp)
        .build()

    //Luodaan arvo johon lisätään datalistamme sekä x ja y akselien konfiguraatiot
    val linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = lineData,
                lineStyle = LineStyle(color = color8),
                intersectionPoint = IntersectionPoint(),
                selectionHighlightPoint = SelectionHighlightPoint(),
                selectionHighlightPopUp = SelectionHighlightPopUp()
            )
        )
    )

    //Konfiguroidaan selitteet kaavion alle
    val legendsConfig = LegendsConfig(
        legendLabelList = arrayListOf(
            LegendLabel(
                color = color3,
                name = "Alin lämpötila °C"
            ),
            LegendLabel(
                color = color1,
                name = "Ylin lämpötila °C"
            ),
            LegendLabel(
                color = color8,
                name = "Sademäärä mm"
            )
        ),
        gridColumnCount = 2
    )

    val colorPaletteList = listOf(color1, color3,)

    //Luodaan data -arvo pylväiden
    val barPlotData = BarPlotData(
        groupBarList = groupBarData,
        barStyle = BarStyle(barWidth = 25.dp),
        barColorPaletteList = colorPaletteList
    )

    //Yhdistetään viivadata ja pylväsdata yhteen data arvoon.
    //Lisätään data-arvoon myös akselien konfiguraatiot
    val combinedChartData = CombinedChartData(
        combinedPlotDataList = listOf(barPlotData, linePlotData),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    Column() {
        Text(
            modifier = Modifier.padding(10.dp, 50.dp, 10.dp, 10.dp),
            text = "Lämpötila ja sademäärä ennuste 23.5. - 1.6.2025"
        )

        Column(
            Modifier
                .height(500.dp)
        ) {
            //Kutsutaan funktiota, joka piirtää yhdistetyn kaavion.
            //Funktiolle syötetäään data arvo, joka sisältää kaavioiden datan sekä akselien konfiguraatiot
            CombinedChart(
                modifier = Modifier
                    .height(400.dp),
                combinedChartData = combinedChartData
            )
            Legends(
                legendsConfig = legendsConfig
            )
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