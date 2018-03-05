package com.pintumagang.android_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pintumagang.android_app.R;

import com.pintumagang.android_app._sliders.FragmentSlider;
import com.pintumagang.android_app._sliders.SliderIndicator;
import com.pintumagang.android_app._sliders.SliderPagerAdapter;
import com.pintumagang.android_app._sliders.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    private RelativeLayout prodi_if;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = (SliderView) rootView.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.pagesContainer);

        prodi_if = (RelativeLayout) rootView.findViewById(R.id.prodi_if);

        prodi_if.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LowonganFragment lf = new LowonganFragment();
               // Toast.makeText(getActivity(), "Klik me",Toast.LENGTH_SHORT).show();
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, lf);
                ft.addToBackStack("list");
                ft.commit();
            }

        });



        setupSlider();
        // Inflate the layout for this fragment
        return rootView;
    }

   /* public View onClick(View view) {

        switch (view.getId()) {
            case R.id.prodi_if:
                NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
                android.support.v4.app.FragmentTransaction fragmentNotificationsTransaction = getChildFragmentManager().beginTransaction();
                fragmentNotificationsTransaction.replace(R.id.content, notifikasiFragment);
                fragmentNotificationsTransaction.commit();
                break;
        }
     }
*/
    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
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

}
