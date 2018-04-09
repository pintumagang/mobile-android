package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pintumagang.android_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LamarFragment extends Fragment {


    public LamarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lamar, container, false);
    }

}
