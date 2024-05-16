package com.example.musicapp

import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.musicapp.data.remote.models.ContributorsDTO
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.domain.models.ContributorsVO
import com.example.musicapp.domain.models.CurrentTrackVO
import com.example.musicapp.domain.models.TrackListVO
import com.example.musicapp.presentation.MainActivityViewModel
import com.example.musicapp.presentation.home.HomeFragmentDirections
import com.example.musicapp.service.MusicService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val sessionToken by lazy {
        SessionToken(
            applicationContext,
            ComponentName(applicationContext, MusicService::class.java)
        )
    }

    private var progressBarUpdateJob: Job? = null

    private val controllerFuture by lazy {
        MediaController.Builder(applicationContext, sessionToken).buildAsync()
    }

    private val mainActivityViewModel: MainActivityViewModel by viewModels { MainActivityViewModel.Factory }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragmentContainer)
        val navView = binding.miniPlayerLayout.bottomNavigationMenu
        NavigationUI.setupWithNavController(navView, navController)

        binding.miniPlayerLayout.bottomNavigationMenu.setBackgroundColor(
            resources.getColor(
                R.color.player_background_color,
                null
            )
        )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id != R.id.accountFragment) {
                // Если текущий destination не является фрагментом артиста, вы можете удалять или заменять фрагмент
                // Удаление фрагмента из стека навигации
//                controller.popBackStack(R.id.accountFragment, true)
            }
        }

        binding.miniPlayerLayout.currentTrackTrackName.isSelected = true


        mainActivityViewModel.nextButtonEnabled.observe(this) { isEnabled ->
            binding.miniPlayerLayout.buttonNext.isEnabled = isEnabled
        }

        mainActivityViewModel.prevButtonEnabled.observe(this) { isEnabled ->
            binding.miniPlayerLayout.buttonPrevious.isEnabled = isEnabled
        }

        binding.miniPlayerLayout.buttonNext.setOnClickListener {
            mainActivityViewModel.nextPosition()
        }

        binding.miniPlayerLayout.buttonPrevious.setOnClickListener {
            mainActivityViewModel.prevPosition()
        }

        binding.miniPlayerLayout.currentTrackArtistName.setOnClickListener {
            val artists: List<ContributorsVO> = mainActivityViewModel.track.value!!.contributors
            val bottomSheetFragment: ArtistsBottomSheetFragment = ArtistsBottomSheetFragment.newInstance(artists)
            supportFragmentManager.setFragmentResultListener(CONTRIBUTORS_CHOOSE_KEY, this) { key, bundle ->
                val contributor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(CONTRIBUTORS_REQUEST_KEY, ContributorsDTO::class.java)
                } else {
                    ContributorsDTO(27,"Daft Punk", "https://e-cdns-images.dzcdn.net/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/500x500-000000-80-0-0.jpg")
                }
                contributor?.let { it1 -> openArtistProfile(it1) }
                binding.miniPlayerLayout.root.transitionToStart()
            }
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        mainActivityViewModel.track.observe(this) { track ->
            if (track != null) changeTrack(track)

            binding.miniPlayerLayout.currentTrackTrackName.isSelected =
                binding.miniPlayerLayout.currentTrackTrackName.width >= 140

            binding.miniPlayerLayout.currentTrackExplicitContentIcon.visibility =
                if (track?.explicitLyrics == true)
                    View.VISIBLE
                else
                    View.GONE
        }

        mainActivityViewModel.isPlaying.observe(this) { isPlaying ->
            binding.miniPlayerLayout.currentTrackPlayPauseIcon.isSelected = isPlaying
            val controller = controllerFuture.get() as? MediaController
            if (controller != null) {
                if (isPlaying)
                    controller.play()
                else
                    controller.pause()
            }
        }

        mainActivityViewModel.currentPlaylist.observe(this) { trackList ->
            val title = mainActivityViewModel.currentPlaylist.value?.title
            binding.miniPlayerLayout.playlistName.text = title
        }

        mainActivityViewModel.currentPosition.observe(this) { pos ->
            val list = mainActivityViewModel.currentPlaylist.value?.list
            list?.let {
                val track = list[pos]
                CoroutineScope(Dispatchers.IO).launch {
                    mainActivityViewModel.playTrack(track.id)
                    val mediaItem = MediaItem.Builder()
                        .setMediaId("media-1")
                        .setUri(track.preview.toUri())
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setArtist(track.artist.name)
                                .setTitle(track.title)
                                .setArtworkUri(track.album?.picture?.toUri())
                                .build()
                        ).build()

                    // Get the MediaController instance from the future
                    val controller = controllerFuture.get() as? MediaController

                    // Switch to the main thread to interact with MediaController
                    withContext(Dispatchers.Main) {
                        controller?.let {
                            it.setMediaItem(mediaItem)
                            it.prepare()
                            it.play()

                        }
                    }
                }
            }
        }
    }

    fun startNewTrackList(trackList: TrackListVO, position: Int = 0) {
        startUpdatingProgressBar()
        mainActivityViewModel.setCurrentTrackList(trackList = trackList)
        mainActivityViewModel.setCurrentPosition(position)
    }


    override fun onStart() {
        super.onStart()
        binding.miniPlayerLayout.currentTrackPlayPauseIcon.setOnClickListener {
            mainActivityViewModel.playPause()
        }
    }

    override fun onStop() {
        super.onStop()
        MediaController.releaseFuture(controllerFuture)
        progressBarUpdateJob?.cancel()

    }

    override fun onResume() {
        super.onResume()
        initPlayer()
    }

    private fun initPlayer() {
        controllerFuture.addListener({
            val controller = controllerFuture.get() as? MediaController
            controller?.let {
                it.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            mainActivityViewModel.nextPosition()
                        }
                    }
                })
            }
        }, MoreExecutors.directExecutor())
    }

    private fun changeTrack(currentTrackVO: CurrentTrackVO) {
        binding.miniPlayerLayout.currentTrackTrackName.text = currentTrackVO.title

        binding.miniPlayerLayout.currentTrackArtistName.text =
            currentTrackVO.contributors.joinToString(separator = ", ") { it.name }

        Log.d("mymy", binding.miniPlayerLayout.currentTrackExplicitContentIcon.visibility.toString())
        Glide
            .with(binding.miniPlayerLayout.root)
            .load(currentTrackVO.album.picture)
            .override(300) // Загрузка изображения в оригинальном размере
            .dontTransform() // Отключение трансформаций
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Кэширование оригинального изображения
            .transform(RoundedCornersTransformation(20))
            .into(binding.miniPlayerLayout.currentTrackTrackImage)


    }

    private fun startUpdatingProgressBar() {
        progressBarUpdateJob?.cancel()
        progressBarUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            val controller = controllerFuture.get() as? MediaController
            while (isActive) {
                val currentPosition = controller?.currentPosition ?: 0
                val duration = controller?.duration ?: 1
                val progress = if (duration > 0) (currentPosition * 100 / duration).toInt() else 0
                binding.miniPlayerLayout.trackProgressBar.progress =
                    progress
                binding.miniPlayerLayout.fullscreenCurrentPosition.text =
                    formatTime(currentPosition)
                delay(1000)
            }
        }
    }

    fun openArtistProfile(contributor: ContributorsDTO) {
        val navController = findNavController(R.id.fragmentContainer)
        val action = R.id.action_accountFragment_self
        val bundle = bundleOf("id" to contributor.id)
        navController.navigate(action, bundle)
    }

    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 1000) / 60
        return String.format("%d:%02d", minutes, seconds)
    }
}

