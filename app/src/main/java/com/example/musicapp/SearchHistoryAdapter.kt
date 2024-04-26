package com.example.musicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(var searchHistory: List<String>): RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {


    inner class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val searchText: TextView = itemView.findViewById(R.id.search_history_request_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_history_request_card, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.searchText.text = searchHistory[position]
    }

    public fun clear(){
        searchHistory = emptyList()
        notifyDataSetChanged()
    }
}