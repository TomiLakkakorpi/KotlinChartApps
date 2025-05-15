package com.example.bubblechartappv1

import com.example.bubblechartappv1.ui.theme.BubbleChartAppV1Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

//YCharts Imports
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.bubblechart.model.BubbleChartData
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.bubblechart.BubbleChart
import co.yml.charts.ui.bubblechart.model.Bubble
import co.yml.charts.ui.bubblechart.model.BubbleStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import com.example.bubblechartappv1.ui.theme.colorHifk
import com.example.bubblechartappv1.ui.theme.colorHpk
import com.example.bubblechartappv1.ui.theme.colorIlves
import com.example.bubblechartappv1.ui.theme.colorJukurit
import com.example.bubblechartappv1.ui.theme.colorJyp
import com.example.bubblechartappv1.ui.theme.colorKalpa
import com.example.bubblechartappv1.ui.theme.colorKiekkoespoo
import com.example.bubblechartappv1.ui.theme.colorKookoo
import com.example.bubblechartappv1.ui.theme.colorKärpät
import com.example.bubblechartappv1.ui.theme.colorLukko
import com.example.bubblechartappv1.ui.theme.colorPelicans
import com.example.bubblechartappv1.ui.theme.colorSaipa
import com.example.bubblechartappv1.ui.theme.colorSport
import com.example.bubblechartappv1.ui.theme.colorTappara
import com.example.bubblechartappv1.ui.theme.colorTps
import com.example.bubblechartappv1.ui.theme.colorÄssät
import kotlin.collections.List

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
                        text = "Liiga runkosarja 2024 \n X-akseli: Joukkueen sijoitus runkosarjassa \n Y-akseli: runkosarjan pisteet \n  Kuplan koko: Joukkueen budjetti kaudelle",
                        fontSize = 16.sp
                    )
                    DrawBubbleChart()
                    Row() {
                        Column(
                           modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                textAlign = TextAlign.Left,
                                text = "1: Lukko",
                                color = colorLukko
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "2: Ilves",
                                color = colorIlves
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "3: KalPa",
                                color = colorKalpa
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "4: HIFK",
                                color = colorHifk
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "5: SaiPa",
                                color = colorSaipa
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "6: KooKoo",
                                color = colorKookoo
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "7: Ässät",
                                color = colorÄssät
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "8: K-Espoo",
                                color = colorKiekkoespoo
                            )
                        }

                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                textAlign = TextAlign.Left,
                                text = "9: Tappara",
                                color = colorTappara
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "10: HPK",
                                color = colorHpk
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "11: Sport",
                                color = colorSport
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "12: TPS",
                                color = colorTps
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "13: Kärpät",
                                color = colorKärpät
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "14: Jyp",
                                color = colorJyp
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "15: Pelicans",
                                color = colorPelicans
                            )
                            Text(
                                textAlign = TextAlign.Left,
                                text = "16: Jukurit",
                                color = colorJukurit
                            )
                        }
                    }
                }
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
    val pointsData: List<Bubble> =
        listOf(
            Bubble( //Lukko
                center = Point(1F, 112F),
                density = 29.50F,
                bubbleStyle = BubbleStyle(solidColor = colorLukko),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(//Ilves
                center = Point(2F, 111F),
                density = 31.00F,
                bubbleStyle = BubbleStyle(solidColor = colorIlves),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(  //Kalpa
                center = Point(3F, 107F),
                density = 23.00F,
                bubbleStyle = BubbleStyle(solidColor = colorKalpa),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //HIFK
                center = Point(4F, 107F),
                density = 33.50F,
                bubbleStyle = BubbleStyle(solidColor = colorHifk),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),
            Bubble(  //SaiPa
                center = Point(5F, 106F),
                density = 20.00F,
                bubbleStyle = BubbleStyle(solidColor = colorSaipa),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),
            Bubble( //KooKoo
                center = Point(6F, 99F),
                density = 20.00F,
                bubbleStyle = BubbleStyle(solidColor = colorKookoo),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Ässät
                center = Point(7F, 95F),
                density = 23.50F,
                bubbleStyle = BubbleStyle(solidColor = colorÄssät),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //K-Espoo
                center = Point(8F, 91F),
                density = 20.00F,
                bubbleStyle = BubbleStyle(solidColor = colorKiekkoespoo),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Tappara
                center = Point(9F, 90F),
                density = 32.50F,
                bubbleStyle = BubbleStyle(solidColor = colorTappara),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //HPK
                center = Point(10F, 83F),
                density = 19.00F,
                bubbleStyle = BubbleStyle(solidColor = colorHpk),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Sport
                center = Point(11F, 83F),
                density = 18.51F,
                bubbleStyle = BubbleStyle(solidColor = colorSport),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //TPS
                center = Point(12F, 79F),
                density = 24.00F,
                bubbleStyle = BubbleStyle(solidColor = colorTps),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Kärpät
                center = Point(13F, 77F),
                density = 32.50F,
                bubbleStyle = BubbleStyle(solidColor = colorKärpät),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(//JYP
                center = Point(14F, 76F),
                density = 24.00F,
                bubbleStyle = BubbleStyle(solidColor = colorJyp),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Pelicans
                center = Point(15F, 75F),
                density = 30.00F,
                bubbleStyle = BubbleStyle(solidColor = colorPelicans),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble( //Jukurit
                center = Point(16F, 49F),
                density = 15.00F,
                bubbleStyle = BubbleStyle(solidColor = colorJukurit),
                selectionHighlightPoint = SelectionHighlightPoint(Color.Black),
                selectionHighlightPopUp = SelectionHighlightPopUp(Color.Cyan)
            ),

            Bubble(
                center = Point(17F, 0F),
                density = 0F)
            )

    // Luodaan "steps" arvo, jolla määritellään Y akselin askeleiden määrä
    val steps = 12

    // Luodaan xAxisData arvo, jossa konfiguroidaan x akselille ominaisuuksia.
    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .steps(pointsData.size -1)
        .labelData { i -> pointsData[i].center.x.toInt().toString() }
        //.labelAndAxisLinePadding(20.dp)
        .startDrawPadding(10.dp)
        .build()

    // Luodaan yAxisData arvo, jossa konfiguroidaan y akselille ominaisuuksia.
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = 0f
            val yMax = 120f
            //val yMax = pointsData.maxOf {it.y}
            val yScale = (yMax - yMin) / steps
            ((i*yScale) + yMin).formatToSinglePrecision()
        }.build()

    // Luodaan data arvo, johon lisätään datalista(pointsData) sekä x ja y akseleiden konfiguraatiot
    val data = BubbleChartData(
        bubbles = pointsData,
        isZoomAllowed = true,
        //DataUtils.getBubbleChartDataWithSolidStyle(pointsData),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    // Käytetään YCharts funktiota diagrammin piirtämiseen
    BubbleChart(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(400.dp),

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