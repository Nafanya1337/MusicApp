package com.example.musicapp.modules

import com.example.musicapp.service.AudioPlayerService
import org.koin.dsl.module

val ServicesModule = module {
    single<Class<AudioPlayerService>> { AudioPlayerService::class.java }
    single<AudioPlayerService> { AudioPlayerService() }
}