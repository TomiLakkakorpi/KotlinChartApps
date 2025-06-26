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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp

//MathParser importit
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/** Esimerkki ympyrän piirtämiseen
 *
 * Tässä esimerkissä hyödynnetään kaavaa x²+y²=r², jolla lasketaan ympyrän keskipiste ja säteen pituus.
 * Tämän jälkeen käytetään kaavoja x = h + r * cos(t) ja y = k + r * sin(t), joilla lasketaan x ja y arvot välille 0-2 radiaania.
 *
 * Kun x ja y arvot on laskettu, ympyrä piirretään käyttäen yCharts LineChart -kuvaajaa
 * Esimerkissä kaavion piirtoon liittyviä asioita ei ole kommentoitu.
 * Jos haluat opiskella miten kaavioita toteutetaan, palaa aiempiin esimerkkeihin, joissa kuvaajien piirtoa käydään läpi.
 */

var Calculator4lineChartList = mutableListOf<Point>()
var Calculator4lineChartListCenter = mutableListOf<Point>()

@Composable
fun GraphingCalculatorScreen4(navController: NavController) {

    //Alustetaan text -muuttujat, joihin lisätään käyttäjän syöttämät tekstit tekstikentistä
    var hText by remember { mutableStateOf("") }
    var kText by remember { mutableStateOf("") }
    var rText by remember { mutableStateOf("") }

    //Laskutoimituksiin käytetyt muuttujat
    var e1: Expression
    var e2: Expression

    //x ja y -muuttujat, joita käytetään kun pisteitä lisätään listaan.
    var xValue by remember {mutableFloatStateOf(0.0f)}
    var yValue by remember {mutableFloatStateOf(0.0f)}

    //Muuttujat h(leveys), k(korkeus), r(säde) ja t(rad) arvoille
    var h by remember { mutableFloatStateOf(0.0f) }
    var k by remember { mutableFloatStateOf(0.0f) }
    var r by remember { mutableFloatStateOf(0.0f) }
    var t by remember { mutableFloatStateOf(0.0f) }

    //Muuttuja laskutiheydelle
    var tIncrement by remember {mutableFloatStateOf(0.0f)}

    //text muuttujat, joihin lisätään keskipiste ja säde kun ympyrä piirretään.
    var centerPoint by remember {mutableStateOf("")}
    var radius by remember {mutableStateOf("")}

    //Text muuttujat, joita käytetään arvojen näyttämiseen kaavassa.
    var hDisplayText = ""
    var kDisplayText = ""
    var rDisplayText = ""

    //Käyttöliittymän päivitykseen käytetty ylimääräinen muuttuja
    var uiUpdate by remember {mutableStateOf("")}

    hDisplayText = if(floatCheck(hText)) {
        if(hText.toFloat() > 0.0f) {
            "+$hText"
        } else if (hText.toFloat() < 0.0f){
            hText
        } else {
            "-h"
        }
    } else {
        "-h"
    }

    kDisplayText = if(floatCheck(kText)) {
        if(kText.toFloat() > 0.0f) {
            "+$kText"
        } else if (kText.toFloat() < 0.0f){
            kText
        } else {
            "-h"
        }
    } else {
        "-h"
    }

    rDisplayText = if(floatCheck(rText)) {
        if(positiveNumberCheck(rText)) {
            rText
        } else {
            "r²"
        }
    } else {
        "r²"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column()
        {
            val context = LocalContext.current
            var steps: Int

            if (Calculator4lineChartList.size >= 10) {
                steps = 10
            } else {
                steps = Calculator4lineChartList.size
            }

            val xAxisData = AxisData.Builder()
                .axisStepSize(30.dp)
                .startDrawPadding(5.dp)
                .steps(Calculator4lineChartList.size - 1)
                .labelAndAxisLinePadding(25.dp)
                .labelData { i ->
                    val xMin = Calculator4lineChartList.minOf { it.x } //* 1.25f
                    val xMax = Calculator4lineChartList.maxOf { it.x } //* 1.25f
                    val xScale = (xMax - xMin) / steps
                    ((i * xScale) + xMin).formatToSinglePrecision()
                }.build()

            val yAxisData = AxisData.Builder()
                .steps(steps)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val yMin = Calculator4lineChartList.minOf { it.y } //* 1.25f
                    val yMax = Calculator4lineChartList.maxOf { it.y } //* 1.25f
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }.build()

            val data = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = Calculator4lineChartList,
                            lineStyle = LineStyle(color = Color.Black),
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            //shadowUnderLine = ShadowUnderLine(),
                            selectionHighlightPopUp = SelectionHighlightPopUp()
                            //waveFillColor = WaveFillColor(topColor = Color.Green, bottomColor = Color.Red),
                        ),
                        Line(
                            dataPoints = Calculator4lineChartListCenter,
                            lineStyle = LineStyle(color = Color.Black),
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            //shadowUnderLine = ShadowUnderLine(),
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
                modifier = Modifier.padding(20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                text = "Graafinen laskin 4: Ympyrän piirto"
            )

            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(300.dp)
            ) {
                if(Calculator4lineChartList.isNotEmpty() && Calculator4lineChartListCenter.isNotEmpty()){
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        lineChartData = data
                    )
                }
            }

            Row() {
                Column() {
                    Text(
                        modifier = Modifier.width(170.dp),
                        text = "Kaava:",
                        fontSize = 20.sp
                    )

                    Text(
                        modifier = Modifier.width(170.dp),
                        text = "Keskipiste (h,k):",
                        fontSize = 20.sp
                    )

                    Text(
                        modifier = Modifier.width(170.dp),
                        text = "Säde r:",
                        fontSize = 20.sp
                    )
                }
                Column() {
                    Text(
                        text = "(x$hDisplayText)² + (y$kDisplayText)² = $rDisplayText",
                        fontSize = 20.sp
                    )

                    Text(
                        text = centerPoint,
                        fontSize = 20.sp
                    )

                    Text(
                        text = radius,
                        fontSize = 20.sp
                    )
                }
            }

            Row(
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
            ) {
                TextField(
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(100.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = hText,
                    onValueChange = { newText ->
                        hText = newText
                    },
                    label = {
                        Text(text = "h arvo")
                    },
                )
                TextField(
                    textStyle = TextStyle(fontSize = 15.sp),
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(100.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = kText,
                    onValueChange = { newText ->
                        kText = newText
                    },
                    label = {
                        Text(text = "k arvo")
                    },
                )
                TextField(
                    textStyle = TextStyle(fontSize = 15.sp),
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(100.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = rText,
                    onValueChange = { newText ->
                        rText = newText
                    },
                    label = {
                        Text(text = "r² arvo")
                    },
                )
            }

            Row() {
                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                    onClick = {


                        if(Calculator4lineChartList.isEmpty() && Calculator4lineChartListCenter.isEmpty()) {
                            if(floatCheck(kText) && floatCheck(hText) && floatCheck(rText)) {
                                if(positiveNumberCheck(rText)) {
                                    Toast.makeText(context, "Ympyrää lasketaan, odota hetki!", Toast.LENGTH_SHORT).show()

                                    k = kText.toFloat() * -1.0f
                                    h = hText.toFloat() * -1.0f
                                    r = floatSquareRoot(rText.toFloat())

                                    if(r >= 100) {
                                        tIncrement = 0.1f
                                    } else if(r >= 50) {
                                        tIncrement = 0.05f
                                    } else {
                                        tIncrement = 0.01f
                                    }

                                    //Center dot
                                    Calculator4lineChartListCenter.add(Point(h+0.05f, k))
                                    Calculator4lineChartListCenter.add(Point(h, k-0.05f))
                                    Calculator4lineChartListCenter.add(Point(h-0.05f, k))
                                    Calculator4lineChartListCenter.add(Point(h, k-0.05f))
                                    Calculator4lineChartListCenter.add(Point(h+0.05f, k))

                                    CoroutineScope(IO).launch {
                                        while (t < 2) {
                                            var xFormula = Argument("x=$h+$r*cos($t π)")
                                            e1 = Expression("x", xFormula)

                                            xValue = e1.calculate().toFloat()

                                            var yFormula = Argument("y=$k+$r*sin($t π)")
                                            e2 = Expression("y", yFormula)

                                            yValue = e2.calculate().toFloat()

                                            Calculator4lineChartList.add(Point(xValue, yValue))

                                            t = "%.2f".format(t + tIncrement).toFloat()
                                        }

                                        uiUpdate = " "
                                        uiUpdate = ""
                                    }
                                    centerPoint = "($h,$k)"
                                    radius = "%.2f".format(r)
                                } else {
                                    Toast.makeText(context, "r² ei voi olla negatiivinen", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Ympyrää ei voitu piirtää, tarkista syötetyt arvot!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Kaava piirretty jo, tyhjennä taulukko ja yritä uudestaan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Piirrä ympyrä")
                }

                Button(
                    onClick = {
                        if (Calculator4lineChartList.isNotEmpty() && Calculator4lineChartListCenter.isNotEmpty()) {
                            CoroutineScope(IO).launch {
                                while(Calculator4lineChartList.isNotEmpty()) {
                                    Calculator4lineChartList.removeAt(Calculator4lineChartList.size -1)
                                }
                                while(Calculator4lineChartListCenter.isNotEmpty()) {
                                    Calculator4lineChartListCenter.removeAt(Calculator4lineChartListCenter.size -1)
                                }
                            }
                        } else {
                            Toast.makeText(context, "Taulukko on jo tyhjä!", Toast.LENGTH_SHORT).show()
                        }

                        hText = ""
                        kText = ""
                        rText = ""
                        centerPoint = ""
                        t = 0.0f
                        radius = ""
                    }
                ) {
                    Text("Tyhjennä kuvaaja")
                }
            }

            Box(
                modifier = Modifier.padding(5.dp),
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

            Text(
                modifier = Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp),
                fontSize = 20.sp,
                text = "Esimerkissä käytetyt kaavat: " +
                        "\n(x-h)² + (y-k)² = r² " +
                        "\nx = h + r * cos(t),  t=[0-2π]" +
                        "\ny = k + r * sin(t),  t=[0-2π]"
            )

            //hidden text component to update UI via changing its state
            Text(text = uiUpdate)
        }
    }
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

private fun floatCheck(str: String): Boolean {
    val regex = Regex("[+-]?([0-9]*[.])?[0-9]+")
    return str.matches(regex)
}

private fun positiveNumberCheck(str: String): Boolean {
    val regex = Regex("^[+]?([.]\\d+|\\d+[.]?\\d*)\$")
    return str.matches(regex)
}