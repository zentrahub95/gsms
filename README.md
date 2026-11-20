# Gsms (Guard SMS)

Gsms adalah aplikasi SMS untuk Android yang otomatis menyaring SMS promo Indonesia ke karantina, sehingga inbox hanya berisi pesan penting. Pesan promo tidak memunculkan notifikasi dan bisa dipulihkan selama masa karantina.

## Status

Masih dalam pengembangan dan belum ada rilis. APK debug memakai applicationId `org.fossify.gsms`; itu hanya hack internal, bukan identitas final.

## Cara kerja

Setiap SMS masuk diperiksa dengan urutan berikut:

1. Daftar putih: pesan berisi kode, OTP, verifikasi, "jangan berikan", tagihan, transaksi, PIN, atau password selalu masuk inbox.
2. Pengirim ada di `app/src/main/assets/promo_senders.json`: masuk karantina.
3. Pengirim alfanumerik (bukan nomor, mis. CHATIME) dan isi pesan memuat kata promo (gratis, promo, diskon, undian, hadiah, cashback, kupon, "beli 1", "s&k", tukar, tautan): masuk karantina.
4. Selain itu: masuk inbox seperti biasa.

Pesan dari nomor yang diblokir atau berisi kata kunci terblokir juga masuk karantina, bukan dibuang, sehingga OTP atau tagihan dari pengirim yang terlanjur diblokir bisa dipulihkan.

Buka menu titik tiga di layar utama, pilih "Karantina (promo dan diblokir)" untuk memulihkan atau menghapus pesan. Pesan karantina dihapus otomatis setelah 7 hari.

## Batasan

- Tidak mendukung RCS, hanya SMS dan MMS.
- Harus dijadikan aplikasi SMS default agar bisa menerima dan menyaring pesan.
- MMS tidak disaring.

## Cara build

Butuh JDK 17.

```
./gradlew assembleFossDebug
```

## Cara berkontribusi

Tambahkan pengirim promo lewat pull request ke `app/src/main/assets/promo_senders.json`. Bug dan usul fitur lewat Issues di https://github.com/zentrahub95/gsms/issues.

## Lisensi

GPLv3, lihat [LICENSE](LICENSE). Berbasis [Fossify Messages](https://github.com/FossifyOrg/Messages); atribusi untuk tim Fossify dipertahankan.
