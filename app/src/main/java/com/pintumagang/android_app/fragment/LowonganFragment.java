package com.pintumagang.android_app.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.entity.Lowongan;

import com.pintumagang.android_app.URLs;

/**
 * A simple {@link Fragment} subclass.
 */
public class LowonganFragment extends Fragment{


    private EditText editTextFilter;
    private List<Lowongan> lowonganList;
    private List<Lowongan> lowonganFilter;
    private LowonganAdapter mAdapter;
    private View rootView;
    private AdView mAdView;
    public RecyclerView recyclerView;
    private String prodi;
    private SwipeRefreshLayout swipeRefreshLayout;



    public LowonganFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lowongan, container, false);



        MobileAds.initialize(getActivity(), "ca-app-pub-3679403662348605/5386502676");
        mAdView = (AdView) rootView.findViewById(R.id.adView);





        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        prodi = (String) getArguments().getSerializable("prodiValue");


        lowonganList = new ArrayList<Lowongan>();
        lowonganList = loadLowongan();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        EditText search_view = (EditText) rootView.findViewById(R.id.search_view);


        //lowonganFilter.addAll(lowonganList);
        mAdapter = new LowonganAdapter(getActivity(), lowonganList);
        recyclerView.setAdapter(mAdapter);

        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //initializing the productlist

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lowonganList.clear();
                loadLowongan();

            }
        });

        // Inflate the layout for this fragment

        return rootView;
    }


    //a list to store all the products


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Lowongan> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Lowongan namaLowongan : lowonganList ) {

            //if the existing elements contains the search input
            if (namaLowongan.getNama_lowongan().toLowerCase().contains(text.toLowerCase()) || namaLowongan.getNama_perusahaan().toLowerCase().contains(text.toLowerCase()) || namaLowongan.getLokasi().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                //System.out.println("11"+namaLowongan.getNama_lowongan().toString());
                filterdNames.add(namaLowongan);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        mAdapter.filterList(filterdNames);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOWONGAN_LIST,

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
                            LowonganAdapter adapter = new LowonganAdapter(getActivity(), lowonganList);
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

                params.put("id_prodi", prodi);
                System.out.println(prodi);
                return params;
            }
        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
        return lowonganList;
    }



    public class LowonganAdapter extends RecyclerView.Adapter<LowonganAdapter.LowonganViewHolder> {

        private Context mCtx;
        private List<Lowongan> lowonganList = new ArrayList<>();
        private List<Lowongan> lowonganFilter = new ArrayList<>();
        //private CustomFilter mFilter;

        public LowonganAdapter(Context mCtx, List<Lowongan> lowonganList) {
            this.mCtx = mCtx;
            this.lowonganList = lowonganList;
            //lowonganFilter =lowonganList;
          //  mFilter = new CustomFilter(LowonganAdapter.this);
        }

        @Override
        public LowonganViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.lowongan_list, null);
            return new LowonganViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LowonganViewHolder holder, final int position) {
            final Lowongan lowongan = lowonganList.get(position);



            //loading the image
            Glide.with(mCtx)
                    .load(lowongan.getLogo())
                    .into(holder.imageView);

            holder.textViewNamaLowongan.setText(lowongan.getNama_lowongan());
            holder.textViewNamaPerusahaan.setText(lowongan.getNama_perusahaan());
            holder.textViewLokasi.setText(String.valueOf(lowongan.getLokasi()));
            if(lowongan.getWaktu_input().equals("0")){
                holder.textViewWaktuInput.setText(String.valueOf("Baru hari ini"));
            }else {
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

        class LowonganViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewNamaLowongan, textViewNamaPerusahaan, textViewLokasi, textViewWaktuInput;
            public ImageView imageView;
            public CardView cardView;

            public LowonganViewHolder(View itemView) {
                super(itemView);

                textViewNamaLowongan = (TextView) itemView.findViewById(R.id.textViewNamaLowongan);
                textViewNamaPerusahaan = (TextView) itemView.findViewById(R.id.textViewNamaPerusahaan);
                textViewLokasi = (TextView) itemView.findViewById(R.id.textViewLokasi);
                textViewWaktuInput = (TextView) itemView.findViewById(R.id.textViewWaktuInput);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }


        public void filterList(ArrayList<Lowongan> filteredLowongan){

            this.lowonganList = filteredLowongan;
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
        }
    }
}


