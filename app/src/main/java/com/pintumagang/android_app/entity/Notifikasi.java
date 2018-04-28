package com.pintumagang.android_app.entity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pintumagang.android_app.R;

public class Notifikasi {

    private String title;
    private String message;

    public Notifikasi(String title, String message) {
        this.title = title;
        this.message = message;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getmessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}

