package com.example.nallanudi.screens

import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToSaved: () -> Unit,
    onNavigateToFlashcard: () -> Unit,
    navController: NavController? = null
) {
    val userName by viewModel.userName.collectAsState()
    val userAvatar by viewModel.userAvatar.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val filteredWords by viewModel.filteredWords.collectAsState()
    val wordOfTheDay by viewModel.wordOfTheDay.collectAsState()
    
    // Get actual saved words IDs from database for accurate icon state
    val savedWordsData by viewModel.savedWords.collectAsState()
    val savedWordIds = remember(savedWordsData) {
        savedWordsData.map { it.id }.toSet()
    }

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    // THEME COLORS
    val primaryColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val outlineColor = MaterialTheme.colorScheme.outline

    // TTS Setup
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setPitch(1.0f)
                tts?.setSpeechRate(0.85f)
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
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, params, "tts1")
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(text = "Close", color = primaryColor)
                }
            },
            title = { Text(text = "About Nalla Nudi", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Nalla Nudi is an English to Kannada bridge dictionary designed for students to learn technical and academic terminology easily.\n\nThe app supports subject-based learning, saved words, and flashcard revision for better memory retention.",
                    lineHeight = 22.sp
                )
            },
            containerColor = surfaceColor,
            shape = RoundedCornerShape(22.dp)
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = surfaceColor
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(getAvatarIcon(userAvatar), contentDescription = null, tint = onPrimaryContainer)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Hello, $userName!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                        Text(text = "Happy Learning", fontSize = 12.sp, color = onSurfaceColor.copy(alpha = 0.6f))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = outlineColor.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("My List") },
                    selected = false,
                    onClick = { onNavigateToSaved() },
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Revise with Flashcards") },
                    selected = false,
                    onClick = { onNavigateToFlashcard() },
                    icon = { Icon(Icons.Outlined.AutoAwesome, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Profile Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController?.navigate("profile")
                    },
                    icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("About Nalla Nudi") },
                    selected = false,
                    onClick = { showAboutDialog = true },
                    icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    ) {
        Scaffold(
            containerColor = containerColor,
            bottomBar = {
                NavigationBar(containerColor = surfaceColor) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            navController?.navigate("saved") { popUpTo("home") { inclusive = false } }
                        },
                        icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = null) },
                        label = { Text("My List") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = {
                            selectedTab = 2
                            navController?.navigate("flashcard") { popUpTo("home") { inclusive = false } }
                        },
                        icon = { Icon(Icons.Outlined.EditNote, contentDescription = null) },
                        label = { Text("Revise") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = {
                            selectedTab = 3
                            navController?.navigate("profile") { popUpTo("home") { inclusive = false } }
                        },
                        icon = { Icon(getAvatarIcon(userAvatar), contentDescription = null) },
                        label = { Text("Profile") }
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(containerColor)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(primaryContainer)
                            .padding(top = 18.dp, start = 18.dp, end = 18.dp, bottom = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Outlined.Menu, contentDescription = null, tint = onPrimaryContainer)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = userName, fontWeight = FontWeight.Medium, color = onPrimaryContainer)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    modifier = Modifier.size(40.dp).shadow(2.dp, CircleShape),
                                    shape = CircleShape,
                                    color = surfaceColor,
                                    onClick = { navController?.navigate("profile") }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(getAvatarIcon(userAvatar), contentDescription = null, tint = primaryColor, modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "ನಲ್ಲ ನುಡಿ", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = onPrimaryContainer)
                        Text(text = "NALLA NUDI", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = onPrimaryContainer)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "An English to Kannada Bridge Dictionary", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 13.sp, color = onPrimaryContainer.copy(alpha = 0.8f))
                    }
                }

                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            viewModel.updateSearchQuery(it)
                        },
                        placeholder = { Text("Search Technical Terms...", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = outlineColor.copy(alpha = 0.3f)
                        ),
                        singleLine = true
                    )
                }

                // Global Search Results Section
                if (searchQuery.isNotEmpty()) {
                    item {
                        Text(
                            text = "Search Results (${searchResults.size})",
                            modifier = Modifier.padding(start = 22.dp, top = 6.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = primaryColor
                        )
                    }

                    if (searchResults.isEmpty()) {
                        item {
                            Text(
                                text = "No matches found in the entire database",
                                modifier = Modifier.fillMaxWidth().padding(22.dp),
                                textAlign = TextAlign.Center,
                                color = onSurfaceColor.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        items(searchResults) { word ->
                            val isSaved = savedWordIds.contains(word.id)
                            WordCard(
                                word = word,
                                isSaved = isSaved,
                                onSpeak = { speakWord(it) },
                                onToggleSave = { if (isSaved) viewModel.removeSavedWord(word) else viewModel.saveWord(word) }
                            )
                        }
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp), color = outlineColor.copy(alpha = 0.2f))
                    }
                }

                item {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SubjectChip(title = "Science", selected = selectedSubject == "Science") {
                            viewModel.updateSubject("Science")
                        }
                        SubjectChip(title = "Mathematics", selected = selectedSubject == "Math") {
                            viewModel.updateSubject("Math")
                        }
                        SubjectChip(title = "Commerce", selected = selectedSubject == "Commerce") {
                            viewModel.updateSubject("Commerce")
                        }
                    }
                }

                // WORD OF THE DAY (Only show if not searching to keep focus)
                if (searchQuery.isEmpty()) {
                    wordOfTheDay?.let { word ->
                        item {
                            val isSaved = savedWordIds.contains(word.id)
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(color = primaryContainer, shape = RoundedCornerShape(10.dp), shadowElevation = 3.dp) {
                                        Text(
                                            text = word.subject, 
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), 
                                            fontSize = 12.sp,
                                            color = onPrimaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Word of The Day",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = onSurfaceColor
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(text = word.term, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 26.sp, color = primaryColor, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = word.kannadaMeaning, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = onSurfaceColor)
                                    Spacer(modifier = Modifier.height(18.dp))
                                    Text(text = word.explanation, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, lineHeight = 24.sp, fontSize = 16.sp, color = onSurfaceColor.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = "Example Sentence", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = word.exampleSentence, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 15.sp, lineHeight = 24.sp, color = onSurfaceColor)
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        SmallActionButton(icon = Icons.Outlined.VolumeUp, iconColor = onSurfaceColor) { speakWord(word.term) }
                                        SmallActionButton(
                                            icon = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                            iconColor = if (isSaved) primaryColor else onSurfaceColor
                                        ) {
                                            if (isSaved) {
                                                viewModel.removeSavedWord(word)
                                            } else {
                                                viewModel.saveWord(word)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Text(text = "$selectedSubject Terms (${filteredWords.size})", modifier = Modifier.padding(start = 22.dp, top = 6.dp, bottom = 8.dp), fontWeight = FontWeight.Medium, fontSize = 18.sp, color = onSurfaceColor)
                }

                items(filteredWords) { word ->
                    val isSaved = savedWordIds.contains(word.id)
                    WordCard(
                        word = word,
                        isSaved = isSaved,
                        onSpeak = { speakWord(it) },
                        onToggleSave = { if (isSaved) viewModel.removeSavedWord(word) else viewModel.saveWord(word) }
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun WordCard(
    word: com.example.nallanudi.models.Word,
    isSaved: Boolean,
    onSpeak: (String) -> Unit,
    onToggleSave: (com.example.nallanudi.models.Word) -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Surface(color = primaryContainer, shape = RoundedCornerShape(10.dp), shadowElevation = 3.dp) {
                Text(text = word.subject, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, color = onPrimaryContainer)
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = word.term, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 24.sp, color = primaryColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = word.kannadaMeaning, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = onSurfaceColor)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = word.explanation, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 15.sp, lineHeight = 22.sp, color = onSurfaceColor.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Example Sentence", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = primaryColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = word.exampleSentence, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 14.sp, lineHeight = 22.sp, color = onSurfaceColor)
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SmallActionButton(icon = Icons.Outlined.VolumeUp, iconColor = onSurfaceColor) { onSpeak(word.term) }
                SmallActionButton(
                    icon = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    iconColor = if (isSaved) primaryColor else onSurfaceColor
                ) {
                    onToggleSave(word)
                }
            }
        }
    }
}

@Composable
fun SubjectChip(title: String, selected: Boolean, onClick: () -> Unit) {
    val lavender = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.surface
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = surfaceColor,
        shadowElevation = if (selected) 6.dp else 3.dp,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) lavender else Color.Transparent)
    ) {
        Text(text = title, modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp), fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun SmallActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
    ) {
        Icon(icon, contentDescription = null, tint = iconColor)
    }
}