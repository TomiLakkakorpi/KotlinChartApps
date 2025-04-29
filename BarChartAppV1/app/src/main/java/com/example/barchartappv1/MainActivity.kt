package com.example.barchartappv1

import com.example.barchartappv1.ui.theme.BarChartAppV1Theme
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//Color Imports
import com.example.barchartappv1.ui.theme.color1
import com.example.barchartappv1.ui.theme.color2
import com.example.barchartappv1.ui.theme.color3
import com.example.barchartappv1.ui.theme.color4
import com.example.barchartappv1.ui.theme.color5
import com.example.barchartappv1.ui.theme.color6

//YCharts Imports
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarChartAppV1Theme {

                //Luodaan laatikko komponentti
                Box(
                    modifier = Modifier
                        //Asetetaan laatikon kooksi koko näyttö
                        .fillMaxSize()

                        //Asetetaan laatikon reunoille 20dp tyhjä reuna
                        .padding(20.dp)
                ) {
                    //Kutsutaan funktiota, jossa muodostamme diagrammin
                    DrawBarChart()
                }
            }
        }
    }
}

//Luodaan funktio, joka rakentaa ja piirtää pylväsdiagrammin
//Kotlinissa käyttöliittymäfunktiot merkitään @Composable merkinnällä
@Composable
fun DrawBarChart() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        //Asetetaan maksimiarvopalkeille, tässä voi myös tarkistaa taulukon suurimman arvon ja valita sen tähän
        val maxRange = 700000F

        //Asetetaan montako "askelta" haluamme taulukolle. Maksimiarvon ollessa 700000, asettamalla arvo 7 saamme akselille viivat 100 tuhannen välein
        val yStepSize = 7

        //Luodaan lista johon lisätään haluamamme data
        val list = arrayListOf(
            BarData(
                //Asetetaan paikka x akselilla (monesko pylväs)
                //Asetetaan palkin y arvo (palkin korkeus)
                point = Point(1F, 681802F),

                //Lisätään palkille väri
                color = color1,

                //Asetetaan otsikko tiedolle
                label = "Helsinki",
            ),

            BarData(
                point = Point(2F, 318507F),
                color = color2,
                label = "Espoo",
            ),

            BarData(
                point = Point(3F, 258770F),
                color = color3,
                label = "Tampere",
            ),

            BarData(
                point = Point(4F, 250073F),
                color = color4,
                label = "Vantaa",
            ),

            BarData(
                point = Point(5F, 215530F),
                color = color5,
                label = "Oulu",
            ),

            BarData(
                point = Point(6F, 204618F),
                color = color6,
                label = "Turku",
            ),
        )

        // Luodaan xAxisData arvo jossa konfiguroidaan x akselille eri ominaisuuksia.
        val xAxisData = AxisData.Builder()
            .axisStepSize(30.dp)
            .steps(list.size - 1)
            .bottomPadding(40.dp)
            .axisLabelAngle(20f)
            .startDrawPadding(25.dp)
            .labelData { index -> list[index].label }
            .build()

        // Luodaan yAxisData arvo jossa konfiguroidaan y akselille eri ominaisuuksia.
        val yAxisData = AxisData.Builder()
            .steps(yStepSize)
            .labelAndAxisLinePadding(20.dp)
            .axisOffset(10.dp)
            .labelData { index -> (index * (maxRange / yStepSize)).toString() }
            .build()

        // Luodaan arvo, johon lisätään datalistamme sekä x ja y akselien konfiguraatiot
        val barChartData = BarChartData(
            chartData = list,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                paddingBetweenBars = 25.dp,
                barWidth = 20.dp
            ),
            showYAxis = true,
            showXAxis = true,
            horizontalExtraSpace = 50.dp
        )

        //Kutsutaan YCharts funktiota joka piirtää diagrammin
        //Annetaan funktiolle lisäämämme data sekä x ja y akselien konfiguraatiot
        BarChart(
            modifier = Modifier
                .height(350.dp),
            barChartData = barChartData
        )
    }
}

// @Preview merkittyjä funktiota voidaan tarkastella "preview" osiossa ennen ohjelman ajoa.
@Preview(showBackground = true)
@Composable
fun BarChartPreview() {
    BarChartAppV1Theme {
        //Kutsutaan funktiota, joka sisältää kaiken koodin diagrammin piirtämiseen, jotta sitä voidaan tarkastella preview ikkunassa
        DrawBarChart()
    }
}