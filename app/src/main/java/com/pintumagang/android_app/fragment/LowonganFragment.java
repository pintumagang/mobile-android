package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.pintumagang.android_app.R;
import com.pintumagang.android_app.entity.Lowongan;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.LowonganAdapter;

import com.pintumagang.android_app.URLs;

/**
 * A simple {@link Fragment} subclass.
 */
public class LowonganFragment extends Fragment {


    public LowonganFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lowongan, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //initializing the productlist
        lowonganList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadLowongan();

        // Inflate the layout for this fragment

        return rootView;
    }


    //a list to store all the products
    List<Lowongan> lowonganList;

    //the recyclerview
    RecyclerView recyclerView;


    private void loadLowongan() {

        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_LOWONGAN_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

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
                                        lowongan.getString("logo")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            LowonganAdapter adapter = new LowonganAdapter(getActivity(), lowonganList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
