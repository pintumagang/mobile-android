package com.pintumagang.android_app.fragment;


import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pintumagang.android_app.FilePath;
import com.pintumagang.android_app.MainActivity;
import com.pintumagang.android_app.R;
import com.pintumagang.android_app.SharedPrefManager;
import com.pintumagang.android_app.URLs;
import com.pintumagang.android_app.VolleyMultipartRequest;
import com.pintumagang.android_app.entity.Mahasiswa;
import com.pintumagang.android_app.entity.User;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class LamarFragment extends Fragment {


    TextView PdfNameEditText,SelectButton, UploadButton;
    Uri uri,uri1;
    public static final String PDF_UPLOAD_HTTP_URL = "http://androidblog.esy.es/AndroidJSon/file_upload.php";
    public int PDF_REQ_CODE = 1;
    View rootView;
    File file;
    byte[] bytes;
    String fileName,extension;
    EditText namaperusahaan,namalowongan,infotambahan;
    String PdfNameHolder, PdfPathHolder, PdfID, namafile,nmlowongan,nmperusahaan,idlowongan;

    public LamarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_lamar, container, false);

        AllowRunTimePermission();

        namaperusahaan = (EditText) rootView.findViewById(R.id.editTextNamaPerusahaanLamar);
        namalowongan = (EditText) rootView.findViewById(R.id.editTextNamaLowonganLamar);
        infotambahan = (EditText) rootView.findViewById(R.id.editTextInfoTambahan);
        SelectButton = (TextView) rootView.findViewById(R.id.button);
        UploadButton = (TextView) rootView.findViewById(R.id.button2);
        PdfNameEditText = (TextView) rootView.findViewById(R.id.namacv);


        nmperusahaan = (String) getArguments().getSerializable("nama_perusahaan");
        nmlowongan = (String) getArguments().getSerializable("nama_lowongan");
        idlowongan = (String) getArguments().getSerializable("id_lowongan");
        namaperusahaan.setText(nmperusahaan);
        namalowongan.setText(nmlowongan);


        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // PDF selection code start from here .

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);

            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PdfNameEditText.getText().toString() == ""){
                    Toast.makeText(getActivity(),"Anda belum memasukkan CV", Toast.LENGTH_LONG).show();
                }
                else {
                    kirimLamaran(file);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();


            Cursor returnCursor =getActivity().getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            namafile = returnCursor.getString(nameIndex);

            try {
                file = new File(FilePath.getPath(getActivity(),uri));
                fileName = file.getName();
                PdfNameEditText.setText(fileName);

                extension = fileName.substring(fileName.lastIndexOf("."));




            } catch (Exception e) {
                Toast.makeText(getActivity(), "ERROR " + e.getMessage() + "\n" + e.getCause(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }




    private void kirimLamaran(final File file) {

        final android.support.v7.app.AlertDialog.Builder Alert_builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Mahasiswa mahasiswa =SharedPrefManager.getInstance(getActivity()).getMahasiswa();
        final String username = user.getUsername();
        final String id_user = String.valueOf(user.getId());
        final String id_mahasiswa = String.valueOf(mahasiswa.getId());
        final String info_tambahan = infotambahan.getText().toString();
        System.out.println("info: "+infotambahan.getText().toString());
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Mohon tunggu...","Mengirimkan lamaran...",false,false);

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_KIRIM_LAMARAN,
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


                            //SharedPrefManager.getInstance(getActivity().getApplicationContext()).ubahFotoProfil(obj.getString("link_foto"));


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
                long cvname = System.currentTimeMillis();
                String link_cv = "http://pintumagang.jktserver.com/mobile-android/cv_lamaran/"+cvname+"_"+username + ".pdf";
                params1.put("id_mhs", id_mahasiswa);
                params1.put("link_cv", link_cv);
                params1.put("id_lowongan", idlowongan);
                params1.put("info_tambahan",info_tambahan);

                return params1;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long cvname = System.currentTimeMillis();

                try {
                    params.put("cv", new DataPart(cvname +"_"+username + ".pdf", loadFile(file)));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }




    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(getActivity(),"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {
        switch (RC) {
            case 1:
                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),"Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),"Permission Canceled", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



}
