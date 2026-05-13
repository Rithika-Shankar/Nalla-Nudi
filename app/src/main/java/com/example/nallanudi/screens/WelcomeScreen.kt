package com.example.nallanudi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onContinueClick: (String, String) -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var selectedAvatar by remember {
        mutableStateOf("Student")
    }

    // THEME COLORS
    val primaryColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val outlineColor = MaterialTheme.colorScheme.outline

    val avatars = listOf(
        AvatarItem(
            label = "Student",
            icon = Icons.Outlined.School
        ),

        AvatarItem(
            label = "Computer",
            icon = Icons.Outlined.Computer
        ),

        AvatarItem(
            label = "Reader",
            icon = Icons.Outlined.AutoStories
        ),

        AvatarItem(
            label = "User",
            icon = Icons.Outlined.Person
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = containerColor
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // TOP HEADER
            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

                colors = CardDefaults.cardColors(
                    containerColor = primaryContainer
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "ನಲ್ಲ ನುಡಿ",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "NALLA NUDI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "An English to Kannada Bridge Dictionary",

                        textAlign = TextAlign.Center,

                        color = onPrimaryContainer.copy(alpha = 0.8f),

                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Welcome 👋",

                fontSize = 30.sp,

                fontWeight = FontWeight.Bold,

                color = onSurfaceColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose your avatar and enter your name",

                textAlign = TextAlign.Center,

                color = onSurfaceColor.copy(alpha = 0.7f),

                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // AVATARS
            Text(
                text = "Choose Avatar",

                fontWeight = FontWeight.SemiBold,

                fontSize = 18.sp,

                color = onSurfaceColor
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                avatars.forEach { avatar ->

                    val isSelected =
                        selectedAvatar == avatar.label

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,

                        modifier = Modifier.clickable {
                            selectedAvatar = avatar.label
                        }
                    ) {

                        Surface(
                            modifier = Modifier
                                .size(78.dp)
                                .shadow(
                                    elevation = if (isSelected) 8.dp else 3.dp,
                                    shape = CircleShape
                                ),

                            shape = CircleShape,

                            color = if (isSelected)
                                primaryColor
                            else
                                surfaceColor,

                            border = androidx.compose.foundation.BorderStroke(
                                width = 2.dp,

                                color = if (isSelected)
                                    primaryColor
                                else
                                    outlineColor.copy(alpha = 0.3f)
                            )
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    avatar.icon,
                                    contentDescription = null,

                                    modifier = Modifier.size(34.dp),

                                    tint = if (isSelected)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        primaryColor
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // NAME FIELD
            OutlinedTextField(
                value = name,

                onValueChange = {
                    name = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(18.dp)
                    ),

                placeholder = {
                    Text("Enter your name")
                },

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,

                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = outlineColor.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(34.dp))

            // BUTTON
            Button(
                onClick = {

                    val finalName =
                        if (name.isBlank())
                            "Learner"
                        else
                            name

                    onContinueClick(
                        finalName,
                        selectedAvatar
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {

                Text(
                    text = "Continue",

                    fontSize = 18.sp,

                    color = MaterialTheme.colorScheme.onPrimary,

                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

data class AvatarItem(
    val label: String,
    val icon: ImageVector
)