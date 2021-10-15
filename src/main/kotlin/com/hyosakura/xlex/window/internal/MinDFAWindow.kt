package com.hyosakura.xlex.window.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hyosakura.xlex.common.ApplicationData
import com.hyosakura.xlex.window.MainWindowState
import com.hyosakura.xlex.window.surfaceText

@Composable
fun MinDFAWindow(state: MainWindowState) {
    val availableChar = ApplicationData.converter.getAvailableChar(state.dfa!!)
    val width = state.window.size.width.div(1 + availableChar.size)
    Column {
        Row {
            surfaceText("ID", width)
            for (char in availableChar) {
                surfaceText(char.joinToString(""), width)
            }
        }
        Spacer(modifier = Modifier.height(10.dp).background(Color.White))
        for (dfaState in state.minDfaTable) {
            Row {
                surfaceText("${dfaState.id} ${if (dfaState in state.minDfa!!.acceptStates) "(accept)" else ""}", width)
                for (char in availableChar) {
                    val next = state.dfa!!.nextStates(dfaState, char).firstOrNull()
                    surfaceText(
                        "${next?.id ?: ""}",
                        width
                    )
                }
            }
        }
    }
}