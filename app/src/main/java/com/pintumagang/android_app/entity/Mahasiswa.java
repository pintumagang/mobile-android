package com.pintumagang.android_app.entity;

/**
 * Created by aribambang on 05/03/18.
 */

public class Mahasiswa {
    private int id,id_user;
    private String namaDepan, namaBelakang, perguruan_tinggi,hp,cv;

    public Mahasiswa(int id,int id_user, String namaDepan, String namaBelakang) {
        this.id = id;
        this.id_user = id_user;
        this.namaDepan = namaDepan;
        this.namaBelakang = namaBelakang;
        this.perguruan_tinggi = perguruan_tinggi;
        this.hp = hp;
        this.cv = cv;
    }

    public int getId() {

        return id;
    }

    public int getId_user() {

        return id_user;
    }

    public String getNamaDepan() {

        return namaDepan;
    }

    public String getNamaBelakang() {

        return namaBelakang;
    }

    /*public String getPerguruan_tinggi() {

        return perguruan_tinggi;
    }
    public String getHp() {

        return hp;
    }
    public String getCv() {

        return cv;*/


}
