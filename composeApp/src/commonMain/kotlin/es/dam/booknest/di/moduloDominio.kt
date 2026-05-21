package es.dam.booknest.di

import es.dam.booknest.infraestructure.RepositorioRestUser
import es.dam.booknest.model.IUserRepository
import org.koin.dsl.module

/**
 * Interfaces de repositorio
 */
val moduloDominio = module {
    /**
     * > adb reverse tcp:8000 tcp:8000
     * para poder acceder desde el movil a la pc terminal
     * 8000
     */
    //ordenador
    single<IUserRepository>{ RepositorioRestUser("http://localhost:8000/api/v1/users",get()) }
    //movil
    //single<IUserRepository>{ RepositorioRestUser("http://172.16.114.113:8000/api/v1/users",get()) }

}