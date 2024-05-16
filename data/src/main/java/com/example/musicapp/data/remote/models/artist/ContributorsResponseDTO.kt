package com.example.musicapp.data.remote.models.artist

import com.example.musicapp.data.remote.models.ContributorsDTO
import com.google.gson.annotations.SerializedName

data class ContributorsResponseDTO(
    @SerializedName("contributors")
    val contributors: List<ContributorsDTO>
)
