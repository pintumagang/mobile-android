package com.pintumagang.android_app.fragment;


import android.Manifest;
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
    String PdfNameHolder, PdfPathHolder, PdfID, namafile;

    public LamarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_lamar, container, false);

        AllowRunTimePermission();

        SelectButton = (TextView) rootView.findViewById(R.id.button);
        UploadButton = (TextView) rootView.findViewById(R.id.button2);
        PdfNameEditText = (TextView) rootView.findViewById(R.id.namacv);

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
                    PdfUploadFunction();
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
            PdfNameEditText.setText(namafile);

            File file = new File(uri.toString());
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                System.out.println(buf);
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public byte[] getFileDataFromDrawable(File bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        return byteArrayOutputStream.toByteArray();
    }




    private void kirimLamaran() {

        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        final android.support.v7.app.AlertDialog.Builder Alert_builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Mahasiswa mahasiswa =SharedPrefManager.getInstance(getActivity()).getMahasiswa();
        //final String id_user = String.valueOf(user.getId());
        final String username = user.getUsername();
        final String id_user = String.valueOf(user.getId());
        final String id_mahasiswa = String.valueOf(mahasiswa.getId());
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Mohon tunggu...","Mengunggah foto...",false,false);

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
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                //params.put("foto", new DataPart(imagename +"_"+username + ".pdf", uri));


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    public void PdfUploadFunction() {

        PdfNameHolder = PdfNameEditText.getText().toString().trim();
        PdfPathHolder = FilePath.getPath(getActivity(), uri);

        if (PdfPathHolder == null) {

            Toast.makeText(getActivity(), "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();

        } else {

            try {

                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(getActivity(), PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "pdf")
                        .addParameter("name", PdfNameHolder)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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
