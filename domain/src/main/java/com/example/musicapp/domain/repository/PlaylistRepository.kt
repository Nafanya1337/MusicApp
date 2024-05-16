package com.example.musicapp.domain.repository

import com.example.musicapp.domain.models.PlaylistType
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.models.home.ChartResponseVO
import com.example.musicapp.domain.models.home.RadioResponseVO

interface PlaylistRepository {
    suspend fun getChart(): ChartResponseVO

    suspend fun getRadio(): RadioResponseVO

    suspend fun getTracklist(id: Long, playlistType: PlaylistType): List<TrackVO>
}