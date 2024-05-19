package com.example.musicapp.presentation.utils

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback <T> (
    var oldList: List<T>,
    var newList: List<T>,
    private val comparatorRule: (T, T) -> Boolean = { old, new -> old == new }
) : DiffUtil.Callback()  {
    override fun getOldListSize(): Int =
        oldList.size


    override fun getNewListSize(): Int =
        newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        comparatorRule(oldList[oldItemPosition], newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        comparatorRule(oldList[oldItemPosition], newList[newItemPosition])
}