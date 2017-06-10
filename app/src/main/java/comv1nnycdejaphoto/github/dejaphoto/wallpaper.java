package comv1nnycdejaphoto.github.dejaphoto;

import java.io.IOException;
import java.net.ConnectException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


public class wallpaper extends Activity {

    private BackgroundService ma = new BackgroundService();

    public wallpaper(){}

    /*METHOD CHANGES SYSTEM WALLPAPER
        call by passing in a resource id
        changeWallpaper(R.drawable.hello);
        parameters: int resource, this NEEDS to be an ID for a DRAWABLE
        picture.
     */

    public void changeWallpaper(Uri uri, String location, int likes){
        Context main = BackgroundService.getContext();
        try {
            /*Bitmap is one type of image, open the uri with bitmap*/
            Log.i("Wallpaper Uri",uri.getPath());
            Bitmap source = MediaStore.Images.Media.getBitmap(main.getContentResolver(), uri);

            /*Create a new canvas using the bitmap, because we cannot change directly on the bitmap so we need a new one*/
            Bitmap bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            /*Sert the text color be black*/
            paint.setColor(Color.rgb(255, 255, 0));
            paint.setTextSize(55);
            /*Draw on the bitmap*/
            canvas.drawText(location, 10, (bitmap.getHeight() - bitmap.getHeight()/5), paint);
            canvas.drawText("Likes: " + Integer.toString(likes), bitmap.getWidth()-300, (bitmap.getHeight() - bitmap.getHeight()/5), paint);

            /*Change the wallpaper to the loaded bitmap*/
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ma.getContext());
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeWallpaper(Uri uri, String location,Context context,int likes){
        try {
            /*Bitmap is one type of image, open the uri with bitmap*/
            Log.i("Wallpaper Uri",uri.getPath());
            Bitmap source = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            /*Create a new canvas using the bitmap, because we cannot change directly on the bitmap so we need a new one*/
            Bitmap bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            /*Sert the text color be black*/
            paint.setColor(Color.rgb(255, 255, 0));
            paint.setTextSize(55);
            /*Draw on the bitmap*/
            canvas.drawText(location, 10, (bitmap.getHeight() - bitmap.getHeight()/5)-55, paint);
            canvas.drawText("Likes: " + Integer.toString(likes), 10, (bitmap.getHeight() - bitmap.getHeight()/5), paint);
            /*Change the wallpaper to the loaded bitmap*/
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeWallpaper(int resource){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(ma.getContext());
        try {
            myWallpaperManager.setResource(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Method display a photo indicates that there is no pic to display */
    public void emptyPicture(){
        WindowManager windowManager = (WindowManager) MainActivity.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        /* get size of the screen */
        Point size = new Point();
        display.getSize(size);

        /* create a new canvas using the bitmap */
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, config);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /* set the text color be white */
        paint.setColor(Color.rgb(252, 252, 252));

        /* set the text size */
        paint.setTextSize(50);
        String string = "DejaPhoto\n";

        /* draw on the bitmap */
        canvas.drawText(string, size.x/3, size.y/2, paint);
        string = "No photos in album";

        /* draw on the bitmap */
        canvas.drawText(string, size.x/3-150, size.y/2 +50, paint);

        /* change the wallpaper to the loaded bitmap */
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(ma.getContext());
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
