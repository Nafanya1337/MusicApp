package com.example.musicapp.data.remote.models.search

enum class StatusClassDTO(var msg: String = "") {
    SUCCESS,
    ERROR,
    INTERNET_FAIL,
    NO_TRACKS_FAIL,
    NO_SEARCH;

    var errorMessage: String = ""

    fun withError(message: String): StatusClassDTO {
        this.errorMessage = message
        return this
    }
}