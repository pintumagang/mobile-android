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
import com.pintumagang.android_app.SharedPrefManager;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuntingprofilFragment extends Fragment {

    private View rootView;
    private EditText editTextUsername, editTextEmail, editTextNamadepan, editTextNamabelakang, editTextPerguruantinggi,editTextHp, editTextLinkedin;
    private ImageView imgUser;
    private TextView btn_ubahpassword,btn_updateprofil;

    public SuntingprofilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_suntingprofil, container, false);

        editTextUsername =(EditText)rootView.findViewById(R.id.editTextUsername);
        editTextEmail = (EditText)rootView.findViewById(R.id.editTextEmail);
        editTextNamadepan = (EditText)rootView.findViewById(R.id.editTextNamadepan);
        editTextNamabelakang = (EditText) rootView.findViewById(R.id.editTextNamabelakang);
        editTextPerguruantinggi = (EditText) rootView.findViewById(R.id.editTextPerguruantinggi);
        editTextHp = (EditText)rootView.findViewById(R.id.editTextHp);
        editTextLinkedin = (EditText)rootView.findViewById(R.id.editTextLinkedin);
        imgUser = (ImageView)rootView.findViewById(R.id.imgUser1);
        btn_ubahpassword = (TextView)rootView.findViewById(R.id.btn_ubahpassword);
        btn_updateprofil = (TextView)rootView.findViewById(R.id.btn_updateprofil);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Mahasiswa mahasiswa = SharedPrefManager.getInstance(getActivity()).getMahasiswa();


        String namaDepan = mahasiswa.getNamaDepan();
        String namaBelakang = mahasiswa.getNamaBelakang();
        String perguruan_tinggi = mahasiswa.getPerguruan_tinggi();
        String hp = mahasiswa.getHp();
        String linkedin = mahasiswa.getLinkedin();
        //setting the values to the textviews
        //textViewId.setText(String.valueOf(user.getId()));
        Glide.with(this)
                .load(mahasiswa.getFoto())
                .into(imgUser);
        editTextUsername.setText(user.getUsername());
        editTextEmail.setText(user.getEmail());

        editTextNamadepan.setText(namaDepan);
        editTextNamabelakang.setText(namaBelakang);
        editTextPerguruantinggi.setText(perguruan_tinggi);
        editTextHp.setText(hp);
        editTextLinkedin.setText(linkedin);

        btn_updateprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_ubahpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UbahpasswordFragment ubf = new UbahpasswordFragment();
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, ubf);
                ft.addToBackStack("list");
                ft.commit();
            }
        });

        return rootView;
    }

}
