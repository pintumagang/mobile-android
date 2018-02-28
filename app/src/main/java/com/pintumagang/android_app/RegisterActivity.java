package com.pintumagang.android_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void click_link(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.link_instagram:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ari.bambang"));
                break;
            case R.id.link_sign_in:
                intent = new Intent(this, LoginActivity.class);
                finish();
                break;

        }
        startActivity(intent);
    }
}
