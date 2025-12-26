package com.example.memoria.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.memoria.R
import com.example.memoria.ui.components.MemoriaBottomNavBar
import com.example.memoria.ui.components.MemoryCard
import com.example.memoria.ui.theme.Spacing
import java.util.*

/**
 * Memory game screen with card matching logic.
 * Replaces MemoryGameActivity XML layout with Compose.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Card image IDs - 4 pairs
    val cardImageIds = remember {
        mutableListOf(
            R.drawable.img0, R.drawable.img0,
            R.drawable.img1, R.drawable.img1,
            R.drawable.img2, R.drawable.img2,
            R.drawable.img3, R.drawable.img3
        ).apply {
            Collections.shuffle(this)
        }
    }
    
    // Game state
    val flippedCards = remember { mutableStateOf(setOf<Int>()) }
    val matchedCards = remember { mutableStateOf(setOf<Int>()) }
    val firstCardIndex = remember { mutableStateOf(-1) }
    val isBusy = remember { mutableStateOf(false) }
    val matchesFound = remember { mutableIntStateOf(0) }
    
    // Handle card click
    fun handleCardClick(index: Int) {
        if (isBusy.value) return
        if (matchedCards.value.contains(index)) return
        if (flippedCards.value.contains(index)) return
        
        flippedCards.value = flippedCards.value + index
        
        if (firstCardIndex.value == -1) {
            // First card clicked
            firstCardIndex.value = index
        } else {
            // Second card clicked
            isBusy.value = true
            val secondIndex = index
            
            if (cardImageIds[firstCardIndex.value] == cardImageIds[secondIndex]) {
                // Match found
                matchedCards.value = matchedCards.value + firstCardIndex.value + secondIndex
                matchesFound.intValue++
                flippedCards.value = flippedCards.value - firstCardIndex.value - secondIndex
                firstCardIndex.value = -1
                isBusy.value = false
                
                // Check if game is complete
                if (matchesFound.intValue == cardImageIds.size / 2) {
                    // Show win message and return to home
                    Handler(Looper.getMainLooper()).postDelayed({
                        onGameComplete()
                    }, 2000)
                }
            } else {
                // No match - flip back after delay
                Handler(Looper.getMainLooper()).postDelayed({
                    flippedCards.value = flippedCards.value - firstCardIndex.value - secondIndex
                    firstCardIndex.value = -1
                    isBusy.value = false
                }, 1000)
            }
        }
    }
    
    val onCardClick: (Int) -> Unit = { index -> handleCardClick(index) }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(
                        onClick = onHomeClick,
                        modifier = Modifier.padding(horizontal = Spacing.xs)
                    ) {
                        Text(
                            text = "Выход",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            MemoriaBottomNavBar(
                currentRoute = "game",
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.lg)
        ) {
            if (matchesFound.intValue == cardImageIds.size / 2) {
                // Game complete
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Вы выиграли!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(cardImageIds) { index, imageResId ->
                        MemoryCard(
                            cardImageResId = if (flippedCards.value.contains(index) || matchedCards.value.contains(index)) imageResId else null,
                            isFlipped = flippedCards.value.contains(index),
                            isMatched = matchedCards.value.contains(index),
                            onClick = { onCardClick(index) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

