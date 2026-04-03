package com.example.manaduit.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import com.example.manaduit.R
import com.example.manaduit.data.Transaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionBottomSheet(
    private val onSave: (Transaction) -> Unit
) : BottomSheetDialogFragment() {

    private var type = "expense"
    private var selectedDate: Long = System.currentTimeMillis()
    private var imageUri: Uri? = null
    private val PICK_IMAGE = 1001

    private lateinit var imgPreview: ImageView
    private lateinit var placeholder: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.modal_add_transaction,
            container,
            false
        )

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)

        val btnExpense = view.findViewById<Button>(R.id.btnPengeluaran)
        val btnIncome = view.findViewById<Button>(R.id.btnPemasukan)
        val btnSave = view.findViewById<Button>(R.id.btnSimpan)

        val inputTanggal = view.findViewById<EditText>(R.id.inputTanggal)
        val btnClose = view.findViewById<ImageView>(R.id.btnClose)
        val uploadArea = view.findViewById<FrameLayout>(R.id.uploadArea)

        imgPreview = view.findViewById(R.id.imgPreview)
        placeholder = view.findViewById(R.id.layoutPlaceholder)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("id"))

        inputTanggal.setText(sdf.format(Date(selectedDate)))

        setExpenseActive(btnExpense, btnIncome)

        // DATE PICKER
        inputTanggal.setOnClickListener {

            val cal = Calendar.getInstance()

            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    cal.set(year, month, day)
                    selectedDate = cal.timeInMillis
                    inputTanggal.setText(sdf.format(Date(selectedDate)))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // TOGGLE
        btnExpense.setOnClickListener {
            type = "expense"
            setExpenseActive(btnExpense, btnIncome)
        }

        btnIncome.setOnClickListener {
            type = "income"
            setIncomeActive(btnExpense, btnIncome)
        }

        // PICK IMAGE
        uploadArea.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(intent, PICK_IMAGE)
        }

        // SAVE
        btnSave.setOnClickListener {

            val title = etTitle.text.toString()
            val amount = etAmount.text.toString()
            val desc = etDescription.text.toString()

            if (title.isEmpty() || amount.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Judul dan jumlah wajib diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                transactionId = UUID.randomUUID().toString(),
                type = type,
                title = title,
                description = desc,
                amount = amount.toLong(),
                date = selectedDate
            )

            onSave(transaction)
            dismiss()
        }

        btnClose.setOnClickListener { dismiss() }

        return view
    }

    // HANDLE IMAGE RESULT
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == -1) {

            imageUri = data?.data

            // 🔥 tampilkan preview
            imgPreview.setImageURI(imageUri)
            imgPreview.visibility = View.VISIBLE
            placeholder.visibility = View.GONE

            Toast.makeText(
                requireContext(),
                "Struk berhasil dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setExpenseActive(btnExpense: Button, btnIncome: Button) {
        btnExpense.setBackgroundResource(R.drawable.bg_toggle_red)
        btnExpense.setTextColor(Color.WHITE)

        btnIncome.setBackgroundResource(R.drawable.bg_toggle_gray)
        btnIncome.setTextColor(Color.BLACK)
    }

    private fun setIncomeActive(btnExpense: Button, btnIncome: Button) {
        btnIncome.setBackgroundResource(R.drawable.bg_toggle_green)
        btnIncome.setTextColor(Color.WHITE)

        btnExpense.setBackgroundResource(R.drawable.bg_toggle_gray)
        btnExpense.setTextColor(Color.BLACK)
    }
}