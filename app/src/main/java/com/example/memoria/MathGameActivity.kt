package com.example.memoria

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.memoria.ui.screens.MathGameScreen
import com.example.memoria.ui.theme.MemoriaTheme

/**
 * Math game activity - arithmetic quiz game.
 */
class MathGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MemoriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MathGameScreen(
                        onHomeClick = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.slide_in_center_from_left,
                                R.anim.slide_in_right_from_center
                            )
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
                        },
                        onGameComplete = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

