package cz.optimization.odpadky.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.optimization.odpadky.TrashbinClusterItem;

/**
 * Created by evi on 6. 1. 2018.
 */

public class TrashbinDbHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_NAME = "odpadky.sqlite";
    private static String DB_PATH;
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private List<TrashbinClusterItem> mListLocations;


    //Constructor
    public TrashbinDbHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.myContext = context;
        DB_PATH = myContext.getDatabasePath(DB_NAME).toString();
    }

    /**
     * Creates a empty database on the system and rewrites it with my own database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time we open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    public void createOpenDb() {
        try {
            createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            openDataBase();
        } catch (SQLException sqle) {
            try {
                throw sqle;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<TrashbinClusterItem> selectAllPoints() throws ExecutionException, InterruptedException {
        mListLocations  = new FetchAllLocations().execute().get();
        return mListLocations;
    }

    public List<TrashbinClusterItem> selectOneTypePoints(String[] trashTypeIndex) throws ExecutionException, InterruptedException {
        mListLocations = new FetchSelectedLocations().execute(trashTypeIndex).get();
        return mListLocations;
    }

    public class FetchAllLocations extends AsyncTask<Void, Void, List> {

        // Invoked on a background thread
        @Override
        protected List<TrashbinClusterItem> doInBackground(Void... params) {
            // Make the query to get the data

            List<TrashbinClusterItem> itemsList = new ArrayList<>();

            // Get the content resolver
            ContentResolver resolver = myContext.getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class
            Cursor cursor = resolver.query(TrashbinContract.TrashbinEntry.CONTENT_URI, null, null, null,
                    null);

            if (cursor != null) {

                itemsList = addPoints(cursor);
            }

            return itemsList;
        }

        // Invoked on UI thread
        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
        }
    }

    private List<TrashbinClusterItem> addPoints(Cursor cursor) {

        List<TrashbinClusterItem> ListItems = new ArrayList<>();

        int locationCount = 0;
        String address = "";
        double lat = 0;
        double lng = 0;
        double progress = 0;
        //   String trashType = "";
        //    Float colour = BitmapDescriptorFactory.HUE_ROSE;
        String snippet = "";

        // Number of locations available in the SQLite database table
        locationCount = cursor.getCount();

        // Move the current record pointer to the first row of the table
        cursor.moveToFirst();

        for (int i = 0; i < locationCount; i++) {

            lat = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_LAT));
            lng = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_LONG));

            address = cursor.getString(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_ADDRESS));
            progress = cursor.getDouble(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_PROGRESS));
            double progressRounded = (double) Math.round(progress * 100) / 100;
            snippet = "Naplněnost: " + (progressRounded * 100) + " %";

            // Getting colour of the point
            /** trashType = cursor.getString(cursor.getColumnIndex(TrashbinContract.TrashbinEntry.COLUMN_TRASHTYPE_INDEX));

             switch (trashType) {
             case "BS":
             colour = BitmapDescriptorFactory.HUE_GREEN;
             break;
             case "CS":
             colour = BitmapDescriptorFactory.HUE_AZURE;
             break;
             case "K":
             colour = BitmapDescriptorFactory.HUE_MAGENTA;
             break;
             case "NK":
             colour = BitmapDescriptorFactory.HUE_ORANGE;
             break;
             case "PA":
             colour = BitmapDescriptorFactory.HUE_BLUE;
             break;
             case "PL":
             colour = BitmapDescriptorFactory.HUE_YELLOW;
             break;
             }
             */

            // Adding the marker to the List
            ListItems.add(new TrashbinClusterItem(lat, lng, address, snippet));

            // Traverse the pointer to the next row
            cursor.moveToNext();
        }

        cursor.close();
        return ListItems;
    }

    public class FetchSelectedLocations extends AsyncTask<String[], Void, List<TrashbinClusterItem>> {

        // Invoked on a background thread
        @Override
        protected List<TrashbinClusterItem> doInBackground(String[]... strings) {
            // Make the query to get the data
            List<TrashbinClusterItem> itemsList = new ArrayList<>();

            String selection = TrashbinContract.TrashbinEntry.COLUMN_TRASHTYPE_INDEX + "=?";
            String[] selArgs = strings[0];
            Cursor cursor = myContext.getContentResolver().query(TrashbinContract.TrashbinEntry.CONTENT_URI, null, selection, selArgs,
                    null);

            itemsList = addPoints(cursor);
            return itemsList;
        }

        // Invoked on UI thread
        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
        }
    }
}


