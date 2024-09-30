package com.inventory.videospliterapp.component

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.inventory.videospliterapp.modal.VideoModel
import com.inventory.videospliterapp.navigation.Reader
import com.inventory.videospliterapp.screens.VideoCard

@Composable
fun VideoPlayer(videoUrl: String,screenHeightUser:Float=0.3f,onVideoItemClicked : (String)->Unit={}) {
    val context = LocalContext.current

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp



    val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
    LaunchedEffect(videoUrl) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = false
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }


    AndroidView(
        modifier = Modifier.height(screenHeight*screenHeightUser),
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                hideController()
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                setOnClickListener {

                    Log.d("VideoCard", "This is from VideoPlayer $videoUrl")
                    onVideoItemClicked.invoke(videoUrl)
                    if (isControllerVisible) {
                        hideController()
                    } else {
                        showController()
                    }
                }

            }
        }
    )
}

@Composable
fun BelowTextElement(textString: String? = "Default", isSelected: Boolean, onClick: () -> Unit) {

    var textWidthInPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current.density

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() } // Make the entire column clickable
    ) {
        BasicText(
            text = textString!!,
            modifier = Modifier
                .padding(bottom = 5.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    textWidthInPx = layoutCoordinates.size.width
                },
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .width((textWidthInPx / density).dp)
                    .height(2.dp)
                    .background(Color.Gray)
            )
        }
    }
}


@Composable
fun VideoList(videos: List<VideoModel>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
    ) {

        items(videos, key = { it.id }) { videoItem ->
            VideoCard(video = videoItem) { videoUrl ->

                Log.d("VideoCard", "Video List $videoUrl ")

                val encodedUrl = Uri.encode(videoUrl) // Encode the video URL before navigating
                navController.navigate("${Reader.VideoDetialScreen.name}/$encodedUrl")

            }
        }

    }
}

