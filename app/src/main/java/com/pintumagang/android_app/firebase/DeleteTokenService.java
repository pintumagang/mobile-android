package com.pintumagang.android_app.firebase;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pintumagang.android_app.config.SharedPrefManager;
import com.pintumagang.android_app.config.URLs;
import com.pintumagang.android_app.entity.User;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DeleteTokenService extends IntentService {

    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();

            //FcmInstanceIdService fcmInstanceIdService = new FcmInstanceIdService();
            //fcmInstanceIdService.onTokenRefresh();


            Log.d(TAG, "Getting new token");
            String token = FirebaseInstanceId.getInstance().getToken();
            System.out.println("TOKEN DELETED. NEW TOKEN FROM SERVICE: "+FirebaseInstanceId.getInstance().getToken());
            registerToken(token);



        }
        catch (IOException e)
        {
            System.out.println("INTENT SERVICE IOException");

            e.printStackTrace();
        }
    }


    private void registerToken(String token) {

        User user = SharedPrefManager.getInstance(this).getUser();
        final String id_user = String.valueOf(user.getId());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .add("id_user",id_user)
                .build();

        Request request = new Request.Builder()
                .url(URLs.URL_TOKEN)
                .post(body)
                .build();



        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
