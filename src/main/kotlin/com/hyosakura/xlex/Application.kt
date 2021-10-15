package com.hyosakura.xlex

import androidx.compose.runtime.Composable
import com.hyosakura.xlex.window.MainWindow

@Composable
fun Application(state: ApplicationState) {
    MainWindow(state.mainWindowState)
}