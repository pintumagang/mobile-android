package com.pintumagang.android_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.pintumagang.android_app.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView developer = (TextView) findViewById(R.id.textViewDeveloper);

        developer.setText(Html.fromHtml("Ari Bambang Kurniawan (Mobile Engineer) <br>\n" +
                "Jonathan Eprilio (UI Engineer) <br>\n" +
                "Kamaludin Akbar (Web Developer) <br>\n" +
                "Gisella Al Khumaira (Web Developer) <br>\n" +
                "Ilham Prayudha (Database Administrator) <br>\n" +
                "Sulaiman Osman (Web Developer)"));

        TextView alamat = (TextView) findViewById(R.id.textViewAlamatTentang);
        alamat.setText(Html.fromHtml("Institut Teknologi Sumatera<br>\n" +
                "Lampung Selatan, Lampung<br>\n" +
                "Indonesia"));
    }

}
