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
import com.hyosakura.regexparser.automata.NFA
import com.hyosakura.xlex.common.ApplicationData
import com.hyosakura.xlex.window.MainWindowState
import com.hyosakura.xlex.window.flatten
import com.hyosakura.xlex.window.surfaceText
import java.util.stream.Collectors

@Composable
fun NFAWindow(state: MainWindowState) {
    val availableChar = ApplicationData.converter.getAvailableChar(state.nfa!!)
    val width = state.window.size.width.div(2 + availableChar.size)
    val stateHorizontal = rememberScrollState(0)
    Column(modifier = Modifier.verticalScroll(stateHorizontal)) {
        Row {
            surfaceText("ID", width)
            for (char in availableChar) {
                surfaceText(char.joinToString(""), width)
            }
            surfaceText("ε", width)
        }
        Spacer(modifier = Modifier.height(10.dp).background(Color.White))
        for (nfaState in state.nfaTable) {
            Row {
                surfaceText("${nfaState.id} ${if (nfaState in state.nfa!!.acceptStates) "(accept)" else ""}", width)
                for (char in availableChar) {
                    val next = state.nfa!!.nextStates(nfaState, char)
                    surfaceText(
                        if (next.isEmpty()) {
                            ""
                        } else {
                            next.flatten()
                        },
                        width
                    )
                }
                surfaceText(
                    (state.nfa as NFA)
                        .getFreeStates(mutableListOf(nfaState))
                        .minus(nfaState)
                        .stream()
                        .map { it.id }
                        .collect(
                            Collectors.toList()
                        ).joinToString(", "),
                    width
                )
            }
        }
    }
}