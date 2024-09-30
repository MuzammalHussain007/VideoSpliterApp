package com.inventory.videospliterapp.screens

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventory.videospliterapp.component.VideoList
import com.inventory.videospliterapp.component.VideoPlayer
import com.inventory.videospliterapp.modal.VideoModel
import com.inventory.videospliterapp.mvvm.VideoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun VideoScreen(
    videoViewModel: VideoViewModel = hiltViewModel(),
    navController: NavController
) {
    var hasPermission by remember { mutableStateOf(false) }


    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            videoViewModel.showDialog = false
            Log.d("PermissionKey", "VideoScreen: ")
            videoViewModel.loadVideos()
        }
    }


    LaunchedEffect(Unit) {
        hasPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            videoViewModel.loadVideos()
        } else {
            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_VIDEO)
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
        ) {
            if (!hasPermission) {
                PermissionColume(videoViewModel,permissionLauncher)
            } else if (videoViewModel.videos.value.isEmpty()) {
                LoadingColume()
            } else {
                VideoList(videoViewModel.videos.value, navController)
            }
        }
    }
}

@Composable
fun PermissionColume(
    videoViewModel: VideoViewModel,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val scope = rememberCoroutineScope()
        val activity = LocalContext.current as? Activity


        LaunchedEffect(Unit) {
            scope.launch {
                delay(2000)
            }
            videoViewModel.showDialog = true

        //    showDialog = true
        }
        val context = LocalContext.current

        if (showDialog) {
            CustomDialog(
                permissionLauncher,
                onDismiss = {
                    videoViewModel.showDialog = false
                    activity?.finish()
                },
                onSuccess = {
                    videoViewModel.showDialog = false

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    startActivity(context, intent, null)

                }
            )
        }


    }
}

@Composable
fun LoadingColume() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color.Red,
            strokeWidth = 8.dp,
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun VideoCard(video: VideoModel, onCardClicked: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.Start,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
        ) {
            VideoPlayer(video.data) {
                Log.d("VideoCard", "VideoPlayer: $it ")
                onCardClicked.invoke("$it")

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${video.name}", modifier = Modifier.padding(4.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray, fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}





@Composable
fun CustomDialog(
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    onDismiss: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permission Dialogue",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "This app requires permission for all videos..")

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onSuccess.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text(text = "Confirm", color = Color.White)
                    }
                }
            }
        }
    }
}


