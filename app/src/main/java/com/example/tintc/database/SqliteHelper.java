package com.example.tintc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteHelper extends SQLiteOpenHelper {
    //version database
    private static final int DATABASE_VERSION = 1;
    // tên database
    private static final String DATABASE_NAME = "Name";
    //tên bảng database
    public static final String TABLE_NEWS = "NEWS";
    public static final String TABLE_ARTICLE = "ARTICLE";


    public static final String ID_NEWS = "ID_NEWS";
    public static final String URL_NEWS = "URL_NEWS";
    public static final String HTML_NEWS = "HTML_NEWS";

    private static final String CREATE_TABLE_NEWS = "CREATE TABLE " +  SqliteHelper.TABLE_NEWS + " (" +
            SqliteHelper.ID_NEWS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SqliteHelper.URL_NEWS +  " TEXT , " + SqliteHelper.HTML_NEWS + " TEXT);";
    private static final String DELETE_TABLE_NEWS = "DROP TABLE IF EXISTS " + SqliteHelper.TABLE_NEWS;

    public static final String SOURCE = "SOURCE";
    public static final String AUTHOR = "AUTHOR";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String URL = "URL";
    public static final String IMAGE = "IMAGE";
    public static final String TIME = "TIME";

    private static final String CREATE_TABLE_ARTICLE = "CREATE TABLE " +  SqliteHelper.TABLE_ARTICLE + " (" +
            SqliteHelper.URL +  " TEXT PRIMARY KEY, " + SqliteHelper.SOURCE + " TEXT," +
            SqliteHelper.AUTHOR + " TEXT, " + SqliteHelper.TIME + " TEXT, " + SqliteHelper.TITLE + " TEXT,"
            + SqliteHelper.DESCRIPTION + " TEXT, " + SqliteHelper.IMAGE + " BLOB);";
    private static final String DELETE_TABLE_ARTICLE = "DROP TABLE IF EXISTS " + SqliteHelper.TABLE_ARTICLE ;

    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NEWS);
        db.execSQL(CREATE_TABLE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_NEWS);
        db.execSQL(DELETE_TABLE_ARTICLE);
        onCreate(db);
    }
}
