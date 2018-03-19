package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.entity.Lowongan;

/**
 * A simple {@link Fragment} subclass.
 */
public class LowongandetailFragment extends Fragment {
    Lowongan lowonganList;

    public LowongandetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lowongandetail, container, false);

        TextView nama_lowongan = (TextView) rootView.findViewById(R.id.nama_lowongan_detail);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.logo_detail);
        if (getArguments() != null){
            lowonganList = (Lowongan) getArguments().getSerializable("lowonganValue");
        }

        if (lowonganList !=null){
            Glide.with(this)
                    .load(lowonganList.getLogo())
                    .into(imageView);
            String nama = lowonganList.getNama_lowongan().toString();
            nama_lowongan.setText(nama);

        }else {
            System.out.println("tidak ada data");
        }

        // Inflate the layout for this fragment
        return rootView;

    }

}
