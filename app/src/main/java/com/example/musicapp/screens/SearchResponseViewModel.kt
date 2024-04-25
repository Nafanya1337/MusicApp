package com.example.musicapp.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.remote.common.TrackItem
import com.example.musicapp.data.remote.lastfm.LastFmApi
import com.example.musicapp.data.remote.lastfm.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SearchResponseViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    val success: MutableLiveData<Boolean> = MutableLiveData()
    val tracks: MutableLiveData<List<TrackItem>> = MutableLiveData()

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
                            success.value = true
                            Log.d("ABOBA", "Data received: ${response}")
                            tracks.value = response?.results?.trackmatches?.track ?: emptyList()
                        },
                        { error ->
                            success.value = false
                            Log.e("ABOBA", "Error: ${error.message}")
                        }
                    )
            )
        } ?: Log.e("ABOBA", "lastFmApi is null")
    }

}