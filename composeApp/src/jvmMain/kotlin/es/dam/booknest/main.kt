package es.dam.booknest

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import es.dam.booknest.di.initKoin

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Fronend-BookNest",
    ) {
        App()
    }
}