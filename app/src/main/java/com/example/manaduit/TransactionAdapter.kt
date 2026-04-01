package com.example.manaduit

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.manaduit.data.Transaction
import java.util.Date

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var list = listOf<Transaction>()

    fun setData(newList: List<Transaction>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvAmount = view.findViewById<TextView>(R.id.tvAmount)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
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
        holder.tvAmount.text = "Rp ${data.amount}"
        holder.tvDate.text = Date(data.date).toString()

        // Warna beda
        if (data.type == "income") {
            holder.tvAmount.setTextColor(Color.GREEN)
        } else {
            holder.tvAmount.setTextColor(Color.RED)
        }
    }
}