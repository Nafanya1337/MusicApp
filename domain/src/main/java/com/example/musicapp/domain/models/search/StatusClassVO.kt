package com.example.musicapp.domain.models.search

enum class StatusClassVO(var msg: String = "") {
    SUCCESS,
    ERROR,
    INTERNET_FAIL,
    NO_TRACKS_FAIL,
    NO_SEARCH
}