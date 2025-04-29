package com.example.bubblechartappv1

import com.example.bubblechartappv1.ui.theme.BubbleChartAppV1Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

//YCharts Imports
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.bubblechart.model.BubbleChartData
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.bubblechart.BubbleChart

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BubbleChartAppV1Theme {
                DrawBubbleChart()
            }
        }
    }
}

//Luodaan funktio, joka rakentaa ja piirtää pylväsdiagrammin
//Kotlinissa käyttöliittymäfunktiot merkitään @Composable merkinnällä
@Composable
fun DrawBubbleChart() {
    //Luodaan "pointsData" arvo, johon asetetaan lista esimerkkidataa.
    //Oikeassa käyttötapauksessa tämä data haettaisiin esimerkiksi tietokannasta.
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

    // Luodaan "steps" arvo, jolla määritellään Y akselin askeleiden määrä
    val steps = 5

    // Luodaan xAxisData arvo, jossa konfiguroidaan x akselille ominaisuuksia.
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(pointsData.size -1)
        .labelData { i ->pointsData[i].x.toInt().toString() }
        .labelAndAxisLinePadding(20.dp)
        .build()

    // Luodaan yAxisData arvo, jossa konfiguroidaan y akselille ominaisuuksia.
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = 0
            val yMax = pointsData.maxOf {it.y}
            val yScale = (yMax - yMin) / steps
            ((i*yScale) + yMin).formatToSinglePrecision()
        }.build()

    // Luodaan data arvo, johon lisätään datalista(pointsData) sekä x ja y akseleiden konfiguraatiot
    val data = BubbleChartData(
        DataUtils.getBubbleChartDataWithSolidStyle(pointsData),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    // Käytetään YCharts funktiota diagrammin piirtämiseen
    BubbleChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(500.dp),

        //Määritellään diagrammin dataksi yllä luomamme data-arvo, joka sisältää diagrammin datan ja akseleiden konfiguraatiot
        bubbleChartData = data
    )
}

// @Preview merkittyjä funktiota voidaan tarkastella "preview" osiossa ennen ohjelman ajoa.
@Preview(showBackground = true)
@Composable
fun BubbleChartPreview() {
    BubbleChartAppV1Theme {
        //Kutsutaan funktiota, joka sisältää kaiken koodin diagrammin piirtämiseen, jotta sitä voidaan tarkastella preview ikkunassa
        DrawBubbleChart()
    }
}