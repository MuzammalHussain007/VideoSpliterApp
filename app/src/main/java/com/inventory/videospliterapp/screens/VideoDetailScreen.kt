import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.inventory.videospliterapp.component.BelowTextElement
import com.inventory.videospliterapp.component.VideoPlayer
import com.inventory.videospliterapp.modal.ResolutionItem
import com.inventory.videospliterapp.navigation.Reader
import com.inventory.videospliterapp.viewModal.VideoDetailViewModal

val resolutions = listOf(
    ResolutionItem(1, "144p", 256, 144),
    ResolutionItem(2, "240p", 426, 240),
    ResolutionItem(3, "480p", 854, 480),
    ResolutionItem(4, "720p", 1280, 720),
    ResolutionItem(5, "1080p", 1920, 1080),
    ResolutionItem(6, "4K", 3840, 2160)
)


@Composable
fun VideoDetialScreen(
    navController: NavController = NavController(LocalContext.current),
    url: String? = ""
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val viewModal: VideoDetailViewModal = viewModel()

    var selectedText by remember { mutableStateOf("Whatsapp") }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        if (url != null) {
            Column {
                VideoPlayer(videoUrl = url, 0.5f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    FloatingActionButton(onClick = {
                        if (selectedText=="Size")
                        {
                            Log.d("VideoSplitBySize", "Width: ${viewModal.videoWidth} ")
                            Log.d("VideoSplitBySize", "Height: ${viewModal.videoHeight} ")
                            val outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/VideoSplitterApp/BySize")
                            viewModal.changeVideoResolution(url,outputDir.toString(),256,144)

                        }else
                        {
                            if (selectedText == "Whatsapp")
                            {
                                viewModal.time?.let { convertToSeconds(it) }?.let {
                                    viewModal.splitVideo(
                                        context,
                                        url!!,
                                        it
                                    )
                                }
                            }
                            navController.navigate("${Reader.SplitVideoScreen.name}/$selectedText")
                        }



                    }, modifier = Modifier.padding(end = 10.dp, top = 10.dp)) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Add")
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BelowTextElement(
                        textString = "Whatsapp",
                        isSelected = selectedText == "Whatsapp"
                    ) {
                        selectedText = "Whatsapp"
                    }
                    /*Spacer(modifier = Modifier.width(10.dp))
                    BelowTextElement(
                        textString = "Duration",
                        isSelected = selectedText == "Duration"
                    ) {
                        selectedText = "Duration"
                    }*/
                    Spacer(modifier = Modifier.width(10.dp))
                    BelowTextElement(
                        textString = "Size",
                        isSelected = selectedText == "Size"
                    ) {
                        selectedText = "Size"
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))


                when (selectedText) {
                    "Whatsapp" -> {
                        SelectedTextArea(viewModal,"Whatsapp", url)
                    }

                    "Duration" -> {
                        SelectedTextArea(viewModal,"Duration", url)
                    }

                    "Size" -> {
                        SelectedTextArea(viewModal,"Size", url)
                    }
                }

            }
        }

        Image(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back Arrow",
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}


@Composable
fun SelectedTextArea(viewModal: VideoDetailViewModal,textString: String? = "Default", videoURL: String? = "") {

    var videoDuration = getVideoDuration(LocalContext.current, Uri.parse(videoURL))


    val durationInSeconds = videoDuration / 1000

    var selectedCard by remember { mutableStateOf<String?>("10 Second") }


    var clipCount = selectedCard?.let { durationInSeconds.toInt() / convertToSeconds(it) } ?: 0

    val viewModal: VideoDetailViewModal = viewModel()

    val context = LocalContext.current

    LaunchedEffect(videoURL) {
        if (!videoURL.isNullOrEmpty()) {
            if (textString.equals("Whatsapp"))
            {
             //   viewModal.splitVideo(context, videoURL, convertToSeconds(selectedCard.toString()))
            }

        }
    }

    val chunkCountNew by viewModal.chunkCount.collectAsState()

    Log.d("VideoSplitter", "SelectedTextArea: $chunkCountNew")


    // viewModal.splitVideo(LocalContext.current,videoURL!!,10)


    val cardItems =
        listOf("10 Second", "15 Second", "30 Second", "45 Second", "55 Second", "1 Minute")


    Surface(
        modifier = Modifier
            .fillMaxHeight(0.85f)
            .fillMaxWidth()
            .padding(10.dp),
        color = Color.White
    ) {
        when (textString) {
            "Whatsapp" -> {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    chunkCountNew?.let {
                        DynamicText(
                            selectedCard?.let { convertToSeconds(it) }.toString(),
                            it
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))


                    Log.d("VideoDetialScreen", "Video Duration: $durationInSeconds")


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                    ) {

                        items(cardItems) { time ->

                            Log.d("VideoDetialScreen", "SelectedTextArea: $time")
                            RoundCard(
                                noOfSecond = time,
                                isCardSelected = selectedCard == time,
                                isCardClicked = {
                                    selectedCard = it
                                    viewModal.time = it
                                    clipCount = chunkCountNew!!
                                },
                                durationInSeconds.toInt()
                            )
                        }

                    }

                }

            }

            "Size" -> {

                VideoSplitBySize(viewModal)

            }
        }
    }
}

@Composable
fun VideoSplitBySize(viewModal: VideoDetailViewModal) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {

        ResolutionGrid(viewModal,resolutions)

    }
}


@Composable
fun RoundCard(
    noOfSecond: String = "15",
    isCardSelected: Boolean = false,
    isCardClicked: (String) -> Unit = {},
    videoDurationInSeconds: Int? = 0
) {
    val timeInSeconds = convertToSeconds(noOfSecond)

    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(10.dp)
            .border(
                width = 2.dp,
                color = if (isCardSelected) Color.Blue else Color.White
            )
            .clickable(
                enabled = timeInSeconds <= videoDurationInSeconds!!
            ) {

                isCardClicked.invoke(noOfSecond)
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = noOfSecond, fontSize = 15.sp, color = Color.Black)
        }

    }
}

@Composable
fun DynamicText(duration: String, clipCount: Int) {
    Text(
        text = buildAnnotatedString {
            append("Each Clip ")
            withStyle(style = SpanStyle(fontSize = 20.sp, color = Color.Blue)) {
                append("$duration ")
            }
            append("split into ")
            withStyle(style = SpanStyle(fontSize = 20.sp, color = Color.Blue)) {
                append("$clipCount")
            }
            append(" clips")
        },
        fontSize = 14.sp,
        color = Color.Black
    )
}


fun getVideoDuration(context: Context, videoUri: Uri): Long {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, videoUri)
        val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val duration = durationStr?.toLong() ?: 0L
        duration
    } catch (e: Exception) {
        0L
    } finally {
        retriever.release() // Always release the retriever when done
    }
}


fun convertToSeconds(timeString: String): Int {
    return when {
        timeString.contains("Second", true) -> {
            timeString.split(" ")[0].toInt()
        }

        timeString.contains("Minute", true) -> {
            val minutes = timeString.split(" ")[0].toInt()
            minutes * 60
        }

        else -> 0
    }
}


fun getOutputFilePath(context: Context): String {
    val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
    return "$outputDir/output_%03d.mp4"
}

@Composable
fun ResolutionGrid(viewModal: VideoDetailViewModal, resolutions: List<ResolutionItem>) {
    // Initialize the selectedResolution with the first item in the list
    var selectedResolution by remember { mutableStateOf(resolutions.firstOrNull()) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Adjust the number of columns as needed
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(resolutions.size) { index ->
            val resolution = resolutions[index]
            // Make the entire Row clickable
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedResolution = resolution
                    } // Select item when Row (text or button) is clicked
            ) {
                RadioButton(
                    selected = selectedResolution == resolution,
                    onClick = {
                        selectedResolution = resolution

                        viewModal.videoWidth = resolution.width
                        viewModal.videoHeight = resolution.height

                              },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Blue, // Change this to your desired color for the selected state
                        unselectedColor = Color.Gray // Change this for the unselected state
                    )
                )
                Text(
                    text = resolution.resolution,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}








