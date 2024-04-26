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
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.musicapp.LastFmApp
import com.example.musicapp.SearchHistoryAdapter
import com.example.musicapp.TrackAdapter
import com.example.musicapp.clearSearchHistory
import com.example.musicapp.data.remote.common.TrackItem
import com.example.musicapp.databinding.FragmentSearchBinding
import com.example.musicapp.getSearchHistory
import com.example.musicapp.saveSearchRequest
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
        val trackAdapter = TrackAdapter(emptyList())
        var searchadapter =
            SearchHistoryAdapter(
                requireContext().getSearchHistory()?.toList() ?: listOf()
            )
        val searchViewModel = ViewModelProvider(this).get(SearchResponseViewModel::class.java)
        val lastFmApp = activity?.application as LastFmApp
        val lastFmApi = lastFmApp.lastFmApi
        val spacingInPixels = 10
        binding.recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPixels))
        // Создаем Observable из изменений текста в EditText
        val textChangesObservable = Observable.create<String> { emitter ->
            binding.editText.addTextChangedListener { s ->
                emitter.onNext(s?.toString() ?: "")
                binding.searchPb.visibility = if (s.toString().isNotEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    View.VISIBLE
                } else {
                    searchadapter =
                        SearchHistoryAdapter(
                            requireContext().getSearchHistory()?.toList() ?: listOf()
                        )
                    binding.recyclerView.adapter = searchadapter
                    View.GONE
                }
            }
        }

        // Добавляем задержку с помощью оператора debounce
        val disposable = textChangesObservable
            .debounce(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                // Проверяем, пустое ли поле EditText
                val isTextEmpty = text.isEmpty()

                // Показываем или скрываем значок очистки в зависимости от наличия текста
                binding.closeIcon.visibility = if (isTextEmpty) View.INVISIBLE else View.VISIBLE
                binding.binIcon.visibility = if (isTextEmpty && searchadapter.searchHistory.isNotEmpty()) View.VISIBLE else View.GONE

                // Выполняем поиск только если текст не пустой
                if (!isTextEmpty) {
                    searchViewModel.searchTrack(lastFmApi, text)
                    binding.searchPb.visibility = View.GONE
                    binding.recyclerView.adapter = trackAdapter
                }


            }

        compositeDisposable.add(disposable)


        binding.recyclerView.adapter = searchadapter

        binding.noInternet.button.setOnClickListener {
            searchViewModel.searchTrack(lastFmApi, binding.editText.text?.toString() ?: "")
        }

        searchViewModel.status.observe(viewLifecycleOwner) {
            binding.recyclerView.visibility =
                if (it == SearchResponseViewModel.StatusClass.SUCCESS) {
                    requireContext().saveSearchRequest(binding.editText.text.toString())
                    View.VISIBLE
                } else if (it == SearchResponseViewModel.StatusClass.NO_SEARCH) {
                    binding.recyclerView.adapter = searchadapter
                    View.VISIBLE
                } else View.GONE

            binding.noInternet.root.visibility =
                if (it == SearchResponseViewModel.StatusClass.INTERNET_FAIL) View.VISIBLE
                else View.GONE

            binding.noSearchResults.root.visibility =
                if (it == SearchResponseViewModel.StatusClass.NO_TRACKS_FAIL) View.VISIBLE
                else View.GONE

            Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
        }

        searchViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            val currentAdapter = binding.recyclerView.adapter
            if (currentAdapter is TrackAdapter) {
                currentAdapter.tracks = if (tracks.isEmpty()) emptyList() else tracks
                currentAdapter.notifyDataSetChanged()
            }
        }

        binding.closeIcon.setOnClickListener {
            hideKeyboard()
            binding.editText.clearFocus()
            binding.editText.text.clear()
            val currentAdapter = binding.recyclerView.adapter
            if (currentAdapter is TrackAdapter) {
                val adapter = ((binding.recyclerView.adapter) as TrackAdapter)
                adapter.clearTrackList()
            }
            searchViewModel.clear()
        }

        binding.binIcon.setOnClickListener {
            ((binding.recyclerView.adapter) as SearchHistoryAdapter).clear()
            context?.clearSearchHistory()
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