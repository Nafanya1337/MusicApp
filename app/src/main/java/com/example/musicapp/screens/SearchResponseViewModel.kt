package com.example.musicapp.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.remote.common.TrackItem
import com.example.musicapp.data.remote.lastfm.LastFmApi
import com.example.musicapp.data.remote.lastfm.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SearchResponseViewModel(application: Application) : AndroidViewModel(application) {

    enum class StatusClass {
        SUCCESS,
        INTERNET_FAIL,
        NO_TRACKS_FAIL,
        NO_SEARCH
    }

    private val compositeDisposable = CompositeDisposable()
    private val _tracks: MutableLiveData<List<TrackItem>> = MutableLiveData(emptyList())
    private val _status: MutableLiveData<StatusClass> = MutableLiveData(StatusClass.NO_SEARCH)

    public val status: LiveData<StatusClass>
        get() = _status

    public val tracks: LiveData<List<TrackItem>>
        get() = _tracks

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun searchTrack(lastFmApi: LastFmApi, name: String) {
        lastFmApi.let {
            compositeDisposable.add(
                it.trackSearch(name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { response ->
                            _status.value =
                                if (response.results.trackmatches.track != emptyList<TrackItem>()) {
                                    StatusClass.SUCCESS
                                } else {
                                    StatusClass.NO_TRACKS_FAIL
                                }
                            _tracks.value = response.results.trackmatches.track
                        },
                        { error ->
                            _status.value = StatusClass.INTERNET_FAIL
                            _tracks.value = emptyList()
                        }
                    )
            )
        }
    }

    fun clear() {
        _status.value = StatusClass.NO_SEARCH
        _tracks.value = emptyList()
    }

}