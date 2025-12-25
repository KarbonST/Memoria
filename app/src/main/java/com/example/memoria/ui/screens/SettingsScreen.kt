package com.example.memoria.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.memoria.ui.components.MemoriaBottomNavBar
import com.example.memoria.ui.theme.Spacing
import java.util.*

/**
 * Settings screen with reminder time picker.
 * Replaces SettingsActivity XML layout with Compose.
 */
@Composable
fun SettingsScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onScheduleReminder: (Int, Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var reminderTime by remember { mutableStateOf<String?>(null) }
    
    val showTimePicker: () -> Unit = {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val timeString = String.format("%02d:%02d", selectedHour, selectedMinute)
                reminderTime = timeString
                onScheduleReminder(selectedHour, selectedMinute)
            },
            hour,
            minute,
            android.text.format.DateFormat.is24HourFormat(context)
        ).show()
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MemoriaBottomNavBar(
                currentRoute = "settings",
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick,
                onSettingsClick = { /* Already on settings */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(Spacing.xl))
            
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(Spacing.xxl))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    Text(
                        text = "Напоминания",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "Установите время для ежедневных напоминаний о тренировке памяти",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (reminderTime != null) {
                        Text(
                            text = "Напоминание установлено на: $reminderTime",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(Spacing.sm))
                    
                    Button(
                        onClick = showTimePicker,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Spacing.xxxl),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(Spacing.sm))
                        Text("Установить напоминание")
                    }
                }
            }
        }
    }
}

