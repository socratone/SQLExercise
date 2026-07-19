package io.github.socratone.sqlexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.socratone.sqlexercise.ui.SQLExerciseApp
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SQLExerciseTheme {
                SQLExerciseApp()
            }
        }
    }
}
