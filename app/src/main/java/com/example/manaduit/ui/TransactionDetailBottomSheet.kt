package com.example.manaduit.ui

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.*
import com.example.manaduit.R
import com.example.manaduit.data.Transaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailBottomSheet(
    private val data: Transaction
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.modal_detail_transaction, container, false)

        val tvType = view.findViewById<TextView>(R.id.tvType)
        val tvAmount = view.findViewById<TextView>(R.id.tvAmount)
        val tvDesc = view.findViewById<TextView>(R.id.tvDesc)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val img = view.findViewById<ImageView>(R.id.imgStruk)
        val empty = view.findViewById<View>(R.id.layoutEmptyStruk)
        val header = view.findViewById<View>(R.id.header)
        val btnClose = view.findViewById<ImageView>(R.id.btnClose)

        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))

        tvDate.text = sdf.format(Date(data.date))
        tvDesc.text = if (data.description.isNullOrEmpty()) "-" else data.description

        if (data.type == "income") {
            tvType.text = "Pemasukan"
            tvAmount.text = "+${formatRupiah(data.amount)}"
            header.setBackgroundResource(R.drawable.bg_gradient_green)
        } else {
            tvType.text = "Pengeluaran"
            tvAmount.text = "-${formatRupiah(data.amount)}"
            header.setBackgroundResource(R.drawable.bg_gradient_red)
        }

        if (!data.imageUri.isNullOrEmpty()) {

            val uri = android.net.Uri.parse(data.imageUri)

            img.visibility = View.VISIBLE
            empty.visibility = View.GONE

            img.setImageURI(uri)

            img.setOnClickListener {
                val dialog = Dialog(requireContext())
                val imageView = ImageView(requireContext())
                imageView.setImageURI(uri)
                imageView.adjustViewBounds = true
                dialog.setContentView(imageView)
                dialog.show()
            }

        } else {
            img.visibility = View.GONE
            empty.visibility = View.VISIBLE
        }

        btnClose.setOnClickListener { dismiss() }

        return view
    }

    private fun formatRupiah(number: Long): String {
        return "Rp %,d".format(number).replace(',', '.')
    }
}