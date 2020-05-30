package com.example.tintc.database;
public class SqlliteHelper {
    //version database
    private static final int DATABASE_VERSION = 1;
    // tên database
    private static final String DATABASE_NAME = "Name";
    //tên bảng database
    private static final String TABLE_NEWS = "NEWS";

    private static final String COLUMN_NEWS_ID = "id";
    private static final String COLUMN_NEWS_TITLE = "title";
    private static final String COLUMN_NEWS_URL_CONTENT = "content";
    private static final String COLUMN_NEWS_URLIMAGE = "url";
//
//    private String CREATE_NEWS_TABLE = "CREATE TABLE " +TABLE_NEWS + "(" + COLUMN_NEWS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NEWS_TITLE +" TEXT," +COLUMN_NEWS_IMAGE + " TEXT," +
//
//            ")";
}
