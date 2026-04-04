package com.example.manaduit.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.manaduit.R
import com.example.manaduit.data.Transaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionBottomSheet(
    private val onSave: (Transaction) -> Unit
) : BottomSheetDialogFragment() {

    private var type = "expense"
    private var selectedDate: Long = System.currentTimeMillis()

    private var imageUri: Uri? = null
    private var tempImageUri: Uri? = null

    private lateinit var imgPreview: ImageView
    private lateinit var placeholder: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.modal_add_transaction, container, false)

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

        inputTanggal.setOnClickListener {

            val cal = Calendar.getInstance()

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    cal.set(year, month, day)
                    selectedDate = cal.timeInMillis
                    inputTanggal.setText(sdf.format(Date(selectedDate)))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.maxDate = System.currentTimeMillis()
            datePicker.show()
        }

        btnExpense.setOnClickListener {
            type = "expense"
            setExpenseActive(btnExpense, btnIncome)
        }

        btnIncome.setOnClickListener {
            type = "income"
            setIncomeActive(btnExpense, btnIncome)
        }

        uploadArea.setOnClickListener {
            showImagePickerDialog()
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val amountStr = etAmount.text.toString().trim()
            val desc = etDescription.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Judul wajib diisi"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (amountStr.isEmpty()) {
                etAmount.error = "Jumlah wajib diisi"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            // PERBAIKAN parsing amount
            val amount = amountStr.replace(".", "").toLongOrNull()

            if (amount == null || amount <= 0) {
                etAmount.error = "Jumlah tidak valid"
                etAmount.requestFocus()
                return@setOnClickListener
            }

            if (selectedDate > System.currentTimeMillis()) {
                inputTanggal.error = "Tidak bisa pilih tanggal masa depan"
                inputTanggal.requestFocus()
                return@setOnClickListener
            }

            val transaction = Transaction(
                transactionId = UUID.randomUUID().toString(),
                type = type,
                title = title,
                description = desc,
                amount = amount,
                date = selectedDate,
                imageUri = imageUri?.toString()
            )

            onSave(transaction)
            dismiss()
        }

        btnClose.setOnClickListener { dismiss() }

        return view
    }

    private fun showImagePickerDialog() {

        val options = arrayOf("Kamera", "Galeri")

        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Sumber Gambar")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }

        imageLauncher.launch(intent)
    }

    private fun openCamera() {

        try {

            val photoFile = File.createTempFile(
                "IMG_${System.currentTimeMillis()}",
                ".jpg",
                requireContext().cacheDir
            )

            tempImageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                photoFile
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            imageLauncher.launch(intent)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
        }
    }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                try {

                    val uri = result.data?.data ?: tempImageUri

                    if (uri != null) {

                        imageUri = uri

                        imgPreview.setImageURI(uri)
                        imgPreview.visibility = View.VISIBLE
                        placeholder.visibility = View.GONE
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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