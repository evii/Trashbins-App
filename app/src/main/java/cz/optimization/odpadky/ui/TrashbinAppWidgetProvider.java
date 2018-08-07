package cz.optimization.odpadky.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import cz.optimization.odpadky.MapsActivity;
import cz.optimization.odpadky.R;

public class TrashbinAppWidgetProvider extends AppWidgetProvider {

    public static String METAL_BUTTON = "android.appwidget.action.METAL_BUTTON";


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MapsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            Intent intent2 = new Intent(METAL_BUTTON);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trashbin_appwidget);
            views.setOnClickPendingIntent(R.id.all_containers_tv, pendingIntent);
           views.setOnClickPendingIntent(R.id.metal_containers_tv, pendingIntent2);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (METAL_BUTTON.equals(intent.getAction())) {




            Intent i = new Intent();
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            MapsActivity maps = new MapsActivity();
            maps.fetchPlaces();
            maps.fetchContainersType(String. valueOf(R.string.metal));

        }

        super.onReceive(context, intent);
    }


}
