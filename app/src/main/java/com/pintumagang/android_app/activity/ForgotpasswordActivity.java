package com.pintumagang.android_app.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.config.URLs;
import com.pintumagang.android_app.volley.VolleySingleton;

import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotpasswordActivity extends AppCompatActivity {

    private Button btn_reset;
    private EditText editTextEmailUsername;
    private ProgressBar progressbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = (ProgressBar)findViewById(R.id.progressBar1);
        //progressbar.setVisibility(View.INVISIBLE);


        editTextEmailUsername = (EditText)findViewById(R.id.username_email);
        btn_reset = (Button)findViewById(R.id.btn_reset_password);


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
    }

    private void forgotPassword() {
        //first getting the values
        final String username_email = editTextEmailUsername.getText().toString();
        final AlertDialog.Builder Alert_builder = new AlertDialog.Builder(this);
        //Displaying Progressbar
        //validating inputs
        if (TextUtils.isEmpty(username_email)) {
            editTextEmailUsername.setError("Masukkan Username atau Email anda");
            editTextEmailUsername.requestFocus();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this, "Mohon tunggu...","Mengambil data...",false,false);

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //org.json.JSONArray message = obj.getJSONArray("message");

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                Alert_builder.setTitle("Info:");
                                Alert_builder.setMessage(obj.getString("message"));
                                //Alert_builder.setIcon(R.drawable.alert);
                                // adding one button
                                Alert_builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(android.content.DialogInterface dialog, int which) {

                                }
                                });

                                Alert_builder.create();
                                Alert_builder.show();


                                //starting the profile activity
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
                params.put("username_email", username_email);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
