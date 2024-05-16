package com.example.musicapp.presentation.search


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicapp.MusicApp
import com.example.musicapp.domain.models.search.SearchRequestVO
import com.example.musicapp.domain.models.search.StatusClassVO
import com.example.musicapp.domain.models.TrackVO
import com.example.musicapp.domain.repository.SearchRequestRepository
import com.example.musicapp.domain.usecase.search.ClearSearchHistoryUseCase
import com.example.musicapp.domain.usecase.search.GetSearchHistoryUseCase
import com.example.musicapp.domain.usecase.search.SaveSearchRequestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(private val searchRequestRepositoryImpl: SearchRequestRepository) :
    ViewModel() {

    private val getSearchHistoryUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetSearchHistoryUseCase(searchRequestRepositoryImpl)
    }
    private val saveSearchHistoryUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveSearchRequestUseCase(
            searchRequestRepositoryImpl
        )
    }
    private val clearSearchHistoryUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ClearSearchHistoryUseCase(
            searchRequestRepositoryImpl
        )
    }

    val tracks: MutableLiveData<List<TrackVO>> = MutableLiveData(emptyList())
    val history: MutableLiveData<List<SearchRequestVO>> = MutableLiveData(emptyList())
    val status: MutableLiveData<StatusClassVO> = MutableLiveData(StatusClassVO.NO_SEARCH)

    private var searchJob: Job? = null
    fun searchTrack(searchRequest: SearchRequestVO) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)  // задержка перед началом поиска
            try {
                val searchResponseVO = searchRequestRepositoryImpl.searchTracks(request = searchRequest)
                status.postValue(searchResponseVO.status)
                tracks.postValue(searchResponseVO.list)
            } catch (e: Exception) {
                Log.e("boobs", e.message.toString())
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val searchRequestRepositoryImpl =
                    (this[APPLICATION_KEY] as MusicApp).searchRequestRepository
                SearchViewModel(
                    searchRequestRepositoryImpl
                )
            }
        }
    }
}