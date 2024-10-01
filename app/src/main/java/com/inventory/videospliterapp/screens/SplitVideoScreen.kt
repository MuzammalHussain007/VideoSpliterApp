package com.inventory.videospliterapp.screens

import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventory.videospliterapp.component.VideoList
import com.inventory.videospliterapp.mvvm.VideoViewModel
import java.io.File


@Composable
fun SplitVideoScreen(
    navController: NavController,
    buttonClickBehaviour: String?
) {
    var outputDir: File? = null
    val videoViewModel: VideoViewModel = hiltViewModel()
    val context = LocalContext.current
    outputDir = if (buttonClickBehaviour == "Whatsapp") {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/VideoSplitterApp")

    } else {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/VideoSplitterApp/BySize")

    }
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

        Log.d("SplitVideoScreen", "$buttonClickBehaviour")

        videoViewModel.loadVideosFromFolder(context, outputDir.toString())
        VideoList(videoViewModel.videos.value, navController)

    }

}


