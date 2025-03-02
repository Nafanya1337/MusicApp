package com.example.musicapp.presentation.main

import androidx.media3.common.MediaItem

data class PlayerState(
    val isPLaying: Boolean = false,
    val mediaItem: MediaItem? = null,
    val duration: Long = 29000,
    val queue: List<MediaItem> = emptyList(),
    val isLoading: Boolean = false,
    val hasNext: Boolean = true,
    val hasPrevious: Boolean = true
)