package com.hyosakura.xlex.window.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.hyosakura.xlex.window.MainWindowState

@OptIn(ExperimentalUnitApi::class)
@Composable
fun RegexTreeWindow(state: MainWindowState) {
    Text(
        text = "${"\n".repeat(15)}${state.pattern.toString()}",
        textAlign = TextAlign.Center,
        fontSize = TextUnit(25F, TextUnitType.Sp),
        modifier = Modifier.fillMaxSize()
    )
}