package comv1nnycdejaphoto.github.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 * This class used third party library Gson made by google.
 * Obtained the library from https://github.com/google/gson.
 */
public class DejaWidget extends AppWidgetProvider {

    private wallpaper wp=new wallpaper();
    private SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences ;
    private Default_Gallery defaultGallery;

    /* index for the index of picture is displaying*/
    private int index;

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

        //set all buttons onclick events
        setOnClickEvent(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //this method set the onClick event for all buttons
    public void setOnClickEvent(Context context, RemoteViews views) {
    /*The onClick event, first argument will be the button id, second one will be the string
      Assigning a String to a pending Intent, the string works like a case switch later on*/
        views.setOnClickPendingIntent(R.id.release, getPendingIntent(context, Release));
        views.setOnClickPendingIntent(R.id.karma, getPendingIntent(context, Karma));
        views.setOnClickPendingIntent(R.id.left, getPendingIntent(context, Left));
        views.setOnClickPendingIntent(R.id.right, getPendingIntent(context, Right));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /*Read the Preferences*/
        readPreferences(context);

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

        /*Update the photos*/
        readPreferences(context);

        /*The case switch statements. It is matching the string being saved in the pending Intent*/
        /*The toast text just help for debug, make the button being clicked, will be deleted later*/
        if (Release.equals(intent.getAction())) {
            /*Call the hide function to remove from display*/
            defaultGallery.getPictures().elementAt(index).hide();
            Gson gson = new Gson();
            String json = gson.toJson(defaultGallery);

            /* Updated the Gallery*/
            editor.putString("Gallery", json);
            Toast.makeText(context, "Release", Toast.LENGTH_SHORT).show();
        }

        if (Karma.equals(intent.getAction())) {
            /* Call the add Karma function to modify the picture*/
            defaultGallery.getPictures().elementAt(index).addKarma();
            Gson gson = new Gson();
            String json = gson.toJson(defaultGallery);

            /*Updated the Gallery*/
            editor.putString("Gallery", json);
            Toast.makeText(context, "Added Karma", Toast.LENGTH_SHORT).show();
        }

        if (Left.equals(intent.getAction())) {
            if(defaultGallery.get_photos() != 0 ) {
                /* To indicate the wallpaper is changed*/
                Boolean changed = false;

                /* To save the current displaying index*/
                int last = index;

                /* If it already checks for all the picture in the Gallery*/
                while ((index - 1) != last) {
                    /* if index is 0, make the index become the last element*/
                    if ((index) == 0)
                        index = defaultGallery.get_photos();

                    /* Check is it already released, if not, display it*/
                    if (defaultGallery.getPictures().elementAt(index - 1).getDisplay()) {
                        /*Get the picture from the gallery*/
                        Picture picture = defaultGallery.getPictures().elementAt(index - 1);
                        File file = new File(picture.getImage());
                        Uri uriFromGallery = Uri.fromFile(file);
                        /*Make it becomes the wallpaper*/
                        wp.changeWallpaper(uriFromGallery, picture.getLocatio());
                        /*Update the index*/
                        editor.putInt("Index", index - 1);
                        changed = true;
                        break;
                    }
                    index = index - 1;
                }

                /*if the wall paper didn't change, it means all pictures are released or empty gallery*/
                if (!changed)
                    Toast.makeText(context, "No picture can be displayed", Toast.LENGTH_SHORT).show();
            }
            else
                wp.emptyPicture();
        }

        if (Right.equals(intent.getAction())) {
            if(defaultGallery.get_photos() != 0) {
                Boolean changed = false;
                int last = index;
                while ((index + 1) != last) {
                    if (index == (defaultGallery.get_photos() - 1))
                        index = -1;

                    if (defaultGallery.getPictures().elementAt(index + 1).getDisplay()) {
                        Picture picture = defaultGallery.getPictures().elementAt(index + 1);
                        File file = new File(picture.getImage());
                        Uri uriFromGallery = Uri.fromFile(file);
                        wp.changeWallpaper(uriFromGallery, picture.getLocatio());
                        editor.putInt("Index", index + 1);
                        changed = true;
                        break;
                    }
                    index = index + 1;
                }

                if (!changed)
                    Toast.makeText(context, "No picture can be displayed", Toast.LENGTH_SHORT).show();
            }
            else
                wp.emptyPicture();
        }
        editor.apply();
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

