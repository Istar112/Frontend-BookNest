package es.dam.booknest.di

import es.dam.booknest.aplication.book.list.GetAllBooksUseCase
import es.dam.booknest.aplication.user.signup.CreateUserUseCase
import org.koin.dsl.module


val moduloAplicacion = module{
    factory { CreateUserUseCase(get()) }
    factory { GetAllBooksUseCase(get()) }
}