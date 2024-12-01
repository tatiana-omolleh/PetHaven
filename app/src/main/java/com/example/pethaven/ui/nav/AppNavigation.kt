package com.example.pethaven.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pethaven.model.data.User
import com.example.pethaven.ui.composables.Dashboard
import com.example.pethaven.ui.screens.HomeScreen
import com.example.pethaven.ui.screens.PetListScreen
import com.example.pethaven.ui.screens.ProfileScreen
import com.example.pethaven.ui.screens.SettingsScreen
import com.example.pethaven.viewmodel.AuthUiState
import com.example.pethaven.viewmodel.AuthViewModel
import com.example.pethaven.viewmodel.PetViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    petViewModel: PetViewModel
) {
    val userState by authViewModel.authUiState.collectAsState()
    val petState by petViewModel.petUiState.collectAsState()
    val user =
        if (userState is AuthUiState.Authenticated)
            (userState as AuthUiState.Authenticated).user
        else User(id = "demo-user", name = "John Doe", email = "johndoe@email.com")

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.Home.route
    ) {
        composable(Screen.Dashboard.Home.route) {
            Dashboard(
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                navController = navController,
            ) {
                HomeScreen(
                    user = user
                )
            }
        }
        composable(Screen.Dashboard.Pets.route) {
            var showModal by remember { mutableStateOf(false) }
            val toggleShowModal = { showModal = !showModal }
            Dashboard(
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                navController = navController,
                onAddPetClick = { toggleShowModal() }
            ) {
                PetListScreen(
                    petState = petState,
                    petViewModel = petViewModel,
                    toggleShowModal = toggleShowModal,
                    showModal = showModal,
                    userId = user.id
                )
            }
        }
        composable(Screen.PetProfile.route) {
            ProfileScreen(petName = "Fluffy", navController = navController)
        }
        composable(Screen.Dashboard.Community.route) {
            Dashboard(
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                navController = navController
            ) {
                Text(
                    text = "Community content"
                )
            }
        }
        composable(
            Screen.Settings.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }

        ) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignOut = {
                    authViewModel.onSignOut()
                    navController.navigate(Screen.Dashboard.Home.route) {
                        popUpTo(Screen.Dashboard.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}