package com.inventory.videospliterapp.modal

data class VideoModel(
    val id: Long,
    val name: String,
    val duration: Long,
    val size: Long,
    val data: String // Path to the video file
)