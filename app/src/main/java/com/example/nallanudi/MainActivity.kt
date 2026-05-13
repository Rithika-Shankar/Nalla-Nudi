package com.example.nallanudi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nallanudi.database.AppDatabase
import com.example.nallanudi.database.DatabasePreload
import com.example.nallanudi.screens.*
import com.example.nallanudi.ui.theme.NallaNudiTheme
import com.example.nallanudi.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and viewModel
        val database = AppDatabase.getDatabase(this)
        val wordDao = database.wordDao()
        viewModel = MainViewModel(wordDao)

        // Preload data
        DatabasePreload.preloadData(this, wordDao)

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val useDarkTheme = isDarkMode ?: isSystemInDarkTheme()

            NallaNudiTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NallaNudiApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NallaNudiApp(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                onContinueClick = { name, avatar ->
                    viewModel.updateProfile(name, avatar)
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSaved = {
                    navController.navigate("saved")
                },
                onNavigateToFlashcard = {
                    navController.navigate("flashcard")
                },
                navController = navController
            )
        }

        composable("saved") {
            SavedWordsScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composable("flashcard") {
            FlashcardScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composable("profile") {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }
    }
}