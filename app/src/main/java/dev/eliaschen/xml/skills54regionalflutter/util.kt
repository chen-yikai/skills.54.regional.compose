package dev.eliaschen.xml.skills54regionalflutter

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
import androidx.compose.runtime.setValue
import org.w3c.dom.Element

@Composable
fun WhiteText(
    text: String,
    size: TextUnit = 20.sp,
    weight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Text(
        text, color = Color.White, fontWeight = weight, fontSize = size, style = TextStyle(
            shadow = Shadow(color = Color.Black, blurRadius = 10f, offset = Offset(0f, 2f))
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

fun getElementByTag(ele: Element){
    return
}