package com.inventory.videospliterapp.viewModal

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File


class VideoDetailViewModal : ViewModel() {
    private val _chunkCount = MutableStateFlow<Int?>(null)
    val chunkCount: StateFlow<Int?> get() = _chunkCount

    var videoWidth :Int? = null
    var videoHeight :Int? = null
    var time :String?=""

    fun changeVideoResolution(inputFilePath: String, outputFilePath: String, width: Int, height: Int) {
        val uniqueIdForVideo = System.currentTimeMillis()
        val command = "-i $inputFilePath -vf scale=$width:$height ${outputFilePath}/$uniqueIdForVideo.mp4"

        Log.d("VideoSplitBySize", "changeVideoResolution: $command")

        var file = File(outputFilePath)

        if (!file.exists()) {
            file.mkdir()
        }

        // Run FFmpegKit command
        FFmpegKit.executeAsync(command) { session ->
            val returnCode = session.returnCode

            Log.d("VideoSplitBySize", "changeVideoResolution: $returnCode ")
          //  Log.d("VideoSplitBySize", "changeVideoResolution: ${returnCode.isValueSuccess}  ")
            Log.d("VideoSplitBySize", "changeVideoResolution: ${session.allLogsAsString}  ")
            if (returnCode.isValueSuccess) {
                Log.d("VideoSplit", "changeVideoResolution:Success: Video resolution changed to ${width}x${height}")
            } else {
                val logs = session.allLogsAsString
                Log.d("VideoSplitBySize", "${logs} ")
                println("Error: Video resolution change failed")
            }
        }
    }

    fun splitVideo(context: Context, inputFilePath: String, segmentTime: Int) {

        val outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/VideoSplitterApp")

        if (!outputDir.exists())
        {
            outputDir.mkdirs()
        }
        // Ensure output directory exists
        if (outputDir == null || !outputDir.exists()) {
            Log.e("VideoSplitter", "Output directory is null or does not exist!")
            return
        }

        // Delete existing output files
                val existingFiles = outputDir.listFiles { file ->
                    file.name.matches(Regex("output_\\d{3}\\.mp4"))
                } ?: emptyArray()

                existingFiles.forEach { file ->
                    if (file.delete()) {
                        Log.d("VideoSplitter", "Deleted existing file: ${file.name}")
                    } else {
                        Log.e("VideoSplitter", "Failed to delete file: ${file.name}")
                    }
                }

        // Construct output file pattern
        val outputFilePattern = File(outputDir, "output_%03d.mp4").absolutePath

        // Adjusted FFmpeg command for splitting video
        val command = "-i \"$inputFilePath\" -c copy -map 0 -f segment -segment_time $segmentTime -reset_timestamps 1 \"$outputFilePattern\""

        // Log the command for debugging
        Log.d("VideoSplitter", "Executing FFmpeg command: $command")

        // Execute FFmpeg command asynchronously
        FFmpegKit.executeAsync(command) { session: FFmpegSession ->
            val returnCode = session.returnCode

            Log.d("VideoSplitter", "ReturnCode: $returnCode")


            if (returnCode.isValueSuccess) {
                // Log session details
                val logOutput = session.allLogsAsString
                Log.d("VideoSplitter", "FFmpeg session output: $logOutput")

                // Count the number of output chunks
                val numberOfChunks = outputDir.listFiles { file ->
                    file.name.matches(Regex("output_\\d{3}\\.mp4"))
                }?.size ?: 0



                Log.d("VideoSplitter", "Number of chunks created: $numberOfChunks")
                Log.d("VideoSplitter", "OutputFile pattern $outputFilePattern")
                _chunkCount.value=numberOfChunks
            } else {
                // Log the failure details
                val errorLog = session.allLogsAsString
                Log.e("VideoSplitter", "FFmpeg command failed with return code: $returnCode")
                Log.e("VideoSplitter", "FFmpeg error message: $errorLog")
                _chunkCount.value=0
            }
        }
    }
}




