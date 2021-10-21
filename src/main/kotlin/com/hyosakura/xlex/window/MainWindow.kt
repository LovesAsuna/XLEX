package com.hyosakura.xlex.window

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Window
import com.hyosakura.regexparser.automata.State
import com.hyosakura.regexparser.regex.AbstractPattern
import com.hyosakura.xlex.common.ApplicationData
import com.hyosakura.xlex.window.internal.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.stream.Collectors

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MainWindow(state: MainWindowState) {
    val scope = rememberCoroutineScope()

    fun exit() = scope.launch { state.exit() }

    Window(
        state = state.window,
        title = "XLEX",
        resizable = false,
        onCloseRequest = { exit() }
    ) {
        Column(modifier = Modifier.background(Color(222, 222, 222))) {
            Row {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().background(Color(240, 240, 240)).offset(y = 5.dp),
                    label = {
                        Text(
                            text = "Regex",
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                        )
                    },
                    placeholder = {
                        Text("请输入正则表达式(支持()*|+?)")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = TextUnit(15F, TextUnitType.Sp)),
                    value = state.regex,
                    maxLines = 1,
                    onValueChange = {
                        state.regex = it
                    }
                )
            }
            Row(
                modifier = Modifier.padding(5.dp).background(Color(200, 200, 200)).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    clear(state)
                }) {
                    Text("Clear")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    scope.launch {
                        regexTree(state)
                    }
                }) {
                    Text("Parse")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    scope.launch {
                        nfa(state)
                    }
                }) {
                    Text("NFA")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    scope.launch {
                        dfa(state)
                    }
                }) {
                    Text("DFA")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    scope.launch {
                        minDFA(state)
                    }
                }) {
                    Text("Minimize DFA")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    scope.launch {
                        generateCode(state)
                    }
                }) {
                    Text("Generate CPP Code")
                }
            }
            Row(modifier = Modifier.background(Color(240, 240, 240))) {
                //显示
                when (state.showType) {
                    ShowType.ERROR -> {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        state.error!!.printStackTrace(PrintStream(byteArrayOutputStream))
                        OutlinedTextField(
                            value = String(byteArrayOutputStream.toByteArray()).replace("\t", " ".repeat(4)),
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = TextUnit(20F, TextUnitType.Sp), color = Color.Red),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    ShowType.CLEAR -> ClearWindow()
                    ShowType.REGEX_TREE -> RegexTreeWindow(state)
                    ShowType.NFA -> NFAWindow(state)
                    ShowType.DFA -> DFAWindow(state)
                    ShowType.MIN_DFA -> MinDFAWindow(state)
                    ShowType.CODE -> {
                        OutlinedTextField(
                            value = state.code,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = TextUnit(25F, TextUnitType.Sp)),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun surfaceText(text: String, width: Dp) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colors.primarySurface)),
        color = Color(240, 240, 240)
    ) {
        Text(
            text = text,
            modifier = Modifier.width(width),
            fontSize = TextUnit(25F, TextUnitType.Sp),
            textAlign = TextAlign.Center
        )
    }
}

fun error(state: MainWindowState) {
    state.showType = ShowType.ERROR
}

fun clear(state: MainWindowState) {
    state.regex = ""
    state.pattern = null
    state.showType = ShowType.CLEAR
}

fun regexTree(state: MainWindowState) {
    runCatching {
        require(state.regex.isNotEmpty()) {
            "正则表达式为空!"
        }
        state.pattern = AbstractPattern.compile(state.regex)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.REGEX_TREE
    }
}

fun nfa(state: MainWindowState) {
    runCatching {
        requireNotNull(state.pattern) {
            "需要先解析正则树！"
        }
        val nfa = state.pattern!!.toNFA()
        state.nfa = nfa
        state.nfaTable.clear()
        state.nfaTable.addAll(nfa.stateMap.values)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.NFA
    }
}

fun dfa(state: MainWindowState) {
    runCatching {
        requireNotNull(state.nfa) {
            "需要先生成NFA！"
        }
        val dfa = ApplicationData.converter.convert(state.nfa!!)
        state.dfa = dfa
        state.dfaTable.clear()
        state.dfaTable.addAll(dfa.stateMap.values)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.DFA
    }
}

fun minDFA(state: MainWindowState) {
    runCatching {
        requireNotNull(state.dfa) {
            "需要先生成DFA！"
        }
        val dfa = ApplicationData.converter.minimize(state.dfa!!)
        state.minDfa = dfa
        state.minDfaTable.clear()
        state.minDfaTable.addAll(dfa.stateMap.values)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.MIN_DFA
    }
}

fun generateCode(state: MainWindowState) {
    runCatching {
        requireNotNull(state.minDfa) {
            "需要先生成最小化DFA！"
        }
        state.code = ApplicationData.generator.generate(state.minDfa!!)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.CODE
    }
}

internal fun Collection<State>.flatten(): String {
    return this.stream()
        .map { it.id }
        .collect(
            Collectors.toList()
        ).joinToString(", ")
}