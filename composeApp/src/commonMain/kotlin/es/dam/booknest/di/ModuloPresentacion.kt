package es.dam.booknest.di

import es.dam.booknest.ui.home.HomeViewModel
import es.dam.booknest.ui.reading.MyReadingsViewModel
import es.dam.booknest.ui.signup.SignupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val moduloPresentacion = module {
    viewModel { SignupViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyReadingsViewModel(get(), get(), get()) }
}