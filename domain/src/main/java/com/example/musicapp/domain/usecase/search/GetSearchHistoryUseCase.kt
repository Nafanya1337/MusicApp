package com.example.musicapp.domain.usecase.search

import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.repository.SearchRequestRepository

class GetSearchHistoryUseCase(private val searchRequestRepository: SearchRequestRepository) {
    fun execute(): List<SearchRequestVO> =
        searchRequestRepository.getSearchHistory()

}