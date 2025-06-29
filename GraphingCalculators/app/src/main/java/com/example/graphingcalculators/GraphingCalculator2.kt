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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

//YCharts Importit
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

//MathParser Importit
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/** Esimerkki yhden kaavan piirtoon, mukautettavalla piirtoalueella ja lisäysarvolla
 *
 * Tässä esimerkissä käyttäjä syöttää tekstikenttään kaavan.
 * Tässä esimerkissä käyttäjä voi myös valita piirtoalueen sekä x lisäysarvon. (kuinka tiheästi x:n arvo lasketaan)
 *
 * Kun y:n arvot on laskettu, kaava piirretään käyttäen YCharts WaveChart kuvaajaa.
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa esimerkkiin WaveChartAppV1.
 */

//Alustetaan dynaaminen lista datapisteille
var Calculator2lineChartList = mutableListOf<Point>()

@Composable
fun GraphingCalculatorScreen2(navController: NavController) {

    //Alustetaan text- muuttuja, johon lisätään käyttäjän syöttämät teksti tekstikentästä
    var text by remember { mutableStateOf("") }

    //Muuttuja johon lisätään kaava kun "piirrä kaava" näppäintä painetaan
    var formula by remember {mutableStateOf("")}

    //Muuttuja jolla tarkistetaan onko kaava piirretty
    var chart1Drawn by remember {mutableStateOf(false)}

    //Laskutoimituksiin käytetyt muuttujat
    var e: Expression
    var x: Argument
    var y: Argument

    //x ja y -muuttujat, joita käytetään kun pisteitä lisätään listaan.
    var xValue by remember {mutableFloatStateOf(0.5f)}
    var yValue = 0f

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

            if (Calculator2lineChartList.size >= 10) {
                steps = 10
            } else {
                steps = Calculator2lineChartList.size
            }

            val xAxisData = AxisData.Builder()
                .axisStepSize(30.dp)
                .startDrawPadding(48.dp)
                .steps(Calculator2lineChartList.size - 1)
                .labelAndAxisLinePadding(25.dp)
                .labelData { i ->
                    val xMin = Calculator2lineChartList.minOf { it.x }
                    val xMax = Calculator2lineChartList.maxOf { it.x }
                    val xScale = (xMax - xMin) / steps
                    ((i * xScale) + xMin).formatToSinglePrecision()
                }.build()

            val yAxisData = AxisData.Builder()
                .steps(steps)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yMin = Calculator2lineChartList.minOf { it.y }
                    val yMax = Calculator2lineChartList.maxOf { it.y }
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }.build()

            val data = WaveChartData(
                wavePlotData = WavePlotData(
                    lines = listOf(
                        Wave(
                            dataPoints = Calculator2lineChartList,
                            waveStyle = LineStyle(color = Color.Black),
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

            Row() {
                Text(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(10.dp, 20.dp, 0.dp, 0.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Graafinen laskin 2: Yhden kaavan syöttö, säädettävä piirtoalue"
                )
            }

            //Näytetään ruudun yläreunassa käyttäjälle piirretty kaava
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Piirretty Kaava: $formula"
            )

            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(300.dp)
            ) {
                //Varmistetaan, ettei lista ole tyhjä.
                //Jos tyhjää listaa yritettäisiin piirtää, ohjelma kaatuu
                if(Calculator2lineChartList.isNotEmpty()){

                    //Kutsutaan funktiota, joka piirtää kaavion.
                    WaveChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        waveChartData = data
                    )
                }
            }

            //Tekstikenttä kaavan syöttämiseen.
            //Käyttäjän syöttämä kaava asetetaan text muuttujaan
            TextField(
                modifier = Modifier.width(150.dp),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                label = {
                    Text(text = "Kirjoita kaava")
                },
            )

            Row() {
                //Näppäin kaavan laskemiseen.
                Button(
                    modifier = Modifier
                        .width(160.dp)
                        .padding(0.dp, 0.dp, 20.dp, 0.dp),
                    onClick = {
                        //Asetetaan käyttäjän syöttämä kaava text muuttujasta formula muuttujaan.
                        formula = text

                        //Varmistetaan että formula -muuttuja ei ole tyhjä.
                        if(formula.isNotEmpty()){

                            //Varmistetaan ettei kaavaa ole jo piirretty
                            if(!chart1Drawn) {

                                //Lasketaan y:n arvoja välillä xStart - xEnd. y:n arvoja lasketaan niin kauan kunnes xValue1 on yhtäsuuri kuin xEnd.
                                while(xValue <= xEnd) {

                                    //Asetetaan x:n arvoksi xValue muuttujan arvo
                                    x = Argument("x=$xValue")

                                    //Y:n arvo lasketaan käyttäjän syöttämällä kaavalla, ja x muuttujan arvolla
                                    y = Argument(formula, x)
                                    e = Expression("y", y)

                                    //Lisätään laskettu y:n arvo yValue1 muuttujaan.
                                    yValue = e.calculate().toFloat()

                                    //Lisätään listaan piste xValue ja yValue muuttujien arvoilla.
                                    Calculator2lineChartList.add(Point(xValue, yValue, ""))

                                    //Kun laskut on tehty, lisätään x:n arvoon xIncrement -arvo
                                    xValue = xValue + xIncrement
                                }
                                chart1Drawn = true
                            } else {
                                Toast.makeText(context, "Kaava on jo piirretty", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Syötä kaava!", Toast.LENGTH_SHORT).show()
                        }

                        //Muutetaan text muuttujan arvoa, jotta käyttöliittymä saadaan päivitettyä ja piirretty kaava näkyviin.
                        text = " "
                        text = ""
                    }
                ) {
                    Text("Piirrä kaavio")
                }

                //Näppäin listan tyhjentämiseen.
                Button(
                    onClick = {
                        //Varmistetaan ettei lista ole tyhjä. Jos lista ei ole tyhjä, edetään while -kohtaan.
                        if (Calculator2lineChartList.isNotEmpty()) {
                            //Poistetaan listasta viimeinen piste, niin kauan kunnes lista on tyhjä
                            while(Calculator2lineChartList.isNotEmpty()) {
                                Calculator2lineChartList.removeAt(Calculator2lineChartList.size -1)
                            }

                            //Palautetaan arvoja takaisin seuraavaa kaavaa varten
                            xValue = xStart
                            chart1Drawn = false
                            text = " "
                            text = ""
                            formula = ""
                        } else {
                            Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Tyhjennä taulukko")
                }
            }

            Row() {
                Column() {
                    //Tekstikenttä x:n lähtöarvon muokkaamiseen
                    TextField(
                        modifier = Modifier
                            .padding(5.dp)
                            .width(150.dp),
                        value = xStart.toString(),
                        onValueChange = { newText ->
                            xStart = newText.toFloat()
                        },
                        label = {
                            Text(text = "x Lähtöarvo")
                        },
                    )

                    //Tekstikenttä x:n lisäysarvon muokkaamiseen
                    TextField(
                        modifier = Modifier
                            .padding(5.dp)
                            .width(150.dp),
                        value = xIncrement.toString(),
                        onValueChange = { newText ->
                            xIncrement = newText.toFloat()
                        },
                        label = {
                            Text(text = "x lisäysarvo")
                        },
                    )
                }
                Column() {
                    //Tekstikenttä x:n loppuarvon muokkaamiseen
                    TextField(
                        modifier = Modifier
                            .padding(5.dp)
                            .width(150.dp),
                        value = xEnd.toString(),
                        onValueChange = { newText ->
                            xEnd = newText.toFloat()
                        },
                        label = {
                            Text(text = "x Loppuarvo")
                        },
                    )

                    //Näppäin arvojen asettamiseen.
                    Button(
                        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
                        modifier = Modifier.padding(5.dp),
                        onClick = {
                            xValue = xStart
                            Toast.makeText(context, "X arvot asetettu!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("Aseta arvot")
                    }
                }
            }

            Box(
                modifier = Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp),
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