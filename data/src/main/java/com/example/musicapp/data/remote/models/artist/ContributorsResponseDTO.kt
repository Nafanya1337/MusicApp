package com.example.musicapp.data.remote.models.artist

import com.example.musicapp.data.remote.models.tracks.ContributorsDTO
import com.google.gson.annotations.SerializedName

data class ContributorsResponseDTO(
    @SerializedName("contributors")
    val contributors: List<ContributorsDTO>
)
