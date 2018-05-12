package com.pintumagang.android_app.config;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pintumagang.android_app.activity.LoginActivity;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

/**
 * Created by aribambang on 08/03/18.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "pintumagangsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_ID_MAHASISWA = "keyidmahasiswa";
    private static final String KEY_NAMA_DEPAN ="keynamadepan";
    private static final String KEY_NAMA_BELAKANG ="keynamabelakang";
    private static final String KEY_PERGURUAN_TINGGI = "keyperguruantinggi";
    private static final String KEY_HP = "keyhp";
    private static final String KEY_LINKEDIN = "keylinkedin";
    private static final String KEY_FOTO = "keyfoto";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }

    public void mahasiswaLogin(Mahasiswa mahasiswa) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, mahasiswa.getId_user());
        editor.putInt(KEY_ID_MAHASISWA, mahasiswa.getId());
        editor.putString(KEY_NAMA_DEPAN, mahasiswa.getNamaDepan());
        editor.putString(KEY_NAMA_BELAKANG, mahasiswa.getNamaBelakang());
        editor.putString(KEY_FOTO,mahasiswa.getFoto());
        editor.putString(KEY_PERGURUAN_TINGGI, mahasiswa.getPerguruan_tinggi());
        editor.putString(KEY_HP,mahasiswa.getHp());
        editor.putString(KEY_LINKEDIN,mahasiswa.getLinkedin());

        editor.apply();
    }


    public void ubahFotoProfil(String foto){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FOTO,foto);
        editor.apply();
    }

    public void ubahEmailUser(String email){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL,email);
        editor.apply();
    }

    public void ubahProfilMahasiswa(String nama_depan,String nama_belakang,String perguruan_tinggi,String hp, String linkedin) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAMA_DEPAN, nama_depan);
        editor.putString(KEY_NAMA_BELAKANG, nama_belakang);
        editor.putString(KEY_PERGURUAN_TINGGI, perguruan_tinggi);
        editor.putString(KEY_HP, hp);
        editor.putString(KEY_LINKEDIN, linkedin);

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null)
        );
    }

    public Mahasiswa getMahasiswa() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Mahasiswa(

                sharedPreferences.getInt(KEY_ID_MAHASISWA, -1),
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAMA_DEPAN, null),
                sharedPreferences.getString(KEY_NAMA_BELAKANG, null),
                sharedPreferences.getString(KEY_FOTO,null),
                sharedPreferences.getString(KEY_PERGURUAN_TINGGI,null),
                sharedPreferences.getString(KEY_HP,null),
                sharedPreferences.getString(KEY_LINKEDIN,null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
