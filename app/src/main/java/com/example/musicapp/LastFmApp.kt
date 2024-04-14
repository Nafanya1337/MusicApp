package com.example.musicapp

import android.app.Application
import com.example.musicapp.data.remote.lastfm.LastFmApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class LastFmApp: Application() {

    public lateinit var lastFmApi: LastFmApi

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
    }

    private fun configureRetrofit(){
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        lastFmApi = retrofit.create(LastFmApi::class.java)
    }

}