package com.example.musicapp.domain.models.artist

import com.example.musicapp.domain.models.ContributorsVO
import java.util.Date

data class AlbumVO(
    val id: Long,
    val title: String,
    val cover: String,
    val genre: Int,
    val releaseDate: Date,
    val recordType: String,
    val tracklist: String,
    val explicitLyrics: Boolean,
    var contributors: List<ContributorsVO>
)
