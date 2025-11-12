# 🎬 Aplikasi Pemesanan Tiket Bioskop Multi-Thread

Aplikasi ini adalah contoh sederhana implementasi **Java Swing GUI** untuk sistem pemesanan tiket bioskop yang **menangani masalah konkurensi (rebutan kursi)** menggunakan **Java Threads** dan **sinkronisasi**, serta terhubung dengan **database MySQL**.

---

## 💻 Struktur Program

Program terdiri dari empat kelas utama:

| Kelas | Deskripsi |
|-------|------------|
| **MainGUI.java** | Kelas utama yang membangun antarmuka pengguna menggunakan Java Swing. Menerima input dari pengguna dan memicu proses pemesanan dalam thread terpisah. |
| **TiketBookingTask.java** | Mengimplementasikan interface `Runnable`. Setiap permintaan pemesanan tiket dijalankan dalam thread berbeda. |
| **DBConnectorMySQL.java** | Menangani semua interaksi dengan database MySQL seperti koneksi, pengecekan kursi, dan proses booking. |
| **DBConnector.java** | (Opsional, bisa digabung ke `DBConnectorMySQL`). Bertugas membuat koneksi JDBC ke database MySQL. |

---

## 🧵 Penggunaan Thread (Concurrency Control)

Tujuan utama penggunaan thread di sini adalah untuk **mensimulasikan banyak pengguna** yang mencoba memesan kursi bioskop **secara bersamaan**.

### 🧨 Masalah Konkurensi (Race Condition)

Contoh skenario:
1. **Andi** dan **Budi** sama-sama ingin memesan kursi `A1`.
2. Andi melakukan `SELECT` — kursi masih kosong.
3. Budi juga melakukan `SELECT` — kursi masih kosong.
4. Andi melakukan `INSERT` (berhasil).
5. Budi juga melakukan `INSERT` (berhasil) → Terjadi **kursi ganda**!

### ✅ Solusi: Sinkronisasi di Java

Untuk mencegah hal ini, digunakan blok `synchronized` di dalam method `run()` pada kelas `TiketBookingTask.java`:

```java
@Override
public void run() {
    synchronized (DBConnectorMySQL.class) {
        // 1. Cek Kursi (SELECT)
        // 2. Jika Kosong, Pesan Kursi (INSERT)
    }
}
