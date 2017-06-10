package comv1nnycdejaphoto.github.dejaphoto;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by avery2 on 6/9/17.
 */

/**
 * interface for wallpaper
 * used in the event we have different types of wallpaper that need to be displayed
 * with different behavior
 */
public interface Iwallpaper {

    public void changeWallpaper(Uri uri, String location, int likes);

    public void changeWallpaper(Uri uri, String location,Context context,int likes);

    public void changeWallpaper(int resource);

    public void emptyPicture();
}
