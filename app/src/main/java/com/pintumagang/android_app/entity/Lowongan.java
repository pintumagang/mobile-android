package com.pintumagang.android_app.entity;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by aribambang on 04/03/18.
 */

public class Lowongan extends ArrayList<Parcelable> {
    private int id_lowongan;
    private String nama_lowongan;
    private int id_perusahaan;
    private String waktu_input;
    private String lokasi;
    private String nama_perusahaan;
    private String logo;
    private String deadline_submit;
    private String deskripsi;

    public Lowongan(int id_lowongan, String nama_lowongan, int id_perusahaan, String waktu_input, String lokasi, String nama_perusahaan, String logo,String deadline_submit,String deskripsi) {
        this.id_lowongan = id_lowongan;
        this.nama_lowongan = nama_lowongan;
        this.id_perusahaan = id_perusahaan;
        this.waktu_input = waktu_input;
        this.lokasi = lokasi;
        this.nama_perusahaan = nama_perusahaan;
        this.logo = logo;
        this.deadline_submit = deadline_submit;
        this.deskripsi = deskripsi;
    }

    public int getId_lowongan() {
        return id_lowongan;
    }

    public String getNama_lowongan() {
        return nama_lowongan;
    }

    public int getId_perusahaan() {
        return id_perusahaan;
    }

    public String getWaktu_input() {
        return waktu_input;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }

    public String getLogo() {
        return logo;
    }

    public String getDeadline_submit() {
        return deadline_submit;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}