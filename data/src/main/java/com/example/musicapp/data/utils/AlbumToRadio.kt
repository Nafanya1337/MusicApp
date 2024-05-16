package com.example.musicapp.data.utils

import com.example.musicapp.domain.models.artist.AlbumVO
import com.example.musicapp.domain.models.home.RadioVO

object AlbumToRadio {
    inline fun AlbumVO.toRadioVO(): RadioVO {
        return RadioVO(
            id = this.id,
            title = this.title,
            picture =  this.cover,
            trackList = this.tracklist,
            type = this.recordType,
            contributor = this.contributors
        )
    }
}