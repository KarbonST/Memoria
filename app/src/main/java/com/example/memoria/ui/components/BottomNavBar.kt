package com.example.memoria.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.memoria.R
import com.example.memoria.ui.theme.Spacing

/**
 * Bottom navigation bar component.
 * Replaces the old XML-based navigation bar with Material 3 NavigationBar.
 */
@Composable
fun MemoriaBottomNavBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.button_description_home)
                )
            },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = onHomeClick,
            modifier = Modifier.height(Spacing.minTouchTarget)
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.button_description_profile)
                )
            },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = onProfileClick,
            modifier = Modifier.height(Spacing.minTouchTarget)
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.button_description_settings)
                )
            },
            label = { Text("Settings") },
            selected = currentRoute == "settings",
            onClick = onSettingsClick,
            modifier = Modifier.height(Spacing.minTouchTarget)
        )
    }
}


