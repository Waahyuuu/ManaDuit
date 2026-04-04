# ManaDuit - Personal Finance Tracker

**ManaDuit** adalah aplikasi Android sederhana yang digunakan untuk membantu pengguna mencatat dan mengelola keuangan sehari-hari, baik pemasukan maupun pengeluaran.

Aplikasi ini membantu pengguna memantau transaksi keuangan sehingga dapat mengetahui kondisi keuangan secara lebih jelas dan mengontrol pengeluaran. Pengguna dapat mengetahui pemasukan dalam setiap bulan maupun dalam setahun sehingga mempermudah dalam perancangan keuangan kedepannya.

---

# 📱 Preview Aplikasi

```
Preview_Aplikasi/
├── home.png
├── add_transaction.png
└── transaction_list.png
```

Contoh penggunaan di README:

```
![Home](screenshots/home.png)
![Add Transaction](screenshots/add_transaction.png)
![Transaction List](screenshots/transaction_list.png)
```

---

# ✨ Fitur Utama

* 📥 Menambahkan transaksi **pemasukan**
* 📤 Menambahkan transaksi **pengeluaran**
* 📜 Menampilkan **riwayat transaksi**
* 📅 Menampilkan **tanggal transaksi**
* 📱 Tampilan sederhana dan mudah digunakan

---

# 🚀 Rencana Pengembangan

Beberapa fitur yang dapat dikembangkan pada aplikasi ini:

* 📊 Grafik pemasukan dan pengeluaran
* 🏷 Kategori transaksi
* 🔍 Pencarian transaksi
* 📆 Filter transaksi berdasarkan tanggal
* ☁ Backup dan restore data
* 📑 Export laporan keuangan

---

# 🛠 Teknologi yang Digunakan

Project ini dibangun menggunakan teknologi berikut:

* Kotlin
* Android Studio
* RecyclerView
* Material Design
* Local Data Storage

---

# 📂 Struktur Project

```
ManaDuit
│
├── adapter
│   └── TransactionAdapter.kt
│
├── data
│   └── Transaction.kt
│
├── ui
│   └── MainActivity.kt
│
├── layout
│   ├── activity_main.xml
│   └── item_transaction.xml
│
└── util
```

### Penjelasan Struktur

| Folder  | Fungsi                                   |
| ------- | ---------------------------------------- |
| adapter | Mengatur tampilan data pada RecyclerView |
| data    | Model data transaksi                     |
| ui      | Activity atau tampilan aplikasi          |
| layout  | XML untuk desain tampilan                |
| util    | Fungsi tambahan / helper                 |

---

# ⚙️ Cara Menjalankan Project

### 1. Clone Repository

```
git clone https://github.com/Waahyuuu/ManaDuit.git
```

### 2. Buka Project

Buka project menggunakan **Android Studio**.

### 3. Sync Gradle

Tunggu hingga proses **Gradle Sync selesai**.

### 4. Jalankan Aplikasi

Jalankan aplikasi menggunakan:

* Android Emulator
  atau
* Perangkat Android langsung

---

# 🧪 Cara Menggunakan Aplikasi

1. Buka aplikasi ManaDuit
2. Tambahkan transaksi baru
3. Pilih jenis transaksi:

   * Pemasukan
   * Pengeluaran
4. Simpan transaksi
5. Transaksi akan muncul di daftar riwayat

---

# 📦 Build APK

Untuk membuat file APK:

```
Build
→ Build Bundle(s) / APK(s)
→ Build APK(s)
```

File APK akan tersedia pada folder:

```
app/build/outputs/apk/
```

---

# 🤝 Kontribusi

Kontribusi sangat terbuka.

Langkah kontribusi:

1. Fork repository
2. Buat branch baru
3. Tambahkan fitur atau perbaikan
4. Buat pull request

---

# 👨‍💻 Author

**Wahyu**

Mahasiswa Sistem Informasi
STIMATA Malang

GitHub:
https://github.com/Waahyuuu

---

# ⭐ Support

Jika project ini bermanfaat, silakan berikan **⭐ Star** pada repository ini.
