package comv1nnycdejaphoto.github.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class LocationChangeWidget extends AppWidgetProvider {

    private wallpaper wp=new wallpaper();
    private SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences ;
    static Default_Gallery defaultGallery;
    /* index for the index of picture is displaying*/
    private int index;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

                /*Get the private Strings above*/
        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.deja_widget);

        //set all buttons onclick events
        setOnClickEvent(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    public static void setOnClickEvent(Context context, RemoteViews views) {
    /*The onClick event, first argument will be the button id, second one will be the string
      Assigning a String to a pending Intent, the string works like a case switch later on*/
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    /* This method will put a string into a pending Intent */
    protected PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void readPreferences(Context context){
        /* Read the shared preferences*/
        sharedPreferences = context.getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        /*gson is a way to put the object into shared preferences*/
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery","");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
        index = sharedPreferences.getInt("Index",0);
    }
}

