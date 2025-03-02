package com.example.musicapp.domain.models.tracks

import com.example.musicapp.domain.models.artist.ArtistVO

data class TrackVO(
    val id: Long = 0,
    val title: String = "",
    val explicitLyrics: Boolean = false,
    val preview: String = "",
    val artist: ArtistVO = ArtistVO(),
    var album: AlbumInfoVO? = null,
    val position: Int? = null,
    val duration: Int
) : Playlistable
