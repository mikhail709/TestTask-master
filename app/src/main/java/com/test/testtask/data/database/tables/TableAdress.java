package com.test.testtask.data.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.test.testtask.data.database.DatabaseContentProvider;
import com.test.testtask.data.entity.User;

public class TableAdress {
    public static final String TABLE_ADRESS_NAME = "table_adress";
    public static final String COLUMN_ID = "id_adress";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_SUIT = "suit";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_ZIPCODE = "zipcode";
    public static final String COLUMN_GEO_ID = "id_geo";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_ADRESS_NAME + " ("
            + COLUMN_ID + " INTEGER AUTO INCREMENT PRIMARY KEY, "
            + COLUMN_STREET + " TEXT NOT NULL, "
            + COLUMN_SUIT + " TEXT NOT NULL, "
            + COLUMN_CITY + " TEXT NOT NULL, "
            + COLUMN_ZIPCODE + " TEXT NOT NULL, "
            + COLUMN_GEO_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_GEO_ID + ") REFERENCES "
            + TableGeo.TABLE_GEO_NAME + " (" + TableGeo.COLUMN_ID + "))";

    public static final Uri CONTENT_URI = DatabaseContentProvider.BASE_CONTENT_URI.buildUpon()
            .appendPath(TABLE_ADRESS_NAME).build();

    public static void onCreate (SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade (SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ADRESS_NAME + "'");
    }

    public static ContentValues getContentValues (User.Address address, long id_geo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STREET,  address.getStreet());
        contentValues.put(COLUMN_SUIT, address.getSuite());
        contentValues.put(COLUMN_CITY, address.getCity());
        contentValues.put(COLUMN_ZIPCODE, address.getZipcode());
        contentValues.put(COLUMN_GEO_ID, id_geo);
        return contentValues;
    }

}
