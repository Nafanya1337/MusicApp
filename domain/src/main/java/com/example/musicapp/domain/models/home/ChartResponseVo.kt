package com.example.musicapp.domain.models.home

import com.example.musicapp.domain.models.tracks.TrackVO

data class ChartResponseVO(
    val track: TrackResponseVO
)

data class TrackResponseVO(
    val data: List<TrackVO>
)