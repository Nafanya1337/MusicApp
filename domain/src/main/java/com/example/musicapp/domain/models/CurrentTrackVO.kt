package com.example.musicapp.domain.models

import kotlin.time.Duration

data class CurrentTrackVO(
    val id: Long,
    val title: String,
    val link: String,
    val share: String,
    val duration: Duration,
    val releaseDate: String,
    val explicitLyrics: Boolean,
    val preview: String,
    val contributors: List<ContributorsVO>,
    val album: AlbumInfoVO
)

data class AlbumInfoVO(
    val id: Long,
    val title: String,
    val picture: String,
    val pictureBig: String?
)

data class ContributorsVO(
    val id: Long,
    val name: String,
    val picture: String
)