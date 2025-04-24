package com.example.piechartappv1

import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.piechartappv1.ui.theme.PieChartAppV1Theme
import com.example.piechartappv1.ui.theme.color1
import com.example.piechartappv1.ui.theme.color2
import com.example.piechartappv1.ui.theme.color3
import com.example.piechartappv1.ui.theme.color4
import com.example.piechartappv1.ui.theme.color5
import com.example.piechartappv1.ui.theme.color6
import com.example.piechartappv1.ui.theme.color7
import com.example.piechartappv1.ui.theme.color8
import com.example.piechartappv1.ui.theme.color9
import com.example.piechartappv1.ui.theme.color10
import com.example.piechartappv1.ui.theme.color11

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PieChartAppV1Theme {

                //Luodaan laatikko komponentti
                Box(
                    modifier = Modifier
                        //Asetetaan laatikon kooksi koko näyttö
                        .fillMaxSize()

                        //Asetetaan laatikon reunoille 20dp tyhjä reuna
                        .padding(20.dp)
                ) {
                    Column() {
                        Text(
                            text = "Suomen väkiluku kaupungittain",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(0.dp, 20.dp),
                            fontSize = 20.sp
                        )

                        //Kutsutaan funktiota, jossa muodostamme diagrammin
                        DrawPieChart()
                    }
                }

            }
        }
    }
}

//Luodaan funktio, joka rakentaa ja piirtää piirakkadiagrammin
//Kotlinissa käyttöliittymäfunktiot merkitään @Composable merkinnällä
@Composable
fun DrawPieChart() {
    // Luodaan pieChartData arvo, johon asetetaan arvoksi lista suomen kaupunkeja, niiden väkiluvut sekä piirakalle haluttu väri.
    val pieChartData = PieChartData(
        slices = listOf(
            //Data tilastokeskukselta 31.12.2024
            PieChartData.Slice("Helsinki", 684018f, color = color1),
            PieChartData.Slice("Espoo", 320931f, color = color2),
            PieChartData.Slice("Tampere", 260180f, color = color3),
            PieChartData.Slice("Vantaa", 251269f, color = color4),
            PieChartData.Slice("Oulu", 216152f, color = color5),
            PieChartData.Slice("Turku", 206073f, color = color6),
            PieChartData.Slice("Jyväskylä", 149194f, color = color7),
            PieChartData.Slice("Kuopio", 125666f, color = color8),
            PieChartData.Slice("Lahti", 121337f, color = color9),
            PieChartData.Slice("Pori", 83305f, color = color10)
        ),
        //Määritellään minkälaisen kuvaajan haluamme
        plotType = PlotType.Pie
    )

    // Luodaan konfigurointi arvo, jossa määritellään piirakalle eri ominaisuuksia
    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE,
            isAnimationEnable = true,
            chartPadding = 30,
            backgroundColor = Color.White,
            showSliceLabels = false,
            animationDuration = 2000,
        )

    // Luodaan sarake, jonka korkeudeksi määritetään 500dp
    Column(modifier = Modifier.height(500.dp)) {

        //Luodaan sarakkeiden väliin 20dp rako
        Spacer(modifier = Modifier.height(20.dp))

        //Muodostetaan selitteiden luettelo ruudukkomuotoon
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData, 3))

        //Kutsutaan funktiota, joka piirtää diagrammin
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            pieChartData,
            pieChartConfig
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PieChartAppV1Theme {
        DrawPieChart()
    }
}