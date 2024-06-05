package com.example.musicapp.data.repository.playlist

import android.util.Log
import com.example.musicapp.data.remote.interfaces.MusicApi
import com.example.musicapp.data.remote.models.tracks.TrackDTO
import com.example.musicapp.data.utils.ToDomainUtil.toDomain
import com.example.musicapp.domain.models.home.ChartResponseVO
import com.example.musicapp.domain.models.home.RadioResponseVO
import com.example.musicapp.domain.models.home.TrackResponseVO
import com.example.musicapp.domain.models.tracks.PlaylistType
import com.example.musicapp.domain.models.tracks.PlaylistType.ALBUM
import com.example.musicapp.domain.models.tracks.PlaylistType.ALL_ARTIST_TRACKS
import com.example.musicapp.domain.models.tracks.PlaylistType.RADIO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val musicApi: MusicApi
) : PlaylistRepository {
    override suspend fun getChart(): ChartResponseVO {
        return withContext(Dispatchers.IO) {
            try {
                musicApi.chart().body()!!.toDomain()
            } catch (e: Exception) {
                ChartResponseVO(TrackResponseVO(emptyList()))
            }
        }
    }

    override suspend fun getRadio(): RadioResponseVO {
        return withContext(Dispatchers.IO) {
            try {
                musicApi.radio().body()!!.toDomain()
            } catch (e: Exception) {
                Log.e("boobs", e.message.toString())
                RadioResponseVO(emptyList())
            }
        }
    }

    override suspend fun getTracklist(id: Long, playlistType: PlaylistType): List<TrackVO> {
        var data: List<TrackDTO> = emptyList()
        withContext(Dispatchers.IO) {
            try {
                val response =
                    when (playlistType) {
                        RADIO -> musicApi.getRadioTracks(id = id)
                        ALBUM -> musicApi.getArtistAlbumTracks(id = id)
                        ALL_ARTIST_TRACKS -> musicApi.getArtistTopSongs(id = id, limit = 100)
                    }
                data = response.body()?.list?: emptyList()
            } catch (e: Exception) {

            }
        }
        return data.map { it.toDomain() }
    }
}