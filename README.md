# (FIN) Kotlin kaaviosovelluksia ja graafisia laskimia
## Esimerkkisovelluksia YCharts ja MathParser kirjastoja käyttämällä 

# (ENG) Kotlin Chart Applications & Graphing Calculators
## Example applications made with YCharts and MathParser -libraries  

### (FIN) Yhteenveto (In English below)  
Tämän projektin tavoitteena oli luoda esimerkkisovelluksia kaavioiden piirtoon sekä graafisia laskimia. Esimerkit ovat suunnattu Oulun ammattikorkeakoulun suomenkielisille opiskelijoille ja esimerkit on kommentoitu suomeksi projektin toimeksiantajan pyynnöstä.  

#### Projektissa käytetyt kirjastot:  
• [YCharts](https://github.com/codeandtheory/YCharts)  
• [MathParser](https://github.com/mariuszgromada/MathParser.org-mXparser)  

#### Projektin sisältö:  
**ChartApplications:** Tämä sovellus sisältää esimerkit alla luetelluista kaavioista:  
     • Pylväskaavio  
     • Viivakaavio  
     • Aaltokaavio  
     • Piirakkakaavio  
     • Donitsikaavio  
     • Kuplakaavio  
     • Yhdistetty kaavio (pylväskaavio & viivakaavio)  

**GraphingCalculators:** Tässä sovelluksessa on kaikki graafiset laskimet.  
     • UserInputExample1 – Käyttäjän syöttämän datan käyttö  
     • GraphingCalculator1 – Yhden kaavan piirtäminen (esim. y=5x²-4)  
     • GraphingCalculator2 – Yhden kaavan piirtäminen, mukautettu piirtoalue ja laskentatiheys
     • GraphingCalculator3 – Kahden kaavan piirtäminen (Kaavojen vertailumahdollisuus)  
     • GraphingCalculator4 – Ympyrän piirtäminen  
     • GraphingCalculator5 – Datanmuunnokset (potenssiin korotus, neliöjuuri)  
     • GraphingCalculator6 – Esimerkit 1, 2, 3 & 5 yhdistetty viimeisteltyyn versioon.  

**CustomUIComponent1:** Tässä esimerkissä ei käytetä kumpaakaan kirjastoa, mitä muissa esimerkeissä käytetään. Esimerkki on kuitenkin projektissa mukana, näyttämässä että dataa voidaan visualisoida myös ilman kirjastoja. 
Esimerkin alkuperäinen tekijä: Stevdza-San [Custom UI Component with Jetpack Compose & Canvas | Part #1 - Preview](https://youtu.be/XuZvHKwD_iM?si=OyuhhgWdmC3G9She)

**YCharts ja MathParser -kirjastojen ohjedokumentti.docx:** Ohjetiedosto, jossa esimerkkejä käsitellään tarkemmin. Dokumentti sisältää myös ohjeet molempien tässä projektissa käytettyjen kirjastojen käyttöön.  

#### Käyttöohjeet:
Jos käytät Github Desktop -sovellusta:  
1. Avaa Github Desktop, paina ruudun vasemmasta ylänurkasta "File" ja sen jälkeen "Clone repository"  
2. Valitse avautuneesta ikkunasta "URL" ja syötä tämän github repositoryn osoite siihen. Lisää myös paikallinen sijainti tiedostoille.
3. Paina "Clone" ja projekti kloonataan tietokoneellesi. 
4. Kun projekti on kloonattu, avaa android studio.
5. Paina "open" näppäintä ja etsi avautuneesta ikkunasta projektin kansio. (Huom! älä avaa kansiota "KotlinChartApps-main", vaan sen sisällä oleva "ChartApplications" TAI "GraphingCalculators" -kansio)
6. Kun olet löytänyt kansion, paina "OK".
7. Android Studio avaa projektin, ensimmäisessä käynnistyksessä saattaa kestää hieman pidempään.
8. Kun projekti on avautunut, valitse yläreunasta android emulaattori, jonka API taso on vähintään 31.
9. Paina vihreää play näppäintä ruudun yläreunassa "app" tekstin oikealta.
10. Sovellus avautuu ja voit alkaa tutkia esimerkkejä.

Jos et käytä Github Desktop -sovellusta ja haluat ladata tiedostot:
1. Projektin Github sivulla, paina vihreää "code" näppäintä.  
2. Paina "Download Zip" 
3. Kun kansio on ladattu, siirrä se haluamaasi sijaintiin.
4. Avaa android studio ja paina "open" näppäintä
5. Etsi kansio tietokoneeltasi ja paina "OK" (Huom! älä avaa kansiota "KotlinChartApps-main, vaan sen sisällä oleva ChartApplications TAI GraphingCalculators -kansio)
6. Kun olet löytänyt kansion, paina "OK".
7. Android Studio avaa projektin, ensimmäisessä käynnistyksessä saattaa kestää hieman pidempään.
8. Kun projekti on avautunut, valitse yläreunasta android emulaattori, jonka API taso on vähintään 31.
9. Paina vihreää play näppäintä ruudun yläreunassa "app" tekstin oikealta puolelta.
10. Sovellus avautuu ja voit alkaa tutkia esimerkkejä.

### (ENG) Summary  
The goal of this project was to create example graphing applications and graphing calculators. These examples were made for finnish students at Oulu University of Applied Sciences. The examples have been commented in Finnish at the request of the project's client. Although the examples were made for finnish students, any English-speaking users may also utilize these examples.   

#### Libraries used in this project:  
• [YCharts](https://github.com/codeandtheory/YCharts)  
• [MathParser](https://github.com/mariuszgromada/MathParser.org-mXparser)  

#### This project’s contents:  
**ChartApplications:** This app contains examples for the basic graphing applications listed below:  
     • BarChart  
     • LineChart  
     • WaveChart  
     • PieCharts  
     • DonutChart  
     • BubbleChart  
     • CombinedChart (BarChart & LineChart)  

**GraphingCalculators:** This app contains the graphing calculators and user input example.  
     • UserInputExample1 - Using user-entered values  
     • GraphingCalculator1 – Drawing one formula (e.g. y=5x²-4)  
     • GraphingCalculator2 – Drawing one formula with custom drawing area and counting density  
     • GraphingCalculator3 – Drawing two formulas (possibility to compare two graphs)  
     • GraphingCalculator4 – Drawing a circle  
     • GraphingCalculator5 – Data transformations (exponentiation, square root)  
     • GraphingCalculator6 – Features from examples 1, 2, 3 & 5 combined into one ”finished” calculator  

**CustomUIComponent1:** This example doesn't use either of the libraries that are used in the other examples. However, the example is included in this project to show that data visualization can be done without libraries.
Original author of the example: Stevdza-San [Custom UI Component with Jetpack Compose & Canvas | Part #1 - Preview](https://youtu.be/XuZvHKwD_iM?si=OyuhhgWdmC3G9She)

**YCharts ja MathParser -kirjastojen ohjedokumentti.docx:** Instructional document made in finnish for finnish students, in which the examples are discussed in more detail. The document also contains instructions for both libraries used in this project.  

#### Instructions:
If you are using the Github Desktop application:
1. Open Github Desktop, click "File" in the upper left corner of the screen and then "Clone repository"
2. Select "URL" in the window that opens and enter the address of this github repository. Also add the local location for the files.
3. Click "Clone" and the project will be cloned to your computer.
4. Once the project is cloned, open android studio.
5. Click the "open" button and find the project folder in the window that opens. (Note! do not open the "KotlinChartApps-main" folder, but the "ChartApplications" OR "GraphingCalculators" folder inside it)
6. Once you have found the folder, click "OK".
7. Android Studio will open the project, it may take a little longer on the first launch.
8. Once the project has opened, select an android emulator with an API level of at least 31 from the top of the screen.
9. Press the green play button at the top of the screen to the right of the "app" text.
10. The application will open and you can start exploring the examples.

If you are not using the Github Desktop application and want to download the files instead:
1. On the project's Github page, press the green "code" button.
2. Press "Download Zip"
3. Once the folder has downloaded, move it to the desired local location.
4. Open android studio and press "open" button
5. Find the folder on your computer and press "OK" (Note! do not open the folder "KotlinChartApps-main", but the ChartApplications OR GraphingCalculators folder inside it)
6. Once you have found the folder, press "OK".
7. Android Studio will open the project, it may take a little longer on the first launch.
8. Once the project has opened, select an android emulator with an API level of at least 31 at the top.
9. Press the green play button at the top of the screen to the right of the "app" text.
10. The application will open and you can start exploring the examples.