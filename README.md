# Gsms (Guard SMS)

Gsms adalah aplikasi SMS yang otomatis menyaring SMS promo Indonesia. Dibangun di atas [Fossify Messages](https://github.com/FossifyOrg/Messages) (GPLv3) dan dirilis dengan lisensi yang sama. Atribusi penuh untuk tim Fossify; semua fitur pesan dasar berasal dari proyek tersebut.

## Cara kerja

Setiap SMS masuk diperiksa dengan urutan berikut:

1. Daftar putih: pesan berisi kode, OTP, verifikasi, "jangan berikan", tagihan, transaksi, PIN, atau password selalu masuk inbox.
2. Pengirim ada di `app/src/main/assets/promo_senders.json`: masuk karantina.
3. Pengirim alfanumerik (bukan nomor, mis. CHATIME) dan isi pesan memuat kata promo (gratis, promo, diskon, undian, hadiah, cashback, kupon, "beli 1", "s&k", tukar, tautan): masuk karantina.
4. Selain itu: masuk inbox seperti biasa.

Pesan karantina tidak muncul di inbox dan tidak memunculkan notifikasi. Buka menu titik tiga di layar utama, pilih "SMS promo (karantina)" untuk memulihkan atau menghapusnya. Pesan karantina dihapus otomatis setelah 7 hari.

## Batasan

- Tidak mendukung RCS, hanya SMS dan MMS.
- Aplikasi harus dijadikan aplikasi SMS default agar bisa menerima dan menyaring pesan.
- MMS tidak disaring (promo biasanya berupa SMS).
- SMS promo dikarantina 7 hari, lalu dihapus permanen.
- Daftar pengirim promo bersifat awal; tambahkan sendiri di `promo_senders.json`.

## Lisensi

GPLv3, lihat [LICENSE](LICENSE). Berdasarkan Fossify Messages oleh Fossify.
