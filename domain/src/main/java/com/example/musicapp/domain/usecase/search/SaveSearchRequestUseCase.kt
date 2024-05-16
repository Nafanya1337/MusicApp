package com.example.musicapp.domain.usecase.search

import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.repository.SearchRequestRepository

class SaveSearchRequestUseCase(private val searchRequestRepository: SearchRequestRepository) {
    fun execute(searchRequest: SearchRequestVO): Boolean =
        searchRequestRepository.saveSearchRequest(searchRequest)
}