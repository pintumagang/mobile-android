package com.pintumagang.android_app.database;

public class FcmPushDBSqlData {
    public static final String SQL_DB_CREATE_TABLE
            = "CREATE TABLE IF NOT EXISTS FCM_PUSH_LOG " +
            "(NO INTEGER PRIMARY KEY AUTOINCREMENT , ID_USER INTEGER , TITLE TEXT, MESSAGE TEXT, REG_DATE TEXT)";

    public static final String SQL_DB_INSERT_DATA
            = "INSERT INTO FCM_PUSH_LOG (" +
            "ID_USER, TITLE, MESSAGE, REG_DATE" +
            ") VALUES ( " +
            "?, ?, ?, datetime('now','localtime')" +
            ")";

    public static final String SQL_DB_SELECT_ALL
            = "SELECT " +
            "NO, ID_USER, TITLE, MESSAGE, REG_DATE " +
            "FROM FCM_PUSH_LOG " +
            "WHERE ID_USER = ? " +
            "ORDER BY NO";

    public static final String SQL_DB_DELETE_DATA
            = "DELETE FROM FCM_PUSH_LOG " +
            "WHERE NO = ?";
}
