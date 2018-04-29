package com.pintumagang.android_app.entity;

public class Lamaran {
    private int id_pelamar;
    private String nama_lowongan;
    private String waktu_input;
    private String status;
    private String nama_perusahaan;

    public Lamaran(int id_pelamar, String nama_lowongan,String nama_perusahaan, String waktu_input, String status) {
        this.id_pelamar = id_pelamar;
        this.nama_lowongan = nama_lowongan;
        this.nama_perusahaan = nama_perusahaan;
        this.waktu_input = waktu_input;
        this.status = status;
    }

    public int getId_pelamar() {
        return id_pelamar;
    }

    public String getNama_lowongan() {
        return nama_lowongan;
    }

    public String getWaktu_input() {
        return waktu_input;
    }

    public String getStatus() {
        return status;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }
}
