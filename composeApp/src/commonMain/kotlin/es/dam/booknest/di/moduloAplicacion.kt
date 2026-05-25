package es.dam.booknest.di

import es.dam.booknest.aplication.book.list.GetAllBooksUseCase
import es.dam.booknest.aplication.book.status.AddBookToFinishedUseCase
import es.dam.booknest.aplication.book.status.AddBookToProcessUseCase
import es.dam.booknest.aplication.book.status.GetReadingStatusUseCase
import es.dam.booknest.aplication.book.status.UpdateReadingUseCase
import es.dam.booknest.aplication.reading.GetUserReadingsUseCase
import es.dam.booknest.aplication.user.signup.CreateUserUseCase
import org.koin.dsl.module


val moduloAplicacion = module{
    factory { CreateUserUseCase(get()) }
    factory { GetAllBooksUseCase(get()) }
    factory { AddBookToProcessUseCase(get()) }
    factory { AddBookToFinishedUseCase(get()) }
    factory { GetReadingStatusUseCase(get()) }
    factory { UpdateReadingUseCase(get()) }
    factory { GetUserReadingsUseCase(get(), get()) }
}