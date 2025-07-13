package com.example.progress_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.progress_tracker.ui.theme.ProgressTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ProgressTrackerTheme {

                Scaffold(
                    topBar = { NavBar() },
                    contentWindowInsets = androidx.compose.foundation.layout.WindowInsets.systemBars,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) { innerPadding ->
                    ProgressApp( modifier = Modifier.padding((innerPadding)))
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(){
    CenterAlignedTopAppBar(
        title = { Text("Habit Tracker.io") }
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProgressTrackerTheme {
    }
}