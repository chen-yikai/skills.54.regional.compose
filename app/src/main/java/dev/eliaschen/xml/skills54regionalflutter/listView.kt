package dev.eliaschen.xml.skills54regionalflutter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import org.w3c.dom.Element
import java.text.SimpleDateFormat
import java.util.Date
import javax.xml.parsers.DocumentBuilderFactory
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ListScreen(nav: NavController = rememberNavController()) {
    var searchValue by remember { mutableStateOf("") }
    val context = LocalContext.current
    val listFile = context.assets.open("weatherData/city_list.xml")
    val xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val listXml = xml(xmlBuilder.parse(listFile).documentElement as Element)
    var currentTime by remember { mutableStateOf("") }
    var searchEnabled by remember { mutableStateOf(false) }
    var searchHint by remember { mutableStateOf(listOf<String>()) }
    var userList by remember { mutableStateOf(listOf<String>()) }
    val sharePref = context.getSharedPreferences("app", Context.MODE_PRIVATE)
    var showMenu by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm", getDefault()).format(Date())
            delay(1000L)
        }
    }

    fun convertStringToList(string: String): List<String> {
        val trimmedString = string.trim().removeSurrounding("[", "]")
        val list = if (trimmedString.isEmpty()) {
            emptyList()
        } else {
            trimmedString.split(",").map { it.trim() }
        }
        return list
    }

    LaunchedEffect(Unit) {
        val cityList = listXml.getEleList("city")
        searchHint = emptyList()
        for (list in 0 until cityList.length) {
            val listEle = cityList.item(list) as Element
            searchHint += getEleContent(listEle, "name")
        }
    }

    LaunchedEffect(Unit) {
        userList = convertStringToList(sharePref.getString("list", "[新北市]")!!)
        if (userList.isEmpty()) {
            userList += "新北市"
        }
    }

    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = {
                showMenu = !showMenu
            }) {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .width(150.dp)
                ) {
                    DropdownMenuItem(text = { Text("編輯列表", color = Color.Gray) },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Edit, tint = Color.Gray, contentDescription = ""
                            )
                        },
                        onClick = { /* Do something... */ })
                    DropdownMenuItem(text = { Text("設定", color = Color.Gray) }, trailingIcon = {
                        Icon(
                            Icons.Outlined.Settings, tint = Color.Gray, contentDescription = ""
                        )
                    }, onClick = { /* Do something... */ })
                    HorizontalDivider()
                    DropdownMenuItem(text = { Text("攝氏°C", color = Color.Gray) },
                        onClick = {},
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Check, tint = Color.Gray, contentDescription = ""
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("華氏°F", color = Color.Gray) },
                        onClick = {},
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Check, tint = Color.Gray, contentDescription = ""
                            )
                        })
                }
                Icon(
                    painter = painterResource(R.drawable.menu), contentDescription = ""
                )
            }
        }
        Text("天氣", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Sh()
        OutlinedTextField(onValueChange = {
            searchValue = it
            searchEnabled = searchValue.isNotEmpty()
        },
            singleLine = true,
            value = searchValue,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("輸入城市地點來搜尋") },
            trailingIcon = {
                if (searchValue.isNotEmpty()) {
                    IconButton(onClick = {
                        searchValue = ""
                        searchEnabled = false
                    }) {
                        Icon(painter = painterResource(R.drawable.clear), contentDescription = "")
                    }
                } else {
                    Icon(painter = painterResource(R.drawable.search), contentDescription = "")
                }
            })
        Sh()
        Column {
            val cityList = listXml.getEleList("city")
            if (!searchEnabled) {
                for (list in 0 until cityList.length) {
                    val listEle = cityList.item(list) as Element
                    val cityWeatherFile =
                        context.assets.open("weatherData/${getEleContent(listEle, "file_name")}")
                    var weatherCondition by remember { mutableStateOf("sunny") }
                    val bg = context.assets.open("weatherBg/${weatherCondition}.jpg")
                        .use { BitmapFactory.decodeStream(it).asImageBitmap() }
                    val cityWeatherEle =
                        xmlBuilder.parse(cityWeatherFile).documentElement as Element
                    val cityname = getEleContent(listEle, "file_name").split(".")[0]
                    if (userList.toString().contains(getEleContent(listEle, "name"))) {
                        Box(Modifier
                            .padding(bottom = 15.dp)
                            .height(120.dp)
                            .fillMaxWidth()
                            .clickable {
                                nav.navigate(
                                    "home/${cityname}"
                                )
                            }) {
                            Image(
                                bitmap = bg,
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(5.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                            )
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                            ) {
                                Column(Modifier.align(Alignment.TopStart)) {
                                    WhiteText(
                                        if (listEle.getAttribute("type") == "current") "當前位置" else getEleContent(
                                            listEle, "name"
                                        )
                                    )
                                    WhiteText(currentTime, 15.sp)
                                }
                                Row(Modifier.align(Alignment.BottomEnd)) {
                                    WhiteText(
                                        "H: ${
                                            getEleContent(
                                                cityWeatherEle, "high_temperature"
                                            ).dropLast(2)
                                        }", size = 15.sp
                                    )
                                    Sw(10.dp)
                                    WhiteText(
                                        "L: ${
                                            getEleContent(
                                                cityWeatherEle, "low_temperature"
                                            ).dropLast(
                                                2
                                            )
                                        }", size = 15.sp
                                    )
                                }
                                for (i in 0 until getEleList(cityWeatherEle, "hour").length) {
                                    val cityWeatherHour =
                                        getEleList(cityWeatherEle, "hour").item(i) as Element
                                    if (currentTime.slice(0..1) == getEleContent(
                                            cityWeatherHour, "time"
                                        ).slice(0..1)
                                    ) {
                                        weatherCondition = getEleContent(
                                            cityWeatherHour, "weather_condition"
                                        )
                                        WhiteText(
                                            getChineseCondition(
                                                getEleContent(
                                                    cityWeatherHour, "weather_condition"
                                                )
                                            ),
                                            size = 15.sp,
                                            modifier = Modifier.align(Alignment.BottomStart)
                                        )
                                        WhiteText(
                                            getEleContent(cityWeatherHour, "temperature"),
                                            size = 40.sp,
                                            modifier = Modifier.align(
                                                Alignment.TopEnd
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Column {
                    val hintFilter = searchHint.filter { it.contains(searchValue) }
                    if (hintFilter.isNotEmpty()) {
                        hintFilter.forEach {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(it)
                                if (!userList.contains(it)) {
                                    IconButton(onClick = {
                                        userList += it
                                        sharePref.edit().putString("list", userList.toString())
                                            .apply()
                                        searchValue = ""
                                        searchEnabled = false
                                    }) {
                                        Icon(
                                            painter = painterResource(R.drawable.add),
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                            HorizontalDivider()
                        }
                    } else {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text("找不到 $searchValue")
                        }
                    }
                }
            }
        }
    }
}
