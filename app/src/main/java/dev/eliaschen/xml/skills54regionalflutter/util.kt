package dev.eliaschen.xml.skills54regionalflutter

import android.content.res.TypedArray
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import kotlin.math.log

@Composable
fun WhiteText(
    text: String,
    size: TextUnit = 20.sp,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Text(
        text, color = color, fontWeight = weight, fontSize = size, style = TextStyle(
            shadow = Shadow(color = Color.Black, blurRadius = 10f, offset = Offset(0f, 2f))
        ), modifier = modifier
    )
}

@Composable
fun WhiteTextContent(
    text: String,
    size: TextUnit = 15.sp,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Text(
        text, color = color, fontWeight = weight, fontSize = size, style = TextStyle(
        ), modifier = modifier
    )
}

@Composable
fun Sh(height: Dp = 20.dp) {
    val height by remember { mutableStateOf(height) }
    Spacer(Modifier.height(height))
}

@Composable
fun Sw(width: Dp = 20.dp) {
    Spacer(Modifier.width(width))
}

class xml(private val root: Element) {
    fun getEleContent(name: String, index: Int = 0): String {
        return root.getElementsByTagName(name).item(index).textContent
    }

    fun getEleList(name: String): NodeList {
        return root.getElementsByTagName(name)
    }
}

fun getEleContent(element: Element, name: String, index: Int = 0): String {
    return element.getElementsByTagName(name).item(index).textContent
}

fun getEleList(element: Element, name: String): NodeList {
    return element.getElementsByTagName(name)
}

fun getChineseCondition(en: String): String {
    return when (en) {
        "cloudy" -> "多雲"
        "sunny" -> "晴天"
        "overcast" -> "陰天"
        "rain" -> "雨天"
        "thunder" -> "打雷"
        else -> ""
    }
}