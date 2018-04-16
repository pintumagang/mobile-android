package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.SharedPrefManager;
import com.pintumagang.android_app.URLs;
import com.pintumagang.android_app.VolleySingleton;
import com.pintumagang.android_app.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerusahaanFragment extends Fragment {
    View rootView;
    String id_perusahaan;
    ImageView imgPerusahaan;
    TextView textViewNamaPerusahaan,textViewKotaProvinsi,textViewDeskripsiPerusahaan,textViewJenisPerusahaan,textViewEmail,textViewTelepon,textViewWebsite;


    public PerusahaanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_perusahaan, container, false);
        id_perusahaan = (String) getArguments().getSerializable("id_perusahaan");

        imgPerusahaan = (ImageView) rootView.findViewById(R.id.imgPerusahaan);
        textViewNamaPerusahaan = (TextView) rootView.findViewById(R.id.textViewNamaPerusahaan);
        textViewKotaProvinsi = (TextView) rootView.findViewById(R.id.textViewKotaProvinsi);
        textViewDeskripsiPerusahaan = (TextView) rootView.findViewById(R.id.textViewDeskripsiPerusahaan);
        textViewJenisPerusahaan = (TextView) rootView.findViewById(R.id.textViewJenisPerusahaan);
        textViewEmail = (TextView) rootView.findViewById(R.id.textViewEmail);
        textViewTelepon = (TextView) rootView.findViewById(R.id.textViewTelepon);
        textViewWebsite = (TextView) rootView.findViewById(R.id.textViewWebsite);

        get_detail_perusahaan();

        return rootView;
    }

    private void get_detail_perusahaan() {
        //first getting the values
        //final String username_email = editTextEmailUsername.getText().toString();
        final AlertDialog.Builder Alert_builder = new AlertDialog.Builder(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        id_perusahaan = (String) getArguments().getSerializable("id_perusahaan");
        final String idperusahaan = id_perusahaan;


        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DETAIL_PERUSAHAAN,
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
                                //Toast.makeText(getActivity().getApplicationContext(), obj.getString("perusahaan"), Toast.LENGTH_SHORT).show();

                                JSONObject perusahaanJson = obj.getJSONObject("perusahaan");

                                Glide.with(getActivity())
                                        .load(perusahaanJson.getString("logo"))
                                        .into(imgPerusahaan);

                                textViewNamaPerusahaan.setText(perusahaanJson.getString("nama_perusahaan"));
                                textViewDeskripsiPerusahaan.setText(perusahaanJson.getString("deskripsi"));
                                textViewKotaProvinsi.setText(perusahaanJson.getString("nama_kabkot"));
                                textViewEmail.setText(perusahaanJson.getString("email"));
                                textViewJenisPerusahaan.setText(perusahaanJson.getString("jenis_industri"));

                                textViewWebsite.setText(perusahaanJson.getString("link_website"));

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
                params.put("id_perusahaan", idperusahaan);
                System.out.println(idperusahaan);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
