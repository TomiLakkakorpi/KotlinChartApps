package com.example.bubblechartappv1

import com.example.bubblechartappv1.ui.theme.BubbleChartAppV1Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

//YCharts Imports
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.bubblechart.model.BubbleChartData
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.bubblechart.BubbleChart
import co.yml.charts.ui.bubblechart.model.Bubble
import co.yml.charts.ui.bubblechart.model.BubbleStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp

//Color Imports
import com.example.bubblechartappv1.ui.theme.color1
import com.example.bubblechartappv1.ui.theme.color2
import com.example.bubblechartappv1.ui.theme.color3
import com.example.bubblechartappv1.ui.theme.color4
import com.example.bubblechartappv1.ui.theme.color5
import com.example.bubblechartappv1.ui.theme.color6
import com.example.bubblechartappv1.ui.theme.color7
import com.example.bubblechartappv1.ui.theme.color8
import com.example.bubblechartappv1.ui.theme.color9
import com.example.bubblechartappv1.ui.theme.color10
import com.example.bubblechartappv1.ui.theme.color11
import com.example.bubblechartappv1.ui.theme.color12
import com.example.bubblechartappv1.ui.theme.color13
import com.example.bubblechartappv1.ui.theme.color14
import com.example.bubblechartappv1.ui.theme.color15
import com.example.bubblechartappv1.ui.theme.color16

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BubbleChartAppV1Theme {
                Column() {
                    Text(
                        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp),
                        textAlign = TextAlign.Left,
                        text = "Liiga runkosarja 2024 \n X-akseli: Kolmen pisteen voitot \n Y-akseli: runkosarjan pisteet \n  Kuplan koko: Joukkueen budjetti kaudelle",
                        fontSize = 16.sp
                    )
                    DrawBubbleChart()
                    Row() {
                        Column(
                            modifier = Modifier.padding(10.dp),
                        ) {
                            drawInfoRow(color1, "Lukko")
                            drawInfoRow(color2, "Ilves")
                            drawInfoRow(color3, "KalPa")
                            drawInfoRow(color4, "HIFK")
                            drawInfoRow(color5, "SaiPa")
                            drawInfoRow(color6, "KooKoo")
                            drawInfoRow(color7, "Ässät")
                            drawInfoRow(color8, "K-Espoo")
                        }

                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            drawInfoRow(color9, "Tappara")
                            drawInfoRow(color10, "HPK")
                            drawInfoRow(color11, "Sport")
                            drawInfoRow(color12, "TPS")
                            drawInfoRow(color13, "Kärpät")
                            drawInfoRow(color14, "Jyp")
                            drawInfoRow(color15, "Pelicans")
                            drawInfoRow(color16, "Jukurit")
                        }
                    }
                }
            }
        }
    }
}

//Luodaan funktio, jossa määritellään ja rakennetaan pylväsdiagrammi
@Composable
fun DrawBubbleChart() {
    //Luodaan "dataList" arvo, johon asetetaan lista esimerkkidataa.
    //Oikeassa käyttötapauksessa data ei olisi kovakoodattu.
    val dataList = arrayListOf(
            Bubble( //Lukko
                center = Point(32F, 112F),                                          //"center" määrittelee kuplan paikan koordinaatistossa x ja y -arvojen avulla.
                density = 29.50F,                                                   //"density" määrittelee kuplan koon.
                bubbleStyle = BubbleStyle(solidColor = color1),                     //Määritellään kuplan väri
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(//Ilves
                center = Point(32F, 111F),
                density = 31.00F,
                bubbleStyle = BubbleStyle(solidColor = color2),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(  //Kalpa
                center = Point(29F, 107F),
                density = 23.00F,
                bubbleStyle = BubbleStyle(solidColor = color3),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //HIFK
                center = Point(27F, 107F),
                density = 33.50F,
                bubbleStyle = BubbleStyle(solidColor = color4),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),
            Bubble(  //SaiPa
                center = Point(30F, 106F),
                density = 20.00F,
                bubbleStyle = BubbleStyle(solidColor = color5),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),
            Bubble( //KooKoo
                center = Point(23F, 99F),
                density = 20.00F,
                bubbleStyle = BubbleStyle(solidColor = color6),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Ässät
                center = Point(26F, 95F),
                density = 23.50F,
                bubbleStyle = BubbleStyle(solidColor = color7),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //K-Espoo
                center = Point(20F, 91F),
                density = 20.00F,
                bubbleStyle = BubbleStyle(solidColor = color8),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Tappara
                center = Point(24F, 90F),
                density = 32.50F,
                bubbleStyle = BubbleStyle(solidColor = color9),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //HPK
                center = Point(21F, 83F),
                density = 19.00F,
                bubbleStyle = BubbleStyle(solidColor = color10),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Sport
                center = Point(17F, 83F),
                density = 18.51F,
                bubbleStyle = BubbleStyle(solidColor = color11),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //TPS
                center = Point(18F, 79F),
                density = 24.00F,
                bubbleStyle = BubbleStyle(solidColor = color12),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Kärpät
                center = Point(19F, 77F),
                density = 32.50F,
                bubbleStyle = BubbleStyle(solidColor = color13),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(//JYP
                center = Point(19F, 76F),
                density = 24.00F,
                bubbleStyle = BubbleStyle(solidColor = color14),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Pelicans
                center = Point(20F, 75F),
                density = 30.00F,
                bubbleStyle = BubbleStyle(solidColor = color15),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Jukurit
                center = Point(12F, 49F),
                density = 15.00F,
                bubbleStyle = BubbleStyle(solidColor = color16),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            //Lisätään tyhjä kupla koordinaatteihin 0,0. Ilman tätä kaavio käyttäytyi oudosti.
            Bubble(
                center = Point(0F, 0F),
                density = 0F)
        )

    //Luodaan "steps" arvot, jolla määritellään x- ja y- akselien askeleiden määrä
    val yAxisSteps = 12
    val xAxisSteps = 10

    //Luodaan xAxisData arvo, jossa konfiguroidaan x-akselille parametreja.
    val xAxisData = AxisData.Builder()
        .axisStepSize(9.dp)
        .steps(xAxisSteps)
        .labelData { i ->
            val xMin = -4f
            val xMax = 36f
            val xScale = (xMax - xMin) / xAxisSteps
            ((i+xScale) + xMin).formatToSinglePrecision()
        }
        .startDrawPadding(10.dp)
        .build()

    //Luodaan yAxisData arvo, jossa konfiguroidaan y-akselille parametreja.
    val yAxisData = AxisData.Builder()
        .steps(yAxisSteps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = 0f
            val yMax = 120f
            val yScale = (yMax - yMin) / yAxisSteps
            ((i*yScale) + yMin).formatToSinglePrecision() + " P"
        }.build()

    //Luodaan data-arvo, johon lisätään datalista sekä x- ja y- akseleiden konfiguraatiot
    val data = BubbleChartData(
        bubbles = dataList,
        isZoomAllowed = true,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    // Kutsutaan YCharts funktiota kaavion piirtämiseen
    BubbleChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(400.dp),

        //Määritellään diagrammin dataksi yllä luomamme data-arvo, joka sisältää diagrammin datan ja akseleiden konfiguraatiot
        bubbleChartData = data
    )
}

@Composable
fun drawInfoRow(color: Color, text: String){
    Row() {
        Canvas(modifier = Modifier.size(20.dp), onDraw = {
            drawCircle(color = color)
        })

        Text(
            textAlign = TextAlign.Left,
            text = text,
            color = color,
            modifier = Modifier.padding(8.dp, 0.dp,0.dp, 0.dp)
        )
    }
}