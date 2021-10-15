package com.hyosakura.xlex.window.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ClearWindow() {
    Text(
        text = "${"\n".repeat(15)}结果显示区",
        textAlign = TextAlign.Center,
        color = Color(222, 222, 222),
        fontSize = TextUnit(25F, TextUnitType.Sp),
        modifier = Modifier.fillMaxSize()
    )
}