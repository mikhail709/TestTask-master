package com.test.testtask.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.testtask.data.database.tables.TableAdress;
import com.test.testtask.data.database.tables.TableCompany;
import com.test.testtask.data.database.tables.TableGeo;
import com.test.testtask.data.database.tables.TableUser;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context){
        super (context, "NAME", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableGeo.onCreate(db);
        TableAdress.onCreate(db);
        TableCompany.onCreate(db);
        TableUser.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableGeo.onUpgrade(db);
        TableAdress.onUpgrade(db);
        TableCompany.onUpgrade(db);
        TableUser.onUpgrade(db);
        onCreate(db);
    }
}
