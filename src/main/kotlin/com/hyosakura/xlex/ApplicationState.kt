package com.hyosakura.xlex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hyosakura.xlex.window.MainWindowState

@Composable
fun rememberApplicationState(applicationExit: () -> Unit) = remember {
    ApplicationState {
        applicationExit()
    }
}


class ApplicationState(exit: MainWindowState.() -> Unit) {
    val mainWindowState = MainWindowState(
        exit
    )

    fun exit() {
        mainWindowState.exit()
    }
}