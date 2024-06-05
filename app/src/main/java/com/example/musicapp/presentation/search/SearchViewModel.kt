package com.example.musicapp.presentation.search


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.models.search.StatusClassVO
import com.example.musicapp.domain.models.tracks.TrackVO
import com.example.musicapp.domain.usecase.search.ClearSearchHistoryUseCase
import com.example.musicapp.domain.usecase.search.GetSearchHistoryUseCase
import com.example.musicapp.domain.usecase.search.SaveSearchRequestUseCase
import com.example.musicapp.domain.usecase.search.SearchTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchHistoryUseCase: SaveSearchRequestUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase
) :
    ViewModel() {

    val tracks: MutableLiveData<List<TrackVO>> = MutableLiveData(emptyList())
    val history: MutableLiveData<List<SearchRequestVO>> = MutableLiveData(emptyList())
    val status: MutableLiveData<StatusClassVO> = MutableLiveData(StatusClassVO.NO_SEARCH)

    private var searchJob: Job? = null
    fun searchTrack(searchRequest: SearchRequestVO) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)  // задержка перед началом поиска
            try {
                val searchResponseVO = searchTracksUseCase.execute(searchRequest = searchRequest)
                status.postValue(searchResponseVO.status)
                tracks.postValue(searchResponseVO.list)
            } catch (e: Exception) {
                status.postValue(StatusClassVO.INTERNET_FAIL)
                tracks.postValue(emptyList())
            }
        }
    }

    fun getSearchHistory() {
        history.value = getSearchHistoryUseCase.execute()
    }

    fun saveSearchRequest(request: SearchRequestVO) {
        if (saveSearchHistoryUseCase.execute(searchRequest = request)) {
            getSearchHistory()
        }
    }

    fun clearSearchHistory() {
        if (clearSearchHistoryUseCase.execute()) {
            getSearchHistory()
        }
    }

    fun clear() {
        status.value = StatusClassVO.NO_SEARCH
        tracks.value = emptyList()
    }
}