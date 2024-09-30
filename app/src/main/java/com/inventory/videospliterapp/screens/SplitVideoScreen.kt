package com.inventory.videospliterapp.screens

import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventory.videospliterapp.component.VideoList
import com.inventory.videospliterapp.mvvm.VideoViewModel

@Composable
fun SplitVideoScreen(navController: NavController)
{
    val videoViewModel: VideoViewModel = hiltViewModel()
    val context = LocalContext.current
    val outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/VideoSplitterApp")
    Column(modifier = Modifier.padding(top = 15.dp)) {
        Image(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back Arrow",
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    navController.popBackStack()
                }
        )

        Log.d("SplitVideoScreen", "uri path = $outputDir ")

         videoViewModel.loadVideosFromFolder(context, outputDir.toString())

        Log.d("SplitVideoScreen", "SplitVideoScreen: list size = ${videoViewModel.videos.value.size} ")

        VideoList(videoViewModel.videos.value,navController)

    }

}


