package com.pintumagang.android_app.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.SharedPrefManager;
import com.pintumagang.android_app.URLs;
import com.pintumagang.android_app.VolleySingleton;
import com.pintumagang.android_app.entity.Lamaran;
import com.pintumagang.android_app.entity.Lowongan;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatuslamarFragment extends Fragment {

    private EditText editTextFilter;
    private List<Lamaran> lamaranList;
    private LamaranAdapter mAdapter;
    private View rootView;
    private AdView mAdView;
    public RecyclerView recyclerView;

    public StatuslamarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_statuslamar, container, false);
        // Inflate the layout for this fragment
        MobileAds.initialize(getActivity(), "ca-app-pub-3679403662348605/5386502676");
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lamaranList = new ArrayList<Lamaran>();
        lamaranList = loadFavorit();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        mAdapter = new LamaranAdapter(getActivity(), lamaranList);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private List<Lamaran> loadFavorit() {
        final List<Lamaran> listLamaran = new ArrayList<Lamaran>();

        //final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        Mahasiswa mahasiswa = SharedPrefManager.getInstance(getActivity()).getMahasiswa();
        final String id_mhs = String.valueOf(mahasiswa.getId());
        //Displaying Progressbar
        //progressBar.setVisibility(View.VISIBLE);
        //getActivity().setProgressBarIndeterminate(true);
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_STATUS_LAMAR,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject lamaran = array.getJSONObject(i);

                                //adding the product to product list
                                lamaranList.add(new Lamaran(
                                        lamaran.getInt("id_pelamar"),
                                        lamaran.getString("nama_lowongan"),
                                        lamaran.getString("nama_perusahaan"),
                                        lamaran.getString("waktu_input"),
                                        lamaran.getString("status_lamar")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            LamaranAdapter adapter = new LamaranAdapter(getActivity(), lamaranList);
                            recyclerView.setAdapter(adapter);
                            //progressBar.setVisibility(rootView.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBar.setVisibility(rootView.GONE);
                        Toast.makeText(getContext(), "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_mhs", id_mhs);
                return params;
            }
        };

        //adding our stringrequest to queue
        //Volley.newRequestQueue(getActivity()).add(stringRequest);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        return lamaranList;
    }

    public class LamaranAdapter extends RecyclerView.Adapter<LamaranAdapter.LamaranViewHolder> {

        private Context mCtx;
        private List<Lamaran> lamaranList = new ArrayList<>();
        private List<Lowongan> lowonganFilter = new ArrayList<>();
        //private CustomFilter mFilter;

        public LamaranAdapter(Context mCtx, List<Lamaran> lamaranList) {
            this.mCtx = mCtx;
            this.lamaranList = lamaranList;
        }

        @Override
        public LamaranViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.lamaran_list, null);
            return new LamaranViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LamaranViewHolder holder, final int position) {
            final Lamaran lamaran = lamaranList.get(position);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());


            //loading the image
            holder.textViewNamaLowongan.setText(lamaran.getNama_lowongan());
            holder.textViewNamaPerusahaan.setText(lamaran.getNama_perusahaan());

            if(lamaran.getStatus().equals("Baru")){
                holder.textViewStatusLamar.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTulisan));
                holder.textViewStatusLamar.setText(String.valueOf("Status: "+lamaran.getStatus()));
            }else {
                holder.textViewStatusLamar.setText(String.valueOf("Status: "+lamaran.getStatus()));
            }

                holder.textViewWaktuInput.setText(String.valueOf(lamaran.getWaktu_input()));

        }

        @Override
        public int getItemCount() {
            return lamaranList.size();
        }

        class LamaranViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewNamaLowongan, textViewNamaPerusahaan, textViewStatusLamar, textViewWaktuInput;
            public ImageView imageView,delete_favorit;
            public CardView cardView;

            public LamaranViewHolder(View itemView) {
                super(itemView);

                textViewNamaLowongan = (TextView) itemView.findViewById(R.id.textViewNamaLowonganLamar);
                textViewNamaPerusahaan = (TextView) itemView.findViewById(R.id.textViewNamaPerusahaanLamar);
                textViewStatusLamar = (TextView) itemView.findViewById(R.id.textViewStatusLamar);
                textViewWaktuInput = (TextView) itemView.findViewById(R.id.textViewWaktuInputLamar);
            }
        }

    }

}
