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
    private static final String MyOnClick = "myOnClickTag";

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.deja_widget);
        views.setOnClickPendingIntent(R.id.release, getPendingSelfIntent(context, MyOnClick));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            Log.v("Debug","FIRST TIME\n");
//            RemoteViews remoteViews =  new RemoteViews(context.getPackageName(), R.layout.deja_widget);
//
//            Intent intent = new Intent(context,MainActivity.class);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
//            remoteViews.setOnClickPendingIntent(R.id.release,pendingIntent);
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
        super.onReceive(context, intent);
        if (MyOnClick.equals(intent.getAction())) {
            //your onClick action is here
            //display in short period of time
            Toast.makeText(context, "msg msgasdasd", Toast.LENGTH_SHORT).show();
            wallpaper wp = new wallpaper();
            wp.changeWallpaper(R.drawable.hello);


        }
    }
}

