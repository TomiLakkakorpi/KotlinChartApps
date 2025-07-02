package com.example.graphingcalculators

import android.widget.Toast
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.abs

//Ycharts importit
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.wavechart.WaveChart
import co.yml.charts.ui.wavechart.model.Wave
import co.yml.charts.ui.wavechart.model.WaveChartData
import co.yml.charts.ui.wavechart.model.WavePlotData

//MathParser importit
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/** Esimerkki datan muunnoksiin
 *
 * Tässä esimerkissä piirretään kaava aiempien esimerkkien tapaan.
 * Uutena lisäyksenä, tässä esimerkissä dataa voidaan muokata piirron jälkeen.
 * Kun dataa muunnetaan, kaava piirretään uudelleen muunnetuilla arvoilla
 *
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa aiempiin esimerkkeihin, joissa kuvaajien piirtoa käydään läpi.
 */

//Index muuttujat potenssilaskuille
var Calculator5squareXIndex = 0
var Calculator5squareYIndex = 0

//Index muuttujat neliöjuurilaskuille
var Calculator5squareRootXIndex = 0
var Calculator5squareRootYIndex = 0

//Alustetaan dynaaminen lista datapisteille
var Calculator5lineChartList = mutableListOf<Point>()

@Composable
fun GraphingCalculatorScreen5(navController: NavController) {

    //Alustetaan text -muuttuja, johon lisätään käyttäjän syöttämä teksti tekstikentästä.
    var text by remember { mutableStateOf("") }

    //Muuttuja, johon lisätään kaava, kun "piirrä kaava" näppäintä painetaan
    var formula by remember {mutableStateOf("")}

    //Laskutoimituksiin käytetyt muuttujat
    var e: Expression
    var x: Argument
    var y: Argument

    //x muuttuja, jota käytetään kun pisteitä lisätään listaan.
    var xValue by remember {mutableFloatStateOf(-5f)}

    //Alustetaan muuttujat xStart, xEnd ja xIncrement
    //Näillä muuttujilla käyttäjä voi säätää kaavan piirtoaluetta ja piirtotiheyttä.
    var xStart by remember { mutableFloatStateOf(-5.0f) }
    var xEnd by remember { mutableFloatStateOf(5.0f) }
    var xIncrement by remember { mutableFloatStateOf(0.1f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column()
        {
            val context = LocalContext.current
            var steps: Int

            if (Calculator5lineChartList.size >= 10) {
                steps = 10
            } else {
                steps = Calculator5lineChartList.size
            }

            val xAxisData = AxisData.Builder()
                .axisStepSize(30.dp)
                .startDrawPadding(48.dp)
                .steps(Calculator5lineChartList.size - 1)
                .labelAndAxisLinePadding(25.dp)
                .labelData { i ->
                    val xMin = Calculator5lineChartList.minOf { it.x }
                    val xMax = Calculator5lineChartList.maxOf { it.x }
                    val xScale = (xMax - xMin) / steps
                    ((i * xScale) + xMin).formatToSinglePrecision()
                }.build()

            val yAxisData = AxisData.Builder()
                .steps(steps)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yMin = Calculator5lineChartList.minOf { it.y }
                    val yMax = Calculator5lineChartList.maxOf { it.y }
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }.build()

            val data = WaveChartData(
                wavePlotData = WavePlotData(
                    lines = listOf(
                        Wave(
                            dataPoints = Calculator5lineChartList,
                            waveStyle = LineStyle(color = Color.Black),
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            shadowUnderLine = ShadowUnderLine(),
                            selectionHighlightPopUp = SelectionHighlightPopUp()
                            //waveFillColor = WaveFillColor(topColor = Color.Green, bottomColor = Color.Red),
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
                text = "Graafinen laskin 5: \nDatan muunnos"
            )

            Text(
                modifier = Modifier.padding(10.dp, 5.dp, 0.dp, 0.dp),
                text = "Piirretty Kaava: $formula"
            )

            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(300.dp)
            ) {
                if(Calculator5lineChartList.isNotEmpty()){
                    WaveChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        waveChartData = data
                    )
                }
            }

            TextField(
                modifier = Modifier.width(200.dp),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                label = {
                    Text(text = "Kirjoita kaava")
                },
            )

            Row() {
                Button(
                    modifier = Modifier.padding(0.dp, 10.dp, 20.dp, 0.dp),
                    onClick = {
                        formula = text
                        if(formula.isNotEmpty()){
                            while(xValue <= xEnd) {

                                //Määritellään x ja y argumentit.
                                x = Argument("x=$xValue")
                                y = Argument(formula, x)
                                e = Expression("y", y)

                                //Lisätään pisteet listaan.
                                Calculator5lineChartList.add(Point(xValue, e.calculate().toFloat(), ""))

                                //Lisätään x:n arvoon xIncrement verran seuraavaa laskukertaa varten.
                                xValue = floatAddition(xValue, xIncrement)
                            }
                        } else {
                            Toast.makeText(context, "Syötä kaava!", Toast.LENGTH_SHORT).show()
                        }

                        xValue = xStart

                        text = " "
                        text = ""
                    }
                ) {
                    Text("Piirrä kaava")
                }

                Button(
                    modifier = Modifier.padding(0.dp, 10.dp, 20.dp, 0.dp),
                    onClick = {
                        if (Calculator5lineChartList.isNotEmpty()) {
                            while(Calculator5lineChartList.isNotEmpty()) {
                                //Poistetaan listan arvo kohdasta listan koko -1, eli viimeinen arvo.
                                //Tätä toistetaan kunnes lista on tyhjä
                                Calculator5lineChartList.removeAt(Calculator5lineChartList.size -1)
                            }

                            text = " "
                            text = ""
                            formula = ""

                            Calculator5squareXIndex = 0
                            Calculator5squareYIndex = 0
                            Calculator5squareRootXIndex = 0
                            Calculator5squareRootYIndex = 0
                        } else {
                            Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Tyhjennä taulukko")
                }
            }

            Row() {
                //Näppäin x² korottamiseen
                Button(
                    modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    onClick = {
                        if(Calculator5lineChartList.isEmpty()) {
                            Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                        } else if(Calculator5squareXIndex == Calculator5lineChartList.size) {
                            Toast.makeText(context, "x arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                        } else {
                            while(Calculator5squareXIndex < Calculator5lineChartList.size) {

                                //Otetaan y:n arvo listasta index arvon kohdasta.
                                val y = Calculator5lineChartList[Calculator5squareXIndex].y

                                //Otetaan x:n arvo listasta index arvon kohdasta ja korotetaan se toiseen potenssiin.
                                val x = floatSquared(Calculator5lineChartList[Calculator5squareXIndex].x)

                                //Kun alkuperäinen y:n arvo, ja korotettu x:n arvo on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                Calculator5lineChartList[Calculator5squareXIndex] = Point(x,y)

                                //Lisätään index arvoa seuraavaa laskua varten.
                                Calculator5squareXIndex++
                            }

                            text = " "
                            text = ""
                            Calculator5squareRootXIndex = 0
                        }
                    }
                ) {
                    Text("x²")
                }

                //Näppäin y² korottamiseen
                Button(
                    modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    onClick = {
                        if(Calculator5lineChartList.isEmpty()) {
                            Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                        } else if(Calculator5squareYIndex == Calculator5lineChartList.size){
                            Toast.makeText(context, "y arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                        } else {
                            while(Calculator5squareYIndex < Calculator5lineChartList.size) {

                                //Otetaan x:n arvo listasta index arvon kohdasta.
                                val x = Calculator5lineChartList[Calculator5squareYIndex].x

                                //Otetaan y:n arvo listasta index arvon kohdasta ja korotetaan se toiseen potenssiin.
                                val y = floatSquared(Calculator5lineChartList[Calculator5squareYIndex].y)

                                //Kun alkuperäinen x:n arvo, ja korotettu y:n arvo on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                Calculator5lineChartList[Calculator5squareYIndex] = Point(x,y)

                                //Lisätään index arvoa seuraavaa laskua varten.
                                Calculator5squareYIndex++
                            }
                            text = " "
                            text = ""
                            Calculator5squareRootYIndex = 0
                        }
                    }
                ) {
                    Text("y²")
                }

                //Näppäin x neliöjuuren laskemiseen
                Button(
                    modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    onClick = {
                        if(Calculator5lineChartList.isEmpty()) {
                            Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                        } else if(Calculator5squareRootXIndex == Calculator5lineChartList.size) {
                            Toast.makeText(context, "x arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                        } else {
                            while(Calculator5squareRootXIndex < Calculator5lineChartList.size) {

                                //Otetaan y:n arvo listasta index arvon kohdasta.
                                val y = Calculator5lineChartList[Calculator5squareRootXIndex].y

                                //Otetaan x:n arvo listasta index arvon kohdasta ja lasketaan sen neliöjuuri.
                                val x = floatSquareRoot(Calculator5lineChartList[Calculator5squareRootXIndex].x)

                                //Kun alkuperäinen y:n arvo, ja x:n neliöjuuri on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                Calculator5lineChartList[Calculator5squareRootXIndex] = Point(x,y)

                                //Lisätään index arvoa seuraavaa laskua varten.
                                Calculator5squareRootXIndex++
                            }

                            text = " "
                            text = ""
                            Calculator5squareXIndex = 0
                        }
                    }
                ) {
                    Text("²√x")
                }

                //Näppäin y neliöjuuren laskemiseen
                Button(
                    modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    onClick = {
                        if(Calculator5lineChartList.isEmpty()) {
                            Toast.makeText(context, "Lista on tyhjä, syötä ensin kaava!", Toast.LENGTH_SHORT).show()
                        } else if(Calculator5squareRootYIndex == Calculator5lineChartList.size) {
                            Toast.makeText(context, "y arvot on jo kerran muunnettu", Toast.LENGTH_SHORT).show()
                        } else {
                            while(Calculator5squareRootYIndex < Calculator5lineChartList.size) {

                                //Otetaan x:n arvo listasta index arvon kohdasta.
                                val x = Calculator5lineChartList[Calculator5squareRootYIndex].x

                                //Otetaan y:n arvo listasta index arvon kohdasta ja lasketaan sen neliöjuuri.
                                val y = floatSquareRoot(Calculator5lineChartList[Calculator5squareRootYIndex].y)

                                //Kun alkuperäinen x:n arvo, ja y:n neliöjuuri on tiedossa, asetetaan arvot samaan kohtaan takaisin.
                                Calculator5lineChartList[Calculator5squareRootYIndex] = Point(x,y)

                                //Lisätään index arvoa seuraavaa laskua varten.
                                Calculator5squareRootYIndex++
                            }

                            text = " "
                            text = ""
                            Calculator5squareYIndex = 0
                        }
                    }
                ) {
                    Text("²√y")
                }
            }

            Box(
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
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
    }
}

//Funktio kahden float arvon lisäämiseen
//(Esimerkissä tuli vastaan erikoisia ongelmia, jossa suora num1 + num2 aiheutti ongelmia, siksi erillinen funktio)
private fun floatAddition(numA: Float, numB: Float): Float {
    var value = numA + numB
    return value.formatToSinglePrecision().toFloat()
}

//Funkio potenssiin kaksi korottamiseen
private fun floatSquared(num: Float): Float {
    var value = num*num
    return value.formatToSinglePrecision().toFloat()
}

//Funktio neliöjuuren laskemiseen
private fun floatSquareRoot(num: Float): Float {
    var value = 0f

    if(num<0) {
        var absNum = abs(num)
        var eAbsSquareRoot = Expression("√$absNum")
        value = eAbsSquareRoot.calculate().toFloat()
    } else if(num>=0) {
        var eSquareRoot = Expression("√$num")
        value = eSquareRoot.calculate().toFloat()
    }

    return value.toFloat()
}