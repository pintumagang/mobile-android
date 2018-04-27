package com.pintumagang.android_app.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.SharedPrefManager;
import com.pintumagang.android_app.URLs;
import com.pintumagang.android_app.VolleyMultipartRequest;
import com.pintumagang.android_app.VolleySingleton;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuntingprofilFragment extends Fragment {

    private View rootView;
    private EditText editTextUsername, editTextEmail, editTextNamadepan, editTextNamabelakang, editTextPerguruantinggi,editTextHp, editTextLinkedin;
    private ImageView imgUser;
    private TextView btn_ubahpassword,btn_updateprofil,btn_ubahfoto;

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
        btn_ubahfoto = (TextView)rootView.findViewById(R.id.btn_ubahfoto);

        editTextEmail.setFocusableInTouchMode(true);
        editTextEmail.requestFocus();
        editTextNamadepan.setFocusableInTouchMode(true);
        editTextNamadepan.requestFocus();
        editTextPerguruantinggi.setFocusableInTouchMode(true);
        editTextPerguruantinggi.requestFocus();
        editTextHp.setFocusableInTouchMode(true);
        editTextHp.requestFocus();
        editTextLinkedin.setFocusableInTouchMode(true);
        editTextLinkedin.requestFocus();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Izinkan akses galeri", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getActivity().getPackageName()));
            getActivity().finish();
            startActivity(intent);

        }
        btn_ubahfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });



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
        editTextHp.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextLinkedin.setText(linkedin);

        btn_updateprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_profil();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                //displaying selected image to imageview
                imgUser.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        final android.support.v7.app.AlertDialog.Builder Alert_builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Mahasiswa mahasiswa =SharedPrefManager.getInstance(getActivity()).getMahasiswa();
        final String username = user.getUsername();
        final String id_mahasiswa = String.valueOf(mahasiswa.getId());
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Mohon tunggu...","Mengunggah foto...",false,false);

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_UBAH_FOTO,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        loading.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            //Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            Alert_builder.setTitle("Info:");
                            Alert_builder.setMessage(obj.getString("message"));
                            //Alert_builder.setIcon(R.drawable.alert);
                            // adding one button
                            Alert_builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(android.content.DialogInterface dialog, int which) {

                                }
                            });

                            Alert_builder.create();
                            Alert_builder.show();

                            SharedPrefManager.getInstance(getActivity().getApplicationContext()).ubahFotoProfil(obj.getString("link_foto"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                long imagename = System.currentTimeMillis();
                String link_foto = "http://pintumagang.jktserver.com/mobile-android/image_mahasiswa/"+imagename+"_"+username + ".png";
                params1.put("id_mhs", id_mahasiswa);
                params1.put("link_foto", link_foto);

                return params1;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                params.put("foto", new DataPart(imagename +"_"+username + ".png", getFileDataFromDrawable(bitmap)));


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    private void update_profil() {
        //first getting the values
        //final String username_email = editTextEmailUsername.getText().toString();
        final android.support.v7.app.AlertDialog.Builder Alert_builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        final User user = SharedPrefManager.getInstance(getActivity()).getUser();
        final String id_user = String.valueOf(user.getId());

        final String email = editTextEmail.getText().toString();
        final String namadepan = editTextNamadepan.getText().toString();
        final String namabelakang = editTextNamabelakang.getText().toString();
        final String perguruantinggi = editTextPerguruantinggi.getText().toString();
        final String hp = editTextHp.getText().toString();
        final String linkedin = editTextLinkedin.getText().toString();


        //validating inputs
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Masukkan email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(namadepan)) {
            editTextNamadepan.setError("Masukkan nama depan");
            editTextNamadepan.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(namabelakang)) {
            editTextNamabelakang.setError("Masukkan nama belakang");
            editTextNamabelakang.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(perguruantinggi)) {
            editTextPerguruantinggi.setError("Masukkan perguruan tinggi");
            editTextPerguruantinggi.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(hp)) {
            editTextNamabelakang.setError("Masukkan hp");
            editTextNamabelakang.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(linkedin)) {
            editTextNamabelakang.setError("Masukkan linkedin atau jika tidak punya masukkan -");
            editTextNamabelakang.requestFocus();
            return;
        }


        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Mohon tunggu...","Memperbarui profil...",false,false);

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_PROFIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //progressbar.setVisibility(View.GONE);
                        //progressBar.setVisibility(View.VISIBLE);
                        //setProgressBarIndeterminate(true);


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //org.json.JSONArray message = obj.getJSONArray("message");

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Alert_builder.setTitle("Info:");
                                Alert_builder.setMessage(obj.getString("message"));
                                //Alert_builder.setIcon(R.drawable.alert);
                                // adding one button
                                Alert_builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(android.content.DialogInterface dialog, int which) {

                                    }
                                });

                                Alert_builder.create();
                                Alert_builder.show();

                                JSONObject userJson = obj.getJSONObject("user");
                                JSONObject mahasiswaJson = obj.getJSONObject("mahasiswa");
                                /// Toast.makeText(getApplicationContext(),Integer.valueOf(userJson.getInt("id_user")),Toast.LENGTH_SHORT).show();
                                //creating a new user object


                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).ubahEmailUser(userJson.getString("email"));
                                SharedPrefManager.getInstance(getActivity().getApplicationContext()).ubahProfilMahasiswa(mahasiswaJson.getString("nama_depan"),
                                        mahasiswaJson.getString("nama_belakang"),
                                        mahasiswaJson.getString("perguruan_tinggi"),
                                        mahasiswaJson.getString("hp"),
                                        mahasiswaJson.getString("linkedin"));

                                //starting the profile activity
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", id_user);
                params.put("email_user", email);
                params.put("nama_depan",namadepan);
                params.put("nama_belakang",namabelakang);
                params.put("perguruan_tinggi",perguruantinggi);
                params.put("hp",hp);
                params.put("linkedin",linkedin);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
