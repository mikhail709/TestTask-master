package com.test.testtask.data.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.test.testtask.data.database.DatabaseContentProvider;
import com.test.testtask.data.entity.User;

public class TableCompany {
    public static final String TABLE_COMPANY_NAME = "company";
    public static final String COLUMN_ID = "id_company";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATCH_PHRASE = "catch_phrase";
    public static final String COLUMN_BS = "bs";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_COMPANY_NAME + " ("
            + COLUMN_ID + " INTEGER AUTO INCREMENT PRIMARY KEY, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_CATCH_PHRASE + " TEXT NOT NULL, "
            + COLUMN_BS + " TEXT NOT NULL)";

    public static final Uri CONTENT_URI = DatabaseContentProvider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_COMPANY_NAME).build();

    public static void onCreate (SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade (SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_COMPANY_NAME + "'");
    }

    public static ContentValues getContentValues (User.Company company){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,  company.getName());
        contentValues.put(COLUMN_CATCH_PHRASE, company.getCatchPhrase());
        contentValues.put(COLUMN_BS, company.getBs());
        return contentValues;
    }
}
