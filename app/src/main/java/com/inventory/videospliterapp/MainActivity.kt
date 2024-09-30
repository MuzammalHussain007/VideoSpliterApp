package com.inventory.videospliterapp;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inventory.videospliterapp.navigation.VideoNavigation
import com.inventory.videospliterapp.ui.theme.VideoSpliterAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoSpliterAppTheme {
                  VideoNavigation()
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun VideoScreenPreview() {
    VideoSpliterAppTheme {
        // VideoScreen()
    }
}

