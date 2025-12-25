package com.example.memoria.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.memoria.ui.theme.CardBackFill
import com.example.memoria.ui.theme.CardBackFillDark
import com.example.memoria.ui.theme.CardBackStroke
import com.example.memoria.ui.theme.CardBackStrokeDark
import com.example.memoria.ui.theme.Spacing

/**
 * Memory card component with flip animation.
 * Shows card back or front image based on state.
 */
@Composable
fun MemoryCard(
    cardImageResId: Int?,
    isFlipped: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    
    val cardBackFill = if (isDarkTheme) CardBackFillDark else CardBackFill
    val cardBackStroke = if (isDarkTheme) CardBackStrokeDark else CardBackStroke
    
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.md))
            .clickable(
                enabled = !isMatched && !isFlipped,
                onClickLabel = "Flip card"
            ) { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isMatched) 2.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if ((isFlipped || isMatched) && cardImageResId != null) {
                // Show front of card
                Image(
                    painter = painterResource(id = cardImageResId),
                    contentDescription = "Memory card image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Show back of card
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = cardBackFill,
                            shape = RoundedCornerShape(Spacing.md)
                        )
                        .border(
                            width = 2.dp,
                            color = cardBackStroke,
                            shape = RoundedCornerShape(Spacing.md)
                        )
                )
            }
        }
    }
}

