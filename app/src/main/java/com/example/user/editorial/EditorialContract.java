package com.example.user.editorial;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class EditorialContract {
    private EditorialContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.user.editorial";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EDITORIAL = "editorial";
    public static final String PATH_SAVED = "saved";

    public static final class EditorialEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EDITORIAL);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EDITORIAL;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EDITORIAL;

        public final static String TABLE_NAME = "meanings";


        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_WORD ="word";


        public final static String COLUMN_GRAMMER = "grammer";


        public final static String COLUMN_MEANING = "meaning";

    }
    public static final class SavedEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SAVED);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SAVED;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SAVED;

        public final static String TABLE_NAME = "saved";


        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_TITLE ="title";


        public final static String COLUMN_DESC = "description";


        public final static String COLUMN_TIME = "time";
        public final static String COLUMN_AUTHOR = "author";
        public final static String COLUMN_URL = "url";

    }
}
