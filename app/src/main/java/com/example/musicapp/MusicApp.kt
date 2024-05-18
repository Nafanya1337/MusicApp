package com.example.musicapp

import FirebaseStorageImpl
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.remote.interfaces.MusicApi
import com.example.musicapp.data.repository.PlaylistRepositoryImpl
import com.example.musicapp.data.repository.artist.ArtistRepositoryImpl
import com.example.musicapp.data.repository.login.FirebaseRepositoryImpl
import com.example.musicapp.data.repository.search.SearchRequestRepositoryImpl
import com.example.musicapp.data.repository.track.TrackRepositoryImpl
import com.example.musicapp.data.storage.sharedprefs.SearchRequestSharedPrefsStorage
import com.example.musicapp.domain.models.Playlistable
import com.example.musicapp.domain.models.login.User
import com.google.firebase.FirebaseApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val THEME_PREFERENCES = "theme"
const val DARK_THEME_ENABLED_KEY = "isDarkThemeEnabled"

class MusicApp : Application() {

    companion object {
        var user = MutableLiveData<User?>()
        val userFav = MutableLiveData<MutableList<Playlistable>>()
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

    val firebaseRepositoryImpl by lazy {
        FirebaseRepositoryImpl(
            firebaseStorage = FirebaseStorageImpl()
        )
    }

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        val sharedPreferences = getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        val isDarkThemeEnabled = sharedPreferences.getBoolean(DARK_THEME_ENABLED_KEY, false)

        applyTheme(isDarkThemeEnabled)
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

    fun applyTheme(isDarkThemeEnabled: Boolean) {
        val mode = if (isDarkThemeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}