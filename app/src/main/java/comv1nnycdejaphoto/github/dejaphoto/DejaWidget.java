package comv1nnycdejaphoto.github.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class DejaWidget extends AppWidgetProvider {
    /*This 4 string will indicate which button being clicked*/
    private static final String Release = "release";
    private static final String Karma = "Karma";
    private static final String Left = "left";
    private static final String Right = "right";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        /*Get the private Strings above*/
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.deja_widget);

        /*The onClick event, first argument will be the button id, second one will be the string
                   Assigning a String to a pending Intent, the string works like a case switch later on*/
        views.setOnClickPendingIntent(R.id.release, getPendingIntent(context, Release));
        views.setOnClickPendingIntent(R.id.karma, getPendingIntent(context, Karma));
        views.setOnClickPendingIntent(R.id.left, getPendingIntent(context, Left));
        views.setOnClickPendingIntent(R.id.right, getPendingIntent(context, Right));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        /*This line will actually call onUpdate(), to make sure everything is okay first*/
        super.onReceive(context, intent);

        /*The case switch statements. It is matching the string being saved in the pending Intent*/
        /*The toast text just help for debug, make the button being clicked, will be deleted later*/
        if (Release.equals(intent.getAction())) {
            Toast.makeText(context, "Release", Toast.LENGTH_SHORT).show();
            wallpaper wp = new wallpaper();
            wp.changeWallpaper(R.drawable.hello);
        }
        if (Karma.equals(intent.getAction())) {
            Toast.makeText(context, "Karma", Toast.LENGTH_SHORT).show();
            wallpaper wp = new wallpaper();
            wp.changeWallpaper(R.drawable.hey);
        }
        if (Left.equals(intent.getAction())) {
            Toast.makeText(context, "Left", Toast.LENGTH_SHORT).show();
            wallpaper wp = new wallpaper();
            wp.changeWallpaper(R.drawable.hi);
        }
        if (Right.equals(intent.getAction())) {
            Toast.makeText(context, "Right", Toast.LENGTH_SHORT).show();
            wallpaper wp = new wallpaper();
            wp.changeWallpaper(R.drawable.hi);
        }
    }

    /* This method will put a string into a pending Intent */
    protected PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

