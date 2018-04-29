package com.pintumagang.android_app.fragment;

import android.content.Context;
import android.util.Log;

import com.pintumagang.android_app.dao.FcmPushDao;
import com.pintumagang.android_app.database.FcmPushDBSqlData;
import com.pintumagang.android_app.entity.FcmPushInfo;

public class NotifikasiService {
    private String title = "";
    private String message = "";
    private String body = "";
    private Context mContext;

    public NotifikasiService(String title, String message, String body) {
        if(title != null) this.title = title;
        if(message != null) this.message = message;
        if(body != null) this.body = body;
    }

    public void notificationDbInsert() {
        Log.e("notification Title", title);
        Log.e("notification Message", message);
        Log.e("notification Body", body);
        FcmPushInfo infoData = null;
        //try {
        infoData = new FcmPushInfo(1, title, body, null);

        FcmPushDao pushDao = new FcmPushDao(mContext);
        pushDao.insertDao(FcmPushDBSqlData.SQL_DB_INSERT_DATA, infoData);

        //}catch(Exception e){
        //    e.printStackTrace();
        //}

    }
}
