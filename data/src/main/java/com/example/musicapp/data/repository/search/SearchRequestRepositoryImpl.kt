package com.example.musicapp.data.repository.search

import com.example.musicapp.data.remote.interfaces.MusicApi
import com.example.musicapp.data.remote.models.tracks.TrackDTO
import com.example.musicapp.data.remote.models.tracks.TrackListDTO
import com.example.musicapp.data.remote.models.search.StatusClassDTO
import com.example.musicapp.data.storage.interfaces.SearchRequestStorage
import com.example.musicapp.data.utils.ToDomainUtil.toDomain
import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.models.search.SearchResponseVO
import com.example.musicapp.domain.repository.SearchRequestRepository

class SearchRequestRepositoryImpl(
    private val searchRequestStorage: SearchRequestStorage,
    private val musicApi: MusicApi
) :
    SearchRequestRepository {

    override suspend fun searchTracks(request: SearchRequestVO): SearchResponseVO {
        return searchTrack(musicApi, request.searchText)
    }

    override fun saveSearchRequest(request: SearchRequestVO): Boolean =
        searchRequestStorage.save(request = request.toData())

    override fun getSearchHistory(): List<SearchRequestVO> =
        searchRequestStorage.get().map { SearchRequestVO(searchText = it) }

    override fun clearSearchHistory(): Boolean = searchRequestStorage.clear()

    fun SearchRequestVO.toData() =
        com.example.musicapp.data.storage.models.SearchRequest(this.searchText)


    private suspend fun searchTrack(musicApi: MusicApi, name: String): SearchResponseVO {
        var status = StatusClassDTO.NO_TRACKS_FAIL
        var tracks: List<TrackDTO> = emptyList()
        val trackList by lazy {
            TrackListDTO(title = "Search", list = tracks)
        }
        try {
            val response = musicApi.search(text = name)

            if (response.isSuccessful) {
                tracks = response.body()?.list ?: emptyList()
                status =
                    if (tracks.isNotEmpty()) StatusClassDTO.SUCCESS else StatusClassDTO.NO_TRACKS_FAIL
            } else {
                status = StatusClassDTO.NO_TRACKS_FAIL
            }
        } catch (e: Exception) {
            status = when (e) {
                is java.net.UnknownHostException -> StatusClassDTO.INTERNET_FAIL
                else -> StatusClassDTO.ERROR.withError(e.message ?: "An unexpected error has occurred")
            }
        }

        return SearchResponseVO(list = tracks.map { it.toDomain() }, status = status.toDomain())
    }
}