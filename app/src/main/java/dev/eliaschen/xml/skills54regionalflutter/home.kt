package dev.eliaschen.xml.skills54regionalflutter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

@Preview(showBackground = true)
@Composable
fun HomeScreen(nav: NavController = rememberNavController(), location: String = "current") {
    val context = LocalContext.current
    var first = true
    var currentTime by remember {
        mutableStateOf(
            SimpleDateFormat("HH", Locale.getDefault()).format(
                Date()
            )
        )
    }
    LaunchedEffect(Unit) {
        if (first) {

        } else {

        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }
    val list_places = listOf<String>("current", "taipei", "tainan", "taichung", "taoyuan")

    var pager = rememberPagerState { 5 }
    Scaffold(bottomBar = { bottomBar(pager, nav) }) { innerPadding ->
        HorizontalPager(state = pager, modifier = Modifier.fillMaxSize()) { thisPage ->
            var page = thisPage
            if (first) {
                page = list_places.indexOf(location)
                first = false
            } else {
                page = thisPage
            }
            var bgName by remember { mutableStateOf("rain") }
            var currentData = context.assets.open("weatherData/${list_places[page]}.xml")
            var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            var dataElement = builder.parse(currentData).documentElement as Element
            var rootEle = xml(dataElement)
            var bg = context.assets.open("weatherBg/${bgName}.jpg").use {
                BitmapFactory.decodeStream(it)
            }

            val customPaddingValues = PaddingValues(
                top = 0.dp, bottom = innerPadding.calculateBottomPadding()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(customPaddingValues)
            ) {
                Image(
                    painter = BitmapPainter(bg.asImageBitmap()),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WhiteText(
                        if ((dataElement.getElementsByTagName("current_weather")
                                .item(0) as Element).getAttribute("type").isNotEmpty()
                        ) "當前位置" else "", 30.sp
                    )
                    WhiteText(
                        rootEle.getEleContent("city"), 20.sp
                    )
                    Sh(10.dp)
                    var currentCondition by remember { mutableStateOf("") }
                    val hourList: NodeList = getEleList(dataElement, "hour")
                    for (i in 0 until getEleList(dataElement, "hour").length) {
                        val item = hourList.item(i) as Element
                        if (getEleContent(item, "time").slice(0..1) == currentTime) {
                            WhiteText(
                                " ${getEleContent(item, "temperature")}", 60.sp, FontWeight.Light
                            )
                            bgName = getEleContent(item, "weather_condition")
                            bgName.let {
                                currentCondition = when (it) {
                                    "cloudy" -> "多雲"
                                    "sunny" -> "晴天"
                                    "overcast" -> "陰天"
                                    "rain" -> "雨天"
                                    "thunder" -> "打雷"
                                    else -> ""
                                }
                            }
                            WhiteText(currentCondition)
                        }
                    }
                    Sh(10.dp)
                    Row {
                        val dayEle = getEleList(dataElement, "day").item(0) as Element
                        WhiteText(
                            "H: ${getEleContent(dayEle, "high_temperature").removeRange(3..3)}"
                        )
                        Sw(15.dp)
                        WhiteText(
                            "L: ${getEleContent(dayEle, "low_temperature").removeRange(3..3)}"
                        )
                    }
                    Sh(10.dp)
                    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                        forecastList(rootEle, context, currentTime)
                        Sh(10.dp)
                        daysForecastList(rootEle, context)
                        Sh(10.dp)
                        aqiList(rootEle)
                    }
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun daysForecastList(rootEle: xml, context: Context) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5))
            .border(1.dp, Color(0xff76afca), RoundedCornerShape(5))
            .fillMaxWidth()
            .background(Color(0xff76afca).copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .alpha(0.5f)
        ) {
            Icon(
                painter = painterResource(R.drawable.calender),
                tint = Color.White,
                contentDescription = "",
                modifier = Modifier.size(13.dp)
            )
            Sw(4.dp)
            WhiteTextContent("10天內天氣預報", 13.sp)
        }
        HorizontalDivider()
        var daysForecast: NodeList = rootEle.getEleList("day")
        val minimalTempList = remember { mutableStateListOf<Int>() }
        val maximalTempList = remember { mutableStateListOf<Int>() }
        var minimalTemp: Int by remember { mutableStateOf(0) }
        var maximalTemp: Int by remember { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            for (i in 0 until 5) {
                var currentDayForecast: Element = daysForecast.item(i) as Element
                minimalTempList.add(
                    getEleContent(
                        currentDayForecast, "low_temperature"
                    ).dropLast(2).toInt()
                )
                maximalTempList.add(
                    getEleContent(
                        currentDayForecast, "high_temperature"
                    ).dropLast(2).toInt()
                )
            }
            minimalTemp = minimalTempList.minOrNull() ?: 0
            maximalTemp = maximalTempList.maxOrNull() ?: 0
            Log.i("4044 minimal", "$minimalTemp")
        }
        Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)) {
            for (i in 0 until 5) {
                var currentDayForecast: Element = daysForecast.item(i) as Element
                var dayForecast = xml(currentDayForecast)
                var dayName: String = when (i) {
                    0 -> "一"
                    1 -> "二"
                    2 -> "三"
                    3 -> "四"
                    4 -> "五"
                    else -> ""
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WhiteTextContent(if (i != 0) "禮拜$dayName" else "今天\u3000")
                    Icon(
                        painter = painterResource(
                            context.resources.getIdentifier(
                                getEleContent(currentDayForecast, "weather_condition"),
                                "drawable",
                                context.packageName
                            )
                        ), tint = Color.White, contentDescription = ""
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        WhiteTextContent(
                            getEleContent(
                                currentDayForecast, "low_temperature"
                            ).dropLast(1)
                        )
                        Sw(8.dp)
                        Box(
                            Modifier
                                .width(100.dp)
                                .height(7.dp)
                        ) {
                            Box(
                                Modifier
                                    .width(100.dp)
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .alpha(0.3f)
                                    .background(Color.White)
                            )
                            val highTempStr =
                                getEleContent(currentDayForecast, "high_temperature").dropLast(2)
                            val lowTempStr =
                                getEleContent(currentDayForecast, "low_temperature").dropLast(2)
                            val highTemp = highTempStr.toInt()
                            val lowTemp = lowTempStr.toInt()

                            val width =
                                ((highTemp - lowTemp) * (100.0 / (maximalTemp - minimalTemp))).toFloat()
                            val paddingStart =
                                (lowTemp - minimalTemp).toFloat() * (100.0 / (maximalTemp - minimalTemp)).toFloat()
                            Box(
                                modifier = Modifier
                                    .width(width.dp)
                                    .height(7.dp)
                                    .padding(
                                        start = paddingStart.dp
                                    )
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = if (highTemp < 15) {
                                                listOf(Color(0xff83b3ff), Color(0xff0066ff))
                                            } else if (lowTemp > 15) {
                                                listOf(Color.Yellow, Color(0xffff7700), Color.Red)
                                            } else if (highTemp > 15 && lowTemp < 15) {
                                                listOf(Color.Yellow)
                                            } else {
                                                listOf(Color.Red)
                                            }, start = Offset(0f, 0f), end = Offset(
                                                width, 0f
                                            )
                                        )
                                    )
                            )
                        }
                        Sw(8.dp)
                        WhiteTextContent(
                            getEleContent(
                                currentDayForecast, "high_temperature"
                            ).dropLast(1)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun aqiList(rootEle: xml) {
    Column(
        Modifier
            .fillMaxSize()
            .height(120.dp)
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(5))
            .border(1.dp, Color(0xff76afca), RoundedCornerShape(5))
            .background(Color(0xff76afca).copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .alpha(0.5f)
                .padding(bottom = 5.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.chart),
                tint = Color.White,
                contentDescription = "",
                modifier = Modifier.size(13.dp)
            )
            Sw(4.dp)
            WhiteTextContent("空氣指標", 13.sp)
        }
        HorizontalDivider()
        val current_aqi = rootEle.getEleContent("current_aqi").toInt()
        val panelColor: Color = when (current_aqi) {
            in 0..25 -> Color(0xff027b7a)
            in 25..50 -> Color(0xff009f63)
            in 50..75 -> Color(0xff82BF47)
            in 75..100 -> Color(0xff009f63)
            in 100..125 -> Color(0xffffb43c)
            in 125..150 -> Color(0xfffd942c)
            in 150..175 -> Color(0xffe34939)
            else -> Color.White
        }
        Box(Modifier.fillMaxSize()) {
            Canvas(Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val arcWidth = 450f
                val arcHeight = 450f
                val topLeftOffset = Offset(
                    (canvasWidth - arcWidth) / 2, 50f
                )
                drawArc(
                    color = Color.Gray.copy(alpha = 0.3f),
                    useCenter = false,
                    startAngle = -190f,
                    sweepAngle = 200f,
                    style = Stroke(width = 50f, cap = StrokeCap.Round),
                    size = Size(arcWidth, arcHeight),
                    topLeft = topLeftOffset
                )
                drawArc(
                    color = panelColor,
                    useCenter = false,
                    startAngle = -190f,
                    sweepAngle = (rootEle.getEleContent("current_aqi")
                        .toFloat() * (200 / 500.0)).toFloat(),
                    style = Stroke(width = 50f, cap = StrokeCap.Round),
                    size = Size(arcWidth, arcHeight),
                    topLeft = topLeftOffset
                )
            }
            Column(
                Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WhiteTextContent(rootEle.getEleContent("current_aqi"), 50.sp, FontWeight.Light)
                WhiteTextContent("AQI")
            }
            WhiteTextContent(
                "0", modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(86.dp, -10.dp)
            )
            WhiteTextContent(
                "500", modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(-80.dp, -10.dp)
            )
            Text(
                "良好 ",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(5.dp))
                    .background(panelColor)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }

}

@SuppressLint("DiscouragedApi")
@Composable
fun forecastList(rootEle: xml, context: Context, time: String) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(5))
            .border(1.dp, Color(0xff76afca), RoundedCornerShape(5))
            .background(Color(0xff76afca).copy(alpha = 0.6f))
            .padding(vertical = 15.dp)
    ) {
        item() {
            Sw(15.dp)
        }
        val forecastEle = rootEle.getEleList("hour")
        val forecastEleList = (0 until forecastEle.length).map { forecastEle.item(it) as Element }
        itemsIndexed(forecastEleList) { index, item ->
            val forecastEle = forecastEle.item(index) as Element
            val forecastTime = getEleContent(
                forecastEle, "time"
            ).slice(0..1)
            if (forecastTime.toInt() >= time.toInt()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(50.dp)
                ) {
                    WhiteTextContent(
                        if (forecastTime == time) "現在" else forecastTime,
                        weight = FontWeight.Bold,
                        modifier = Modifier.align(
                            Alignment.TopCenter
                        )
                    )
                    Icon(
                        painter = painterResource(
                            context.resources.getIdentifier(
                                getEleContent(
                                    forecastEle, "weather_condition"
                                ), "drawable", context.packageName
                            )
                        ),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.Center),
                        tint = Color.White
                    )
                    WhiteTextContent(
                        " " + getEleContent(forecastEle, "temperature"),
                        weight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
                Sw(10.dp)
            }
        }
    }
}

@Composable
fun bottomBar(pager: PagerState = rememberPagerState { 2 }, nav: NavController) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White)
            .padding(horizontal = 10.dp)
    ) {
        IconButton(onClick = {}, Modifier.align(Alignment.TopStart)) {
            Icon(
                painter = painterResource(
                    R.drawable.map
                ), contentDescription = "", Modifier.size(30.dp)
            )
        }
        Row(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(painter = painterResource(R.drawable.current_location),
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier
                    .alpha(if (0 == pager.currentPage) 1f else 0.2f)
                    .height(15.dp)
                    .clickable {
                        scope.launch {
                            pager.animateScrollToPage(0)
                        }
                    })
            repeat(4) {
                val animatedFade = animateFloatAsState(
                    targetValue = if (it + 1 == pager.currentPage) 1f else 0.2f, label = "alpha"
                )
                Box(
                    Modifier
                        .padding(horizontal = 5.dp)
                        .clip(CircleShape)
                        .alpha(animatedFade.value)
                        .background(Color.Black)
                        .size(8.dp, 8.dp)
                        .clickable {
                            scope.launch {
                                pager.animateScrollToPage(it + 1)
                            }
                        })

            }
        }
        IconButton(
            onClick = { nav.navigate(Screen.List.route + "/current") },
            Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.list
                ), contentDescription = "", Modifier.size(30.dp)
            )
        }
    }
}