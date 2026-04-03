package com.example.manaduit

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manaduit.ui.AddTransactionBottomSheet
import com.example.manaduit.ui.TransactionViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val tvIncome = findViewById<TextView>(R.id.tvIncome)
        val tvExpense = findViewById<TextView>(R.id.tvExpense)
        val tvSaldo = findViewById<TextView>(R.id.tvSaldo)
        val fabAdd = findViewById<View>(R.id.fabAdd)

        adapter = TransactionAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        // list transaksi
        viewModel.allData.observe(this) {
            adapter.setData(it)
        }

        // total pemasukan
        viewModel.totalIncome.observe(this) { income ->
            val incomeValue = income ?: 0
            tvIncome.text = formatRupiah(incomeValue)

            val expenseValue = viewModel.totalExpense.value ?: 0
            tvSaldo.text = formatRupiah(incomeValue - expenseValue)
        }

        // total pengeluaran
        viewModel.totalExpense.observe(this) { expense ->
            val expenseValue = expense ?: 0
            tvExpense.text = formatRupiah(expenseValue)

            val incomeValue = viewModel.totalIncome.value ?: 0
            tvSaldo.text = formatRupiah(incomeValue - expenseValue)
        }

        // tombol tambah transaksi
        fabAdd.setOnClickListener {

            val sheet = AddTransactionBottomSheet {

                viewModel.insert(it)

            }

            sheet.show(supportFragmentManager, "AddTransaction")
        }
    }

    private fun formatRupiah(number: Long): String {
        return "Rp %,d".format(number).replace(',', '.')
    }
}