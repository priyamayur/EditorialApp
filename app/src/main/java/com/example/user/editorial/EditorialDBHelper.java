package com.example.user.editorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.user.editorial.EditorialContract.EditorialEntry;
import com.example.user.editorial.EditorialContract.SavedEntry;

import static com.example.user.editorial.EditorialContract.SavedEntry.COLUMN_DESC;

public class EditorialDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "editorial.db";

    private static final int DATABASE_VERSION = 1;


    public EditorialDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_EDITORIAL_TABLE =  "CREATE TABLE " + EditorialEntry.TABLE_NAME + " ("
                + EditorialEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EditorialEntry.COLUMN_WORD + " TEXT NOT NULL, "
                + EditorialEntry.COLUMN_GRAMMER + " TEXT NOT NULL, "
                + EditorialEntry.COLUMN_MEANING + " TEXT NOT NULL); ";

        String SQL_CREATE_SAVED_TABLE =  "CREATE TABLE " + SavedEntry.TABLE_NAME + " ("
                + SavedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SavedEntry.COLUMN_TITLE + " TEXT, "
                + SavedEntry.COLUMN_DESC + " TEXT, "
                + SavedEntry.COLUMN_TIME + " TEXT, "
                + SavedEntry.COLUMN_AUTHOR + " TEXT, "
                + SavedEntry.COLUMN_URL + " TEXT); ";


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_EDITORIAL_TABLE);
        db.execSQL(SQL_CREATE_SAVED_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
