package com.example.musicapp.domain.repository

import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.models.search.SearchResponseVO

interface SearchRequestRepository {

    suspend fun searchTracks(request: SearchRequestVO): SearchResponseVO

    fun saveSearchRequest(request: SearchRequestVO): Boolean

    fun getSearchHistory(): List<SearchRequestVO>

    fun clearSearchHistory(): Boolean

}