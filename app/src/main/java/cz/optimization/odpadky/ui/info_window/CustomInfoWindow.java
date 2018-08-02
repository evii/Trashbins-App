package cz.optimization.odpadky.ui.info_window;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.objects.Container;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater mInflater;


    public CustomInfoWindow(LayoutInflater inflater) {
        this.mInflater = inflater;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        final View popup = mInflater.inflate(R.layout.info_window, null);

        TextView text_tv = popup.findViewById(R.id.info_text);
        TextView details_tv = popup.findViewById(R.id.info_snippet);
        TextView list_tv = popup.findViewById(R.id.info_list);

        text_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        String containersList = (String) marker.getTag();

        JSONArray containers = null;
        try {
            containers = new JSONArray(containersList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*JSONObject mJsonObject = new JSONObject();
        for (int i = 0; i < containers.length(); i++) {
            mJsonObject = containers.getJSONObject(i);
            mJsonObject.getString("0");
            mJsonObject.getString("id");
            mJsonObject.getString("1");
            mJsonObject.getString("name");
        }*/



       /* Gson gson = new Gson();
        List<Container> containers = gson.fromJson(containersList, Container.ContainersResult.class);*/


        Log.v("JsonParse", "pocet: " +String.valueOf(containers.length()) + containersList);




        return popup;
    }

}

