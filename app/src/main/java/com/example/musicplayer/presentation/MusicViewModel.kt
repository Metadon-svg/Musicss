package com.example.musicplayer.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.data.Song
import com.example.musicplayer.data.YouTubeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repo: YouTubeRepo,
    private val player: ExoPlayer
) : ViewModel() {
    var songs = mutableStateOf<List<Song>>(emptyList())
    var current = mutableStateOf<Song?>(null)
    var isPlaying = mutableStateOf(false)
    var query = mutableStateOf("NCS") // Поиск по умолчанию

    init { search() }

    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            songs.value = repo.search(query.value)
        }
    }

    fun play(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            current.value = song
            val url = repo.getStreamUrl(song.id) ?: return@launch
            viewModelScope.launch(Dispatchers.Main) {
                player.setMediaItem(MediaItem.fromUri(url))
                player.prepare()
                player.play()
                isPlaying.value = true
            }
        }
    }

    fun toggle() {
        if (player.isPlaying) { player.pause(); isPlaying.value = false } 
        else { player.play(); isPlaying.value = true }
    }
}