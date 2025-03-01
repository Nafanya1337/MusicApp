package com.example.musicapp.data.repository.artist

import com.example.musicapp.data.remote.interfaces.MusicApi
import com.example.musicapp.data.remote.models.tracks.TrackDTO
import com.example.musicapp.data.utils.AlbumToRadio.toRadioVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.artist.AlbumVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.musicapp.data.utils.ToDomainUtil.toDomain
import com.example.musicapp.domain.models.home.RadioVO

class ArtistRepositoryImpl(
    val musicApi: MusicApi
): ArtistRepository {
    override suspend fun getArtistInfo(id: Long): ArtistVO {
        return withContext(Dispatchers.IO) {
            musicApi.getArtistCard(id = id).body()!!.toDomain()
        }
    }

    override suspend fun getArtistTopTracks(id: Long, limit: Int): List<TrackVO> {
        return withContext(Dispatchers.IO) {
            musicApi.getArtistTopSongs(id = id, limit).body()!!.list.map { it.toDomain() }
        }
    }

    override suspend fun getArtistTracks(id: Long): List<TrackVO> {
        return withContext(Dispatchers.IO) {
            musicApi.getArtistTopSongs(id = id, limit = 5).body()!!.list.map { it.toDomain() }
        }
    }

    override suspend fun getArtistAlbums(id: Long): List<RadioVO> {
        return withContext(Dispatchers.IO) {
            val albums: List<AlbumVO> = musicApi.getArtistAlbums(id = id).body()!!.data.map { it.toDomain() }
            albums.forEach { it.contributors = musicApi.getAlbumContributors(id = it.id).body()!!.contributors.map { it.toDomain() } }
            albums.map { it.toRadioVO() }
        }
    }

    private fun TrackDTO.toDomain(): TrackVO {
        return TrackVO(
            id = this.id,
            title = this.title,
            explicitLyrics = this.explicitLyrics,
            preview = this.preview,
            artist = this.artist.toDomain(),
            album = this.album?.toDomain(),
            position = this.position,
            duration = this.duration
        )
    }
}