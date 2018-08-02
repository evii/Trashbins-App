package cz.optimization.odpadky.ui.info_window;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

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

        text_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

      /*  Container container = (Container) marker.getTag();

        TextView list_tv = view.findViewById(R.id.info_list);
        list_tv.setText(container.getProgress());*/

        return popup;
    }

}

