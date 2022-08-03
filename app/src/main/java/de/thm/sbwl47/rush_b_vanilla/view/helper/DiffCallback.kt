package de.thm.sbwl47.rush_b_vanilla.view.helper

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import de.thm.sbwl47.rush_b_vanilla.model.Cell

class DiffCallback(var oldCells: List<Cell>, var newCells: List<Cell>) : DiffUtil.Callback() {

    /**
     * Check if the Items are the same Items by comparing their ID
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCells[oldItemPosition].id == newCells[newItemPosition].id
    }

    /**
     * @return the size of the old list
     */
    override fun getOldListSize(): Int {
        return oldCells.size
    }

    /**
     * @return the size of the new list
     */
    override fun getNewListSize(): Int {
        return newCells.size
    }

    /**
     * @return the difference between the two elements
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCells[oldItemPosition].offset == newCells[newItemPosition].offset
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return null
    }
}