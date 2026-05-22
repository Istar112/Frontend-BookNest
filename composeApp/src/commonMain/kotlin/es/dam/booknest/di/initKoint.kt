package es.dam.booknest.di

import org.koin.core.context.startKoin


fun initKoin() {
    startKoin {
        modules(
            moduloPresentacion,
            moduloInfraestructura,
            moduloAplicacion,
        )
    }
}