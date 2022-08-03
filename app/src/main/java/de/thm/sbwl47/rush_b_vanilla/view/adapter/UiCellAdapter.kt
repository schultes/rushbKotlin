package de.thm.sbwl47.rush_b_vanilla.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.thm.sbwl47.rush_b_vanilla.R
import de.thm.sbwl47.rush_b_vanilla.view.helper.DiffCallback
import de.thm.sbwl47.rush_b_vanilla.view.SquareCell
import de.thm.sbwl47.rush_b_vanilla.model.Cell

class UiCellAdapter(context: Context, private val onClick: ((Cell) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var cells = ArrayList<Cell>()

    private val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return cells.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UiCellViewHolder(inflater.inflate(R.layout.square_cell_itemview, parent, false), onClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as UiCellViewHolder
        holder.bind(cells[position])
    }

   override fun getItemId(position: Int): Long{


        return  cells[position].id.toLong()
    }

    fun updateList(newList: List<Cell>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(DiffCallback(this.cells, newList))
        cells.clear()
        cells.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class UiCellViewHolder(itemView: View, onClick: (Cell) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val view = itemView as SquareCell
        private var cell: Cell? = null

        init {
            view.setOnClickListener {
                cell?.let(onClick)
                it.invalidate()
            }
        }

        fun bind(cell: Cell) {
            this.cell = cell
            view.bind(cell)
        }
    }
}
