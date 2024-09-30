package com.inventory.videospliterapp.mvvm

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventory.videospliterapp.modal.VideoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    var showDialog by mutableStateOf(false)





    private val _videos = mutableStateOf<List<VideoModel>>(emptyList())
    val videos: State<List<VideoModel>> = _videos


    fun loadVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val videoList = galleryRepository.getAllVideos()
            _videos.value = videoList
        }
    }

    fun loadVideosFromFolder(context: Context, folderPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val videoList = galleryRepository.getVideosFromFolder(context,folderPath)
            _videos.value = videoList
        }
    }
}
