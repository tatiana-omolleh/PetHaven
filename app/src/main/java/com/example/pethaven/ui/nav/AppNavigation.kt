package com.example.pethaven.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
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
import androidx.compose.foundation.layout.* // For Column, Row, Spacer, Modifier, etc.
import androidx.compose.material3.* // For Material3 components like Card, Text, LinearProgressIndicator
import androidx.compose.runtime.Composable // For @Composable annotation
import androidx.compose.ui.Modifier // For Modifier class
import androidx.compose.ui.unit.dp // For dp values
import androidx.compose.foundation.lazy.LazyColumn // For LazyColumn
import androidx.compose.foundation.lazy.items // For LazyColumn items


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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Section 1: Community Feed
                    Text(
                        text = "🐾 Community Feed",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn {
                        items(5) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Pet Story ${index + 1}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "This is an inspiring story about a pet's journey...",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 2: Pet Tips
                    Text(
                        text = "📋 Quick Pet Care Tips",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "• Regular grooming keeps your pet healthy and happy.\n" +
                                "• Ensure your pet's vaccinations are up-to-date.\n" +
                                "• Provide a balanced diet suitable for your pet's age and breed.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 3: Fun Poll
                    Text(
                        text = "📊 Fun Poll: Favorite Pet Toy",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Column {
                        Text("🐕 Ball (40%)", style = MaterialTheme.typography.bodyMedium)
                        LinearProgressIndicator(
                            progress = { 0.4f },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🐈 Feather Wand (35%)", style = MaterialTheme.typography.bodyMedium)
                        LinearProgressIndicator(
                            progress = { 0.35f },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🐇 Chew Toy (25%)", style = MaterialTheme.typography.bodyMedium)
                        LinearProgressIndicator(
                            progress = { 0.25f },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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