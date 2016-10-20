package com.test.testtask.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.test.testtask.data.database.DatabaseContentProvider;
import com.test.testtask.data.database.tables.TableAdress;
import com.test.testtask.data.database.tables.TableCompany;
import com.test.testtask.data.database.tables.TableGeo;
import com.test.testtask.data.database.tables.TableUser;
import com.test.testtask.utils.TestTaskApplication;
import com.test.testtask.data.entity.Post;
import com.test.testtask.data.entity.User;
import com.test.testtask.data.network.RestService;
import com.test.testtask.data.network.ServiceGenerator;

import java.util.List;

import retrofit2.Call;

public class DataManager {

    private static DataManager instance = null;

    private RestService restService;
    private ContentResolver contentResolver;

    private DataManager(){
        this.restService = ServiceGenerator.createService(RestService.class);
        this.contentResolver = TestTaskApplication.getAppContext().getContentResolver();
    }

    public static DataManager getInstance(){
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public Call<List<Post>> getAllPosts(){
        return this.restService.getPostList();
    }

    public Call<User> getUserById(int userID){
        return this.restService.getUserInfo(userID);
    }

    public boolean findUserByID(long userID){
        final String request = "SELECT * FROM "
                + TableUser.TABLE_USER_NAME
                + " WHERE "
                + TableUser.COLUMN_ID + " = " + userID + ";";
        Cursor cursor = contentResolver.query(DatabaseContentProvider.BASE_CONTENT_URI, null,request, null, null);
        return cursor.moveToFirst();
    }

    public long saveUser(User user) {
        long idGeo = saveGeo(user.getAddress().getGeo());
        long idAdress = saveAdress(user.getAddress(), idGeo);
        long idCompany = saveCompany(user.getCompany());

        ContentValues contentValues = TableUser.getContentValues(user, idAdress, idCompany);
        return saveValues(contentValues, TableUser.CONTENT_URI);
    }

    private long saveGeo(User.Geo geo){
        ContentValues contentValues = TableGeo.getContentValues(geo);
        return saveValues(contentValues, TableGeo.CONTENT_URI);
    }

    private long saveAdress(User.Address adress, long id_geo){
        ContentValues contentValues = TableAdress.getContentValues(adress, id_geo);
        return saveValues(contentValues, TableAdress.CONTENT_URI);
    }

    private long saveCompany(User.Company company){
        ContentValues contentValues = TableCompany.getContentValues(company);
        return saveValues(contentValues, TableCompany.CONTENT_URI);
    }

    public int updateUser(User user) {
        long idGeo = saveGeo(user.getAddress().getGeo());
        long idAdress = saveAdress(user.getAddress(), idGeo);
        long idCompany = saveCompany(user.getCompany());

        String whereStr = TableUser.COLUMN_ID + " = " + user.getId();
        return updateValues(TableUser.getContentValues(user, idAdress, idCompany), TableUser.CONTENT_URI, whereStr, null);
    }

    private long saveValues(ContentValues contentValues, Uri contentUri) {
        Uri uri = contentResolver.insert(contentUri, contentValues);
        return ContentUris.parseId(uri);
    }

    private int updateValues(ContentValues contentValues, Uri contentUri, String where, String[] args) {
        return contentResolver.update(contentUri, contentValues, where, args);
    }
}
