package com.pintumagang.android_app.entity;

public class Prodi {
    private int id_prodi;
    private String nama_prodi, logo_prodi;

    public Prodi(int id_prodi, String nama_prodi, String logo_prodi) {
        this.id_prodi = id_prodi;
        this.nama_prodi = nama_prodi;
        this.logo_prodi = logo_prodi;
    }

    public int getId() {
        return id_prodi;
    }

    public String getNama_prodi() {
        return nama_prodi;
    }

    public String getLogo_prodi() {
        return logo_prodi;
    }
}
