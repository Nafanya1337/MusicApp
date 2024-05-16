package com.example.musicapp.domain.usecase.search

import com.example.musicapp.domain.repository.SearchRequestRepository

class ClearSearchHistoryUseCase(private val searchRequestRepository: SearchRequestRepository) {
    fun execute(): Boolean = searchRequestRepository.clearSearchHistory()
}