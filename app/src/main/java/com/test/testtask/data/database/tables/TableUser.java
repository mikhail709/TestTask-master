package com.test.testtask.data.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.test.testtask.data.database.DatabaseContentProvider;
import com.test.testtask.data.entity.User;

public class TableUser {
    public static final String TABLE_USER_NAME = "table_user";
    public static final String COLUMN_ID = "id_user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NIKNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_ADRESS_ID = "id_adress";
    public static final String COLUMN_COMPANY_ID = "id_company";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USER_NAME + " ("
            + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_NIKNAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_PHONE + " TEXT NOT NULL, "
            + COLUMN_WEBSITE + " TEXT NOT NULL, "
            + COLUMN_ADRESS_ID + " INTEGER NOT NULL, "
            + COLUMN_COMPANY_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_ADRESS_ID + ") REFERENCES "
            + TableAdress.TABLE_ADRESS_NAME + " (" + TableAdress.COLUMN_ID + "),"
            + "FOREIGN KEY (" + COLUMN_COMPANY_ID + ") REFERENCES "
            + TableCompany.TABLE_COMPANY_NAME + " (" + TableCompany.COLUMN_ID + "))";

    public static final Uri CONTENT_URI = DatabaseContentProvider.BASE_CONTENT_URI.buildUpon()
            .appendPath(TABLE_USER_NAME).build();

    public static void onCreate (SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade (SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER_NAME + "'");
    }

    public static ContentValues getContentValues (User user, long id_adress, long id_company){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,  user.getId());
        contentValues.put(COLUMN_NAME, user.getName());
        contentValues.put(COLUMN_NIKNAME, user.getUsername());
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_PHONE, user.getPhone());
        contentValues.put(COLUMN_WEBSITE, user.getWebsite());
        contentValues.put(COLUMN_ADRESS_ID, id_adress);
        contentValues.put(COLUMN_COMPANY_ID, id_company);
        return contentValues;
    }

}
