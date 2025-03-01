package com.example.musicapp.modules

import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.service.AudioPlayerService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val ManagersModule = module {
    single<PlayerManager> {
        PlayerManager(
            context = androidContext(),
            serviceClass = get<Class<AudioPlayerService>>()
        )
    }
}