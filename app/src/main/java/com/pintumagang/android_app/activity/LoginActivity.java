package com.pintumagang.android_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.pintumagang.android_app.R;
import com.pintumagang.android_app.config.SharedPrefManager;
import com.pintumagang.android_app.config.URLs;
import com.pintumagang.android_app.volley.VolleySingleton;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ImageView twitter, facebook, instagram;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        //buttonLogin = (Button)findViewById(R.id.link_signin);
        editTextUsername = (EditText)findViewById(R.id.username);
        editTextPassword = (EditText)findViewById(R.id.password);

        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fb = new Intent(android.content.Intent.ACTION_VIEW);
                fb.setData(Uri.parse("http://www.facebook.com/pintumagang"));
                startActivity(fb);
            }
        });

        findViewById(R.id.twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitter = new Intent(android.content.Intent.ACTION_VIEW);
                twitter.setData(Uri.parse("http://www.twitter.com/pintumagang"));
                startActivity(twitter);
            }
        });

        findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(android.content.Intent.ACTION_VIEW);
                ig.setData(Uri.parse("http://www.instagram.com/pintumagang"));
                startActivity(ig);
            }
        });

        findViewById(R.id.link_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        findViewById(R.id.link_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }


    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Masukkan username anda");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Masukkan password anda");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  progressBar.setVisibility(View.GONE);
                      System.out.println("aaaaaa "+response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                JSONObject mahasiswaJson = obj.getJSONObject("mahasiswa");
                               /// Toast.makeText(getApplicationContext(),Integer.valueOf(userJson.getInt("id_user")),Toast.LENGTH_SHORT).show();
                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id_user"),
                                        userJson.getString("username"),
                                        userJson.getString("email")

                                );

                                Mahasiswa mahasiswa= new Mahasiswa(

                                        mahasiswaJson.getInt("id_mhs"),
                                        mahasiswaJson.getInt("id_user"),
                                        mahasiswaJson.getString("namaDepan"),
                                        mahasiswaJson.getString("namaBelakang"),
                                        mahasiswaJson.getString("foto"),
                                        mahasiswaJson.getString("perguruan_tinggi"),
                                        mahasiswaJson.getString("hp"),
                                        mahasiswaJson.getString("linkedin")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).mahasiswaLogin(mahasiswa);
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void click_link(View view){
        Intent intent = null;
        switch (view.getId()){
            case R.id.link_sign_up:
                intent = new Intent(this, RegisterActivity.class);
                finish();
                break;
            case R.id.link_forgotpassword:
                intent = new Intent(this, ForgotpasswordActivity.class);

                break;
        }
        startActivity(intent);
    }


}
