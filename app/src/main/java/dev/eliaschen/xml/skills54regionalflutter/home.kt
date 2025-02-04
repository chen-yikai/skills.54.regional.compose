package dev.eliaschen.xml.skills54regionalflutter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.nio.file.WatchEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var currentTime by remember {
        mutableStateOf(
            SimpleDateFormat("HH", Locale.getDefault()).format(
                Date()
            )
        )
    }
    var bgName by remember { mutableStateOf("rain") }
    val currentData = context.assets.open("weatherData/current.xml")
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val dataElement = builder.parse(currentData).documentElement as Element
    val rootEle = xml(dataElement)
    var bg = context.assets.open("weatherBg/${bgName}.jpg").use {
        BitmapFactory.decodeStream(it)
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH", Locale.getDefault()).format(Date())
            delay(1000L)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
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
            Sh()
            var currentCondition by remember { mutableStateOf("") }
            val hourList: NodeList = getEleList(dataElement, "hour")
            for (i in 0 until getEleList(dataElement, "hour").length) {
                val item = hourList.item(i) as Element
                if (getEleContent(item, "time").slice(0..1) == currentTime) {
                    WhiteText(" ${getEleContent(item, "temperature")}", 50.sp, FontWeight.Light)
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
            }
        }
    }
}

@Composable
fun daysForecastList(rootEle: xml, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15))
            .background(Color(0xff76afca).copy(alpha = 0.6f))
            .border(1.dp, Color(0xff76afca), RoundedCornerShape(15))
            .padding(10.dp)
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
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun forecastList(rootEle: xml, context: Context, time: String) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(15))
            .background(Color(0xff76afca).copy(alpha = 0.6f))
            .border(1.dp, Color(0xff76afca), RoundedCornerShape(15))
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
                        size = 15.sp,
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
                        size = 15.sp,
                        weight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
                Sw(10.dp)
            }
        }
    }
}