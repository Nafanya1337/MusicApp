package com.example.musicapp.data.storage.sharedprefs

import android.content.Context
import android.util.Log
import com.example.musicapp.data.storage.interfaces.SearchRequestStorage
import com.example.musicapp.data.storage.models.SearchRequest


private val SEARCH_PREFERENCES = "search"
private val SEARCH_HISTORY_KEY = "search_history"

class SearchRequestSharedPrefsStorage(context: Context) : SearchRequestStorage {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SEARCH_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun save(request: SearchRequest): Boolean {
        return try {
            val set: MutableSet<String> = get().toMutableSet()
            set.add(request.searchText)
            sharedPreferences.edit().putStringSet(
                SEARCH_HISTORY_KEY, set
            ).apply()
            true
        } catch (e: Exception) {
            Log.e("MusicAppException", e.message.toString())
            false
        }
    }

    override fun get(): Set<String> =
        sharedPreferences.getStringSet(SEARCH_HISTORY_KEY, mutableSetOf()) ?: mutableSetOf()

    override fun clear(): Boolean {
        return try {
            sharedPreferences.edit().clear().apply()
            true
        } catch (e: Exception) {
            Log.e("MusicAppException", e.message.toString())
            false
        }
    }
}