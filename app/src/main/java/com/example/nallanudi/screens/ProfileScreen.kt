package com.example.nallanudi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    navController: NavController? = null
) {
    val currentUserName by viewModel.userName.collectAsState()
    val currentUserAvatar by viewModel.userAvatar.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    // User data (initialized from ViewModel)
    var userName by remember(currentUserName) { mutableStateOf(currentUserName) }
    var userAvatar by remember(currentUserAvatar) { mutableStateOf(currentUserAvatar) }
    
    var isEditing by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(userName) }
    var tempAvatar by remember { mutableStateOf(userAvatar) }
    
    var selectedTab by remember { mutableStateOf(3) } // 3 for Profile

    // Use Theme Colors
    val primaryColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer

    val avatars = listOf("Student", "Computer", "Reader", "User")

    fun getAvatarIcon(avatar: String): androidx.compose.ui.graphics.vector.ImageVector {
        return when (avatar) {
            "Student" -> Icons.Outlined.School
            "Computer" -> Icons.Outlined.Computer
            "Reader" -> Icons.Outlined.AutoStories
            else -> Icons.Outlined.Person
        }
    }

    val savedWordsCount = viewModel.savedWords.collectAsState().value.size

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = containerColor,
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Profile",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Manage your account",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = surfaceColor) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController?.navigate("home") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController?.navigate("saved") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = null) },
                    label = { Text("My List") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController?.navigate("flashcard") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Outlined.EditNote, contentDescription = null) },
                    label = { Text("Revise") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { /* Already on Profile */ },
                    icon = { Icon(getAvatarIcon(currentUserAvatar), contentDescription = null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(containerColor),
            contentPadding = PaddingValues(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                // AVATAR
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .shadow(elevation = 8.dp, shape = CircleShape),
                    shape = CircleShape,
                    color = primaryColor
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            getAvatarIcon(userAvatar),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            item {
                // USER NAME SECTION
                if (!isEditing) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = userName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                tempName = userName
                                tempAvatar = userAvatar
                                isEditing = true
                            }
                        ) {
                            Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Profile", color = primaryColor)
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Edit Name", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = onSurfaceColor)
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = tempName,
                                onValueChange = { tempName = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Enter your name") },
                                shape = RoundedCornerShape(14.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Choose Avatar", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = onSurfaceColor)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                avatars.forEach { avatar ->
                                    val isSelected = tempAvatar == avatar
                                    Surface(
                                        modifier = Modifier.size(60.dp).clickable { tempAvatar = avatar },
                                        shape = CircleShape,
                                        color = if (isSelected) primaryColor else surfaceColor,
                                        border = androidx.compose.foundation.BorderStroke(2.dp, if (isSelected) primaryColor else MaterialTheme.colorScheme.outline)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                getAvatarIcon(avatar),
                                                contentDescription = null,
                                                modifier = Modifier.size(28.dp),
                                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else primaryColor
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(onClick = { isEditing = false }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                                    Text("Cancel")
                                }
                                Button(
                                    onClick = {
                                        val finalName = tempName.ifBlank { "Learner" }
                                        viewModel.updateProfile(finalName, tempAvatar)
                                        userName = finalName
                                        userAvatar = tempAvatar
                                        isEditing = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }

            item {
                // APPEARANCE CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Appearance", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (isDarkMode == true) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                                    contentDescription = null,
                                    tint = onSurfaceColor
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Dark Mode", fontSize = 16.sp, color = onSurfaceColor)
                            }
                            Switch(
                                checked = isDarkMode == true,
                                onCheckedChange = { viewModel.toggleDarkMode(it) }
                            )
                        }
                        
                        TextButton(
                            onClick = { viewModel.toggleDarkMode(null) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Use System Theme", color = primaryColor, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                // STATS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Your Stats", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.Bookmark, contentDescription = null, tint = secondaryContainer, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = savedWordsCount.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = onSurfaceColor)
                                Text(text = "Saved Words", fontSize = 12.sp, color = onSurfaceColor.copy(alpha = 0.7f))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Outlined.EditNote, contentDescription = null, tint = secondaryContainer, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = if (savedWordsCount > 0) "Ready" else "Add some", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = onSurfaceColor)
                                Text(text = "Flashcards", fontSize = 12.sp, color = onSurfaceColor.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            item {
                // ABOUT CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "About Nalla Nudi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Version 1.0.0", fontSize = 14.sp, color = onSurfaceColor.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "An English to Kannada bridge dictionary designed for students to learn technical and academic terminology easily. The app supports subject-based learning, saved words, and flashcard revision for better memory retention.",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = onSurfaceColor
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}