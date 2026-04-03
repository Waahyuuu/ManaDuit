package com.example.manaduit

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manaduit.data.Transaction
import com.example.manaduit.ui.AddTransactionBottomSheet
import com.example.manaduit.ui.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter

    private var isSaldoVisible = true
    private var saldoAsli: Long = 0

    // FILTER STATE
    private var selectedMonth: Int? = null
    private var selectedYear: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val tvIncome = findViewById<TextView>(R.id.tvIncome)
        val tvExpense = findViewById<TextView>(R.id.tvExpense)
        val tvSaldo = findViewById<TextView>(R.id.tvSaldo)
        val fabAdd = findViewById<View>(R.id.fabAdd)
        val btnToggle = findViewById<ImageView>(R.id.btnToggleSaldo)
        val tvYearlyExpense = findViewById<TextView>(R.id.tvYearlyExpense)
        val btnMonth = findViewById<TextView>(R.id.btnMonth)
        val emptyState = findViewById<View>(R.id.emptyState)

        val calNow = Calendar.getInstance()
        val currentYear = calNow.get(Calendar.YEAR)

        // DEFAULT = BULAN SEKARANG
        selectedMonth = calNow.get(Calendar.MONTH)
        selectedYear = currentYear

        val monthName = SimpleDateFormat("MMMM yyyy", Locale("id"))
        btnMonth.text = monthName.format(Date())

        adapter = TransactionAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        viewModel.allData.observe(this) { list ->

            applyFilter(list, tvIncome, tvExpense, recyclerView, emptyState)

            val yearlyExpense = list
                .filter {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = it.date
                    cal.get(Calendar.YEAR) == currentYear && it.type == "expense"
                }
                .sumOf { it.amount }

            tvYearlyExpense.text =
                "Biaya Hidup $currentYear ${formatRupiah(yearlyExpense)}"
        }

        // TOTAL SALDO
        viewModel.totalIncome.observe(this) { income ->
            val incomeValue = income ?: 0
            val expenseValue = viewModel.totalExpense.value ?: 0
            updateSaldo(tvSaldo, incomeValue, expenseValue)
        }

        viewModel.totalExpense.observe(this) { expense ->
            val expenseValue = expense ?: 0
            val incomeValue = viewModel.totalIncome.value ?: 0
            updateSaldo(tvSaldo, incomeValue, expenseValue)
        }

        // TAMBAH TRANSAKSI
        fabAdd.setOnClickListener {
            val sheet = AddTransactionBottomSheet {
                viewModel.insert(it)
            }
            sheet.show(supportFragmentManager, "AddTransaction")
        }

        // TOGGLE SALDO
        btnToggle.setOnClickListener {

            isSaldoVisible = !isSaldoVisible

            val newText = if (isSaldoVisible)
                formatAngka(saldoAsli)
            else
                "•".repeat(formatAngka(saldoAsli).length + 3)

            val distance = tvSaldo.width * 0.3f

            tvSaldo.animate()
                .translationX(-distance)
                .alpha(0f)
                .setDuration(180)
                .setInterpolator(android.view.animation.AccelerateInterpolator())
                .withEndAction {

                    tvSaldo.text = newText
                    tvSaldo.translationX = distance

                    tvSaldo.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(180)
                        .setInterpolator(android.view.animation.DecelerateInterpolator())
                        .start()
                }
                .start()

            btnToggle.setImageResource(
                if (isSaldoVisible)
                    android.R.drawable.ic_menu_view
                else
                    android.R.drawable.ic_menu_close_clear_cancel
            )
        }

        // FILTER BULAN
        btnMonth.setOnClickListener {

            val months = arrayOf(
                "Semua",
                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
            )

            val currentMonth = calNow.get(Calendar.MONTH)

            val availableMonths = mutableListOf<String>()
            availableMonths.add("Semua")
            availableMonths.addAll(months.sliceArray(1..currentMonth + 1))

            AlertDialog.Builder(this)
                .setTitle("Pilih Bulan")
                .setItems(availableMonths.toTypedArray()) { _, which ->

                    val selected = availableMonths[which]

                    if (selected == "Semua") {
                        selectedMonth = null
                        selectedYear = null
                        btnMonth.text = "Semua"
                    } else {
                        selectedMonth = which - 1
                        selectedYear = currentYear
                        btnMonth.text = "$selected $currentYear"
                    }

                    applyFilter(
                        viewModel.allData.value ?: emptyList(),
                        tvIncome,
                        tvExpense,
                        recyclerView,
                        emptyState
                    )
                }
                .show()
        }
    }

    private fun applyFilter(
        list: List<Transaction>,
        tvIncome: TextView,
        tvExpense: TextView,
        recyclerView: RecyclerView,
        emptyState: View
    ) {

        val filtered = if (selectedMonth == null) {
            list
        } else {
            list.filter {
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date

                cal.get(Calendar.MONTH) == selectedMonth &&
                        cal.get(Calendar.YEAR) == selectedYear
            }
        }

        adapter.setData(filtered)

        if (filtered.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        val income = filtered
            .filter { it.type == "income" }
            .sumOf { it.amount }

        val expense = filtered
            .filter { it.type == "expense" }
            .sumOf { it.amount }

        tvIncome.text = formatRupiah(income)
        tvExpense.text = formatRupiah(expense)
    }

    private fun updateSaldo(tvSaldo: TextView, income: Long, expense: Long) {
        saldoAsli = income - expense

        tvSaldo.text = if (isSaldoVisible)
            formatAngka(saldoAsli)
        else
            "•".repeat(formatAngka(saldoAsli).length + 3)
    }

    private fun formatRupiah(number: Long): String {
        return "Rp %,d".format(number).replace(',', '.')
    }

    private fun formatAngka(number: Long): String {
        return "%,d".format(number).replace(',', '.')
    }
}