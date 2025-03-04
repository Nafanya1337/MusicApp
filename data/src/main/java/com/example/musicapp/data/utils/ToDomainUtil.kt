package com.example.musicapp.data.utils

import com.example.musicapp.data.remote.models.tracks.AlbumInfoDTO
import com.example.musicapp.data.remote.models.tracks.ContributorsDTO
import com.example.musicapp.data.remote.models.tracks.CurrentTrackDTO
import com.example.musicapp.data.remote.models.tracks.TrackDTO
import com.example.musicapp.data.remote.models.tracks.TrackListDTO
import com.example.musicapp.data.remote.models.artist.AlbumDTO
import com.example.musicapp.data.remote.models.artist.ArtistDTO
import com.example.musicapp.data.remote.models.home.RadioDTO
import com.example.musicapp.data.remote.models.home.RadioResponseDTO
import com.example.musicapp.data.remote.models.search.ChartResponseDTO
import com.example.musicapp.data.remote.models.search.SearchResponseDTO
import com.example.musicapp.data.remote.models.search.StatusClassDTO
import com.example.musicapp.data.remote.models.search.TrackResponseDTO
import com.example.musicapp.domain.models.tracks.AlbumInfoVO
import com.example.musicapp.domain.models.tracks.ContributorsVO
import com.example.musicapp.domain.models.tracks.CurrentTrackVO
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.models.artist.AlbumVO
import com.example.musicapp.domain.models.artist.ArtistVO
import com.example.musicapp.domain.models.home.ChartResponseVO
import com.example.musicapp.domain.models.home.RadioResponseVO
import com.example.musicapp.domain.models.home.RadioVO
import com.example.musicapp.domain.models.home.TrackResponseVO
import com.example.musicapp.domain.models.search.SearchResponseVO
import com.example.musicapp.domain.models.search.StatusClassVO

object ToDomainUtil {

    internal fun ChartResponseDTO.toDomain(): ChartResponseVO {
        return ChartResponseVO(
            track = this.tracks.toDomain()
        )
    }

    internal fun TrackResponseDTO.toDomain(): TrackResponseVO {
        return TrackResponseVO(
            data = this.data.map { it.toDomain() }
        )
    }

    fun ContributorsDTO.toDomain(): ContributorsVO {
        return ContributorsVO(
            id = this.id,
            name = this.name,
            picture = this.picture
        )
    }

    internal fun TrackDTO.toDomain(): TrackVO {
        return TrackVO(
            id = this.id,
            title = this.title,
            explicitLyrics = this.explicitLyrics,
            preview = this.preview,
            album = this.album?.toDomain(),
            artist = this.artist.toDomain(),
            position = this.position,
            duration = this.duration
        )
    }

    internal fun CurrentTrackDTO.toDomain(): CurrentTrackVO {
        return CurrentTrackVO(
            id = this.id,
            title = this.title,
            link = this.link,
            share = this.share,
            duration = this.duration,
            releaseDate = this.releaseDate,
            explicitLyrics = this.explicitLyrics,
            preview = this.preview,
            contributors = this.contributors.map { it.toDomain() },
            album = this.album.toDomain(),
        )
    }

    internal fun AlbumInfoDTO.toDomain(): AlbumInfoVO {
        return AlbumInfoVO(
            id = this.id,
            title = this.title,
            picture = this.picture ?: ""
        )
    }

    internal fun RadioResponseDTO.toDomain(): RadioResponseVO {
        return RadioResponseVO(
            data = this.data.map { it.toDomain() }
        )
    }

    internal fun RadioDTO.toDomain(): RadioVO {
        return RadioVO(
            id = this.id,
            title = this.title,
            picture = this.picture,
            trackList = this.trackList,
            type = this.type,
            contributor = this.constributor?.map { it.toDomain() }
        )
    }

    fun ContributorsVO.toData(): ContributorsDTO {
        return ContributorsDTO(
            id = this.id,
            name = this.name,
            picture = this.picture
        )
    }

    fun RadioVO.toData(): RadioDTO {
        return RadioDTO(
            id = this.id,
            title = this.title,
            picture = this.picture,
            trackList = this.trackList,
            type = this.type,
            constributor = this.contributor?.map { it.toData() }
        )
    }

    internal fun SearchResponseDTO.toDomain(): SearchResponseVO {
        return SearchResponseVO(
            list = this.list.map { it.toDomain() },
            status = this.status.toDomain()
        )
    }

    internal fun TrackListDTO.toDomain(): TrackListVO {
        return TrackListVO(
            title = this.title,
            list = this.list.map { it.toDomain() }
        )
    }

    internal fun StatusClassDTO.toDomain(): StatusClassVO {
        return when (this) {
            StatusClassDTO.NO_TRACKS_FAIL -> StatusClassVO.NO_TRACKS_FAIL
            StatusClassDTO.SUCCESS -> StatusClassVO.SUCCESS
            StatusClassDTO.INTERNET_FAIL -> StatusClassVO.INTERNET_FAIL
            StatusClassDTO.NO_SEARCH -> StatusClassVO.NO_SEARCH
            StatusClassDTO.ERROR -> StatusClassVO.ERROR
        }
    }

    internal fun ArtistDTO.toDomain(): ArtistVO {
        return ArtistVO(
            id = this.id,
            name = this.name,
            share = this.share,
            picture = this.picture ?: "",
            fans = this.fans
        )
    }

    fun AlbumDTO.toDomain(): AlbumVO {
        return AlbumVO(
            id = this.id,
            title = this.title,
            cover = this.cover,
            genre = this.genre,
            releaseDate = this.releaseDate,
            recordType = this.recordType,
            explicitLyrics = this.explicitLyrics,
            tracklist = this.tracklist,
            contributors = this.contributors ?: emptyList()
        )
    }
}