package com.example.musicapp.domain.models.search

import com.example.musicapp.domain.models.tracks.TrackVO

data class SearchResponseVO(
    val list: List<TrackVO>,
    val status: StatusClassVO)