package com.example.musicapp.domain.usecase.search

import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.models.search.SearchResponseVO
import com.example.musicapp.domain.repository.SearchRequestRepository

class SearchTracksUseCase(private val searchRequestRepository: SearchRequestRepository) {
    suspend fun execute(searchRequest: SearchRequestVO): SearchResponseVO =
        searchRequestRepository.searchTracks(request = searchRequest)
}