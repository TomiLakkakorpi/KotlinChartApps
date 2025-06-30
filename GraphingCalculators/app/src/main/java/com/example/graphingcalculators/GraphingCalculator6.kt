package com.example.graphingcalculators

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.math.abs

//Ycharts importit
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

//MathParser importit
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/** Esimerkki, jossa aiempien esimerkkien ominaisuudet on yhdistetty yhteen (paitsi ympyrän piirto)
 *
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa aiempiin esimerkkeihin, joissa kuvaajien piirtoa käydään läpi.
 */

//Index muuttujat potenssilaskuille
var CalculatorMainSquareXIndex1 = 0
var CalculatorMainSquareYIndex1 = 0
var CalculatorMainSquareXIndex2 = 0
var CalculatorMainSquareYIndex2 = 0

//Index muuttujat neliöjuurilaskuille
var CalculatorMainSquareRootXIndex1 = 0
var CalculatorMainSquareRootYIndex1 = 0
var CalculatorMainSquareRootXIndex2 = 0
var CalculatorMainSquareRootYIndex2 = 0

//Index muuttujat, käyrän pituuden laskemiseen
var CalcMainDistanceIndex1 = 0
var CalcMainDistanceIndex2 = 0

//Alustetaan dynaamiset listat datapisteille
var CalculatorMainLineChartList1 = mutableListOf<Point>()
var CalculatorMainLineChartList2 = mutableListOf<Point>()

//Muuttujat, joilla tarkistetaan onko kaavan dataa muunnettu.
var isChart1Altered = true
var isChart2Altered = true

@Composable
fun GraphingCalculatorScreen6(navController: NavController) {

    //Alustetaan text -muuttujat, joihin lisätään käyttäjän syöttämät tekstit tekstikentistä.
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }

    //Muuttujat, joihin lisätään kaavat, kun "piirrä kaava" näppäintä painetaan
    var formula1 by remember {mutableStateOf("")}
    var formula2 by remember {mutableStateOf("")}

    //Laskutoimituksiin käytetyt muuttujat
    var e1: Expression
    var x1: Argument
    var y1: Argument

    var e2: Expression
    var x2: Argument
    var y2: Argument

    //x muuttujat, joita käytetään kun pisteitä lisätään listaan.
    var xValue1 by remember {mutableFloatStateOf(-5f)}
    var xValue2 by remember {mutableFloatStateOf(-5f)}

    //Alustetaan muuttujat xStart, xEnd ja xIncrement
    //Näillä muuttujilla käyttäjä voi säätää kaavan piirtoaluetta ja piirtotiheyttä.
    var xStart by remember { mutableFloatStateOf(-5.0f) }
    var xEnd by remember { mutableFloatStateOf(5.0f) }
    var xIncrement by remember { mutableFloatStateOf(0.1f) }

    //Muuttujat joihin lisätään kaavojen käyrien pituudet
    var distanceValue1 by remember {mutableFloatStateOf(0f)}
    var distanceValue2 by remember {mutableFloatStateOf(0f)}

    //Muuttujat joita käytetään kaavojen näyttämiseen käyttöliittymässä
    var formulaLine1 = ""
    var formulaLine2 = ""

    //Muuttujat joita käytetään käyrien pituuden näyttämiseen käyttöliittymässä
    var distanceLine1 = ""
    var distanceLine2 = ""

    //Muuttujat, joilla tarkistetaan onko kaavojen dataa muutettu
    var alteringChart1 by remember {mutableStateOf(false)}
    var alteringChart2 by remember {mutableStateOf(false)}

    //Muuttujat, joilla tarkistetaan onko kaavojen pituus laskettu.
    var chart1DistanceCalculated by remember {mutableStateOf(false)}
    var chart2DistanceCalculated by remember {mutableStateOf(false)}

    var steps: Int

    if(CalculatorMainLineChartList1.isNotEmpty()) {
        if (CalculatorMainLineChartList1.size >= 10) {
            steps = 10
        } else {
            steps = CalculatorMainLineChartList1.size
        }
    } else if (CalculatorMainLineChartList2.size >= 10) {
        steps = 10
    } else {
        steps = CalculatorMainLineChartList2.size
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        //.startDrawPadding(48.dp)
        .steps(CalculatorMainLineChartList1.size - 1)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val xMin = CalculatorMainLineChartList1.minOf { it.x }
            val xMax = CalculatorMainLineChartList1.maxOf { it.x }
            val xScale = (xMax - xMin) / steps
            ((i * xScale) + xMin).formatToSinglePrecision()
        }.build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(35.dp)
        .labelData { i ->
            val yMin = CalculatorMainLineChartList1.minOf { it.y }
            val yMax = CalculatorMainLineChartList1.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

    val xAxisData2 = AxisData.Builder()
        .axisStepSize(30.dp)
        //.startDrawPadding(48.dp)
        .steps(CalculatorMainLineChartList2.size - 1)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val xMin = CalculatorMainLineChartList2.minOf { it.x }
            val xMax = CalculatorMainLineChartList2.maxOf { it.x }
            val xScale = (xMax - xMin) / steps
            ((i * xScale) + xMin).formatToSinglePrecision()
        }.build()

    val yAxisData2 = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(35.dp)
        .labelData { i ->
            val yMin = CalculatorMainLineChartList2.minOf { it.y }
            val yMax = CalculatorMainLineChartList2.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()

    val dataTwoCharts = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = CalculatorMainLineChartList1,
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp()
                ),
                Line(
                    dataPoints = CalculatorMainLineChartList2,
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp()

                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    val dataChart1 = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = CalculatorMainLineChartList1,
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp()

                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )

    val dataChart2 = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = CalculatorMainLineChartList2,
                    selectionHighlightPoint = SelectionHighlightPoint(),
                    shadowUnderLine = ShadowUnderLine(),
                    selectionHighlightPopUp = SelectionHighlightPopUp()

                ),
            )
        ),
        xAxisData = xAxisData2,
        yAxisData = yAxisData2,
        gridLines = GridLines()
    )

    val context = LocalContext.current

    //Määritellään mitä kaavojen tekstikentissä näytetään
    if(formula1.isNotEmpty() && formula2.isNotEmpty()) {
        formulaLine1 = "Kaava 1: $formula1"
        formulaLine2 = "Kaava 2: $formula2"
    } else if(formula1.isNotEmpty() && formula2.isEmpty()) {
        formulaLine1 = "Kaava 1: $formula1"
        formulaLine2 = ""
    } else if(formula1.isEmpty() && formula2.isNotEmpty()) {
        formulaLine1 = ""
        formulaLine2 = "Kaava 2: $formula2"
    } else {
        formulaLine1 = "Kaavoja ei ole vielä syötetty."
        formulaLine2 = "Syötä vähintään yksi kaava!"
    }

    //Määritellään mitä pituuden tekstikentissä näytetään.
    if(chart1DistanceCalculated && chart2DistanceCalculated) {
        distanceLine1 = "pituus: ${distanceValue1.formatToSinglePrecision()}"
        distanceLine2 = "pituus: ${distanceValue2.formatToSinglePrecision()}"
    } else if(chart1DistanceCalculated && !chart2DistanceCalculated) {
        distanceLine1 = "pituus: ${distanceValue1.formatToSinglePrecision()}"
        distanceLine2 = ""
    } else if(!chart1DistanceCalculated && chart2DistanceCalculated) {
        distanceLine1 = ""
        distanceLine2 = "pituus: ${distanceValue2.formatToSinglePrecision()}"
    } else {
        distanceLine1 = ""
        distanceLine2 = ""
    }

    Column() {
        Box(
            modifier = Modifier
                .padding(0.dp, 40.dp, 0.dp, 5.dp)
                .fillMaxWidth()
                .height(500.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column() {
                    Row() {
                        Column() {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = formulaLine1
                            )

                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = formulaLine2
                            )
                        }

                        Column() {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = distanceLine1
                            )

                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = distanceLine2
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .border(BorderStroke(2.dp, Color.Black))
                    ) {
                        //Jos molemmissa listoissa on dataa, kutsutaan LineChart funktiota, jonka dataksi on määritelty kaksi viivaa.
                        if(CalculatorMainLineChartList1.isNotEmpty() && CalculatorMainLineChartList2.isNotEmpty()){
                            LineChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp),
                                lineChartData = dataTwoCharts
                            )
                        }

                        //Jos vain listassa 1 on dataa, kutsutaan LineChart funktiota, jonka dataksi on määritelty vain yksi viiva.
                        if(CalculatorMainLineChartList1.isNotEmpty() && CalculatorMainLineChartList2.isEmpty()){
                            LineChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp),
                                lineChartData = dataChart1
                            )
                        }

                        //Jos vain listassa 2 on dataa, kutsutaan LineChart funktiota, jonka dataksi on määritelty vain yksi viiva.
                        if(CalculatorMainLineChartList1.isEmpty() && CalculatorMainLineChartList2.isNotEmpty()){
                            LineChart(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp),
                                lineChartData = dataChart2
                            )
                        }
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth()
        ) {
            //Tekstikenttä kaavan 1 syöttämiseen. Kaava asetetaan text1 muuttujaan
            TextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(195.dp)
                    .height(50.dp),
                value = text1,
                onValueChange = { newText ->
                    text1 = newText
                },
                label = {
                    Text(text = "Syötä kaava 1")
                },
            )

            //tekstikenttä kaavan 2 syöttämiseen. Kaava asetetaan text2 muuttujaan
            TextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(195.dp)
                    .height(50.dp),
                value = text2,
                onValueChange = { newText ->
                    text2 = newText
                },
                label = {
                    Text(text = "Syötä kaava 2")
                },
            )
        }

        Row() {
            //Tekstikenttä x lähtöarvon syöttämiseen, arvo asetetaan xStart muuttujaan.
            TextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(126.dp)
                    .height(50.dp),
                value = xStart.toString(),
                onValueChange = { newText ->
                    xStart = newText.toFloat()
                },
                label = {
                    Text(
                        text = "x Lähtöarvo"
                    )
                },
            )

            //Tekstikenttä x loppuarvon syöttämiseen, arvo asetetaan xEnd muuttujaan.
            TextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(126.dp)
                    .height(50.dp),
                value = xEnd.toString(),
                onValueChange = { newText ->
                    xEnd = newText.toFloat()
                },
                label = {
                    Text(text = "x Loppuarvo")
                },
            )

            //Tekstikenttä x lisäysarvon syöttämiseen, arvo asetetaan xIncrement muuttujaan.
            TextField(
                modifier = Modifier
                    .padding(5.dp)
                    .width(126.dp)
                    .height(50.dp),
                value = xIncrement.toString(),
                onValueChange = { newText ->
                    xIncrement = newText.toFloat()
                },
                label = {
                    Text(text = "x lisäysarvo")
                },
            )
        }

        Row() {
            //Column 1
            Column() {
                //Position 1.1
                Text(
                    modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp),
                    fontSize = 15.sp,
                    text = "Muokkaa \nkaavaa 1"
                )


                Text(
                    modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp),
                    fontSize = 15.sp,
                    text = "Muokkaa \nkaavaa 2"
                )

                //Näppäin x² korottamiseen
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if(alteringChart1) {
                            if(CalculatorMainLineChartList1.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareXIndex1 == CalculatorMainLineChartList1.size) {
                                Toast.makeText(context, "x arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareXIndex1 < CalculatorMainLineChartList1.size) {

                                    //Otetaan y:n arvo listasta index arvon kohdasta.
                                    val y = CalculatorMainLineChartList1[CalculatorMainSquareXIndex1].y

                                    //Otetaan x:n arvo listasta index arvon kohdasta ja korotetaan se toiseen potenssiin.
                                    val x = floatSquared(CalculatorMainLineChartList1[CalculatorMainSquareXIndex1].x)

                                    //Kun alkuperäinen y:n arvo, ja korotettu x:n arvo on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList1[CalculatorMainSquareXIndex1] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareXIndex1++
                                }

                                text1 = ""
                                text1 = formula1
                                isChart1Altered = true

                                distanceValue1 = 0.0f
                                CalcMainDistanceIndex1 = 0
                            }
                        }

                        if(alteringChart2) {
                            if(CalculatorMainLineChartList2.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareXIndex2 == CalculatorMainLineChartList2.size) {
                                Toast.makeText(context, "x arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareXIndex2 < CalculatorMainLineChartList2.size) {

                                    //Otetaan y:n arvo listasta index arvon kohdasta.
                                    val y = CalculatorMainLineChartList2[CalculatorMainSquareXIndex2].y

                                    //Otetaan x:n arvo listasta index arvon kohdasta ja korotetaan se toiseen potenssiin.
                                    val x = floatSquared(CalculatorMainLineChartList2[CalculatorMainSquareXIndex2].x)

                                    //Kun alkuperäinen y:n arvo, ja korotettu x:n arvo on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList2[CalculatorMainSquareXIndex2] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareXIndex2++
                                }

                                text2 = ""
                                text2 = formula2
                                isChart2Altered = true

                                distanceValue2 = 0.0f
                                CalcMainDistanceIndex2 = 0
                            }
                        }

                        if(!alteringChart1 && !alteringChart2) {
                            Toast.makeText(context, "Valitse ainakin yksi kaava jota haluat muuntaa!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        fontSize = 15.sp,
                        text = "x²"
                    )
                }

                //Näppäin y² korottamiseen
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if(alteringChart1) {
                            if(CalculatorMainLineChartList1.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareYIndex1 == CalculatorMainLineChartList1.size){
                                Toast.makeText(context, "y arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareYIndex1 < CalculatorMainLineChartList1.size) {

                                    //Otetaan x:n arvo listasta index arvon kohdasta.
                                    val x = CalculatorMainLineChartList1[CalculatorMainSquareYIndex1].x

                                    //Otetaan y:n arvo listasta index arvon kohdasta ja korotetaan se toiseen potenssiin.
                                    val y = floatSquared(CalculatorMainLineChartList1[CalculatorMainSquareYIndex1].y)

                                    //Kun alkuperäinen x:n arvo, ja korotettu y:n arvo on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList1[CalculatorMainSquareYIndex1] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareYIndex1++
                                }
                                text1 = ""
                                text1 = formula1
                                isChart1Altered = true

                                distanceValue1 = 0.0f
                                CalcMainDistanceIndex1 = 0
                            }
                        }

                        if(alteringChart2) {
                            if(CalculatorMainLineChartList2.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareYIndex2 == CalculatorMainLineChartList2.size){
                                Toast.makeText(context, "y arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareYIndex2 < CalculatorMainLineChartList2.size) {

                                    //Otetaan x:n arvo listasta index arvon kohdasta.
                                    val x = CalculatorMainLineChartList2[CalculatorMainSquareYIndex2].x

                                    //Otetaan y:n arvo listasta index arvon kohdasta ja korotetaan se toiseen potenssiin.
                                    val y = floatSquared(CalculatorMainLineChartList2[CalculatorMainSquareYIndex2].y)

                                    //Kun alkuperäinen x:n arvo, ja korotettu y:n arvo on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList2[CalculatorMainSquareYIndex2] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareYIndex2++
                                }
                                text2 = ""
                                text2 = formula2
                                isChart2Altered = true

                                distanceValue2= 0.0f
                                CalcMainDistanceIndex2 = 0
                            }
                        }

                        if(!alteringChart1 && !alteringChart2) {
                            Toast.makeText(context, "Valitse ainakin yksi kaava jota haluat muuntaa!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        fontSize = 15.sp,
                        text = "y²"
                    )
                }
            }

            Column() {
                //Näppäin jolla määritetään, halutaanko kaavaa 1 muokata vai ei
                Switch(
                    checked = alteringChart1,
                    onCheckedChange = {
                        alteringChart1 = it
                    }
                )

                //Näppäin jolla määritetään, halutaanko kaavaa 2 muokata vai ei
                Switch(
                    checked = alteringChart2,
                    onCheckedChange = {
                        alteringChart2 = it
                    }
                )

                //Näppäin x neliöjuuren laskemiseen
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if(alteringChart1) {
                            if(CalculatorMainLineChartList1.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareRootXIndex1 == CalculatorMainLineChartList1.size) {
                                Toast.makeText(context, "x arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareRootXIndex1 < CalculatorMainLineChartList1.size) {

                                    //Otetaan y:n arvo listasta index arvon kohdasta.
                                    val y = CalculatorMainLineChartList1[CalculatorMainSquareRootXIndex1].y

                                    //Otetaan x:n arvo listasta index arvon kohdasta ja lasketaan sen neliöjuuri.
                                    val x = floatSquareRoot(CalculatorMainLineChartList1[CalculatorMainSquareRootXIndex1].x)

                                    //Kun alkuperäinen y:n arvo, ja x:n neliöjuuri on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList1[CalculatorMainSquareRootXIndex1] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareRootXIndex1++
                                }

                                text1 = " "
                                text1 = formula1
                                isChart1Altered = true

                                distanceValue1 = 0.0f
                                CalcMainDistanceIndex1 = 0
                            }
                        }

                        if(alteringChart2) {
                            if(CalculatorMainLineChartList2.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareRootXIndex2 == CalculatorMainLineChartList2.size) {
                                Toast.makeText(context, "x arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareRootXIndex2 < CalculatorMainLineChartList2.size) {

                                    //Otetaan y:n arvo listasta index arvon kohdasta.
                                    val y = CalculatorMainLineChartList2[CalculatorMainSquareRootXIndex2].y

                                    //Otetaan x:n arvo listasta index arvon kohdasta ja lasketaan sen neliöjuuri.
                                    val x = floatSquareRoot(CalculatorMainLineChartList2[CalculatorMainSquareRootXIndex2].x)

                                    //Kun alkuperäinen y:n arvo, ja x:n neliöjuuri on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList2[CalculatorMainSquareRootXIndex2] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareRootXIndex2++
                                }

                                text2 = " "
                                text2 = formula2
                                isChart2Altered = true

                                distanceValue2 = 0.0f
                                CalcMainDistanceIndex2 = 0
                            }
                        }

                        if(!alteringChart1 && !alteringChart2) {
                            Toast.makeText(context, "Valitse ainakin yksi kaava jota haluat muuntaa!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        fontSize = 15.sp,
                        text = "²√x"
                    )
                }

                //Position 2.4
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if(alteringChart1) {
                            if(CalculatorMainLineChartList1.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareRootYIndex1 == CalculatorMainLineChartList1.size) {
                                Toast.makeText(context, "y arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                while(CalculatorMainSquareRootYIndex1 < CalculatorMainLineChartList1.size) {

                                    //Otetaan x:n arvo listasta index arvon kohdasta.
                                    val x = CalculatorMainLineChartList1[CalculatorMainSquareRootYIndex1].x

                                    //Otetaan y:n arvo listasta index arvon kohdasta ja lasketaan sen neliöjuuri.
                                    val y = floatSquareRoot(CalculatorMainLineChartList1[CalculatorMainSquareRootYIndex1].y)

                                    //Kun alkuperäinen x:n arvo, ja y:n neliöjuuri on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList1[CalculatorMainSquareRootYIndex1] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareRootYIndex1++
                                }

                                text1 = " "
                                text1 = formula1
                                isChart1Altered = true

                                distanceValue1 = 0.0f
                                CalcMainDistanceIndex1 = 0
                            }
                        }

                        if(alteringChart2) {
                            if(CalculatorMainLineChartList2.isEmpty()) {
                                Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                            } else if(CalculatorMainSquareRootYIndex2 == CalculatorMainLineChartList2.size) {
                                Toast.makeText(context, "y arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                            } else {
                                //Otetaan x:n arvo listasta index arvon kohdasta.
                                while(CalculatorMainSquareRootYIndex2 < CalculatorMainLineChartList2.size) {

                                    //Otetaan x:n arvo listasta index arvon kohdasta.
                                    val x = CalculatorMainLineChartList2[CalculatorMainSquareRootYIndex2].x

                                    //Otetaan y:n arvo listasta index arvon kohdasta ja lasketaan sen neliöjuuri.
                                    val y = floatSquareRoot(CalculatorMainLineChartList2[CalculatorMainSquareRootYIndex2].y)

                                    //Kun alkuperäinen x:n arvo, ja y:n neliöjuuri on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                    CalculatorMainLineChartList2[CalculatorMainSquareRootYIndex2] = Point(x,y)

                                    //Lisätään index arvoa seuraavaa laskua varten.
                                    CalculatorMainSquareRootYIndex2++
                                }

                                text2 = " "
                                text2 = formula2
                                isChart2Altered = true

                                distanceValue2 = 0.0f
                                CalcMainDistanceIndex2 = 0
                            }
                        }

                        if(!alteringChart1 && !alteringChart2) {
                            Toast.makeText(context, "Valitse ainakin yksi kaava jota haluat muuntaa!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        fontSize = 15.sp,
                        text = "²√y")
                }
            }

            //Column 3
            Column() {
                //Position 3.1
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {

                    }
                ) {
                    Text("")
                }

                //Position 3.2
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {

                    }
                ) {
                    Text("")
                }

                //Position 3.3
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {

                    }
                ) {
                    Text("")
                }

                //Position 3.4
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(70.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {

                    }
                ) {
                    Text("")
                }
            }

            Column() {
                //Näppäin kaavan piirtoon
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(200.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if(formula1 == text1 && formula2 == text2 && !isChart1Altered && !isChart2Altered){
                            Toast.makeText(context, "Syötetyt kaavat on jo piirretty! \nPäivitä kaavoja ja kokeile uudelleen.", Toast.LENGTH_SHORT).show()
                        }

                        if(text1.isEmpty() && text2.isEmpty()) {
                            Toast.makeText(context, "Syötä vähintään yksi kaava!", Toast.LENGTH_SHORT).show()
                        }

                        xValue1 = xStart
                        xValue2 = xStart

                        if(text1.isNotEmpty() && formula1 != text1 || text1.isNotEmpty() && formula1 == text1 && isChart1Altered) {
                            formula1 = text1
                            CoroutineScope(IO).launch {

                                //Jos listassa 1 on dataa, poistetaan se ensin
                                if (CalculatorMainLineChartList1.isNotEmpty()) {
                                    while(CalculatorMainLineChartList1.isNotEmpty()) {
                                        //Poistetaan listan viimeinen arvo, kunnes lista on tyhjä
                                        CalculatorMainLineChartList1.removeAt(CalculatorMainLineChartList1.size -1)
                                    }
                                }

                                while(xValue1 <= xEnd) {

                                    //Määritellään x ja y argumentit ja lasketaan y:n arvo kaavalla ja x:n arvolla
                                    x1 = Argument("x=$xValue1")
                                    y1 = Argument(formula1, x1)
                                    e1 = Expression("y", y1)

                                    //lisätään pisteet listaan 1.
                                    CalculatorMainLineChartList1.add(Point(xValue1, e1.calculate().toFloat(), ""))

                                    //Lisätään x:n arvoon xIncrement verran seuraavaa laskukertaa varten.
                                    xValue1 = xValue1 + xIncrement
                                }
                                text1 = ""
                                text1 = formula1
                                isChart1Altered = false
                            }
                        }

                        if(text2.isNotEmpty() && formula2 != text2 || text2.isNotEmpty() && formula2 == text2 && isChart2Altered) {
                            formula2 = text2
                            CoroutineScope(IO).launch {

                                //Jos listassa 1 on dataa, poistetaan se ensin
                                if (CalculatorMainLineChartList2.isNotEmpty()) {
                                    while(CalculatorMainLineChartList2.isNotEmpty()) {
                                        //Poistetaan listan viimeinen arvo, kunnes lista on tyhjä
                                        CalculatorMainLineChartList2.removeAt(CalculatorMainLineChartList2.size -1)
                                    }
                                }

                                while(xValue2 <= xEnd) {

                                    //Määritellään x ja y argumentit ja lasketaan y:n arvo kaavalla ja x:n arvolla
                                    x2 = Argument("x=$xValue2")
                                    y2 = Argument(formula2, x2)
                                    e2 = Expression("y", y2)

                                    //lisätään pisteet listaan 2.
                                    CalculatorMainLineChartList2.add(Point(xValue2, e2.calculate().toFloat(), ""))

                                    //Lisätään x:n arvoon xIncrement verran seuraavaa laskukertaa varten.
                                    xValue2 = xValue2 + xIncrement
                                }

                                text2 = ""
                                text2 = formula2
                                isChart2Altered = false
                            }
                        }
                    }
                ) {
                    Text("Piirrä kaava(t)")
                }

                //Näppäin käyrän pituuden laskemiseen
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(200.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if(CalculatorMainLineChartList1.isNotEmpty()) {
                            CoroutineScope(IO).launch {
                                while(CalcMainDistanceIndex1 <= CalculatorMainLineChartList1.size -2) {

                                    //Haetaan kahden pisteen x ja y arvot.
                                    var list1x1 = CalculatorMainLineChartList1[CalcMainDistanceIndex1].x
                                    var list1x2 = CalculatorMainLineChartList1[CalcMainDistanceIndex1+1].x
                                    var list1y1 = CalculatorMainLineChartList1[CalcMainDistanceIndex1].y
                                    var list1y2 = CalculatorMainLineChartList1[CalcMainDistanceIndex1+1].y

                                    //Asetetaan distanceValue1 muuttujan arvoksi, nykyinen arvo + kahden pisteen laskettu väli.
                                    distanceValue1 = distanceValue1 + distanceBetweenPoints(list1x1, list1y1, list1x2, list1y2)

                                    //lisätään index arvoa seuraavaa laskua varten
                                    CalcMainDistanceIndex1++
                                }

                                //Kun laskut on tehty, pyöristetään arvo yhden desimaalin tarkkuuteen.
                                distanceValue1.formatToSinglePrecision()
                                chart1DistanceCalculated = true
                            }
                        }

                        if(CalculatorMainLineChartList2.isNotEmpty()) {
                            CoroutineScope(IO).launch {
                                while(CalcMainDistanceIndex2 <= CalculatorMainLineChartList2.size -2) {

                                    //Haetaan kahden pisteen x ja y arvot.
                                    var list2x1 = CalculatorMainLineChartList2[CalcMainDistanceIndex2].x
                                    var list2x2 = CalculatorMainLineChartList2[CalcMainDistanceIndex2+1].x
                                    var list2y1 = CalculatorMainLineChartList2[CalcMainDistanceIndex2].y
                                    var list2y2 = CalculatorMainLineChartList2[CalcMainDistanceIndex2+1].y

                                    //Asetetaan distanceValue2 muuttujan arvoksi, nykyinen arvo + kahden pisteen laskettu väli.
                                    distanceValue2 = distanceValue2 + distanceBetweenPoints(list2x1, list2y1, list2x2, list2y2)

                                    //lisätään index arvoa seuraavaa laskua varten
                                    CalcMainDistanceIndex2++
                                }

                                distanceValue2.formatToSinglePrecision()
                                chart2DistanceCalculated = true
                            }
                        }

                        if(CalculatorMainLineChartList1.isEmpty() && CalculatorMainLineChartList2.isEmpty()) {
                            Toast.makeText(context, "Kaavoja ei ole piirretty! \nPiirrä kaavat ja laske uudelleen.", Toast.LENGTH_SHORT).show()
                        }

                        if(distanceValue1 != 0.0f && distanceValue2 != 0.0f){
                            Toast.makeText(context, "Pituudet on jo laskettu! \nSyötä uudet kaavat tai muunna dataa!.", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Laske pituus")
                }

                //Näppäin kaavojen tyhjentämiseen
                Button(
                    modifier = Modifier
                        .padding(5.dp, 1.dp, 5.dp, 1.dp)
                        .width(200.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Cyan,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        if (CalculatorMainLineChartList1.isEmpty() && CalculatorMainLineChartList2.isEmpty()) {
                            Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                        }

                        if (CalculatorMainLineChartList1.isNotEmpty()) {
                            while(CalculatorMainLineChartList1.isNotEmpty()) {
                                //Poistetaan listan arvo kohdasta listan koko -1, eli viimeinen arvo.
                                //Tätä toistetaan kunnes lista on tyhjä
                                CalculatorMainLineChartList1.removeAt(CalculatorMainLineChartList1.size -1)
                            }

                            text1 = " "
                            text1 = ""
                            formula1 = ""

                            CalculatorMainSquareXIndex1 = 0
                            CalculatorMainSquareYIndex1 = 0
                            CalculatorMainSquareRootXIndex1 = 0
                            CalculatorMainSquareRootYIndex1 = 0

                            chart1DistanceCalculated = false
                            isChart1Altered = false

                            distanceValue1 = 0.0f
                            CalcMainDistanceIndex1 = 0
                        }

                        if (CalculatorMainLineChartList2.isNotEmpty()) {
                            while(CalculatorMainLineChartList2.isNotEmpty()) {
                                //Poistetaan listan arvo kohdasta listan koko -1, eli viimeinen arvo.
                                //Tätä toistetaan kunnes lista on tyhjä
                                CalculatorMainLineChartList2.removeAt(CalculatorMainLineChartList2.size -1)
                            }

                            text2 = " "
                            text2 = ""
                            formula2 = ""

                            CalculatorMainSquareXIndex2 = 0
                            CalculatorMainSquareYIndex2 = 0
                            CalculatorMainSquareRootXIndex2 = 0
                            CalculatorMainSquareRootYIndex2 = 0

                            chart2DistanceCalculated = false
                            isChart2Altered = false

                            distanceValue2 = 0.0f
                            CalcMainDistanceIndex2 = 0
                        }
                    }
                ) {
                    Text("Tyhjennä kaavat")
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
            }
        }
    }
}

private fun floatSquared(num: Float): Float {
    var value = num*num
    return value.formatToSinglePrecision().toFloat()
}

private fun floatSquareRoot(num: Float): Float {
    var value = 0f

    if(num<0) {
        var absNum = abs(num)
        var eAbsSquareRoot = Expression("√$absNum")
        value = eAbsSquareRoot.calculate().toFloat()//.formatToSinglePrecision().toFloat()
    } else if(num>=0) {
        var eSquareRoot = Expression("√$num")
        value = eSquareRoot.calculate().toFloat()//.formatToSinglePrecision().toFloat()
    }

    return value.toFloat()
}

//Funktio kahden pisteen välisen etäisyyden laskemiseen.
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