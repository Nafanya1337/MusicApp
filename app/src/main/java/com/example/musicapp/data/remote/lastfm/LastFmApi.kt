package com.example.musicapp.data.remote.lastfm

import com.example.musicapp.data.remote.common.TrackItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LastFmApi {

    @GET("/2.0/?method=track.search&api_key=ecbe3c53ddcc769393a00e321a7de0f1&format=json")
    fun trackSearch(@Query("track") name: String): Single<SearchResponse>

}