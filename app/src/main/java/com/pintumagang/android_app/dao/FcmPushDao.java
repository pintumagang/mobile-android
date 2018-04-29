package com.pintumagang.android_app.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pintumagang.android_app.database.DBManager;
import com.pintumagang.android_app.database.FcmPushDBSqlData;
import com.pintumagang.android_app.entity.FcmPushInfo;

import java.util.ArrayList;

public class FcmPushDao {
    DBManager dbMng = null;
    SQLiteDatabase dbController = null;

    public FcmPushDao(Context context) {
        dbMng = new DBManager(context, FcmPushDBSqlData.SQL_DB_CREATE_TABLE, "db_push.db");
        Log.e("FcmPush 생성자", FcmPushDBSqlData.SQL_DB_CREATE_TABLE);
        this.dbController = dbMng.dbOpen();

    }

    public void insertDao(String sql, FcmPushInfo info) {
        String[] sqlData = info.getFcmPushInfoArray();
        Log.e("FcmPush insert query", sql);
        this.dbController.execSQL(sql, sqlData);
        dbMng.dbClose();
    }

    // info 해당 position Data 삭제
    public void deleteDao(String sql, int no) {
        String[] sqlData = {Integer.toString(no)};
        Log.e("FcmPush delete query", sql);
        this.dbController.execSQL(sql, sqlData);
        dbMng.dbClose();
    }

    public void selectAllDao(String sql, ArrayList<FcmPushInfo> pushList) {
        Cursor results = this.dbController.rawQuery(sql, null);
        results.moveToFirst();
        Log.e("FcmPush select query", sql);
        while (!results.isAfterLast()) {
            FcmPushInfo info = new FcmPushInfo(
                    results.getInt(0),
                    results.getString(1),
                    results.getString(2),
                    results.getString(3)
            );
            pushList.add(info);
            results.moveToNext();
        }
        results.close();
    }
}
