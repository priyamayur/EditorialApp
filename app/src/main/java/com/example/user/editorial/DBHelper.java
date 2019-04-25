package com.example.user.editorial;
import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;

public class DBHelper extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
