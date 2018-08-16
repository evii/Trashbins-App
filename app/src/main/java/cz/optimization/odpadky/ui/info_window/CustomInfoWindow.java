package cz.optimization.odpadky.ui.info_window;


import android.content.Context;

import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private static final String TAG = "CustomInfoWindow";

    public CustomInfoWindow(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        // inflates layout
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.info_window, null);
        LinearLayout view = (LinearLayout) popup;

        // gets details on containers from sharedpreferences in String - sent from MapsActivity
        String containersList = "";
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(MapsActivity.PREFS_NAME,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MapsActivity.PREFS_KEY)) {
            containersList = sharedpreferences.getString(MapsActivity.PREFS_KEY, "");
        } else {
            Log.d(TAG, "ContainersList not contained in Shared preferences");
        }

        // converts String to list of containers
        Type type = new TypeToken<List<Container>>() {
        }.getType();
        Gson gson = new Gson();
        List<Container> containers = gson.fromJson(containersList, type);

        // creates view and populate them with data
        if (containers != null) {
            if (containers.size() > 0) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 0, 10, 0);

                for (Container listItem : containers) {

                    // horizontal Linear Layout for one item
                    LinearLayout itemLayout = new LinearLayout(mContext);
                    itemLayout.setLayoutParams(params);
                    params.setMargins(10, 0, 10, 0);
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView trashTypeTV = new TextView(mContext);
                    TextView progressTV = new TextView(mContext);
                    String trashType = listItem.getTrashType();
                    trashType = trashType.substring(0, 1).toUpperCase() + trashType.substring(1);
                    trashType = trashType.replace("_", " ");
                    trashTypeTV.setText(trashType);
                    trashTypeTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    int progress = listItem.getProgress();
                    String progressString = String.valueOf(progress) + " %";
                    progressTV.setText(progressString);
                    progressTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    progressTV.setTypeface(null, Typeface.BOLD);

                    if (progress > 90) {
                        progressTV.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                    } else if (progress < 50) {
                        progressTV.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                    } else
                        progressTV.setTextColor(ContextCompat.getColor(mContext, R.color.colorOrange));

                    LinearLayout.LayoutParams fixed_width = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    // setting fixed width for trash label in dp
                    fixed_width.width = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 150, mContext.getResources()
                                    .getDisplayMetrics());

                    trashTypeTV.setLayoutParams(fixed_width);
                    progressTV.setLayoutParams(params);
                    itemLayout.addView(trashTypeTV);
                    itemLayout.addView(progressTV);
                    view.addView(itemLayout);
                }
            }
        }

        return popup;
    }

}
