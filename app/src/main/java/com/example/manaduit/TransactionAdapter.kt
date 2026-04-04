package com.example.manaduit

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.manaduit.data.Transaction
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var list = listOf<Transaction>()
    var onItemClick: ((Transaction) -> Unit)? = null
    var onItemLongClick: ((Transaction) -> Unit)? = null
    fun setData(newList: List<Transaction>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = list[position]

        holder.tvTitle.text = data.title

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id","ID"))
        holder.tvDate.text = sdf.format(Date(data.date))

        if (data.type == "income") {

            holder.tvAmount.setTextColor(Color.parseColor("#00C853"))
            holder.tvAmount.text = "+ ${formatRupiah(data.amount)}"

        } else {

            holder.tvAmount.setTextColor(Color.parseColor("#FF2B2B"))
            holder.tvAmount.text = "- ${formatRupiah(data.amount)}"
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(data)
            true
        }
    }

    private fun formatRupiah(number: Long): String {
        return "Rp %,d".format(number).replace(',', '.')
    }
}