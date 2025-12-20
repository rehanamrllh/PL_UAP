# 📝 To-Do List Manager Application

**Aplikasi Manajemen Tugas Modern dengan Java Swing**

Proyek ini dikembangkan untuk memenuhi **Ujian Akhir Praktikum (UAP) Pemrograman Lanjut 2025** - Universitas Muhammadiyah Malang.

---

## ✨ Highlights

🎨 **Modern GUI** - Desain menarik dengan Java Swing
💾 **Data Persistence** - Penyimpanan permanen dengan CSV
🔍 **Smart Search** - Pencarian real-time dan filtering
📊 **Statistics** - Dashboard dengan visualisasi data
✅ **Complete CRUD** - Create, Read, Update, Delete
🛡️ **Error Handling** - Exception handling komprehensif

---

## 🚀 Quick Start

```bash
cd demo
mvn clean compile
mvn exec:java -Dexec.mainClass="com.uap.ToDoListApp"
```

**📖 Dokumentasi Lengkap:** Lihat [demo/INDEX.md](demo/INDEX.md)

---

## 👥 Anggota Kelompok
| Nama | Peran |
| :--- | :--- |
| **Rayhan Amrullah** | Developer |
| **Ivan Nuryanto** | Developer |

---

## 📚 Dokumentasi

Proyek ini dilengkapi dengan dokumentasi lengkap:

### 🎯 Start Here
- **[INDEX.md](demo/INDEX.md)** - Panduan navigasi dokumentasi
- **[QUICK_START.md](demo/QUICK_START.md)** - Cara menjalankan aplikasi

### 📖 Main Documentation
- **[README_APP.md](demo/README_APP.md)** - Dokumentasi teknis lengkap
- **[USER_GUIDE.md](demo/USER_GUIDE.md)** - Panduan pengguna (Bahasa Indonesia)

### ✅ UAP Requirements
- **[DOKUMENTASI_KETENTUAN.md](demo/DOKUMENTASI_KETENTUAN.md)** - Pemenuhan ketentuan UAP
- **[PROJECT_SUMMARY.md](demo/PROJECT_SUMMARY.md)** - Ringkasan proyek

---

## 🎯 Fitur Utama

### 1️⃣ Dashboard
- Statistik real-time (Total, Completed, Pending, Completion Rate)
- Quick actions untuk navigasi cepat
- Visual cards dengan ikon

### 2️⃣ Task List
- Tabel interaktif dengan sorting
- Real-time search
- Multi-filter (Status + Priority)
- Double-click untuk edit

### 3️⃣ Add/Edit Task
- Form input dengan validasi
- Dropdown untuk Priority & Status
- Error handling dengan dialog
- Dual mode (Add/Edit)

### 4️⃣ History & Statistics
- Completed tasks history
- Completion rate tracking
- Priority-based statistics
- Visual representation

---

## 💻 Teknologi

- **Java 21** (compatible with 11+)
- **Java Swing** untuk GUI
- **Maven** untuk build management
- **CSV** untuk data storage
- **LocalDate** untuk date handling
- **ArrayList & Stream API** untuk data manipulation

---

## 📁 Struktur Proyek

```
demo/
├── src/main/java/com/uap/
│   ├── ToDoListApp.java       # Main entry point
│   ├── MainDashboard.java     # Dashboard & Navigation
│   ├── TaskListPanel.java     # Task list view
│   ├── TaskInputPanel.java    # Add/Edit form
│   ├── HistoryPanel.java      # History & stats
│   ├── Task.java              # Model class
│   ├── TaskManager.java       # Business logic
│   └── FileHandler.java       # File operations
│
├── Documentation/
│   ├── INDEX.md               # 📚 Navigation guide
│   ├── QUICK_START.md         # 🚀 Installation
│   ├── README_APP.md          # 📖 Full docs
│   ├── USER_GUIDE.md          # 📱 User manual
│   ├── DOKUMENTASI_KETENTUAN.md # ✅ Requirements
│   └── PROJECT_SUMMARY.md     # 📊 Summary
│
└── pom.xml                    # Maven config
```

---

## ✅ Pemenuhan Ketentuan UAP

| Ketentuan | Status | Implementasi |
|-----------|--------|--------------|
| **A. GUI Java Swing** | ✅ | 100% Swing components |
| **B. 4+ Halaman** | ✅ | Dashboard, List, Input, History |
| **C. CRUD Complete** | ✅ | Create, Read, Update, Delete |
| **D. File Handling** | ✅ | CSV dengan auto-save |
| **E. Sorting** | ✅ | Multi-column table sorting |
| **F. Searching** | ✅ | Real-time search & filter |
| **G. LocalDate** | ✅ | Date tracking |
| **H. ArrayList** | ✅ | Dynamic data storage |
| **I. Comparator** | ✅ | Custom sorting |
| **J. Exception Handling** | ✅ | Comprehensive try-catch |

**Compliance: 100% ✅**

---

## 🎨 Screenshots

### Dashboard
- Modern interface dengan statistics cards
- Color-coded information
- Quick action buttons

### Task List
- Sortable table
- Real-time search
- Filter by status & priority

### Input Form
- Clean form layout
- Input validation
- User-friendly error messages

### History
- Completed tasks overview
- Visual statistics
- Progress tracking

---

## 🔧 Development

### Prerequisites
- Java JDK 11 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA / Eclipse / VS Code)

### Build & Run
```bash
# Clone or extract project
cd demo

# Compile
mvn clean compile

# Run
mvn exec:java -Dexec.mainClass="com.uap.ToDoListApp"

# Package (optional)
mvn package
```

---

## 📝 Cara Penggunaan

1. **Jalankan aplikasi** menggunakan salah satu method di atas
2. **Tambah task baru** via menu "Add Task"
3. **Lihat semua task** di "Task List"
4. **Edit task** dengan double-click atau tombol Edit
5. **Hapus task** dengan tombol Delete
6. **Lihat history** di menu "History"

**Detail lengkap:** [USER_GUIDE.md](demo/USER_GUIDE.md)

---

## 🎓 Learning Outcomes

Proyek ini mengimplementasikan konsep dari **Modul 1-6**:

1. **Java Basics & OOP** - Classes, inheritance, encapsulation
2. **Collections** - ArrayList, generics, Stream API
3. **Exception Handling** - Try-catch, validation
4. **File I/O** - CSV operations, data persistence
5. **GUI Development** - Swing components, event handling
6. **Design Patterns** - MVC, Observer, Strategy

---

## 🏆 Key Features Breakdown

### Data Management
- ✅ Auto-increment ID
- ✅ Input validation
- ✅ Data persistence
- ✅ Error recovery

### User Interface
- ✅ Modern design
- ✅ Intuitive navigation
- ✅ Visual feedback
- ✅ Responsive layout

### Advanced Features
- ✅ Real-time search
- ✅ Multi-filter
- ✅ Table sorting
- ✅ Statistics dashboard
- ✅ Completion tracking

---

## 📞 Support

**Dokumentasi Lengkap:** [demo/INDEX.md](demo/INDEX.md)

**Quick Start:** [demo/QUICK_START.md](demo/QUICK_START.md)

**User Guide:** [demo/USER_GUIDE.md](demo/USER_GUIDE.md)

---

## 📄 License

Project ini dibuat untuk keperluan akademik (UAP Pemrograman Lanjut 2025).

---

## 🎉 Status Proyek

**Status: COMPLETE ✅**

✅ All source files created
✅ CRUD operations working
✅ GUI implemented
✅ File handling functional
✅ Exception handling added
✅ Documentation complete
✅ Testing performed
✅ Ready for submission

---

**🚀 Ready to use! Happy Task Managing!**

*UAP Pemrograman Lanjut 2025 - Universitas Muhammadiyah Malang*

**Link Repository:** [https://github.com/rehanamrllh/PL_UAP](https://github.com/rehanamrllh/PL_UAP)

---
