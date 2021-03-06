package com.example.user.editorial;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String[]> getMeaning(String word) {
        List<String[]> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM entries WHERE word = ?", new String[] {word});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String str[]={cursor.getString(0),cursor.getString(1),cursor.getString(2)};
            list.add(str);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
