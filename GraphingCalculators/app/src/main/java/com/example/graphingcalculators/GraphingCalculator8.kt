package com.example.graphingcalculators

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/** Tässä esimerkissä lasketaan PNS -suora eli pienimmän neliön suora.
 * PNS -suora lasketaan kaavalla y= kx + b, missä k on kulmakerroin ja b on vakio (kertoo suoran ja y-akselin leikkauskohdan)
 * kulmakerroin (k) saadaan kaavalla:   k = (n * Σ(xi * yi) - (Σxi) * (Σyi)) / (n * Σ(xi^2) - (Σxi)^2)
 * vakio (b) saadaan kaavalla:          b = (Σyi - k * Σxi) / n
 * Kaavoissa:
 * - n on datapisteiden määrä
 * - Σxi on kaikkien x-arvojen summa
 * - Σyi on kaikkien y-arvojen summa
 * - Σ(xi * yi) on kaikkien x*y tulojen summa
 * - Σ(xi^2) on kaikkien x-arvojen neliöiden summa
 *
 * Ensin laskemme siis kulmakertoimen (k) ja vakion (b). Tämän jälkeen syötetään (k) ja (b) kaavaan y = kx + b.
 * y:n arvot lasketaan samalle välille kuin data, jolle pns -suora lasketaan (esimerkissä 1x - 12x).
 *
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa aiempiin esimerkkeihin, joissa kuvaajien piirtoa käydään läpi.
 */

//Alustetaan dynaaminen lista PNS -arvoja varten
var calculator8lineChartListPNS = mutableListOf<Point>()

//Luodaan index -muuttujat laskutoimituksia varten
var xSumIndex = 0
var ySumIndex = 0
var xySumIndex = 0
var xSquaredSumIndex = 0

@Composable
fun GraphingCalculatorScreen8(navController: NavController) {

    var n = 0                   //Datapisteiden määrä (listan pituus)
    var k = 0.0f                //Kulmakerroin
    var b = 0.0f                //vakio b (kertoo suoran ja y-akselin leikkauskohdan)
    var xSum = 0.0f             //x arvojen yhteenlaskettu summa
    var ySum = 0.0f             //y arvojen yhteenlaskettu summa
    var xySum = 0.0f            //x ja y -tulojen yhteenlaskettu summa
    var xSquaredSum = 0.0f      //x arvojen neliöiden yhteenlaskettu summa
    var xValuePNS = -5f
    var yValuePNS = 0f
    var UiUpdate by remember {mutableStateOf("")}
    var infoText by remember {mutableStateOf("")}


    //Luodaan dynaaminen lista, johon lisätään halutut arvot
    //Esimerkissä käytetään datana Nokian osakkeen arvoa Tammikuu 2024 - Joulukuu 2024 välillä.
    val dataList = arrayListOf(
        Point(1f, 3.332f),
        Point(2f, 3.260f),
        Point(3f, 3.291f),
        Point(4f, 3.412f),
        Point(5f, 3.591f),
        Point(6f, 3.558f),
        Point(7f, 3.621f),
        Point(8f, 3.978f),
        Point(9f, 3.924f),
        Point(10f, 4.325f),
        Point(11f, 3.980f),
        Point(12f, 4.274f)
    )

    val monthList = arrayListOf(
        "Tammi",
        "Helmi",
        "Maalis",
        "Huhti",
        "Touko",
        "Kesä",
        "Heinä",
        "Elo",
        "Syys",
        "Loka",
        "Marras",
        "Joulu",
        ""
    )

    val yAxisSteps = 10

    val xAxisData = AxisData.Builder()
        .axisStepSize(50.dp)
        .topPadding(105.dp)
        .steps(dataList.size - 1)
        .axisLabelFontSize(15.sp)
        .labelData { i -> monthList[i].toString() }
        .labelAndAxisLinePadding(25.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yAxisSteps)
        .labelAndAxisLinePadding(30.dp)
        .labelData { i ->
            val yMin = dataList.minOf{it.y}
            val yMax = dataList.maxOf{it.y}
            val yScale = (yMax - yMin) / yAxisSteps
            ((i * yScale) + yMin).formatToSinglePrecision() + " €"
        }.build()

    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = dataList,
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

    val dataTwoLines = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = dataList,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                ),
                Line(
                    dataPoints = calculator8lineChartListPNS,
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    selectionHighlightPopUp = SelectionHighlightPopUp(),
                    lineStyle = LineStyle(color = Color.Red)
                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    Column() {
        Text(
            modifier = Modifier.padding(10.dp, 50.dp, 0.dp, 0.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            text = "Graafinen laskin 8: \nPNS -suora"
        )

        Text(
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 0.dp),
            textAlign = TextAlign.Center,
            text = "Data: Nokian osake Tammikuu 2024 - Joulukuu 2024",
            fontSize = 15.sp
        )

        //Jos PNS -suora on laskettu, näytetään osakekäyrä ja PNS -suora.
        if(calculator8lineChartListPNS.isNotEmpty()) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                lineChartData = dataTwoLines
            )
        } else {
            //Jos PNS -suoraa ei ole vielä laskettu, näytetään vain osakekäyrä
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                lineChartData = data
            )
        }

        Row() {
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    if(dataList.isNotEmpty() && calculator8lineChartListPNS.isEmpty()) {
                        CoroutineScope(IO).launch {

                            //Haetaan x:n alku ja loppuarvot listasta, jotta PNS -suora piirretään samalle alueelle kuin osakekäyrä
                            val xStart = dataList[0].x
                            val xEnd = dataList[dataList.size -1].x

                            //Asetetaan listan pituus n -arvoksi.
                            n = dataList.size

                            //Lasketaan kaikkien x arvojen summa
                            while(xSumIndex <= dataList.size -1) {
                                //Haetaan x:n arvo listasta index -arvon kohdalta.
                                var x = dataList[xSumIndex].x

                                //Lisätään x:n arvo xSum -muuttujaan ja pyöristetään summa kahden desimaalin tarkkuuteen
                                xSum = "%.2f".format(xSum + x).toFloat()

                                xSumIndex++
                            }

                            //Kaikkien y arvojen summa (sama kuin yllä x:n arvoille)
                            while(ySumIndex <= dataList.size -1) {
                                var y = dataList[ySumIndex].y
                                ySum = "%.2f".format(ySum + y).toFloat()
                                ySumIndex++
                            }

                            //Kaikkien x-y arvojen tulon summaa
                            while(xySumIndex <= dataList.size -1) {
                                //Haetaan listasta x ja y -arvot index arvon kohdasta. Lasketaan x:n ja y:n tulo
                                var xy = dataList[xySumIndex].x * dataList[xySumIndex].y

                                //Lisätään saatu tulo, xySum muuttujaan ja pyöristetään summa kahden desimaalin tarkkuuteen
                                xySum = "%.2f".format(xySum + xy).toFloat()
                                xySumIndex++
                            }

                            //kaikkien x arvojen neliöiden summa
                            while(xSquaredSumIndex <= dataList.size -1) {
                                //Haetaan listasta x:n arvo index arvon kohdalta.
                                var x = dataList[xSquaredSumIndex].x

                                //Lisätään x:n neliö muuttujaan xSquaredSum
                                xSquaredSum = xSquaredSum + (x*x)
                                xSquaredSumIndex++
                            }

                            //Lasketaan kulmakerroin, kaavalla: k = (n * Σ(xi * yi) - (Σxi) * (Σyi)) / (n * Σ(xi^2) - (Σxi)^2)
                            var kArgument = Argument("k = ($n * $xySum - ($xSum) * ($ySum)) / ($n * $xSquaredSum - ($xSum)^2)")
                            var kExpression = Expression("k", kArgument)
                            k = kExpression.calculate().toFloat()

                            //Lasketaan vakio b, kaavalla: b = (Σyi - k * Σxi) / n
                            var bArgument = Argument("b = (($ySum - $k * $xSum) / $n)")
                            var bExpression = Expression("b", bArgument)
                            b = bExpression.calculate().toFloat()

                            //Asetetaan xValuePNS arvoksi xStart, jotta laskeminen aloitetaan oikeasta arvosta.
                            xValuePNS = xStart

                            //Lasketaan y:n arvoja kunnes arvot on laskettu välille xStart - xEnd
                            while(xValuePNS <= xEnd) {
                                //Lasketaan y:n arvot kaavalla: y = kx + b
                                var yArgument = Argument("y = $k*$xValuePNS +$b")
                                var yExpression = Expression("y", yArgument)

                                //Asettaan laskettu arvo yValuePNS -muuttujaan.
                                yValuePNS = yExpression.calculate().toFloat()

                                //Lisätään x:n arvo ja laskettu y:n arvo listaan.
                                calculator8lineChartListPNS.add(Point(xValuePNS, yValuePNS))

                                //Lisätään xValuePNS arvoa 0.1 verran seuraavaa laskua varten ja pyöristetään se kahteen desimaaliin
                                xValuePNS = "%.2f".format(xValuePNS + 0.1f).toFloat()
                            }

                            infoText = "Kulmakerroin k: ~${"%.2f".format(k).toFloat()}\nVakio b: ~${"%.2f".format(b).toFloat()}"

                            UiUpdate = " "
                            UiUpdate = ""
                        }
                    }
                }
            ) {
                Text("Laske PNS -suora")
            }

            Text(
                modifier = Modifier.padding(10.dp),
                text = infoText
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

        Text(UiUpdate)
    }
}