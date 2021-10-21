package com.hyosakura.xlex.window.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hyosakura.regexparser.automata.AttachState
import com.hyosakura.xlex.common.ApplicationData
import com.hyosakura.xlex.window.MainWindowState
import com.hyosakura.xlex.window.flatten
import com.hyosakura.xlex.window.surfaceText

@Composable
fun DFAWindow(state: MainWindowState) {
    val availableChar = ApplicationData.converter.getAvailableChar(state.dfa!!)
    val width = state.window.size.width.div(1 + availableChar.size)
    val stateHorizontal = rememberScrollState(0)
    Column(modifier = Modifier.verticalScroll(stateHorizontal)) {
        Row {
            surfaceText("ID", width)
            for (char in availableChar) {
                surfaceText(char.joinToString(""), width)
            }
        }
        Spacer(modifier = Modifier.height(10.dp).background(Color.White))
        for (dfaState in state.dfaTable) {
            dfaState as AttachState
            Row {
                surfaceText("${dfaState.id} (${dfaState.states.flatten()}) ${if (dfaState.accept) "(accept)" else ""}", width)
                for (char in availableChar) {
                    val next = state.dfa!!.nextStates(dfaState, char).firstOrNull() as? AttachState
                    surfaceText(
                        next?.let {
                            "${it.id} (${it.states.flatten()})"
                        } ?: "",
                        width
                    )
                }
            }
        }
    }
}