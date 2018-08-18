package budi.dapuraco;

public class konfigurasi {
    //Dibawah ini merupakan Pengalamatan dimana Lokasi Skrip CRUD PHP disimpan
    //Pada tutorial Kali ini, karena kita membuat localhost maka alamatnya tertuju ke IP komputer
    //dimana File PHP tersebut berada
    //PENTING! JANGAN LUPA GANTI IP SESUAI DENGAN IP KOMPUTER DIMANA DATA PHP BERADA
    public static final String URL="https://dapuraco.000webhostapp.com/";
    public static final String URL_ADD=URL+"Android/pegawai/tambahPgw.php";
    public static final String URL_GET_ALL = URL+"dapuraco/tampilsemuamakanan.php";
    public static final String URL_REGIS = URL+"dapuraco/register.php";
    public static final String URL_LOGIN = URL+"dapuraco/login.php";
    public static final String URL_IMAGE = URL+"dapuraco/gambarmakan/";
    public static final String URL_GET_EMP = URL+"Android/pegawai/tampilPgw.php?id=";
    public static final String URL_UPDATE_EMP = URL+"Android/pegawai/updatePgw.php";
    public static final String URL_DELETE_EMP = URL+"Android/pegawai/hapusPgw.php?id=";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_EMP_ID = "id_menu";
    public static final String KEY_EMP_NAMA = "nama_menu";
    public static final String KEY_EMP_POSISI = "harga_menu"; //desg itu variabel untuk posisi
    public static final String KEY_EMP_GAJIH = "path"; //salary itu variabel untuk gajih

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id_menu";
    public static final String TAG_NAMA = "nama_menu";
    public static final String TAG_HARGA = "harga_menu";
    public static final String TAG_PATH = "path";

    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String EMP_ID = "emp_id";
}
