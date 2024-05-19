package com.example.musicapp.data.remote.interfaces

import com.example.musicapp.data.remote.models.search.ChartResponseDTO
import com.example.musicapp.data.remote.models.tracks.CurrentTrackDTO
import com.example.musicapp.data.remote.models.tracks.TrackListDTO
import com.example.musicapp.data.remote.models.artist.AlbumResponse
import com.example.musicapp.data.remote.models.artist.ArtistDTO
import com.example.musicapp.data.remote.models.artist.ContributorsResponseDTO
import com.example.musicapp.data.remote.models.home.RadioResponseDTO
import com.example.musicapp.data.remote.models.search.SearchResponseDTO

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApi {

    @GET("/search")
    suspend fun search(@Query("q") text: String): Response<SearchResponseDTO>

    @GET("/track/{id}")
    suspend fun track(
        @Path("id") id: Long
    ): Response<CurrentTrackDTO>

    @GET("/chart")
    suspend fun chart(): Response<ChartResponseDTO>

    @GET("/radio/top")
    suspend fun radio(): Response<RadioResponseDTO>

    @GET("/radio/{id}/tracks")
    suspend fun getRadioTracks(
        @Path("id") id:Long
    ): Response<TrackListDTO>

    @GET("/artist/{id}")
    suspend fun getArtistCard(
        @Path("id") id:Long
    ): Response<ArtistDTO>

    @GET("/artist/{id}/top")
    suspend fun getArtistTopSongs(
        @Path("id") id: Long,
        @Query("limit") limit: Int
    ): Response<TrackListDTO>

    @GET("/artist/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") id: Long
    ): Response<AlbumResponse>

    @GET("/album/{id}")
    suspend fun getAlbumContributors(
        @Path("id") id: Long
    ): Response<ContributorsResponseDTO>

    @GET("/album/{id}/tracks")
    suspend fun getArtistAlbumTracks(
        @Path("id") id: Long
    ): Response<TrackListDTO>



}