package cz.optimization.odpadky.ui.clusters;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;
import cz.optimization.odpadky.ui.TrashbinAppWidgetProvider;

/**
 * Created by evi on 30. 1. 2018.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<TrashbinClusterItem> {

    private final Context mContext;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<TrashbinClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(TrashbinClusterItem item,
                                               MarkerOptions markerOptions) {
        //different markers color for each trash type
        final BitmapDescriptor markerDescriptor;

        switch (MapsActivity.position) {
            // all
            case 0:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            // glass
            case 1:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                break;
            // clear glass
            case 2:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                break;
            // metal
            case 3:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                break;
            // plastic
            case 4:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            // paper
            case 5:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
            // carton
            case 6:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                break;
            //electrical
            case 7:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                break;

            default:
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;

        }
        /*if( MapsActivity.position== 0){
        markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);}
        else{
            markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }*/

        markerOptions.icon(markerDescriptor);


    }
}