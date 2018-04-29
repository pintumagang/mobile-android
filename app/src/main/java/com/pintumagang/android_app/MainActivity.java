package com.pintumagang.android_app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.MobileAds;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pintumagang.android_app.entity.User;
import com.pintumagang.android_app.fragment.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private TextView mTextMessage;
    Activity activity = this;
    private final HomeFragment homeFragment = new HomeFragment();
    private final NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
    private final FavoritFragment favoritFragment = new FavoritFragment();
    private final ProfilFragment profilFragment = new ProfilFragment();
    android.support.v4.app.Fragment  active = homeFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (active != homeFragment){

                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content, homeFragment);
                        fragmentTransaction.commit();
                    }

                    else {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(active).show(homeFragment).commit();
                    }
                    active = homeFragment;

                    break;
                case R.id.navigation_notifikasi:
                    if (active != notifikasiFragment){
                        android.support.v4.app.FragmentTransaction fragmentNotificationsTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentNotificationsTransaction.replace(R.id.content, notifikasiFragment);
                        fragmentNotificationsTransaction.commit();

                    }

                    else {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(active).show(notifikasiFragment).commit();
                    }
                    active = notifikasiFragment;
                    break;
                case R.id.navigation_favorit:
                    if (active != favoritFragment){
                        android.support.v4.app.FragmentTransaction fragmentFavoritTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentFavoritTransaction.replace(R.id.content, favoritFragment);
                        fragmentFavoritTransaction.commit();

                    }

                    else {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(active).show(favoritFragment).commit();
                    }

                    active = favoritFragment;
                    break;
                case R.id.navigation_profil:

                    if (active != profilFragment){
                        android.support.v4.app.FragmentTransaction fragmentProfilTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentProfilTransaction.replace(R.id.content, profilFragment);
                        fragmentProfilTransaction.commit();

                    }

                    else {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(active).show(profilFragment).commit();
                    }

                    active = profilFragment;
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment);
        fragmentTransaction.commit();

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseInstanceId.getInstance().getToken();
        Login(FirebaseInstanceId.getInstance().getToken());



    }

    private void Login(final String token) {
        User user = SharedPrefManager.getInstance(this).getUser();
        final String id_user = String.valueOf(user.getId());

        //if everything is fine
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URLs.URL_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Token", token);
                params.put("id_user", id_user);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
