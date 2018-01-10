package cz.optimization.odpadky.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by evi on 6. 1. 2018.
 */

public class TrashbinContract {

    public static final String AUTHORITY = "cz.optimization.odpadky";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TRASHBINS = "final";


    public static final class TrashbinEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRASHBINS).build();


        public static final String TABLE_NAME = "final";

        public static final String COLUMN_LONG = "long";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_TRASHTYPE = "trashtype";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_PROGRESS = "progress";
        public static final String COLUMN_TRASHTYPE_INDEX = "trashtype_index";

    }
}
