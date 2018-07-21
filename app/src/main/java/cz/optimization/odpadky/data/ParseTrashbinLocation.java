package cz.optimization.odpadky.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cz.optimization.odpadky.TrashbinClusterItem;

/**
 * Created by evi on 4. 2. 2018.
 */

public class ParseTrashbinLocation {

    public static List getTrashbinLocation(Cursor cursor){
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
}
