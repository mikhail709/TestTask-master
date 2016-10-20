package com.test.testtask.data.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.test.testtask.data.database.DatabaseContentProvider;
import com.test.testtask.data.entity.User;

public class TableGeo {
    public static final String TABLE_GEO_NAME = "geo";
    public static final String COLUMN_ID = "id_geo";
    public static final String COLUMN_LATITUDE = "lat";
    public static final String COLUMN_LONGITUDE = "lng";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_GEO_NAME + " ("
            + COLUMN_ID + " INTEGER AUTO INCREMENT PRIMARY KEY, "
            + COLUMN_LATITUDE + " REAL NOT NULL, "
            + COLUMN_LONGITUDE + " REAL NOT NULL )";

    public static final Uri CONTENT_URI = DatabaseContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_GEO_NAME).build();

    public static void onCreate (SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade (SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_GEO_NAME + "'");
    }

    public static ContentValues getContentValues (User.Geo geo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LATITUDE,  geo.getLat());
        contentValues.put(COLUMN_LONGITUDE, geo.getLng());
        return contentValues;
    }
}
