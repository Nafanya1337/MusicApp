package com.example.musicapp.domain.models

import com.example.musicapp.domain.models.artist.ArtistVO

data class TrackVO(
    val id: Long,
    val title: String,
    val explicitLyrics: Boolean,
    val preview: String,
    val artist: ArtistVO,
    var album: AlbumInfoVO?,
    val position: Int?
)
