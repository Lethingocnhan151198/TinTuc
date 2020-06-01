package com.example.tintc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tintc.config.Constant;
import com.example.tintc.model.Article;
import com.example.tintc.model.News;
import com.example.tintc.utils.BitmapUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranTien
 * Date 06/01/2020.
 */
public class NewsModify {
    private static NewsModify mInstance;
    private Context mContext;

    private SQLiteDatabase mDbRead;
    private SQLiteDatabase mDbWrite;
    private SqliteHelper mDbHelper;

    private NewsModify(Context context) {
        mContext = context;
        mDbHelper = new SqliteHelper(context);
        mDbRead = mDbHelper.getReadableDatabase();
        mDbWrite = mDbHelper.getWritableDatabase();
    }

    public static NewsModify getInstance(@NonNull Context context) {
        if (mInstance == null) {
            mInstance = new NewsModify(context);
        }
        return mInstance;
    }

    private void insertNews(@NonNull News news) {
        ContentValues values = new ContentValues();
        Toast.makeText(mContext, "Chung cc", Toast.LENGTH_SHORT).show();

        values.put(SqliteHelper.URL_NEWS, news.getUrl());
        values.put(SqliteHelper.HTML_NEWS, news.getHtml());

        mDbWrite.insert(SqliteHelper.TABLE_NEWS, null, values);
    }

    private void insertArticle(@NonNull Article article) throws IOException {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.URL, article.getUrl());
        values.put(SqliteHelper.SOURCE, article.getSource().getName());
        values.put(SqliteHelper.AUTHOR, article.getAuthor());
        values.put(SqliteHelper.TIME, article.getPublishedAt());
        values.put(SqliteHelper.TITLE, article.getTitle());
        values.put(SqliteHelper.DESCRIPTION, article.getDescription());


        values.put(SqliteHelper.IMAGE, BitmapUtils.convertBitmapToByte(article.getImageBitmap()));
        mDbWrite.insert(SqliteHelper.TABLE_ARTICLE, null, values);
    }

    private static final String TAG = "LOG_NewsModify";

    public void insertNewNews(@NonNull News news, @NonNull Article article) throws IOException {
        List<News> listNews = queryAllNews();

            // delete data if history >= (max size)
        if(listNews.size() >= Constant.HISTORY_MAX_SIZE){

            // query oldest news and delete
            Cursor cursorNews = mDbWrite.query(SqliteHelper.TABLE_NEWS, null, null, null, null, null, null);
            if (cursorNews.moveToFirst()) {
                String url = cursorNews.getString(cursorNews.getColumnIndex(SqliteHelper.URL_NEWS));
                mDbWrite.delete(SqliteHelper.TABLE_ARTICLE, SqliteHelper.URL + " = ?", new String[]{url});

                String selection = SqliteHelper.ID_NEWS + " = ?";
                String[] selectionArgs = {String.valueOf(cursorNews.getInt(cursorNews.getColumnIndex(SqliteHelper.ID_NEWS)))};
                mDbWrite.delete(SqliteHelper.TABLE_NEWS, selection, selectionArgs);
            }
            cursorNews.close();
        }

        Log.d(TAG, "insertNewNews: " + news.toString());
        insertArticle(article);
        insertNews(news);
    }

    public List<Article> queryAllArticle(){
        Cursor cursor = mDbRead.query(SqliteHelper.TABLE_ARTICLE, null, null, null, null, null, null);

        List<Article> articles = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(cursor.isAfterLast()){
                String url = cursor.getString(cursor.getColumnIndex(SqliteHelper.URL));
                String source = cursor.getString(cursor.getColumnIndex(SqliteHelper.SOURCE));
                String author = cursor.getString(cursor.getColumnIndex(SqliteHelper.AUTHOR));
                String time = cursor.getString(cursor.getColumnIndex(SqliteHelper.TIME));
                String title = cursor.getString(cursor.getColumnIndex(SqliteHelper.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(SqliteHelper.DESCRIPTION));

                Bitmap image = BitmapUtils.convertByteToBitmap(cursor.getBlob(cursor.getColumnIndex(SqliteHelper.IMAGE)));

                articles.add(new Article(source, author, title, description, url, image, time));
            }
        }

        return articles;
    }

    public List<News> queryAllNews(){
        Cursor cursor = mDbRead.query(SqliteHelper.TABLE_ARTICLE, null, null, null, null, null, null);

        List<News> news = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(cursor.isAfterLast()){
                int id = cursor.getInt(cursor.getColumnIndex(SqliteHelper.ID_NEWS));
                String url = cursor.getString(cursor.getColumnIndex(SqliteHelper.URL_NEWS));
                String html = cursor.getString(cursor.getColumnIndex(SqliteHelper.HTML_NEWS));

                news.add(new News(id, url, html));
            }
        }

        return news;
    }
}
