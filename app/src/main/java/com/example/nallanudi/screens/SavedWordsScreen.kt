package com.example.nallanudi.screens

import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.viewmodels.MainViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWordsScreen(
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

    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    val savedWords by viewModel.savedWords.collectAsState()

    // THEME COLORS
    val primaryColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

    // TTS
    LaunchedEffect(Unit) {
        val audioManager = context.getSystemService(AudioManager::class.java)
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(0.85f)
                tts?.setPitch(1.0f)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun speakWord(word: String) {
        val params = Bundle()
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, params, "savedWord")
    }

    var selectedTab by remember { mutableStateOf(1) } // 1 for My List

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
                            text = "My List",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = onPrimaryContainer
                        )
                        Text(
                            text = "Your saved technical terms",
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
                            popUpTo("saved") { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(Icons.Outlined.Home, contentDescription = null)
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { /* Already on My List */ },
                    icon = {
                        Icon(Icons.Filled.Bookmark, contentDescription = null)
                    },
                    label = { Text("My List") }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController?.navigate("flashcard") {
                            popUpTo("saved") { inclusive = true }
                        }
                    },
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
                            popUpTo("saved") { inclusive = true }
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(containerColor)
        ) {

            // EMPTY STATE
            if (savedWords.isEmpty()) {

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = primaryColor.copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "No Saved Words Yet",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Save words from the home screen\nto see them here",
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        color = onSurfaceColor.copy(alpha = 0.6f),
                        fontSize = 15.sp
                    )
                }

            } else {

                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(savedWords) { word ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = surfaceColor
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Surface(
                                    color = primaryContainer,
                                    shape = RoundedCornerShape(10.dp),
                                    shadowElevation = 3.dp
                                ) {
                                    Text(
                                        text = word.subject,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        fontSize = 12.sp,
                                        color = onPrimaryContainer
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = word.term,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = word.kannadaMeaning,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = onSurfaceColor
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = word.explanation,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp,
                                    fontSize = 15.sp,
                                    color = onSurfaceColor.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Example Sentence",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = primaryColor
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = word.exampleSentence,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp,
                                    fontSize = 15.sp,
                                    color = onSurfaceColor
                                )

                                Spacer(modifier = Modifier.height(22.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    SmallSavedActionButton(
                                        icon = Icons.Outlined.VolumeUp,
                                        iconColor = onSurfaceColor
                                    ) {
                                        speakWord(word.term)
                                    }

                                    SmallSavedActionButton(
                                        icon = Icons.Filled.Bookmark,
                                        iconColor = primaryColor
                                    ) {
                                        viewModel.removeSavedWord(word)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SmallSavedActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}