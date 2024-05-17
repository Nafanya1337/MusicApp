package com.example.musicapp

import AuthStorageImpl
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.remote.interfaces.MusicApi
import com.example.musicapp.data.repository.PlaylistRepositoryImpl
import com.example.musicapp.data.repository.artist.ArtistRepositoryImpl
import com.example.musicapp.data.repository.login.LoginRepositoryImpl
import com.example.musicapp.data.repository.search.SearchRequestRepositoryImpl
import com.example.musicapp.data.repository.track.TrackRepositoryImpl
import com.example.musicapp.data.storage.sharedprefs.SearchRequestSharedPrefsStorage
import com.example.musicapp.domain.models.login.User
import com.google.firebase.FirebaseApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MusicApp : Application() {

    companion object {
        var user = MutableLiveData<User?>()
    }

    public lateinit var musicApi: MusicApi

    val searchRequestRepository by lazy {
        SearchRequestRepositoryImpl(
            searchRequestStorage = SearchRequestSharedPrefsStorage(this),
            musicApi = musicApi
        )
    }

    val artistRepositoryImpl by lazy {
        ArtistRepositoryImpl(
            musicApi = musicApi
        )
    }

    val trackRepositoryImpl by lazy {
        TrackRepositoryImpl(
            musicApi = musicApi
        )
    }

    val playlistRepositoryImpl by lazy {
        PlaylistRepositoryImpl(
            musicApi = musicApi
        )
    }

    val loginRepositoryImpl by lazy {
        LoginRepositoryImpl(
            authStorage = AuthStorageImpl()
        )
    }

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
//        applyTheme()
        FirebaseApp.initializeApp(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.deezer.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        musicApi = retrofit.create(MusicApi::class.java)
    }

//    fun applyTheme() {
//        val isDarkTheme = this.darkThemeIsChecked()
//        val mode = if (isDarkTheme) {
//            AppCompatDelegate.MODE_NIGHT_YES
//        } else {
//            AppCompatDelegate.MODE_NIGHT_NO
//        }
//        AppCompatDelegate.setDefaultNightMode(mode)
//    }

}