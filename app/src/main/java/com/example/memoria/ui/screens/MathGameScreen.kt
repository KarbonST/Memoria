package com.example.memoria.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.memoria.ui.components.MemoriaBottomNavBar
import com.example.memoria.ui.theme.Spacing
import kotlin.random.Random

/**
 * Math game screen with arithmetic problems.
 * 5 problems total, each with 4 answer choices.
 */
data class MathProblem(
    val firstNumber: Int,
    val secondNumber: Int,
    val operator: String, // "+" or "-"
    val correctAnswer: Int,
    val choices: List<Int>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathGameScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Game state
    val currentProblemIndex = remember { mutableIntStateOf(0) }
    val correctAnswers = remember { mutableIntStateOf(0) }
    val showResultScreen = remember { mutableStateOf(false) }
    val showFeedback = remember { mutableStateOf(false) }
    val isCorrect = remember { mutableStateOf(false) }
    val isAnswerSelected = remember { mutableStateOf(false) }
    
    // Generate 5 problems
    val problems = remember {
        List(5) { generateProblem() }
    }
    
    val currentProblem = problems.getOrNull(currentProblemIndex.intValue)
    
    // Handle answer selection
    fun handleAnswerClick(selectedAnswer: Int) {
        if (isAnswerSelected.value) return
        
        isAnswerSelected.value = true
        val isCorrectAnswer = selectedAnswer == currentProblem?.correctAnswer
        
        if (isCorrectAnswer) {
            correctAnswers.intValue++
            isCorrect.value = true
        } else {
            isCorrect.value = false
        }
        
        showFeedback.value = true
        
        // Move to next problem or show results
        Handler(Looper.getMainLooper()).postDelayed({
            showFeedback.value = false
            isAnswerSelected.value = false
            
            if (currentProblemIndex.intValue < problems.size - 1) {
                currentProblemIndex.intValue++
            } else {
                // All problems completed
                showResultScreen.value = true
                // Return to home after showing results
                Handler(Looper.getMainLooper()).postDelayed({
                    onGameComplete()
                }, 3000)
            }
        }, 1500)
    }
    
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .then(
                    if (showFeedback.value) {
                        Modifier.background(
                            if (isCorrect.value) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            when {
                showResultScreen.value -> {
                    // Results screen
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Игра завершена!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(Spacing.xl))
                        Text(
                            text = "Правильных ответов: ${correctAnswers.intValue} из 5",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                currentProblem != null -> {
                    // Game screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Spacing.xl),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xl)
                    ) {
                        // Problem number
                        Text(
                            text = "Пример ${currentProblemIndex.intValue + 1} из 5",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = Spacing.xl)
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        // Math problem
                        Text(
                            text = "${currentProblem.firstNumber} ${currentProblem.operator} ${currentProblem.secondNumber} = ?",
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        // Answer choices
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.md)
                        ) {
                            currentProblem.choices.forEach { choice ->
                                Button(
                                    onClick = { handleAnswerClick(choice) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Spacing.xxxl),
                                    enabled = !isAnswerSelected.value,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                ) {
                                    Text(
                                        text = choice.toString(),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/**
 * Generate a random math problem with addition or subtraction.
 * Both operands are max 2-digit numbers (1-99).
 */
private fun generateProblem(): MathProblem {
    val operator = if (Random.nextBoolean()) "+" else "-"
    val firstNumber = Random.nextInt(1, 100) // 1 to 99
    val secondNumber = Random.nextInt(1, 100) // 1 to 99
    
    val correctAnswer = when (operator) {
        "+" -> firstNumber + secondNumber
        "-" -> {
            // For subtraction, ensure result is non-negative
            if (firstNumber >= secondNumber) {
                firstNumber - secondNumber
            } else {
                secondNumber - firstNumber
            }
        }
        else -> 0
    }
    
    // Adjust first and second numbers for subtraction if needed
    val (actualFirst, actualSecond) = if (operator == "-" && firstNumber < secondNumber) {
        Pair(secondNumber, firstNumber)
    } else {
        Pair(firstNumber, secondNumber)
    }
    
    // Generate 3 wrong answers (different from correct answer and from each other)
    val wrongAnswers = mutableSetOf<Int>()
    while (wrongAnswers.size < 3) {
        // Generate wrong answer by adding/subtracting a reasonable offset
        val offset = Random.nextInt(1, 21) * if (Random.nextBoolean()) 1 else -1
        val wrong = correctAnswer + offset
        // Ensure wrong answers are different from correct answer, non-negative, and unique
        if (wrong != correctAnswer && wrong >= 0 && !wrongAnswers.contains(wrong)) {
            wrongAnswers.add(wrong)
        }
    }
    
    // Combine correct and wrong answers, then shuffle
    val allChoices = (listOf(correctAnswer) + wrongAnswers.toList()).shuffled()
    
    return MathProblem(
        firstNumber = actualFirst,
        secondNumber = actualSecond,
        operator = operator,
        correctAnswer = correctAnswer,
        choices = allChoices
    )
}

