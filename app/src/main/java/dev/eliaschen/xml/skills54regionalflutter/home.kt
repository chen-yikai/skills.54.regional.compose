package dev.eliaschen.xml.skills54regionalflutter

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.nio.file.WatchEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var currentTime by remember { mutableStateOf("") }
    val bg = context.assets.open("weatherBg/cloudy.jpg").use {
        BitmapFactory.decodeStream(it)
    }
    val currentData = context.assets.open("weatherData/current.xml")
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val currentDataElement =
        builder.parse(currentData).documentElement.getElementsByTagName("current_weather")
            .item(0) as Element

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
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
            WhiteText("當前位置", 30.sp)
            WhiteText(
                currentDataElement.getElementsByTagName("city").item(0).textContent, 20.sp
            )
            Sh()
            WhiteText("°", 50.sp, FontWeight.Light)

            WhiteText(currentTime)

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .height(60.dp)
                .padding(15.dp)
                .align(Alignment.BottomCenter)
        ) {}
    }
}