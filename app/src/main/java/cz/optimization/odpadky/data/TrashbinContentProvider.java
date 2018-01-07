package cz.optimization.odpadky.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by evi on 6. 1. 2018.
 */

public class TrashbinContentProvider extends ContentProvider {

    public static final int TRASHBINS = 100;
    public static final int TRASHBIN_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private TrashbinDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {

       UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TrashbinContract.AUTHORITY, TrashbinContract.PATH_TRASHBINS, TRASHBINS);
        uriMatcher.addURI(TrashbinContract.AUTHORITY, TrashbinContract.PATH_TRASHBINS + "/#", TRASHBIN_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new TrashbinDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            // Query for the tasks directory
            case TRASHBINS:
                returnCursor =  db.query(TrashbinContract.TrashbinEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
