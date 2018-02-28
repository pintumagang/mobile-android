package com.pintumagang.android_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button)findViewById(R.id.link_signin);

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        final Intent[] intentt = {null};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("bambang") && pass.equals("bambang")){
                    intentt[0] = new Intent(LoginActivity.this, MainActivity.class);

                    Bundle b = new Bundle();
                    b.putString("username", user);
                    intentt[0].putExtras(b);

                    finish();
                    startActivity(intentt[0]);
                }
                else{
                    Toast.makeText( LoginActivity.this,"Username atau password salah!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void click_link(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.link_instagram:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ari.bambang"));
                break;
            case R.id.link_sign_up:
                intent = new Intent(this, RegisterActivity.class);
                finish();
                break;





        }
        startActivity(intent);
    }
}
