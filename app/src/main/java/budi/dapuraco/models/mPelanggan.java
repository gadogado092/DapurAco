package budi.dapuraco.models;

import java.io.Serializable;

public class mPelanggan implements Serializable{
    private String id_pelanggan;
    private String nama_pelanggan;
    private String email_pelanggan;
    private String nohp_pelanggan;
    private String password_pelanggan;
    private String photo_pelanggan;

    public void mPelanggan(){

    }
    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getEmail_pelanggan() {
        return email_pelanggan;
    }

    public void setEmail_pelanggan(String email_pelanggan) {
        this.email_pelanggan = email_pelanggan;
    }

    public String getNohp_pelanggan() {
        return nohp_pelanggan;
    }

    public void setNohp_pelanggan(String nohp_pelanggan) {
        this.nohp_pelanggan = nohp_pelanggan;
    }

    public String getPassword_pelanggan() {
        return password_pelanggan;
    }

    public void setPassword_pelanggan(String password_pelanggan) {
        this.password_pelanggan = password_pelanggan;
    }

    public String getPhoto_pelanggan() {
        return photo_pelanggan;
    }

    public void setPhoto_pelanggan(String photo_pelanggan) {
        this.photo_pelanggan = photo_pelanggan;
    }
}
