package com.hyosakura.xlex.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import com.hyosakura.regexparser.automata.AutoMata
import com.hyosakura.regexparser.automata.Edge
import com.hyosakura.regexparser.automata.State
import com.hyosakura.regexparser.regex.AbstractPattern

class MainWindowState(
    private val exit: MainWindowState.() -> Unit
) {
    private var _text by mutableStateOf("")

    var regex: String
        get() = _text
        set(value) {
            _text = value
        }

    val window = WindowState(height = 800.dp)

    var error by mutableStateOf<Throwable?>(null)

    var nfaTable = mutableStateListOf<State>()

    var dfaTable = mutableStateListOf<State>()

    var minDfaTable = mutableStateListOf<State>()

    var nfa by mutableStateOf<AutoMata<State, Edge>?>(null)

    var dfa by mutableStateOf<AutoMata<State, Edge>?>(null)

    var minDfa by mutableStateOf<AutoMata<State, Edge>?>(null)

    var code by mutableStateOf("")

    var pattern by mutableStateOf<AbstractPattern?>(null)

    var showType by mutableStateOf(ShowType.CLEAR)

    fun exit() = exit(this)

}

enum class ShowType {
    ERROR,
    CLEAR,
    REGEX_TREE,
    NFA,
    DFA,
    MIN_DFA,
    CODE
}