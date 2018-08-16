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


    public static final String ALL_BUTTON = "android.appwidget.action.ALL_BUTTON";
    public static final String GLASS_BUTTON = "android.appwidget.action.GLASS_BUTTON";
    public static final String CLEAR_GLASS_BUTTON = "android.appwidget.action.CLEAR_GLASS_BUTTON";
    public static final String METAL_BUTTON = "android.appwidget.action.METAL_BUTTON";
    public static final String PLASTIC_BUTTON = "android.appwidget.action.PLASTIC_BUTTON";
    public static final String PAPER_BUTTON = "android.appwidget.action.PAPER_BUTTON";
    public static final String CARTON_BUTTON = "android.appwidget.action.CARTON_BUTTON";
    public static final String ELECTRICAL_BUTTON = "android.appwidget.action.ELECTRICAL_BUTTON";
    public static final String WIDGET_CLICKED_KEY = "WIDGET_CLICKED_KEY";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create Intents for each textView to launch MapsActivity
            /*Intent intent = new Intent(context, MapsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
*/
            Intent intentAll = new Intent(ALL_BUTTON);
            PendingIntent pendingIntentAll = PendingIntent.getBroadcast(context, 0, intentAll, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentGlass = new Intent(GLASS_BUTTON);
            PendingIntent pendingIntentGlass = PendingIntent.getBroadcast(context, 0, intentGlass, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentClearGlass = new Intent(CLEAR_GLASS_BUTTON);
            PendingIntent pendingIntentClearGlass = PendingIntent.getBroadcast(context, 0, intentClearGlass, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentMetal = new Intent(METAL_BUTTON);
            PendingIntent pendingIntentMetal = PendingIntent.getBroadcast(context, 0, intentMetal, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentPlastic = new Intent(PLASTIC_BUTTON);
            PendingIntent pendingIntentPlastic = PendingIntent.getBroadcast(context, 0, intentPlastic, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentPaper = new Intent(PAPER_BUTTON);
            PendingIntent pendingIntentPaper = PendingIntent.getBroadcast(context, 0, intentPaper, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentCarton = new Intent(CARTON_BUTTON);
            PendingIntent pendingIntentCarton = PendingIntent.getBroadcast(context, 0, intentCarton, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentElectrical = new Intent(ELECTRICAL_BUTTON);
            PendingIntent pendingIntentElectrical = PendingIntent.getBroadcast(context, 0, intentElectrical, PendingIntent.FLAG_UPDATE_CURRENT);

            // Get the layout for the App Widget and attach an on-click listener
            // to the textviews
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_trashbin);
            views.setOnClickPendingIntent(R.id.all_containers_tv, pendingIntentAll);
            views.setOnClickPendingIntent(R.id.glass_containers_tv, pendingIntentGlass);
            views.setOnClickPendingIntent(R.id.clear_glass_containers_tv, pendingIntentClearGlass);
            views.setOnClickPendingIntent(R.id.metal_containers_tv, pendingIntentMetal);
            views.setOnClickPendingIntent(R.id.plastic_containers_tv, pendingIntentPlastic);
            views.setOnClickPendingIntent(R.id.paper_containers_tv, pendingIntentPaper);
            views.setOnClickPendingIntent(R.id.carton_containers_tv, pendingIntentCarton);
            views.setOnClickPendingIntent(R.id.electrical_containers_tv, pendingIntentElectrical);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ALL_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, ALL_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        else if (GLASS_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, GLASS_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (CLEAR_GLASS_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, CLEAR_GLASS_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (METAL_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, METAL_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (PLASTIC_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, PLASTIC_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (PAPER_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, PAPER_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (CARTON_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, CARTON_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (ELECTRICAL_BUTTON.equals(intent.getAction())) {

            Intent i = new Intent();
            i.putExtra(WIDGET_CLICKED_KEY, ELECTRICAL_BUTTON);
            i.setClassName("cz.optimization.odpadky", "cz.optimization.odpadky.MapsActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        super.onReceive(context, intent);
    }
}
