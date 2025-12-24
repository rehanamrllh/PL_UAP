# 📋 Manajer Daftar Tugas (To-Do List)

> **Aplikasi manajemen tugas modern yang dibangun dengan Java Swing**

<div align="center">

![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)
![License](https://img.shields.io/badge/License-Akademik-green.svg)

**UAP Pemrograman Lanjut 2025** | Universitas Muhammadiyah Malang

[Fitur](#-fitur) • [Instalasi](#-instalasi) • [Penggunaan](#-penggunaan) • [Struktur Proyek](#-struktur-proyek)

</div>

---

## 📸 Pratinjau

![Pratinjau Dashboard](demo/screenshots/dashboard.png)
>Dashboard dengan statistik real-time dan aksi cepat.

![Pratinjau Daftar Tugas](demo/screenshots/task_list.png)
>Daftar tugas interaktif dengan pengurutan dan pemfilteran.

![Pratinjau Tambah Tugas](demo/screenshots/add_tasks.png)
>Form yang ramah pengguna untuk menambah dan mengedit tugas.

![Pratinjau Riwayat & Statistik](demo/screenshots/history_stats.png)
>Representasi visual dari tugas yang selesai dan statistik.

---

## ✨ Fitur

- **GUI modern** dengan Java Swing
- **Penyimpanan data** menggunakan file CSV
- **Pencarian real-time** dan pemfilteran
- **Dashboard statistik** dengan visualisasi
- **Fungsionalitas CRUD lengkap**
- **ID numerik auto-increment** (1,2,3,...) untuk tugas
- **Penanganan LocalDate** (tanggal dibuat + tenggat)
- **Pemilih tanggal sederhana** (berbasis spinner)
- **Penanganan error yang komprehensif**

---

## 🚀 Instalasi

### Prasyarat

- Java JDK 11 atau lebih baru
- Maven 3.6+
- IDE pilihan Anda (IntelliJ IDEA, Eclipse, VS Code, dll.)

### Build & Jalankan via Terminal
> Jalankan dari folder `demo/` agar file `tasks_data.csv` tersimpan di folder itu.

#### Windows (PowerShell)

```powershell
cd demo
New-Item -ItemType Directory -Force out | Out-Null

$src = Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $src

java -cp out com.uap.app.ToDoApp
```

---

## 📝 Penggunaan

1. **Jalankan aplikasi** menggunakan salah satu metode di atas.
2. **Tambahkan tugas baru** melalui menu "Add Task".
3. **Lihat semua tugas** pada bagian "Task List".
4. **Edit tugas** menggunakan tombol Edit.
5. **Hapus tugas** menggunakan tombol Delete.
6. **Lihat riwayat** pada menu "History".

Data disimpan dalam file CSV bernama `tasks_data.csv` di direktori kerja saat ini.
Jika ingin mengatur ulang data aplikasi, hapus file tersebut.

---

## 🎯 Fitur Utama

### 1️⃣ Dashboard
- Statistik real-time (Total, Selesai, Tertunda, Tingkat Penyelesaian)
- Aksi cepat untuk navigasi yang lebih cepat
- Kartu visual dengan ikon

### 2️⃣ Daftar Tugas
- Tabel interaktif dengan pengurutan
- Pencarian real-time
- Filter berdasarkan status (ALL / Pending / Completed)
- Aksi Edit/Hapus/Selesai melalui tombol

### 3️⃣ Tambah/Edit Tugas
- Form input dengan validasi
- Dropdown untuk Prioritas & Status
- Penanganan error dengan dialog
- Mode ganda (Tambah/Edit)

### 4️⃣ Riwayat & Statistik
- Riwayat tugas yang selesai
- Pelacakan tingkat penyelesaian
- Statistik berdasarkan prioritas
- Representasi visual

---

## 💻 Teknologi yang Digunakan

- **Java** (diuji dengan JDK 21; source/target dikonfigurasi oleh Maven)
- **Java Swing** untuk GUI
- **Maven** untuk manajemen build
- **CSV** untuk penyimpanan data
- **LocalDate** untuk penanganan tanggal
- **ArrayList & Stream API** untuk manipulasi data

---

## 📁 Struktur Proyek

```
demo/
├── src/main/java/com/uap/
│   └── ToDoApp.java                 # Launcher kompatibilitas (memanggil com.uap.app.ToDoApp)
├── src/main/java/com/uap/app/
│   └── ToDoApp.java                 # Titik masuk utama (JFrame + navigasi)
├── src/main/java/com/uap/data/
│   └── DataManager.java             # Load/save CSV + pembuatan ID
├── src/main/java/com/uap/model/
│   └── Task.java                    # Model (LocalDate dibuat/tenggat)
├── src/main/java/com/uap/ui/
│   ├── LocalDatePickerField.java    # Pemilih tanggal sederhana (spinner)
│   ├── UIColors.java                # Warna tema
│   └── UIUtils.java                 # Helper UI
└── src/main/java/com/uap/ui/panels/
	├── DashboardPanel.java
	├── TaskListPanel.java
	├── AddTaskPanel.java
	└── HistoryPanel.java

└── pom.xml                    # Konfigurasi Maven
```

---

### Dashboard
- Antarmuka modern dengan kartu statistik
- Informasi berkode warna untuk wawasan cepat
- Tombol aksi cepat untuk navigasi yang lebih cepat

### Daftar Tugas
- Tabel yang dapat diurutkan dan difilter
- Fungsionalitas pencarian real-time
- Filter berdasarkan status dan prioritas

### Form Input
- Tata letak form yang rapi dan ramah pengguna
- Validasi input dengan pesan kesalahan yang ramah pengguna
- Dropdown untuk memudahkan pemilihan prioritas dan status

### Riwayat
- Ringkasan tugas yang selesai
- Statistik visual untuk pelacakan performa
- Pelacakan progres dari waktu ke waktu

---

## 🔧 Pengembangan

### Prasyarat
- Java JDK 11 atau lebih baru
- Maven 3.6+
- IDE (IntelliJ IDEA / Eclipse / VS Code)

### Build & Jalankan
```bash
# Clone atau ekstrak proyek
cd demo

# Kompilasi
mvn clean compile

# Jalankan (jar)
mvn -DskipTests package
java -jar target/demo-1.0-SNAPSHOT.jar

# Package (opsional)
mvn package
```

---

## 🎓 Capaian Pembelajaran

Proyek ini mengimplementasikan konsep dari **Modul 1-6**:

1. **Dasar Java & OOP** - Kelas, pewarisan, enkapsulasi
2. **Collections** - ArrayList, generics, Stream API
3. **Penanganan Exception** - Try-catch, validasi
4. **File I/O** - Operasi CSV, penyimpanan data
5. **Pengembangan GUI** - Komponen Swing, penanganan event
6. **Design Patterns** - MVC, Observer, Strategy

---

## 📄 Lisensi

Proyek ini dibuat untuk keperluan akademik (UAP Pemrograman Lanjut 2025).

---

**🚀 Siap digunakan! Selamat mengelola tugas!**

*UAP Pemrograman Lanjut 2025 - Universitas Muhammadiyah Malang*

**Tautan Repository:** [https://github.com/rehanamrllh/PL_UAP](https://github.com/rehanamrllh/PL_UAP)

---