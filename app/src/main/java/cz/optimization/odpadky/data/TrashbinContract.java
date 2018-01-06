package cz.optimization.odpadky.data;

import android.provider.BaseColumns;

/**
 * Created by evi on 6. 1. 2018.
 */

public class TrashbinContract {

    public static final class TrashbinEntry implements BaseColumns {

        public static final String TABLE_NAME = "final";

        public static final String COLUMN_LONG = "long";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_TRASHTYPE = "trashtype";
        public static final String COLUMN_ADDRESS = "address";


    }
}
