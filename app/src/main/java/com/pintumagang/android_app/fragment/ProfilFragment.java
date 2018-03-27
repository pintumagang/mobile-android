package com.pintumagang.android_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pintumagang.android_app.*;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

import com.pintumagang.android_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    TextView textViewId, textViewUsername, textViewEmail, textViewNamaDepan;
    //rofilFragment context = this;
    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        if (!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }


        textViewId = (TextView)view.findViewById(R.id.textViewId);
        textViewUsername = (TextView)view.findViewById(R.id.textViewUsername);
        textViewEmail = (TextView)view.findViewById(R.id.textViewEmail);
        textViewNamaDepan = (TextView)view.findViewById(R.id.textViewNamaDepan);


        //getting the current user
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Mahasiswa mahasiswa = SharedPrefManager.getInstance(getActivity()).getMahasiswa();


        String namaDepan = mahasiswa.getNamaDepan();
        String namaBelakang = mahasiswa.getNamaBelakang();
        String namaLengkap = namaDepan +" "+namaBelakang;
        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());

        textViewNamaDepan.setText(namaLengkap);

        //when the user presses logout button
        //calling the logout method
        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                SharedPrefManager.getInstance(getActivity().getApplicationContext()).logout();
            }
        });

        // Inflate the layout for this fragment
        return view;



    }


}
