package com.example.pethaven

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pethaven.ui.composables.LoadingIndicator
import com.example.pethaven.ui.nav.NavGraph
import com.example.pethaven.ui.screens.WelcomeScreen
import com.example.pethaven.ui.theme.PetHavenTheme
import com.example.pethaven.ui.theme.bg_gr
import com.example.pethaven.viewmodel.AuthUiState
import com.example.pethaven.viewmodel.AuthViewModel
import com.example.pethaven.viewmodel.PetViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PetHavenApp() {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authUiState.collectAsState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val dynamicColors = false

    PetHavenTheme (
        dynamicColor = dynamicColors
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (authState) {
                is AuthUiState.Unauthenticated -> {
                    WelcomeScreen(vModel = authViewModel, snackbarHostState = snackbarHostState)
                }
                is AuthUiState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier.aspectRatio(1f),
                        color = bg_gr
                    )
                }
                is AuthUiState.Authenticated -> {
                    // Dashboard
                    val petViewModel: PetViewModel = viewModel()
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        petViewModel = petViewModel
                    )
                }
                is AuthUiState.Error -> {
                    val errorMessage = (authState as AuthUiState.Error).message
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessage,
                        )
                    }
                    WelcomeScreen(vModel = authViewModel, snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}