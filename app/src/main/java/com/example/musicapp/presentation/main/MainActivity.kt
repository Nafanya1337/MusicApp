package com.example.musicapp.presentation.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.app.MusicApp
import com.example.musicapp.data.remote.models.tracks.ContributorsDTO
import com.example.musicapp.data.utils.RoundedCornersTransformation
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.domain.models.tracks.ContributorsVO
import com.example.musicapp.domain.models.tracks.CurrentTrackVO
import com.example.musicapp.domain.models.tracks.TrackListVO
import com.example.musicapp.presentation.artist.ArtistsBottomSheetFragment
import com.example.musicapp.presentation.artist.CONTRIBUTORS_CHOOSE_KEY
import com.example.musicapp.presentation.artist.CONTRIBUTORS_REQUEST_KEY
import com.example.musicapp.service.AudioPlayerService
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainActivityViewModel: MainActivityViewModel by viewModel<MainActivityViewModel>()
    val navController by lazy { findNavController(R.id.container) }


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment

        ViewCompat.setOnApplyWindowInsetsListener(navHostFragment.requireView()) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }

        window.statusBarColor = Color.TRANSPARENT

        setupNavigation()
        setupObservers()

        initUser()
    }

    private fun setupNavigation() {
        NavigationUI.setupWithNavController(
            binding.miniPlayerLayout.bottomNavigationMenu,
            navController
        )
    }

    private fun setupObservers() {
        mainActivityViewModel.user.observe(this) { user ->
            if (user == null) {
                binding.miniPlayerLayout.root.visibility = View.GONE
                navController.navigate(R.id.action_homeFragment_to_auth_nav_graph)
            } else {
                MusicApp.user.value = user
                mainActivityViewModel.initFav()
                initUI()
            }
        }

        mainActivityViewModel.state.observe(this) { state ->
            updatePlayerStateUI(state)
        }

        mainActivityViewModel.playbackPosition.observe(this) { playbackPosition ->
            updatePlayerPlaybackPosition(playbackPosition.toInt())
        }

        mainActivityViewModel.currentTrack.observe(this) { track ->
            if (track != null)
                updatePlayerTrackUI(track)
        }
    }

    private fun updatePlayerStateUI(state: PlayerState) {
        binding.miniPlayerLayout.apply {
            currentTrackPlayPauseIcon.isSelected = state.isPLaying
        }
    }

    private fun updatePlayerTrackUI(track: CurrentTrackVO) {
        binding.miniPlayerLayout.apply {
            currentTrackArtistName.text = track.contributors.joinToString { it.name }
            currentTrackTrackName.text = track.title

            changeVisibilityOfExplicitIcon(track.explicitLyrics.toVisibility())

            Glide.with(this@MainActivity)
                .load(track.album.picture)
                .override(500, 500)
                .transform(RoundedCornersTransformation(20))
                .into(currentTrackTrackImage)

            fullscreenTrackLength.text = totalSecondsToMinutesAndSeconds(track.duration)
            trackProgressBar.max = track.duration
            updatePlayerPlaybackPosition(0)
        }
    }

    private fun changeVisibilityOfExplicitIcon(visibility: Int) {
        val constraintSetEnd = binding.miniPlayerLayout.root.getConstraintSet(R.id.end)
        val constraintSetStart = binding.miniPlayerLayout.root.getConstraintSet(R.id.start)
        constraintSetEnd.setVisibility(R.id.currentTrack_explicit_content_icon, visibility)
        constraintSetStart.setVisibility(R.id.currentTrack_explicit_content_icon, visibility)
        binding.miniPlayerLayout.root.updateState(R.id.end, constraintSetEnd)
        binding.miniPlayerLayout.root.updateState(R.id.start, constraintSetStart)
    }

    private fun totalSecondsToMinutesAndSeconds(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun updatePlayerPlaybackPosition(position: Int) {
        binding.miniPlayerLayout.trackProgressBar.setProgress(position, true)
        binding.miniPlayerLayout.fullscreenCurrentPosition.text = totalSecondsToMinutesAndSeconds(position)
    }

    fun initUser() {
        mainActivityViewModel.initUser()
    }

    private fun initUI() {
        binding.miniPlayerLayout.root.visibility = View.VISIBLE

        binding.miniPlayerLayout.bottomNavigationMenu.setBackgroundColor(
            resources.getColor(
                R.color.player_background_color,
                null
            )
        )


        binding.miniPlayerLayout.currentTrackArtistName.setOnClickListener {
            if (binding.miniPlayerLayout.root.currentState == binding.miniPlayerLayout.root.endState) {
                mainActivityViewModel.currentTrack.value?.let {
                    showArtistsBottomSheetFragment(
                        it.contributors
                    )
                }
            }
        }

        binding.miniPlayerLayout.currentTrackPlayPauseIcon.setOnClickListener {
            mainActivityViewModel.changePlayPauseState()
        }
    }

    private fun showArtistsBottomSheetFragment(artists: List<ContributorsVO>) {
        val bottomSheetFragment: ArtistsBottomSheetFragment =
            ArtistsBottomSheetFragment.newInstance(artists)
        supportFragmentManager.setFragmentResultListener(
            CONTRIBUTORS_CHOOSE_KEY,
            this
        ) { key, bundle ->
            val contributor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(CONTRIBUTORS_REQUEST_KEY, ContributorsDTO::class.java)
            } else {
                ContributorsDTO(
                    27,
                    "Daft Punk",
                    "https://e-cdns-images.dzcdn.net/images/artist/f2bc007e9133c946ac3c3907ddc5d2ea/500x500-000000-80-0-0.jpg"
                )
            }
            contributor?.let { it1 -> openArtistProfile(it1) }
            binding.miniPlayerLayout.root.transitionToStart()
        }
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onStart() {
        super.onStart()
    }

    fun hidePlayer() {
        binding.miniPlayerLayout.root.visibility = View.GONE
    }

    private fun openArtistProfile(contributor: ContributorsDTO) {
        val navController = findNavController(R.id.container)
        val action = R.id.action_accountFragment_self
        val bundle = bundleOf("id" to contributor.id)
        navController.navigate(action, bundle)
    }

    override fun onBackPressed() {
        if (binding.miniPlayerLayout.root.currentState == binding.miniPlayerLayout.root.endState) {
            binding.miniPlayerLayout.root.transitionToStart()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        (mediaController as AudioPlayerService).onDestroy()
        super.onDestroy()
    }

    fun startNewTrackList(trackList: TrackListVO, position: Int) {
        mainActivityViewModel.setPlayList(trackList, position)
    }
}

private fun Boolean.toVisibility(): Int = when(this) {
    true -> View.VISIBLE
    false -> View.GONE
}