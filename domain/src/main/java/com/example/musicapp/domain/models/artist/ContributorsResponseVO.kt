package com.example.musicapp.domain.models.artist

import com.example.musicapp.domain.models.ContributorsVO

data class ContributorsResponse(
    val contributors: List<ContributorsVO>
)