package com.GoMobeil.H2H.Adapters

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import java.util.ArrayList

/**
 * Created by ADMIN on 03-02-2018.
 */
abstract class SelectableAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private val selectedItems: SparseBooleanArray

    val selectedCount: Int
        get() = selectedItems.size()

    init {
        selectedItems = SparseBooleanArray()
    }

    fun isSelected(position: Int): Boolean {
        return getSelectedItems().contains(position)
    }

    fun removeSelection(position: Int) {
        if (getSelectedItems().contains(position)) {
            getSelectedItems().removeAt(position)
        }
        notifyItemChanged(position)
    }

    fun setMultiple(value: Boolean) {
        lbMultiple = value
    }

    fun toggleSelection(position: Int) {

        if (!lbMultiple!!) {
            clearSelection()
        }

        if (selectedItems.get(position, false)) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun clearSelection() {
        val selection = getSelectedItems()
        selectedItems.clear()
        for (i in selection) {
            notifyItemChanged(i)
        }
    }

    fun getSelectedItems(): ArrayList<Int> {
        val items = ArrayList<Int>(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    companion object {
        private val TAG = "SelectableAdapter"
        private var lbMultiple: Boolean? = false
    }

}