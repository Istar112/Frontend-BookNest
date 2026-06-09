package es.dam.booknest

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import es.dam.booknest.infraestructure.user.AuthManager
import es.dam.booknest.infraestructure.user.SessionManager
import es.dam.booknest.ui.AppRoutes
import es.dam.booknest.ui.book.BookDetalle
import es.dam.booknest.ui.home.Home
import es.dam.booknest.ui.home.HomeViewModel
import es.dam.booknest.ui.login.Login
import es.dam.booknest.ui.profile.Profile
import es.dam.booknest.ui.profile.ProfileViewModel
import es.dam.booknest.ui.reading.MyReadings
import es.dam.booknest.ui.reading.MyReadingsViewModel
import es.dam.booknest.ui.signup.Signup
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

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

    val navController = rememberNavController()
    val authManager: AuthManager = koinInject()
    val homeViewModel: HomeViewModel = koinViewModel()
    val myReadingsViewModel: MyReadingsViewModel = koinViewModel()

    val startDestination = if (SessionManager.accessToken != null) {
        AppRoutes.HOME
    } else {
        AppRoutes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoutes.LOGIN) {
            Login(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoToRegister = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            Signup(
                onBack = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.HOME) {
            Home(
                vm = homeViewModel,
                onBookClick = {
                    navController.navigate(AppRoutes.BOOK_DETAIL)
                },
                onMyReadingsClick = {
                    navController.navigate(AppRoutes.MY_READINGS)
                },
                onProfileClick = {
                    navController.navigate(AppRoutes.PROFILE)
                },
                onLogout = {
                    authManager.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.BOOK_DETAIL) {
            BookDetalle(
                vm = homeViewModel,
                onBack = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
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
                },
                onLogout = {
                    authManager.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.PROFILE) {
            val profileViewModel: ProfileViewModel = koinViewModel()

            Profile(
                vm = profileViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    authManager.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}