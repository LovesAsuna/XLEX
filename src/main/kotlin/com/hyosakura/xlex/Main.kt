package com.hyosakura.xlex

import androidx.compose.ui.window.application

lateinit var applicationState: ApplicationState

fun main() = application {
    applicationState = rememberApplicationState {
        exitApplication()
    }
    Application(applicationState)
}