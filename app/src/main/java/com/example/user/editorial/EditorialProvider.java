package com.example.user.editorial;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.user.editorial.EditorialContract.EditorialEntry;
import com.example.user.editorial.EditorialContract.SavedEntry;

public class EditorialProvider  extends ContentProvider {

    public static final String LOG_TAG = EditorialProvider.class.getSimpleName();
    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int EDITORIALS = 100;
    private static final int SAVED = 200;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int EDITORIALS_ID = 101;
    private static final int SAVED_ID = 201;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(EditorialContract.CONTENT_AUTHORITY, EditorialContract.PATH_EDITORIAL, EDITORIALS);
        sUriMatcher.addURI(EditorialContract.CONTENT_AUTHORITY, EditorialContract.PATH_SAVED, SAVED);


        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(EditorialContract.CONTENT_AUTHORITY, EditorialContract.PATH_EDITORIAL + "/#", EDITORIALS_ID);
        sUriMatcher.addURI(EditorialContract.CONTENT_AUTHORITY, EditorialContract.PATH_SAVED + "/#", SAVED_ID);
    }

    /**
     * Database helper object
     */
    private EditorialDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new EditorialDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case EDITORIALS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(EditorialEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SAVED:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(SavedEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EDITORIALS_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = EditorialEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(EditorialEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SAVED_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = SavedEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(SavedEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EDITORIALS:
                return insertWord(uri, contentValues);
            case SAVED:
                return insertSaved(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertWord(Uri uri, ContentValues values) {
        // Check that the name is not null
        String word = values.getAsString(EditorialEntry.COLUMN_WORD);
        if (word == null) {
            throw new IllegalArgumentException("dictionary requires a word");
        }
        String grammer = values.getAsString(EditorialEntry.COLUMN_GRAMMER);
        if (grammer == null) {
            throw new IllegalArgumentException("dictionary requires a grammer");
                        }
        String meaning = values.getAsString(EditorialEntry.COLUMN_MEANING);
        if (meaning == null) {
            throw new IllegalArgumentException("dictionary requires a meaning");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(EditorialEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertSaved(Uri uri, ContentValues values) {
        // Check that the name is not null
        String title = values.getAsString(SavedEntry.COLUMN_TITLE);
        if (title == null) {
            title="";
        }
        String desc = values.getAsString(SavedEntry.COLUMN_DESC);
        if (desc == null) {
            desc="";
        }
        String time = values.getAsString(SavedEntry.COLUMN_TIME);
        if (time == null) {
            time="";
        }
        String author = values.getAsString(SavedEntry.COLUMN_AUTHOR);
        if (author == null) {
            author="";
        }
        String url = values.getAsString(SavedEntry.COLUMN_URL);
        if (url == null) {
            url="";
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(SavedEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EDITORIALS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case EDITORIALS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = EditorialEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(EditorialEntry.COLUMN_WORD)) {
            String name = values.getAsString(EditorialEntry.COLUMN_WORD);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        if (values.containsKey(EditorialEntry.COLUMN_GRAMMER)) {
            String grammer = values.getAsString(EditorialEntry.COLUMN_GRAMMER);
            if (grammer == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }


        if (values.containsKey(EditorialEntry.COLUMN_MEANING)) {
            String meaning = values.getAsString(EditorialEntry.COLUMN_MEANING);
            if (meaning == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(EditorialEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EDITORIALS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(EditorialEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SAVED:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(SavedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EDITORIALS_ID:
                // Delete a single row given by the ID in the URI
                selection = EditorialEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(EditorialEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SAVED_ID:
                // Delete a single row given by the ID in the URI
                selection = SavedEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(SavedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EDITORIALS:
                return EditorialEntry.CONTENT_LIST_TYPE;
            case EDITORIALS_ID:
                return EditorialEntry.CONTENT_ITEM_TYPE;
            case SAVED:
                return SavedEntry.CONTENT_LIST_TYPE;
            case SAVED_ID:
                return SavedEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
