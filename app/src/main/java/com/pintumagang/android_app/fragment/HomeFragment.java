package com.pintumagang.android_app.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pintumagang.android_app.*;

import com.pintumagang.android_app._sliders.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private ArrayList<String> id_prodi = new ArrayList<String>();
    private ArrayList<String> logo_prodi = new ArrayList<String>();
    private ArrayList<String> nama_prodi = new ArrayList<String>();

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    private RelativeLayout prodi_if;

    ExpandableHeightGridView gridView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = (SliderView) rootView.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.pagesContainer);
        getSlider();
        //setupSlider();

        gridView = (ExpandableHeightGridView)rootView.findViewById(R.id.griview);
        getData();
        gridView.setExpanded(true);
        // Inflate the layout for this fragment
        return rootView;
    }


    private void setupSlider() {

        sliderView.setDurationScroll(1000);
        List<Fragment> fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-1.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));


        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    private void getSlider(){
        //Showing a progress dialog while our app fetches the data from url
        //final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...","Fetching data...",false,false);
        sliderView.setDurationScroll(1000);
        final List<Fragment> fragments = new ArrayList<>();


        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLs.URL_SLIDER_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //loading.dismiss();
                        for(int i = 0; i<response.length(); i++){
                            //Creating a json object of the current index
                            JSONObject obj = null;
                            try {
                                //getting json object from current index
                                obj = response.getJSONObject(i);

                                //getting image url and title from json object
                                String foto = obj.getString("foto_slider");
                                fragments.add(FragmentSlider.newInstance(foto));
                                System.out.println(fragments);

                                //System.out.println(obj.getString("nama_prodi"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Displaying our grid
                        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
                        sliderView.setAdapter(mAdapter);
                        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
                        mIndicator.setPageCount(fragments.size());
                        mIndicator.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        //final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...","Fetching data...",false,false);
        id_prodi.clear();
        nama_prodi.clear();
        logo_prodi.clear();

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URLs.URL_PRODI_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        //loading.dismiss();
                        for(int i = 0; i<response.length(); i++){
                            //Creating a json object of the current index
                            JSONObject obj = null;
                            try {
                                //getting json object from current index
                                obj = response.getJSONObject(i);

                                //getting image url and title from json object
                                id_prodi.add(obj.getString("id_prodi"));
                                logo_prodi.add(obj.getString("logo_prodi"));
                                nama_prodi.add(obj.getString("nama_prodi"));
                                //System.out.println(obj.getString("nama_prodi"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Displaying our grid
                        //showGrid(response);
                        GridAdapter gridViewAdapter = new GridAdapter(getActivity(),id_prodi,nama_prodi,logo_prodi);
                        gridView.setAdapter(gridViewAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    public class GridAdapter extends BaseAdapter {

        Context context;
        //private final String [] values;
        //private final int [] images;
        private ImageLoader imageLoader;
        LayoutInflater layoutInflater;
        private ArrayList<String> id_prodi;
        private ArrayList<String> logo_prodi;
        private ArrayList<String> nama_prodi;

        public GridAdapter(Context context, ArrayList<String> id_prodi, ArrayList<String> nama_prodi, ArrayList<String> logo_prodi) {
            this.context = context;
            this.id_prodi= id_prodi;
            this.nama_prodi = nama_prodi;
            this.logo_prodi = logo_prodi;
        }

        @Override
        public int getCount() {
            return logo_prodi.size();
        }

        @Override
        public Object getItem(int position) {
            return logo_prodi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //NetworkImageView networkImageView = new NetworkImageView(context);

            //Initializing ImageLoader
            //imageLoader = VolleyGridRequest.getInstance(context).getImageLoader();
            //imageLoader.get(logo_prodi.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

            GridAdapter.Holder holder = new GridAdapter.Holder();
            View rowView;

            rowView = layoutInflater.inflate(R.layout.grid_item, null);
            holder.tv =(TextView) rowView.findViewById(R.id.textview);
            holder.img = (ImageView) rowView.findViewById(R.id.imageview);

            holder.tv.setText(nama_prodi.get(position));
            Glide.with(context)
                    .load(logo_prodi.get(position))
                    .into(holder.img);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(context, "You Clicked "+id_prodi.get(position), Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("prodiValue", id_prodi.get(position));
                    LowonganFragment lf = new LowonganFragment();
                    lf.setArguments(bundle);
                    // Toast.makeText(getActivity(), "Klik me",Toast.LEgcNGTH_SHORT).show();
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content, lf);
                    ft.addToBackStack("list");
                    ft.commit();
                }
            });

            return rowView;
        }

        public class Holder
        {
            TextView tv;
            ImageView img;
        }

    }






}
