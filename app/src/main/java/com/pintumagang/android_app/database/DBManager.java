package com.pintumagang.android_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager {
    private final int DB_VERSION = 1;

    private Context mContext = null;
    private SQLiteOpenHelper mOpener = null;
    private SQLiteDatabase mDbController = null;
    private String sqlDbCreateTable = "";

    public DBManager(Context mContext, String sqlDbCreateTable, String dbName) {
        this.mContext = mContext;
        this.sqlDbCreateTable = sqlDbCreateTable;
        this.mOpener = new OpenHelper(mContext, dbName, null, DB_VERSION);
    }

    private class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, dbName, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlDbCreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public SQLiteDatabase dbOpen() {
        this.mDbController = mOpener.getWritableDatabase();
        return mDbController;
    }

    public void dbClose() {
        this.mDbController.close();
    }
}
