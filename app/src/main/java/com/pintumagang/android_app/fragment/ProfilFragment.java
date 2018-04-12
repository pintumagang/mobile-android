package com.pintumagang.android_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pintumagang.android_app.*;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

import com.pintumagang.android_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    TextView textViewId, textViewUsername, textViewEmail, textViewNama, textViewPerguruanTinggi,textViewHp,textViewLinkedin;
    ImageView imgUser;
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


        //textViewId = (TextView)view.findViewById(R.id.textViewId);
        textViewUsername = (TextView)view.findViewById(R.id.textViewUsername);
        textViewEmail = (TextView)view.findViewById(R.id.textViewEmail);
        textViewNama = (TextView)view.findViewById(R.id.textViewNama);
        imgUser = (ImageView)view.findViewById(R.id.imgUser);
        textViewPerguruanTinggi = (TextView)view.findViewById(R.id.textViewPT);
        textViewHp = (TextView)view.findViewById(R.id.textViewHp);
        textViewLinkedin = (TextView)view.findViewById(R.id.textViewLinkedin);


        //getting the current user
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Mahasiswa mahasiswa = SharedPrefManager.getInstance(getActivity()).getMahasiswa();


        String namaDepan = mahasiswa.getNamaDepan();
        String namaBelakang = mahasiswa.getNamaBelakang();
        String namaLengkap = namaDepan +" "+namaBelakang;
        String perguruan_tinggi = mahasiswa.getPerguruan_tinggi();
        String hp = mahasiswa.getHp();
        String linkedin = mahasiswa.getLinkedin();
        //setting the values to the textviews
        //textViewId.setText(String.valueOf(user.getId()));
        Glide.with(this)
                .load(mahasiswa.getFoto())
                .into(imgUser);
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());

        textViewNama.setText(namaLengkap);
        textViewPerguruanTinggi.setText(perguruan_tinggi);
        textViewHp.setText(hp);
        textViewLinkedin.setText(linkedin);

        //when the user presses logout button
        //calling the logout method
        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                SharedPrefManager.getInstance(getActivity().getApplicationContext()).logout();
            }
        });

        view.findViewById(R.id.buttonSunting).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SuntingprofilFragment spf = new SuntingprofilFragment();
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, spf);
                ft.addToBackStack("list");
                ft.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;



    }


}
