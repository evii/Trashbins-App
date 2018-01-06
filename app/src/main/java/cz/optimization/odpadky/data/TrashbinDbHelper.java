package cz.optimization.odpadky.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by evi on 6. 1. 2018.
 */

public class TrashbinDbHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_NAME = "odpadky.sqlite";
    private static String DB_PATH;
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    //Constructor
    public TrashbinDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
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
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
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

    public String showNumberOfPoints() {


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

/**
        Cursor cursor = myDataBase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' order by name",
                null);

        cursor.moveToFirst();
        String result="";

        while (!cursor.isAfterLast()) {

            result = cursor.getString(0);
 */


              Cursor cursor = myDataBase.query(TrashbinContract.TrashbinEntry.TABLE_NAME,null, null, null,
                 null, null, null);
              String result = Integer.toString(cursor.getCount());

        return result;
    }
    }


