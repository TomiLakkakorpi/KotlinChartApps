package com.example.piechartappv3

import com.example.piechartappv3.ui.theme.PieChartAppV3Theme
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

//Color Imports
import com.example.piechartappv3.ui.theme.color1
import com.example.piechartappv3.ui.theme.color2
import com.example.piechartappv3.ui.theme.color3
import com.example.piechartappv3.ui.theme.color4
import com.example.piechartappv3.ui.theme.color5
import com.example.piechartappv3.ui.theme.color6
import com.example.piechartappv3.ui.theme.color7
import com.example.piechartappv3.ui.theme.color8
import com.example.piechartappv3.ui.theme.color9
import com.example.piechartappv3.ui.theme.color10
import com.example.piechartappv3.ui.theme.color11

//YCharts Imports
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PieChartAppV3Theme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Column() {
                        Text(
                            text = "Suomen väkiluku kaupungittain",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(0.dp, 20.dp),
                            fontSize = 20.sp
                        )

                        //Kutsutaan funktiota, joka piirtää piirakkakaavion
                        DrawPieChart()
                    }
                }
            }
        }
    }
}

//Luodaan funktio, jossa konfiguroidaan piirakkakaavio
@Composable
fun DrawPieChart() {
    //Luodaan dynaaminen lista, johon asetetaan arvoksi lista suomen kaupunkeja, niiden väkiluvut sekä siivulle haluttu väri.
    val dataList = PieChartData(
        slices = listOf(
            //Lisätään kaavioon siivut, lisätään siivuille otsikko, arvo ja väri
            PieChartData.Slice("Helsinki", 684018f, color = color1),
            PieChartData.Slice("Espoo", 320931f, color = color2),
            PieChartData.Slice("Tampere", 260180f, color = color3),
            PieChartData.Slice("Vantaa", 251269f, color = color4),
            PieChartData.Slice("Oulu", 216152f, color = color5),
            PieChartData.Slice("Turku", 206073f, color = color6),
            PieChartData.Slice("Jyväskylä", 149194f, color = color7),
            PieChartData.Slice("Kuopio", 125666f, color = color8),
            PieChartData.Slice("Lahti", 121337f, color = color9),
            PieChartData.Slice("Pori", 83305f, color = color10),

            //Lisätään piirakkadiagrammiin uusi siivu, johon lisätään muun suomen väkiluku
            PieChartData.Slice("Muu Suomi", 3129089f, color = color11)
        ),
        //Määritellään minkälaisen kaavion haluamme
        plotType = PlotType.Pie
    )

    //Luodaan konfigurointi arvo, jossa määritellään kaaviolle eri parametreja
    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE,
            isAnimationEnable = true,
            chartPadding = 30,
            backgroundColor = Color.White,
            showSliceLabels = true,
            sliceLabelTextSize = 12.sp,
            sliceLabelTextColor = Color.White,
            animationDuration = 2000,
        )

    //Lisätään sarake, jonka korkeudeksi määritetään 500dp
    Column(modifier = Modifier.height(500.dp)) {

        //Lisätään sarakkeiden väliin 20dp rako
        Spacer(modifier = Modifier.height(20.dp))

        //Muodostetaan selitteiden luettelo ruudukkomuotoon
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(dataList, 3))

        //Kutsutaan funktiota, joka piirtää piirakkakaavion
        PieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            dataList,
            pieChartConfig
        ) {}
    }
}