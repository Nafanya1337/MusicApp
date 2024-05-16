package com.example.musicapp.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.domain.models.search.SearchRequestVO

class SearchHistoryAdapter(
    var searchHistory: List<SearchRequestVO>,
    private val clickableImpl: Clickable
) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {

    interface Clickable {
        fun onItemClick(request: SearchRequestVO)
    }


    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchText: TextView = itemView.findViewById(R.id.search_history_request_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_history_request_card, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.searchText.text = searchHistory[position].searchText
        holder.searchText.isSelected = true
        holder.itemView.setOnClickListener {
            clickableImpl.onItemClick(searchHistory[position])
        }
    }
}