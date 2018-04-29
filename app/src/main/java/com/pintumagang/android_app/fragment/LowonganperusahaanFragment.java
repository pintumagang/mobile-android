package com.pintumagang.android_app.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.URLs;
import com.pintumagang.android_app.entity.Lowongan;

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
public class LowonganperusahaanFragment extends Fragment {

    private EditText editTextFilter;
    private List<Lowongan> lowonganList;
    private List<Lowongan> lowonganFilter;
    private LowonganperusahaanAdapter mAdapter;
    private View rootView;
    private AdView mAdView;
    public RecyclerView recyclerView;
    private String id_perusahaan;
    private SwipeRefreshLayout swipeRefreshLayout;

    public LowonganperusahaanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lowonganperusahaan, container, false);

        MobileAds.initialize(getActivity(), "ca-app-pub-3679403662348605/5386502676");
        mAdView = (AdView) rootView.findViewById(R.id.adView);

        id_perusahaan = (String) getArguments().getSerializable("id_perusahaan");



        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        lowonganList = new ArrayList<Lowongan>();
        lowonganList = loadLowongan();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        EditText search_view = (EditText) rootView.findViewById(R.id.search_view);


        //lowonganFilter.addAll(lowonganList);
        mAdapter = new LowonganperusahaanAdapter(getActivity(), lowonganList);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lowonganList.clear();
                loadLowongan();

            }
        });

        return rootView;
    }

    private List<Lowongan> loadLowongan() {
        final List<Lowongan> listLowongan = new ArrayList<Lowongan>();

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);

        //Displaying Progressbar
        //progressBar.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminate(true);
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOWONGAN_PERUSAHAN_LIST,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            if(array.length()==0){
                                Toast.makeText(getContext(), "Tidak ada lowongan", Toast.LENGTH_SHORT).show();
                            }

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject lowongan = array.getJSONObject(i);

                                //adding the product to product list
                                lowonganList.add(new Lowongan(
                                        lowongan.getInt("id_lowongan"),
                                        lowongan.getString("nama_lowongan"),
                                        lowongan.getInt("id_perusahaan"),
                                        lowongan.getString("waktu_input"),
                                        lowongan.getString("lokasi"),
                                        lowongan.getString("nama_perusahaan"),
                                        lowongan.getString("logo"),
                                        lowongan.getString("deadline_submit"),
                                        lowongan.getString("deskripsi")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            LowonganperusahaanAdapter adapter = new LowonganperusahaanAdapter(getActivity(), lowonganList);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(rootView.GONE);
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(rootView.GONE);
                        Toast.makeText(getContext(), "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_perusahaan", id_perusahaan);
                return params;
            }
        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
        return lowonganList;
    }



    public class LowonganperusahaanAdapter extends RecyclerView.Adapter<LowonganperusahaanAdapter.LowonganperusahaanViewHolder> {

        private Context mCtx;
        private List<Lowongan> lowonganList = new ArrayList<>();
        private List<Lowongan> lowonganFilter = new ArrayList<>();
        //private CustomFilter mFilter;

        public LowonganperusahaanAdapter(Context mCtx, List<Lowongan> lowonganList) {
            this.mCtx = mCtx;
            this.lowonganList = lowonganList;
            //lowonganFilter =lowonganList;
            //  mFilter = new CustomFilter(LowonganAdapter.this);
        }

        @Override
        public LowonganperusahaanAdapter.LowonganperusahaanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.lowongan_list, null);
            return new LowonganperusahaanAdapter.LowonganperusahaanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LowonganperusahaanAdapter.LowonganperusahaanViewHolder holder, final int position) {
            final Lowongan lowongan = lowonganList.get(position);


            //loading the image
            Glide.with(mCtx)
                    .load(lowongan.getLogo())
                    .into(holder.imageView);

            holder.textViewNamaLowongan.setText(lowongan.getNama_lowongan());
            holder.textViewNamaPerusahaan.setText(lowongan.getNama_perusahaan());
            holder.textViewLokasi.setText(String.valueOf(lowongan.getLokasi()));
            if (lowongan.getWaktu_input().equals("0")) {
                holder.textViewWaktuInput.setText(String.valueOf("Baru hari ini"));
            } else {
                holder.textViewWaktuInput.setText(String.valueOf(lowongan.getWaktu_input() + " hari yang lalu"));
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mCtx, "you "+lowongan.getNama_lowongan(), Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    //Lowongan lowonganklik = lowongan;
                    bundle.putSerializable("lowonganValue", lowongan);
                    LowongandetailFragment ldf = new LowongandetailFragment();
                    ldf.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content, ldf);
                    ft.addToBackStack("list");
                    ft.commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return lowonganList.size();
        }

        class LowonganperusahaanViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewNamaLowongan, textViewNamaPerusahaan, textViewLokasi, textViewWaktuInput;
            public ImageView imageView;
            public CardView cardView;

            public LowonganperusahaanViewHolder(View itemView) {
                super(itemView);

                textViewNamaLowongan = (TextView) itemView.findViewById(R.id.textViewNamaLowongan);
                textViewNamaPerusahaan = (TextView) itemView.findViewById(R.id.textViewNamaPerusahaan);
                textViewLokasi = (TextView) itemView.findViewById(R.id.textViewLokasi);
                textViewWaktuInput = (TextView) itemView.findViewById(R.id.textViewWaktuInput);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }

    }
}
