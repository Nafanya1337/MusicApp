package com.example.musicapp.screens

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.LastFmApp
import com.example.musicapp.R
import com.example.musicapp.TrackAdapter
import com.example.musicapp.data.remote.common.TrackItem
import com.example.musicapp.databinding.FragmentSearchBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

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

        // Создаем Observable из изменений текста в EditText
        val textChangesObservable = Observable.create<String> { emitter ->
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Отправляем измененный текст в Observable
                    emitter.onNext(s?.toString() ?: "")
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // Добавляем задержку с помощью оператора debounce
        textChangesObservable
            .debounce(5000, TimeUnit.MILLISECONDS) // Задержка в 5 секунд
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                // Приемник обработанных данных
                if (text.isNotEmpty()) {
                    searchViewModel.searchTrack(lastFmApi, text)
                }
            }

        // Настраиваем RecyclerView
        val recyclerView: RecyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

// Создаем адаптер
        val adapter = TrackAdapter(emptyList()) // Передаем пустой список, так как данные будут заполняться позже
        recyclerView.adapter = adapter

        searchViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            adapter.tracks = tracks ?: emptyList()
            adapter.notifyDataSetChanged()
        }

// Запускаем поиск треков при создании фрагмента, если есть сохраненные данные
        searchViewModel.searchTrack(lastFmApi, "")

        binding.closeIcon.setOnClickListener {
            hideKeyboard()
            binding.editText.clearFocus()
            binding.editText.text.clear()
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}