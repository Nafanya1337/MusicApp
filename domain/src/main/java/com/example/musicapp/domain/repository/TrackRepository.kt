package com.example.musicapp.domain.repository

import com.example.musicapp.domain.models.home.ChartResponseVO
import com.example.musicapp.domain.models.CurrentTrackVO

interface TrackRepository {

    suspend fun streamTrack(id: Long): CurrentTrackVO?

    fun downloadTrack(url: String)

    fun addToFavourites(id: Long)

    fun deleteTrack(id: Long)

    fun deleteFromFavourite(id: Long)

    fun getTrackInfo(id: Long)

    suspend fun getCurrent(): CurrentTrackVO?

    suspend fun getNext(): CurrentTrackVO?

    suspend fun getPrevious(): CurrentTrackVO?
}