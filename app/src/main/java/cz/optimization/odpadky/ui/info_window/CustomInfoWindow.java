package cz.optimization.odpadky.ui.info_window;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.MainThread;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context mContext;


    public CustomInfoWindow(Context context) {
        mContext = context;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        final View popup = ((Activity) mContext).getLayoutInflater().inflate(R.layout.info_window, null);
        //  LinearLayout view = (LinearLayout) popup;

        TextView text_tv = popup.findViewById(R.id.info_text);
        TextView details_tv = popup.findViewById(R.id.info_snippet);
        final TextView list_tv = popup.findViewById(R.id.info_detail);

        String placeId = marker.getSnippet();
        text_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());


        MapsActivity.fetchContainersAtPlace(placeId, new MapsActivity.FetchContainersAtPlaceCallbacks() {
            @Override
            public void onSuccess(@NonNull String containersString) {

                list_tv.setText(containersString); // not working
                Log.v("CustomInfoWindow", containersString);

                /*Type type = new TypeToken<List<Container>>() {
                }.getType();
                Gson gson = new Gson();
                List<Container> containers = gson.fromJson(containersString, type);*/

            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }
        });










      /*  String placeId = containers.get(0).getPlaceId();
        list_tv.setText(placeId);*/

       /* if (containers != null) {
            if (containers.size() > 0) {
                LinearLayout itemLayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                itemLayout.setLayoutParams(lp);
                lp.setMargins(10, 0, 10, 0);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (Container listItem : containers) {
                    TextView trashTypeTV = new TextView(mContext);
                    TextView progressTV = new TextView(mContext);
                    trashTypeTV.setText(listItem.getTrashType());
                    trashTypeTV.setTextSize(18f);
                    int progress = listItem.getProgress();
                    String progressString = String.valueOf(progress)+ " %";
                    progressTV.setText(progressString);
                    progressTV.setTextSize(12f);

                    trashTypeTV.setLayoutParams(lp);
                    progressTV.setLayoutParams(lp);
                    itemLayout.addView(trashTypeTV);
                    itemLayout.addView(progressTV);
                }
            }
        }*/


        return popup;
    }

}
//Log.v("LISTCONT ", "Size: " + String.valueOf(containers.size()));
