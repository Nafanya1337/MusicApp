package com.example.musicapp

import android.content.Context

private val SETTINGS_PREFERENCES = "settings"
private val DARK_THEME_KEY = "dark_theme_key"
private val SEARCH_PREFERENCES = "search"
private val SEARCH_HISTORY_KEY = "search_history"

fun Context.saveTheme(isChecked: Boolean) {
    getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE).edit()
        .putBoolean(DARK_THEME_KEY, isChecked).apply()
}

fun Context.darkThemeIsChecked(): Boolean {
    return getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE).getBoolean(
        DARK_THEME_KEY,
        false
    )
}

fun Context.saveSearchRequest(request: String) {
    val set: MutableSet<String> = getSearchHistory()!!.toMutableSet()
    if (set.size == 10)
        set.remove(set.last())
    set.add(request)
    getSharedPreferences(SEARCH_PREFERENCES, Context.MODE_PRIVATE).edit().putStringSet(
        SEARCH_HISTORY_KEY, set
    ).apply()
}

fun Context.getSearchHistory(): Set<String>? {
    return getSharedPreferences(SEARCH_PREFERENCES, Context.MODE_PRIVATE).getStringSet(
        SEARCH_HISTORY_KEY, setOf<String>()
    )
}

fun Context.clearSearchHistory(){
    getSharedPreferences(SEARCH_PREFERENCES, Context.MODE_PRIVATE).edit().clear().apply()
}