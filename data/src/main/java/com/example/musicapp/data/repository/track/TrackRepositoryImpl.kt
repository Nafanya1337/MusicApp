package com.example.musicapp.data.repository.track

import com.example.musicapp.data.remote.interfaces.MusicApi
import com.example.musicapp.data.utils.ToDomainUtil.toDomain
import com.example.musicapp.domain.models.CurrentTrackVO
import com.example.musicapp.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class TrackRepositoryImpl(
    private val musicApi: MusicApi
) : TrackRepository {

    private var currentId by Delegates.notNull<Long>()

    override suspend fun streamTrack(id: Long): CurrentTrackVO? {
        currentId = id
        return musicApi.track(id = id).body()?.toDomain()
    }

    override fun downloadTrack(url: String) {

    }

    override fun getTrackInfo(id: Long) {

    }

    override suspend fun getCurrent(): CurrentTrackVO? {
        return withContext(Dispatchers.IO) {
            streamTrack(id = currentId)
        }
    }

    override suspend fun getNext(): CurrentTrackVO? {
        currentId += 1
        return withContext(Dispatchers.IO) {
            streamTrack(id = currentId)
        }
    }

    override suspend fun getPrevious(): CurrentTrackVO? {
        currentId -= 1
        return withContext(Dispatchers.IO) {
            streamTrack(id = currentId)
        }
    }
}

