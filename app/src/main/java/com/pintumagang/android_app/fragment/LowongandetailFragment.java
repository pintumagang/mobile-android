package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.pintumagang.android_app.entity.Lowongan;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LowongandetailFragment extends Fragment {
    Lowongan lowonganList;
    private AdView mAdView;
    private String id_lowongan,id_perusahaan,namaPerusahaan;

    public LowongandetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lowongandetail, container, false);

        TextView nama_lowongan = (TextView) rootView.findViewById(R.id.isi_nama_lowongan);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.logo_detail);
        final TextView nama_perusahaan = (TextView) rootView.findViewById(R.id.nama_perusahaan_detail);
        TextView lokasi = (TextView) rootView.findViewById(R.id.isi_lokasi_lowongan_detail);
        TextView deadline = (TextView) rootView.findViewById(R.id.isi_deadline_lowongan_detail);
        TextView desc = (TextView) rootView.findViewById(R.id.isi_deskripsi_lowongan_detail);






        MobileAds.initialize(getActivity(), "ca-app-pub-3679403662348605/5386502676");
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (getArguments() != null){
            lowonganList = (Lowongan) getArguments().getSerializable("lowonganValue");
        }

        if (lowonganList !=null){
            Glide.with(this)
                    .load(lowonganList.getLogo())
                    .into(imageView);
            String nama = lowonganList.getNama_lowongan().toString();
            namaPerusahaan = lowonganList.getNama_perusahaan().toString();
            String lokasi_detail = lowonganList.getLokasi().toString();
            String deskripsi = lowonganList.getDeskripsi().toString();
            String deadline_submit = lowonganList.getDeadline_submit();
            id_lowongan = String.valueOf(lowonganList.getId_lowongan());
            id_perusahaan = String.valueOf(lowonganList.getId_perusahaan());


            nama_perusahaan.setText(namaPerusahaan);
            lokasi.setText(lokasi_detail);
            nama_lowongan.setText(nama);
            deadline.setText(deadline_submit);
            desc.setText(deskripsi);

        }else {
            System.out.println("tidak ada data");
        }

        rootView.findViewById(R.id.btn_simpan).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                simpan_lowongan();
            }
        });

        rootView.findViewById(R.id.lihat_perusahaan).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Bundle bundle = new Bundle();
                bundle.putSerializable("id_perusahaan", id_perusahaan);
                PerusahaanFragment pf = new PerusahaanFragment();
                pf.setArguments(bundle);
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, pf);
                ft.addToBackStack("list");
                ft.commit();
            }
        });

        rootView.findViewById(R.id.btn_lamar).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cek_lamar();

            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }

    private void cek_lamar() {
        //first getting the values
        //final String username_email = editTextEmailUsername.getText().toString();
        final AlertDialog.Builder Alert_builder = new AlertDialog.Builder(getActivity());
        Mahasiswa mahasiswa = SharedPrefManager.getInstance(getActivity()).getMahasiswa();
        final String id_mhs = String.valueOf(mahasiswa.getId());
        final String idlowongan = id_lowongan;


        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CEK_LAMAR,
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
                            if (obj.getBoolean("error")) {
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
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("id_lowongan", id_lowongan);
                                bundle.putSerializable("nama_lowongan",lowonganList.getNama_lowongan().toString());
                                bundle.putSerializable("nama_perusahaan",namaPerusahaan);
                                LamarFragment lf = new LamarFragment();
                                lf.setArguments(bundle);
                                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.content, lf);
                                ft.addToBackStack("list");
                                ft.commit();
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
                params.put("id_mhs", id_mhs);
                params.put("id_lowongan", idlowongan);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void simpan_lowongan() {
        //first getting the values
        //final String username_email = editTextEmailUsername.getText().toString();
        final AlertDialog.Builder Alert_builder = new AlertDialog.Builder(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        final String id_user = String.valueOf(user.getId());
        final String idlowongan = id_lowongan;

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SIMPAN_LOWONGAN,
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
                params.put("id_lowongan", idlowongan);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
