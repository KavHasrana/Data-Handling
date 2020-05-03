package com.example.datahandling.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserInfo.db";

    public DBHelper(Context context) {super(context, DATABASE_NAME, null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UsersMaster.Users.TABLE_NAME + " (" +
                        UsersMaster.Users._ID + "INTEGER PRIMARY KEY," +
                        UsersMaster.Users.COLUMN_NAME_USERNAME + "TEXT," +
                        UsersMaster.Users.COLUMN_NAME_PASSWORD + "TEXT)";
        //Use the details from the UserMaster and Users classes we created.Specify the primary key from the BaseColumns

        db.execSQL (SQL_CREATE_ENTRIES); //This will execute the contents of SQL CREATE ENTRIES
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addInfo(String userName, String password){
        //Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase ();

        //Create a new map of values, where column names the keys
        ContentValues values = new ContentValues ();
        values.put (UsersMaster.Users.COLUMN_NAME_USERNAME, userName);
        values.put (UsersMaster.Users.COLUMN_NAME_PASSWORD, password);

        //Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert (UsersMaster.Users.TABLE_NAME, null,values);

    }

    public List readAllInfo()
    {
        SQLiteDatabase db = getReadableDatabase ();

        //define a projection that specifies columns to the database
        //you will actually use after this query
        String[] projection = {
                UsersMaster.Users._ID,
                UsersMaster.Users.COLUMN_NAME_USERNAME,
                UsersMaster.Users.COLUMN_NAME_PASSWORD
        };
        //Filter results WHERE "UserName" = "SLIIT USER"
//        String selection  = UsersMaster.Users.COLUMN_NAME_USERNAME + " = ?";
//        String[] selectionArgs = {""};

        //How you want the results sorted in the resulting cursor
        String sortOrder = UsersMaster.Users.COLUMN_NAME_USERNAME + "DESC";

        Cursor cursor = db.query (
                UsersMaster.Users.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List userNames = new ArrayList <>();
        List password = new ArrayList <>();

        while (cursor.moveToNext ()){
            String username = cursor.getString (cursor.getColumnIndexOrThrow (UsersMaster.Users.COLUMN_NAME_USERNAME));
            String Password = cursor.getString (cursor.getColumnIndexOrThrow (UsersMaster.Users.COLUMN_NAME_PASSWORD));
            userNames.add (username);
            password.add (Password);
        }
        cursor.close ();
        return userNames;
    }

    //This will delete a particular user from the table
    public void deleteInfo(String userName) {
        SQLiteDatabase db = getReadableDatabase ();
        //Define 'where' part of query
        String selection = UsersMaster.Users.COLUMN_NAME_USERNAME + "LIKE ?";
        //Specify arguments n placeholder order
        String[] selectionArgs = { userName };
        //Issue SQL statement
        db.delete (UsersMaster.Users.TABLE_NAME,selection,selectionArgs);

    }

    //This will update particular user from the table
    public void updateInfo(String userName, String password) {
        SQLiteDatabase db = getReadableDatabase ();

        //New value for one column
        ContentValues values = new ContentValues ();
        values.put (UsersMaster.Users.COLUMN_NAME_PASSWORD,password);

        //Where row to update, based on the title
        String selection = UsersMaster.Users.COLUMN_NAME_USERNAME + "LIKE ?";
        String[] selectionArgs = {userName};

        int count = db.update (
                UsersMaster.Users.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }
}
