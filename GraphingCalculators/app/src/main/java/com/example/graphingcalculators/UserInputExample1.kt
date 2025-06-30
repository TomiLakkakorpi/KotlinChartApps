package com.example.graphingcalculators

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

var lineChartListIndex = 0F
var lineChartList = mutableListOf<Point>()

@Composable
fun UserInputExample1Screen(navController: NavController) {
    //Haetaan konteksti, jotta Toast ilmoituksia voidaan käyttää
    val context = LocalContext.current

    //Luodaan text -muuttuja, ja käytetään "by remember", jotta muuttujan arvo säilyy kun käyttöliittymä päivittyy
    var text by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center
        ) {
            val steps = lineChartList.size

            val xAxisData = AxisData.Builder()
                .axisStepSize(35.dp)
                .topPadding(105.dp)
                .steps(lineChartList.size - 1)
                .labelData { i -> lineChartList[i].x.toInt().toString() }
                .labelAndAxisLinePadding(15.dp)
                .build()

            val yAxisData = AxisData.Builder()
                .steps(steps)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yMin = lineChartList.minOf { it.y }
                    val yMax = lineChartList.maxOf { it.y }
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }.build()

            val data = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = lineChartList,
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

            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(300.dp)
            ) {
                //Tarkistetaan onko lista tyhjä
                //Jos lista ei ole tyhjä --> piirretään kaavio
                if(lineChartList.isNotEmpty()){
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        lineChartData = data
                    )
                }
            }

            TextField(
                //Asetetaan tekstikentän arvoksi muuttujan text arvo
                value = text,

                //Kun tekstikentän arvo muuttuu, asetetaan tekstikentän teksti "text" muuttujaan.
                onValueChange = { newText ->
                    text = newText
                },
                label = {
                    Text(text = "Syötä arvo")
                },
            )

            Button(
                onClick = {
                    //Tarkistetaan onko text arvo tyhjä
                    if(text.isNotEmpty()){
                        //Tarkistetaan onko text arvo hyväksyttävissä (sisältääkö se numeroita, jotta siitä saadaan float arvo)
                        if(checkIfValidValue(text)) {
                            //Jos text arvo sisältää joitain numeroita, lisätään käyttäjän syöttämät arvot datalistaan.
                            lineChartList.add(Point(lineChartListIndex, text.toFloat(), ""))

                            //Palautetaan text arvo tyhjäksi
                            text = ""

                            lineChartListIndex++
                        } else {
                            //Jos teksti ei ole hyväksyttävissä oleva arvo, ilmoitetaan siitä käyttäjälle
                            Toast.makeText(context, "Syöttämäsi arvoa ei voida hyväksyä! Syötä arvo muodossa 1.1", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        //Jos tekstikenttä on tyhjä, ilmoitetaan siitä käyttäjälle.
                        Toast.makeText(context, "Syötä arvo!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Lisää arvo kaavioon")
            }

            Button(
                onClick = {
                    //Tarkistetaan onko datalista tyhjä.
                    if (lineChartList.isNotEmpty()) {
                        //Jos datalista sisältää dataa, poistetaan datalistasta arvoja yksikerrallaan kunnes lista on tyhjä
                        while(lineChartList.isNotEmpty()) {
                            lineChartList.removeAt(lineChartList.size -1)
                        }

                        //Palautetaan index muuttujan arvoksi 0.
                        lineChartListIndex = 0f

                        //Muutetaan tekstikentän arvoa jotta käyttöliittymä saadaan päivittymään heti.
                        text = " "
                        text = ""
                    } else {
                        //Jos datalista oli jo tyhjä, ilmoitetaan siitä käyttäjälle.
                        Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Tyhjennä taulukko")
            }

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

//Funktio jolla tarkistetaan onko syötetty teksti hyväksyttävä
fun checkIfValidValue(input: String): Boolean {
    val regex = Regex("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)")
    return input.matches(regex)
}