package com.example.musicapp.data.remote.lastfm

import com.example.musicapp.data.remote.common.TrackItem

data class SearchResponse(
    val results: Results
)

data class Results(
    val trackmatches: TrackMatches
)

data class TrackMatches(
    val track: List<TrackItem>
)
