package com.example.nallanudi.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    navController: NavController? = null
) {
    val userAvatar by viewModel.userAvatar.collectAsState()

    fun getAvatarIcon(avatar: String): androidx.compose.ui.graphics.vector.ImageVector {
        return when (avatar) {
            "Student" -> Icons.Outlined.School
            "Computer" -> Icons.Outlined.Computer
            "Reader" -> Icons.Outlined.AutoStories
            else -> Icons.Outlined.Person
        }
    }

    val savedWords by viewModel.savedWords.collectAsState()

    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(2) } // 2 for Revise

    // THEME COLORS
    val primaryColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

    Scaffold(
        containerColor = containerColor,

        topBar = {
            Surface(
                color = primaryContainer,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 18.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Text(
                            text = "Flashcard Revision",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = onPrimaryContainer
                        )
                        Text(
                            text = "Guess the Kannada meaning",
                            fontSize = 13.sp,
                            color = onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        },

        bottomBar = {
            NavigationBar(
                containerColor = surfaceColor
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController?.navigate("home") {
                            popUpTo("flashcard") { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(Icons.Outlined.Home, contentDescription = null)
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController?.navigate("saved") {
                            popUpTo("flashcard") { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(Icons.Outlined.BookmarkBorder, contentDescription = null)
                    },
                    label = { Text("My List") }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { /* Already on Revise */ },
                    icon = {
                        Icon(Icons.Outlined.EditNote, contentDescription = null)
                    },
                    label = { Text("Revise") }
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        navController?.navigate("profile") {
                            popUpTo("flashcard") { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(getAvatarIcon(userAvatar), contentDescription = null)
                    },
                    label = { Text("Profile") }
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(containerColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // EMPTY STATE
            if (savedWords.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = primaryColor.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "No Flashcards Found",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Save words from the home screen\nto start revising",
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            fontSize = 15.sp,
                            color = onSurfaceColor.copy(alpha = 0.6f)
                        )
                    }
                }

            } else {

                Spacer(modifier = Modifier.height(20.dp))

                // COUNTER
                Surface(
                    color = primaryContainer,
                    shape = RoundedCornerShape(30.dp),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "${currentIndex + 1} of ${savedWords.size}",
                        modifier = Modifier.padding(
                            horizontal = 22.dp,
                            vertical = 10.dp
                        ),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // FLASHCARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(28.dp)
                        ),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = surfaceColor
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = isFlipped,
                            transitionSpec = {
                                fadeIn(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessLow
                                    )
                                ) togetherWith fadeOut()
                            },
                            label = ""
                        ) { flipped ->
                            if (!flipped) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        color = primaryContainer,
                                        shape = RoundedCornerShape(10.dp),
                                        shadowElevation = 3.dp
                                    ) {
                                        Text(
                                            text = savedWords[currentIndex].subject,
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            ),
                                            fontSize = 12.sp,
                                            color = onPrimaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(30.dp))
                                    Text(
                                        text = savedWords[currentIndex].term,
                                        textAlign = TextAlign.Center,
                                        fontSize = 34.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryColor
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "Try guessing the Kannada meaning",
                                        textAlign = TextAlign.Center,
                                        fontSize = 15.sp,
                                        color = onSurfaceColor.copy(alpha = 0.6f)
                                    )
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = savedWords[currentIndex].kannadaMeaning,
                                        textAlign = TextAlign.Center,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryColor
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = savedWords[currentIndex].explanation,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 26.sp,
                                        fontSize = 16.sp,
                                        color = onSurfaceColor.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(28.dp))
                                    Text(
                                        text = "Example",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = onSurfaceColor
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = savedWords[currentIndex].exampleSentence,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 24.sp,
                                        fontSize = 15.sp,
                                        color = onSurfaceColor.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // BUTTONS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilledTonalButton(
                        onClick = {
                            isFlipped = false
                            if (currentIndex > 0) {
                                currentIndex--
                            }
                        },
                        enabled = currentIndex > 0,
                        modifier = Modifier
                            .weight(0.9f)
                            .height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = primaryColor.copy(alpha = 0.2f)
                        )
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Button(
                        onClick = { isFlipped = !isFlipped },
                        modifier = Modifier
                            .weight(1.4f)
                            .height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        )
                    ) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Flip Card",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            isFlipped = false
                            if (currentIndex < savedWords.size - 1) {
                                currentIndex++
                            }
                        },
                        enabled = currentIndex < savedWords.size - 1,
                        modifier = Modifier
                            .weight(0.9f)
                            .height(58.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = primaryColor.copy(alpha = 0.2f)
                        )
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}