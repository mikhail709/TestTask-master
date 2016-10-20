package com.test.testtask.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.test.testtask.data.database.tables.TableAdress;
import com.test.testtask.data.database.tables.TableCompany;
import com.test.testtask.data.database.tables.TableGeo;
import com.test.testtask.data.database.tables.TableUser;

public class DatabaseContentProvider extends ContentProvider{
    public static final String AUTHORITY = "com.test.testtask.data.database";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase db;
    private final UriMatcher uriMatcher = buildUriMatcher();

    private static final int GEO = 100;
    private static final int ADRESS = 101;
    private static final int COMPANY = 102;
    private static final int USER = 103;
    private static final int GEO_ID = 104;
    private static final int ADRESS_ID = 105;
    private static final int COMPANY_ID = 106;
    private static final int USER_ID = 107;

    private UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, TableGeo.TABLE_GEO_NAME, GEO);
        matcher.addURI(AUTHORITY, TableGeo.TABLE_GEO_NAME + "/#", GEO_ID);
        matcher.addURI(AUTHORITY, TableAdress.TABLE_ADRESS_NAME, ADRESS);
        matcher.addURI(AUTHORITY, TableAdress.TABLE_ADRESS_NAME + "/#", ADRESS_ID);
        matcher.addURI(AUTHORITY, TableCompany.TABLE_COMPANY_NAME, COMPANY);
        matcher.addURI(AUTHORITY, TableCompany.TABLE_COMPANY_NAME + "/#", COMPANY_ID);
        matcher.addURI(AUTHORITY, TableUser.TABLE_USER_NAME, USER);
        matcher.addURI(AUTHORITY, TableUser.TABLE_USER_NAME + "/#", USER_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new DatabaseOpenHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();
        return db.rawQuery(selection, null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        return getTableName(match);
    }

    private String getTableName(int uriType) {
        switch (uriType) {
            case GEO:
                return TableGeo.TABLE_GEO_NAME;
            case ADRESS:
                return TableAdress.TABLE_ADRESS_NAME;
            case COMPANY:
                return TableCompany.TABLE_COMPANY_NAME;
            case USER:
                return TableUser.TABLE_USER_NAME;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getType(uri);
        if (tableName == null)
            throw new IllegalArgumentException("Wrong URI: " + uri);
        db = databaseOpenHelper.getWritableDatabase();
        long rowID = db.insert(tableName, null, values);
        Uri resultUri = ContentUris.withAppendedId(Uri.parse(BASE_CONTENT_URI.toString() + "/" + tableName), rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = databaseOpenHelper.getWritableDatabase();
        int count = db.update(getType(uri), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
