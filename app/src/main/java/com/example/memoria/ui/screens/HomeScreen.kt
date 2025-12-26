package com.example.memoria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.memoria.ui.components.MemoriaBottomNavBar
import com.example.memoria.ui.theme.Spacing

/**
 * Home screen - main entry point with play button.
 * Replaces MainActivity XML layout with Compose.
 */
@Composable
fun HomeScreen(
    onMemoryGameClick: () -> Unit,
    onMathGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MemoriaBottomNavBar(
                currentRoute = "home",
                onHomeClick = { /* Already on home */ },
                onProfileClick = onProfileClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.xl),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.xl)
            ) {
                Text(
                    text = "Memoria",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Тренируйте память каждый день",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(Spacing.xxl))
                
                FilledTonalButton(
                    onClick = onMemoryGameClick,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(Spacing.xxxl),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = "Память",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                FilledTonalButton(
                    onClick = onMathGameClick,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(Spacing.xxxl),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(
                        text = "Арифметика",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}


