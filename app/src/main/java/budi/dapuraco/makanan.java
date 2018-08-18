package budi.dapuraco;

public class makanan {
    private String id,nama,harga,path;

    public makanan(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public makanan (String id, String nama, String harga, String path){
        this.id=id;
        this.nama=nama;
        this.harga=harga;
        this.path=path;
    }

}
