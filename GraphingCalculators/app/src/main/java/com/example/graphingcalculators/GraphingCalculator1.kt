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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

/** Esimerkki yhden kaavan piirtoon
 *
 * Tässä esimerkissä käyttäjä syöttää tekstikenttään kaavan.
 * Syötetyn kaavan avulla lasketaan y:n arvot välille -5x - 5x
 *
 * Kun y:n arvot on laskettu, kaava piirretään käyttäen YCharts WaveChart kuvaajaa.
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa esimerkkiin WaveChartAppV1.
 */

//Luodaan dynaaminen lista
var calculator1lineChartList = mutableListOf<Point>()

@Composable
fun GraphingCalculatorScreen1(navController: NavController) {

    //Luodaan text -muuttuja
    //Text arvoon lisätään käyttäjän syöttämä kaava tekstikentästä.
    var text by remember { mutableStateOf("") }

    //Luodaan formula -muuttuja
    //Tähän arvoon lisätään kaava, kun käyttäjä painaa "piirrä kaavio".
    //Tätä kaavaa käytetään kaavion pisteiden laskemiseen.
    var formula by remember {mutableStateOf("")}

    //Alustetaan argumentit ja lauseke kaavion pisteiden laskemiseen.
    var e: Expression
    var x: Argument
    var y: Argument

    //Luodaan xValue ja yValue muuttujat, joita käytetään kun pisteitä lisätään listaan.
    var xValue = -5f
    var yValue = 0f

    //Luodaan muuttuja, jolla tarkistetaan onko kaavaa jo piirretty
    var chart1Drawn by remember {mutableStateOf(false)}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column()
        {
            val context = LocalContext.current
            var steps: Int

            if (calculator1lineChartList.size >= 10) {
                steps = 10
            } else {
                steps = calculator1lineChartList.size
            }

            val xAxisData = AxisData.Builder()
                .axisStepSize(30.dp)
                .startDrawPadding(48.dp)
                .steps(calculator1lineChartList.size - 1)
                .labelAndAxisLinePadding(30.dp)
                .labelData { i ->
                    val xMin = calculator1lineChartList.minOf { it.x }
                    val xMax = calculator1lineChartList.maxOf { it.x }
                    val xScale = (xMax - xMin) / steps
                    ((i * xScale) + xMin).formatToSinglePrecision()
                }.build()

            val yAxisData = AxisData.Builder()
                .steps(steps)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yMin = calculator1lineChartList.minOf { it.y }
                    val yMax = calculator1lineChartList.maxOf { it.y }
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }.build()

            val data = WaveChartData(
                wavePlotData = WavePlotData(
                    lines = listOf(
                        Wave(
                            dataPoints = calculator1lineChartList,
                            waveStyle = LineStyle(color = Color.Black),
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

            //Näytetään ruudun yläreunassa käyttäjälle piirretty kaava
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Piirretty Kaava: $formula"
            )

            Box(
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp)
            ) {

                //Varmistetaan, ettei lista ole tyhjä.
                //Jos tyhjää listaa yritettäisiin piirtää, ohjelma kaatuu
                if(calculator1lineChartList.isNotEmpty()){

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
                    modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                    onClick = {
                        //Asetetaan käyttäjän syöttämä kaava text muuttujasta formula muuttujaan.
                        formula = text

                        //Varmistetaan että formula -muuttuja ei ole tyhjä.
                        if(formula.isNotEmpty()){

                            //Varmistetaan ettei kaavaa ole jo piirretty
                            if(!chart1Drawn) {

                                //Tässä esimerkissä kaavan piirtoalue on kovakoodattu välille -5x - 5x.
                                //Y Arvojen laskeminen aloitetaan siis -5x arvosta.
                                //Y:n arvoja lasketaan syötetyllä kaavalla niin kauan, kunnes x arvo on 5 jolloin arvo välillä -5x - 5x on laskettu.
                                while(xValue <= 5) {

                                    //Asetetaan x:n arvoksi xValue muuttujan arvo
                                    x = Argument("x=$xValue")

                                    //Y:n arvo lasketaan käyttäjän syöttämällä kaavalla, ja x muuttujan arvolla
                                    y = Argument(text, x)
                                    e = Expression("y", y)

                                    //Lisätään laskettu y:n arvo yValue muuttujaan.
                                    yValue = e.calculate().toFloat()

                                    //Lisätään listaan piste xValue ja yValue muuttujien arvoilla.
                                    calculator1lineChartList.add(Point(xValue, yValue, ""))

                                    //Kun laskut on tehty, lisätään x arvoon 0.1
                                    xValue = xValue + 0.1f
                                }

                                chart1Drawn = true
                            } else {
                                Toast.makeText(context, "Kaava on jo piirretty!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            //Jos käyttäjä ei ollut syöttänyt kaavaa, ilmoitetaan siitä käyttäjälle.
                            Toast.makeText(context, "Syötä kaava!", Toast.LENGTH_SHORT).show()
                        }

                        //Muutetaan text muuttujan arvoa, jotta käyttöliittymä saadaan päivitettyä ja piirretty kaava näkyviin.
                        text = " "
                        text = ""
                    }
                ) {
                    Text("Piirrä kaava")
                }
            }

            //Näppäin listan tyhjentämiseen.
            Button(
                onClick = {
                    //Varmistetaan ettei lista ole tyhjä. Jos lista ei ole tyhjä, edetään while -kohtaan.
                    if (calculator1lineChartList.isNotEmpty()) {

                        //Poistetaan listasta viimeinen piste, niin kauan kunnes lista on tyhjä
                        while(calculator1lineChartList.isNotEmpty()) {
                            calculator1lineChartList.removeAt(calculator1lineChartList.size -1)
                        }

                        //Palautetaan arvoja takaisin seuraavaa kaavaa varten
                        text = " "
                        text = ""
                        formula = ""
                        chart1Drawn = false
                    } else {
                        //Jos lista oli jo tyhjä, ilmoitetaan siitä käyttäjälle
                        Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Tyhjennä taulukko")
            }

            Box(
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                //contentAlignment = Alignment.Center
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