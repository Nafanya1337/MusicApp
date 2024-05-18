package com.example.musicapp.modules

import com.example.musicapp.data.remote.interfaces.MusicApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val ApiModule = module {

    factory {
        createApi(retrofit = get())
    }

    single {
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.deezer.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

private fun createApi(retrofit: Retrofit): MusicApi = retrofit.create(MusicApi::class.java)
