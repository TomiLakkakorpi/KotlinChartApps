package com.example.graphingcalculators

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
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

//Alustetaan dynaamiset listat dataa varten
var calculator8lineChartList = mutableListOf<Point>()
var calculator8lineChartListPNS = mutableListOf<Point>()

//Luodaan index -muuttujat laskutoimituksia varten'
var xSumIndex = 0
var ySumIndex = 0
var xySumIndex = 0
var xSquaredSumIndex = 0

@Composable
fun GraphingCalculatorScreen8(navController: NavController) {

    var xValue = 0f             //x -muuttuja esimerkkidatan piirtoon
    var yValue = 0f             //y -muuttuja esimerkkidatan piirtoon

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column()
        {
            val context = LocalContext.current
            var steps: Int

            if (calculator8lineChartList.size >= 10) {
                steps = 10
            } else {
                steps = calculator8lineChartList.size
            }

            val xAxisData = AxisData.Builder()
                .axisStepSize(30.dp)
                //.startDrawPadding(48.dp)
                .steps(calculator8lineChartList.size - 1)
                .labelAndAxisLinePadding(30.dp)
                .labelData { i ->
                    val xMin = 0f
                    val xMax = 20f
                    //val xMin = calculator8lineChartList.minOf { it.x }
                    //val xMax = calculator8lineChartList.maxOf { it.x }
                    val xScale = (xMax - xMin) / steps
                    ((i * xScale) + xMin).formatToSinglePrecision()
                }.build()

            val yAxisData = AxisData.Builder()
                .steps(steps)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yMin = 0f
                    val yMax = 20f
                    //val yMin = calculator8lineChartList.minOf { it.y }
                    //val yMax = calculator8lineChartList.maxOf { it.y }
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }.build()

            val data = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = calculator8lineChartList,
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            shadowUnderLine = ShadowUnderLine(),
                            selectionHighlightPopUp = SelectionHighlightPopUp()
                        )
                    )
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines()
            )

            val dataPNS = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = calculator8lineChartList,
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            shadowUnderLine = ShadowUnderLine(),
                            selectionHighlightPopUp = SelectionHighlightPopUp()
                        ),
                        Line(
                            dataPoints = calculator8lineChartListPNS,
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            shadowUnderLine = ShadowUnderLine(),
                            selectionHighlightPopUp = SelectionHighlightPopUp(),
                            lineStyle = LineStyle(color = Color.Red)
                        ),
                    )
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines()
            )

            Text(
                modifier = Modifier.padding(10.dp, 20.dp, 0.dp, 0.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                text = "Graafinen laskin 8: PNS -suora"
            )
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp)
            ) {
                //Jos molemmat listat sisältävät dataa, kutsutaan kuvaajaa, jossa on molemmat datalistat
                if(calculator8lineChartList.isNotEmpty() && calculator8lineChartListPNS.isNotEmpty()){
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        lineChartData = dataPNS
                    )
                    //Jos vain esimerkkidata on laskettu, näytetään kuvaaja jossa on vain datalista
                } else if(calculator8lineChartList.isNotEmpty() && calculator8lineChartListPNS.isEmpty()){
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        lineChartData = data
                    )
                }
            }

            Row() {
                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                    onClick = {
                        if(calculator8lineChartList.isEmpty()) {
                            while(xValue <= 20f) {
                                //Lasketaan y:lle satunnaisia arvoja välillä 0-20.
                                yValue = (0 until 20).random().toFloat()

                                //Lisätään x ja y -arvot listaan.
                                calculator8lineChartList.add(Point(xValue, yValue))

                                //Lisätään x:n arvoa ja pyöristetään arvo kahteen desimaaliin.
                                xValue = "%.2f".format(xValue + 1f).toFloat()
                            }
                        }

                        UiUpdate = " "
                        UiUpdate = ""
                    }
                ) {
                    Text("Piirrä esimerkkidata")
                }

                Button(
                    onClick = {
                        if (calculator8lineChartList.isNotEmpty()) {
                            while(calculator8lineChartList.isNotEmpty()) {
                                calculator8lineChartList.removeAt(calculator8lineChartList.size -1)
                            }

                            UiUpdate = " "
                            UiUpdate = ""
                        }

                        if(calculator8lineChartListPNS.isNotEmpty()) {
                            while(calculator8lineChartListPNS.isNotEmpty()) {
                                calculator8lineChartListPNS.removeAt(calculator8lineChartListPNS.size -1)
                            }

                            n = 0
                            k = 0.0f
                            b = 0.0f
                            xSum = 0.0f
                            xySum = 0.0f
                            xSquaredSum = 0.0f
                            xValuePNS = -5f
                            yValuePNS = 0f

                            xSumIndex = 0
                            ySumIndex = 0
                            xySumIndex = 0
                            xSquaredSumIndex = 0

                            infoText = ""
                        }
                    }
                ) {
                    Text("Tyhjennä taulukko")
                }
            }

            Row() {
                Button(
                    onClick = {
                        if(calculator8lineChartList.isNotEmpty() && calculator8lineChartListPNS.isEmpty()) {
                            CoroutineScope(IO).launch {

                                //Haetaan xStart ja xEnd listasta (Ensimmäinen ja viimeinen arvo). Y:n arvot lasketaan tälle välille
                                val xStart = calculator8lineChartList[0].x
                                val xEnd = calculator8lineChartList[calculator8lineChartList.size -1].x

                                //Asetetaan listan pituus n -arvoksi.  n = calcu
                                n = calculator8lineChartList.size

                                //Lasketaan Kaikkien x arvojen summa
                                while(xSumIndex <= calculator8lineChartList.size -1) {

                                    //Haetaan x:n arvo listasta index -arvon kohdalta.
                                    var x = calculator8lineChartList[xSumIndex].x

                                    //Lisätään x:n arvo xSum -muuttujaan ja pyöristetään summa kahden desimaalin tarkkuuteen
                                    xSum = "%.2f".format(xSum + x).toFloat()
                                    xSumIndex++
                                }

                                //Kaikkien y arvojen summa (sama kuin yllä x:n arvoille)
                                while(ySumIndex <= calculator8lineChartList.size -1) {
                                    var y = calculator8lineChartList[ySumIndex].y
                                    ySum = "%.2f".format(ySum + y).toFloat()

                                    ySumIndex++
                                }

                                //Kaikkien x-y arvojen tulon summaa
                                while(xySumIndex <= calculator8lineChartList.size -1) {

                                    //Haetaan listasta x ja y -arvot index arvon kohdasta. Lasketaan x:n ja y:n tulo
                                    var xy = calculator8lineChartList[xySumIndex].x * calculator8lineChartList[xySumIndex].y

                                    //Lisätään saatu tulo, xySum muuttujaan ja pyöristetään summa kahden desimaalin tarkkuuteen
                                    xySum = "%.2f".format(xySum + xy).toFloat()

                                    xySumIndex++
                                }

                                //kaikkien x arvojen neliöiden summa
                                while(xSquaredSumIndex <= calculator8lineChartList.size -1) {

                                    //Haetaan listasta x:n arvo index arvon kohdalta.
                                    var x = calculator8lineChartList[xSquaredSumIndex].x

                                    //Lisätään x:n neliö muuttujaan xSquaredSum
                                    xSquaredSum = xSquaredSum + (x*x)

                                    xSquaredSumIndex++
                                }

                                //Lasketaan kulmakerroin, kaava: k = (n * Σ(xi * yi) - (Σxi) * (Σyi)) / (n * Σ(xi^2) - (Σxi)^2)
                                var kArgument = Argument("k = ($n * $xySum - $xSum * $ySum) / ($n * $xSquaredSum - $xSum^2)")
                                var kExpression = Expression("k", kArgument)
                                k = kExpression.calculate().toFloat()

                                //Lasketaan vakio b, kaava: b = (Σyi - k * Σxi) / n
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
                                    xValuePNS = "%.2f".format(xValuePNS + 0.25f).toFloat()
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

                Button(
                    modifier = Modifier.padding(10.dp, 0.dp),
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

            Text(
                modifier = Modifier.padding(10.dp),
                text = infoText
            )
        }
    }
}