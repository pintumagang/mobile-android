package com.pintumagang.android_app.database;

public class FcmPushDBSqlData {
    public static final String SQL_DB_CREATE_TABLE
            = "CREATE TABLE IF NOT EXISTS FCM_PUSH_LOG " +
            "(NO INTEGER PRIMARY KEY AUTOINCREMENT , TITLE TEXT, BODY TEXT, REG_DATE TEXT)";

    public static final String SQL_DB_INSERT_DATA
            = "INSERT INTO FCM_PUSH_LOG (" +
            "TITLE, BODY, REG_DATE" +
            ") VALUES ( " +
            "?, ?, datetime('now','localtime')" +
            ")";

    public static final String SQL_DB_SELECT_ALL
            = "SELECT " +
            "NO, TITLE, BODY, REG_DATE " +
            "FROM FCM_PUSH_LOG " +
            "ORDER BY NO";

    public static final String SQL_DB_DELETE_DATA
            = "DELETE FROM FCM_PUSH_LOG " +
            "WHERE NO = ?";
}
