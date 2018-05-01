package com.pintumagang.android_app.fragment;

import android.content.Context;
import android.util.Log;

import com.pintumagang.android_app.dao.FcmPushDao;
import com.pintumagang.android_app.database.FcmPushDBSqlData;
import com.pintumagang.android_app.entity.FcmPushInfo;

public class NotifikasiService {
    private int id_user;
    private String title = "";
    private String message = "";
    private Context mContext;

    public NotifikasiService(Context mContext,int id_user,String title, String message) {
        this.mContext = mContext;
        this.id_user = id_user;
        if(title != null) this.title = title;
        if(message != null) this.message = message;
    }

    public void notifikasiDbInsert() {
        Log.e("notification Title", title);
        Log.e("notification Message", message);
        FcmPushInfo infoData = null;

        infoData = new FcmPushInfo(1, id_user, title, message, null);
        FcmPushDao pushDao = new FcmPushDao(mContext);

        pushDao.insertDao(FcmPushDBSqlData.SQL_DB_INSERT_DATA, infoData);

    }
}
