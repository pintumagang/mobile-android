package com.pintumagang.android_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNamaDepan;
    private EditText editTextNamaBelakang;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNamaDepan = (EditText)findViewById(R.id.firstName);
        editTextNamaBelakang = (EditText)findViewById(R.id.lastName);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


    public void click_link(View view){
        Intent intent = null;
        switch (view.getId()){

            case R.id.link_sign_in:
                intent = new Intent(this, LoginActivity.class);

                break;

        }
        startActivity(intent);
    }


    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String namaDepan = editTextNamaDepan.getText().toString().trim();
        final String namaBelakang = editTextNamaBelakang.getText().toString().trim();

        //final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        //first we will do the validations


        if (TextUtils.isEmpty(namaDepan)) {
            editTextNamaDepan.setError("Masukkan nama depan");
            editTextNamaDepan.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(namaBelakang)) {
            editTextNamaBelakang.setError("Masukkan nama belakang");
            editTextNamaBelakang.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Masukkan username");
            editTextUsername.requestFocus();
            return;
        }



        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Masukkan email anda");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email tidak valid");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Masukkan password");
            editTextPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     //   progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                JSONObject mahasiswaJson = obj.getJSONObject("mahasiswa");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email")
                                );

                                Mahasiswa mahasiswa= new Mahasiswa(
                                        mahasiswaJson.getInt("id"),
                                        mahasiswaJson.getInt("id_user"),
                                        mahasiswaJson.getString("namaDepan"),
                                        mahasiswaJson.getString("namaBelakang"),
                                        mahasiswaJson.getString("perguruan_tinggi"),
                                        mahasiswaJson.getString("hp"),
                                        mahasiswaJson.getString("cv")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                SharedPrefManager.getInstance(getApplicationContext()).mahasiswaLogin(mahasiswa);

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
                params.put("email", email);
                params.put("password", password);
            //    params.put("gender", gender);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
