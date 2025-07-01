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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import androidx.navigation.NavController

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

//YCharts importit
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp

//Mathparser importit
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/** Tässä esimerkissä kaava lasketaan yhden kaavan sijasta kahdella kaavalla, sekä kolmannella argumentilla (t)
 * Käyttäjä syöttää x:n kaavan (esim x=t*2) ja y:n kaavan (esim y=t^2).
 * x:n ja y:n arvot lasketaan kaavoilla välille tStart - tEnd, tIncrement arvon välein.
 * Kun arvot on laskettu, käyrä piirretään hyödyntäen YChartsin viivakaaviota.
 *
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa aiempiin esimerkkeihin, joissa kuvaajien piirtoa käydään läpi.
 */

//Alustetaan dynaamiset listat datapisteille
var Calculator7lineChartList = mutableListOf<Point>()

//Index muuttuja, käyrän pituuden laskemiseen
var calc7DistanceIndex = 0

@Composable
fun GraphingCalculatorScreen7(navController: NavController){

    //Muuttujat, joihin lisätään kaavat, kun "piirrä kaava" näppäintä painetaan
    var xFormula by remember { mutableStateOf("") }
    var yFormula by remember { mutableStateOf("") }

    //t arvon muuttujat
    var tValue by remember {mutableFloatStateOf(0f)}
    var tIncrement by remember {mutableFloatStateOf(0.05f)}
    var tStart by remember { mutableStateOf("-2") }
    var tEnd by remember { mutableStateOf("2") }

    //Y ja x arvojen muuttujat
    var xValue by remember { mutableFloatStateOf(0.0f) }
    var yValue by remember { mutableFloatStateOf(0.0f) }

    //Muuttujat käyrän pituuteen
    var distanceValue by remember {mutableFloatStateOf(0.0f)}
    var distanceString by remember {mutableStateOf("")}

    var UiUpdate by remember {mutableStateOf("")}

    val context = LocalContext.current
    var steps: Int

    if (Calculator7lineChartList.size >= 10) {
        steps = 10
    } else {
        steps = Calculator7lineChartList.size
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .startDrawPadding(5.dp)
        .steps(Calculator7lineChartList.size - 1)
        .labelAndAxisLinePadding(25.dp)
        .labelData { i ->
            val xMin = Calculator7lineChartList.minOf { it.x } //* 1.25f
            val xMax = Calculator7lineChartList.maxOf { it.x } //* 1.25f
            val xScale = (xMax - xMin) / steps
            ((i * xScale) + xMin).formatToSinglePrecision()
        }.build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = Calculator7lineChartList.minOf { it.y } //* 1.25f
            val yMax = Calculator7lineChartList.maxOf { it.y } //* 1.25f
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = Calculator7lineChartList,
                    lineStyle = LineStyle(color = Color.Black),
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    //shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp()
                    //waveFillColor = WaveFillColor(topColor = Color.Green, bottomColor = Color.Red),
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column() {
            Text(
                modifier = Modifier.padding(10.dp, 20.dp, 0.dp, 0.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                text = "Graafinen laskin 7: \nParametrikäyrä kolmella argumentilla"
            )

            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(300.dp)
            ) {
                if(Calculator7lineChartList.isNotEmpty()) {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        lineChartData = data
                    )
                }
            }

            Row() {
                TextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(140.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = xFormula,
                    onValueChange = { newText ->
                        xFormula = newText
                    },
                    label = {
                        Text(text = "x kaava")
                    },
                )

                TextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(140.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = yFormula,
                    onValueChange = { newText ->
                        yFormula = newText
                    },
                    label = {
                        Text(text = "y kaava")
                    },
                )
            }

            Row() {
                TextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(113.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = tStart,
                    onValueChange = { newText ->
                        tStart = newText
                    },
                    label = {
                        Text(text = "t lähtöarvo")
                    }
                )

                Text(
                    modifier = Modifier.padding(0.dp, 20.dp),
                    text = "≤ t ≤ ",
                    fontSize = 25.sp
                )

                TextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(113.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = tEnd,
                    onValueChange = { newText ->
                        tEnd = newText
                    },
                    label = {
                        Text(text = "t loppuarvo")
                    },
                )
            }

            Row() {
                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                    onClick = {
                        //Asetetaan tValue arvoksi tStart, jotta laskeminen aloitetaan tStart arvosta.
                        tValue = tStart.toFloat()

                        CoroutineScope(IO).launch {

                            //lasketaan x ja y arvoja kunnes tValue saavuttaa tEnd arvon.
                            while(tValue <= tEnd.toFloat()) {

                                //Asetetaan "t" tekstin kohdalle muuttujan tValue arvo.
                                var updatedXFormula = xFormula.replace("t", tValue.toString())

                                //Luodaan x:n argumentti
                                var xArgument = Argument(updatedXFormula)

                                //Lasketaan x:n arvo ja asetetaan se xValue muuttujaan
                                var ex = Expression("x", xArgument)
                                xValue = ex.calculate().toFloat()

                                //Sama kuin x:n kohdalla, "t" tekstin tilalle asetetaan tValue arvo.
                                var updatedYFormula = yFormula.replace("t", tValue.toString())

                                //Luodaan y:n argumentti
                                var yArgument = Argument(updatedYFormula)

                                //Lasketaan y:n arvo ja asetetaan se yValue muuttujaan
                                var ey = Expression("y", yArgument)
                                yValue = ey.calculate().toFloat()

                                //Lisätään piste listaan, käyttäen xValue ja yValue muuttujia.
                                Calculator7lineChartList.add(Point(xValue, yValue))

                                //Lisätään t:n arvoa seuraavaa laskua varten, pyöristetään t:n arvo kahteen desimaaliin.
                                tValue = "%.2f".format(tValue + tIncrement).toFloat()
                            }

                            UiUpdate = " "
                            UiUpdate = ""
                        }
                    }
                ) {
                    Text("Piirrä kaava")
                }

                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                    onClick = {
                        if(Calculator7lineChartList.isNotEmpty()) {
                            //Poistetaan listan viimeinen arvo niin kauan kunnes lista on tyhjä
                            while(Calculator7lineChartList.isNotEmpty()) {
                                Calculator7lineChartList.removeAt(Calculator7lineChartList.size -1)
                            }
                            UiUpdate = " "
                            UiUpdate = ""

                            xFormula = ""
                            yFormula = ""
                        }
                    }
                ) {
                    Text("Tyhjennä kaava")
                }
            }

            Row() {
                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                    onClick = {
                        if(Calculator7lineChartList.isNotEmpty()) {
                            CoroutineScope(IO).launch {
                                while(calc7DistanceIndex <= Calculator7lineChartList.size -2) {

                                    //Haetaan listasta kahden pisteen arvot.
                                    var listx1 = Calculator7lineChartList[calc7DistanceIndex].x
                                    var listy1 = Calculator7lineChartList[calc7DistanceIndex].y
                                    var listx2 = Calculator7lineChartList[calc7DistanceIndex+1].x
                                    var listy2 = Calculator7lineChartList[calc7DistanceIndex+1].y

                                    //Kutsutaan funktiota joka laskee pisteiden välisen etäisyyden, lisätään arvo distanceValue muuttujaan.
                                    distanceValue = distanceValue + distanceBetweenPoints(listx1, listy1, listx2, listy2)
                                    calc7DistanceIndex++
                                }
                                distanceString = "Käyrän pituus: $distanceValue"
                            }
                        }
                    }
                ) {
                    Text("Käyrän pituus")
                }

                Text(
                    text = distanceString,
                    modifier = Modifier.padding(0.dp, 15.dp)
                )
            }

            //Näppäin jolla palataan takaisin päävalikkoon
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
                Text("Päävalikkoon")
            }

            Text(UiUpdate)
        }
    }
}

//funktio kahden pisteen välisen etäisyyden laskemiseen
private fun distanceBetweenPoints(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    var xDifference = x2-x1
    var yDifference = y2-y1

    xDifference = abs(xDifference)
    yDifference = abs(yDifference)

    var xSquaredExpression = Expression ("√$xDifference")
    var ySquaredExpression = Expression ("√$yDifference")

    var xSquared = xSquaredExpression.calculate().toFloat()
    var ySquared = ySquaredExpression.calculate().toFloat()

    var combinedValue = xSquared + ySquared

    return combinedValue
}