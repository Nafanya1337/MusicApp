package com.example.musicapp.domain.repository

import com.example.musicapp.domain.models.tracks.CurrentTrackVO

interface TrackRepository {

    suspend fun streamTrack(id: Long): CurrentTrackVO?

    fun downloadTrack(url: String)

    fun getTrackInfo(id: Long)

    suspend fun getCurrent(): CurrentTrackVO?
}