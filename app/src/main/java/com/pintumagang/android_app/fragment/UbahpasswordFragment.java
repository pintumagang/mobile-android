package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.config.SharedPrefManager;
import com.pintumagang.android_app.config.URLs;
import com.pintumagang.android_app.volley.VolleySingleton;
import com.pintumagang.android_app.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UbahpasswordFragment extends Fragment {
    View rootView;
    EditText passwordLama,passwordBaru,konfirmasiPassword;
    TextView ubahPassword;


    public UbahpasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ubahpassword, container, false);
        passwordLama= (EditText)rootView.findViewById(R.id.passwordlama);
        passwordBaru= (EditText)rootView.findViewById(R.id.passwordbaru);
        konfirmasiPassword = (EditText)rootView.findViewById(R.id.konfirmasipassword);
        ubahPassword = (TextView)rootView.findViewById(R.id.btn_ubahkatasandi);

        passwordLama.setFocusableInTouchMode(true);
        passwordLama.requestFocus();
        passwordBaru.setFocusableInTouchMode(true);
        passwordBaru.requestFocus();
        konfirmasiPassword.setFocusableInTouchMode(true);
        konfirmasiPassword.requestFocus();



        ubahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_password();
            }
        });
        return rootView;
    }

    private void update_password() {
        //first getting the values
        //final String username_email = editTextEmailUsername.getText().toString();
        final android.support.v7.app.AlertDialog.Builder Alert_builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        final String id_user = String.valueOf(user.getId());

        final String passwdlama = passwordLama.getText().toString();
        final String passwdbaru = passwordBaru.getText().toString();
        final String konfirmasipasswd = konfirmasiPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(passwdlama)) {
            passwordLama.setError("Masukkan password lama");
            passwordLama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwdbaru)) {
            passwordBaru.setError("Masukkan password baru");
            passwordBaru.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(konfirmasipasswd)) {
            konfirmasiPassword.setError("Masukkan password baru");
            konfirmasiPassword.requestFocus();
            return;
        }

        if (!konfirmasiPassword.getText().toString().equals(passwordBaru.getText().toString())){

            konfirmasiPassword.setError("Password tidak cocok");
            konfirmasiPassword.requestFocus();
            return;
        }


        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UBAH_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressbar.setVisibility(View.GONE);
                        //progressBar.setVisibility(View.VISIBLE);
                        //setProgressBarIndeterminate(true);


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
                                passwordBaru.setText("");
                                passwordLama.setText("");
                                konfirmasiPassword.setText("");

                                //starting the profile activity
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("password_lama", passwdlama);
                params.put("password_baru",passwdbaru);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
