package com.example.musicapp.data.storage.interfaces

import com.example.musicapp.data.storage.models.SearchRequest

interface SearchRequestStorage {

    fun save(request: SearchRequest): Boolean

    fun get(): Set<String>

    fun clear(): Boolean

}