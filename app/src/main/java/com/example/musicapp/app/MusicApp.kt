package com.example.musicapp.app


import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.models.tracks.Playlistable
import com.example.musicapp.domain.models.login.User
import com.example.musicapp.modules.ApiModule
import com.example.musicapp.modules.RepositoryModule
import com.example.musicapp.modules.StorageModule
import com.example.musicapp.modules.UseCaseModule
import com.example.musicapp.modules.ViewModelModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


const val THEME_PREFERENCES = "theme"
const val DARK_THEME_ENABLED_KEY = "isDarkThemeEnabled"

class MusicApp : Application() {

    companion object {
        var user = MutableLiveData<User?>()
        val userFav = MutableLiveData<MutableList<Playlistable>>()
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MusicApp)
            modules(listOf(ApiModule,
                RepositoryModule,
                StorageModule,
                UseCaseModule,
                ViewModelModule))
        }
        val sharedPreferences = getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        val isDarkThemeEnabled = sharedPreferences.getBoolean(DARK_THEME_ENABLED_KEY, false)

        applyTheme(isDarkThemeEnabled)
        FirebaseApp.initializeApp(this)
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