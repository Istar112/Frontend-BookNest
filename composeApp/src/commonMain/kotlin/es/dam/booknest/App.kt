package es.dam.booknest

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.dam.booknest.ui.signup.Signup

import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import es.dam.booknest.ui.AppRoutes
import es.dam.booknest.ui.book.BookDetalle
import es.dam.booknest.ui.home.Home
import es.dam.booknest.ui.home.HomeViewModel
import es.dam.booknest.ui.reading.MyReadings
import es.dam.booknest.ui.reading.MyReadingsViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * En esta  clase tenemos  un navHost para la navegación de la aplicación,
 * será la que se encargue de navegar entre las pantallas.
 *
 * 1. La aplicación empieza en home o en login segun el token
 * 2. Si no hay token, se muestra el login
 * 3. Si hay token, se muestra el home
 * 4. Si se pulsa el botón de registro, se muestra el registro
 * 5. Si se pulsa el botón de volver, se muestra el login de nuevo
 *
 * Las rutas se encuentran en /ui/AppRoutes.kt
 */
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

    // navController para controlar la navegación
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = koinViewModel()
    val myReadingsViewModel: MyReadingsViewModel = koinViewModel()

    // navHost para controlar la navegación
    val navHost = NavHost(navController, startDestination = "register") {
        composable("login") {
            // login
        }
        composable("register") {
            Signup(
                onBack = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                })
        }

        composable("home") {
            Home(
                vm = homeViewModel,
                onBookClick = {
                    navController.navigate(AppRoutes.BOOK_DETAIL)
                },
                onMyReadingsClick = {
                    navController.navigate(AppRoutes.MY_READINGS)
                }
            )
        }

        composable("book_detail") {
            BookDetalle(
                vm = homeViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onAddToTracking = { numPag, dateStart ->
                    homeViewModel.uiState.value.selectedBook?.id?.toIntOrNull()?.let { id ->
                        homeViewModel.addBookToTracking(id, numPag, dateStart)
                    }
                },
                onMarkAsFinished = { finishDate, rating ->
                    homeViewModel.uiState.value.selectedBook?.id?.toIntOrNull()?.let { id ->
                        homeViewModel.addBookToFinished(id, rating, finishDate)
                    }
                }
            )
        }

        composable(AppRoutes.MY_READINGS) {
            MyReadings(
                vm = myReadingsViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }


}