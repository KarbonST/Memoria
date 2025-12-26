package com.example.memoria

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.memoria.ui.screens.HomeScreen
import com.example.memoria.ui.theme.MemoriaTheme

/**
 * Main activity - entry point of the app.
 * Converted from Java to Kotlin with Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            MemoriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        onMemoryGameClick = {
                            val intent = Intent(this, MemoryGameActivity::class.java)
                            startActivity(intent)
                        },
                        onMathGameClick = {
                            val intent = Intent(this, MathGameActivity::class.java)
                            startActivity(intent)
                        },
                        onProfileClick = {
                            val intent = Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.slide_in_center_from_right,
                                R.anim.slide_in_left_from_center
                            )
                        },
                        onSettingsClick = {
                            val intent = Intent(this, SettingsActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.slide_in_center_from_right,
                                R.anim.slide_in_right_from_center
                            )
                        }
                    )
                }
            }
        }
    }
}

