package com.example.musicapp.screens

import android.R
import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.musicapp.LastFmApp
import com.example.musicapp.TrackAdapter
import com.example.musicapp.data.remote.common.TrackItem
import com.example.musicapp.databinding.FragmentSearchBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchViewModel = ViewModelProvider(this).get(SearchResponseViewModel::class.java)
        val lastFmApp = activity?.application as LastFmApp
        val lastFmApi = lastFmApp.lastFmApi
        val spacingInPixels = 10
        binding.recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPixels))
        // Создаем Observable из изменений текста в EditText
        val textChangesObservable = Observable.create<String> { emitter ->
            binding.editText.addTextChangedListener { s -> emitter.onNext(s?.toString() ?: "") }
        }

        // Добавляем задержку с помощью оператора debounce
        val disposable = textChangesObservable
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(1000, TimeUnit.MILLISECONDS) // Задержка в 5 секунд
            .subscribe { text ->
                // Приемник обработанных данных
                if (text.isNotEmpty()) {
                    searchViewModel.searchTrack(lastFmApi, text)
                }
            }

        compositeDisposable.add(disposable)


        binding.recyclerView.adapter = TrackAdapter(emptyList())

        binding.noInternet.button.setOnClickListener {
            searchViewModel.searchTrack(lastFmApi, binding.editText.text?.toString() ?: "")
        }

        searchViewModel.success.observe(viewLifecycleOwner) { success ->
            if (!success) {
                binding.recyclerView.visibility = View.GONE
                binding.noInternet.root.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.noInternet.root.visibility = View.GONE
            }

        }

        searchViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            ((binding.recyclerView.adapter) as TrackAdapter).tracks = if (tracks.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.noSearchResults.root.visibility = View.VISIBLE
                emptyList()
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.noSearchResults.root.visibility = View.GONE
                tracks
            }
            ((binding.recyclerView.adapter) as TrackAdapter).notifyDataSetChanged()
        }

        binding.closeIcon.setOnClickListener {
            hideKeyboard()
            binding.editText.clearFocus()
            binding.editText.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        var view = requireActivity().currentFocus
        view?.let { View(activity) }
        imm?.let { imm -> imm.hideSoftInputFromWindow(view?.windowToken, 0) }
    }

}


class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalSpaceHeight
        outRect.bottom = verticalSpaceHeight
    }
}