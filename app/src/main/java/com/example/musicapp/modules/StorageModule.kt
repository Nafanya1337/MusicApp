package com.example.musicapp.modules


import FirebaseStorageImpl
import com.example.musicapp.data.storage.interfaces.FirebaseStorage
import com.example.musicapp.data.storage.interfaces.SearchRequestStorage
import com.example.musicapp.data.storage.sharedprefs.SearchRequestSharedPrefsStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val StorageModule = module {

    single<FirebaseStorage> {
        FirebaseStorageImpl()
    }

    single<SearchRequestStorage> {
        SearchRequestSharedPrefsStorage(context = androidContext())
    }

}