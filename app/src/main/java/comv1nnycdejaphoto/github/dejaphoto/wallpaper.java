package comv1nnycdejaphoto.github.dejaphoto;

import java.io.IOException;
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

    private MainActivity ma = new MainActivity();

    public wallpaper(){}

    /*METHOD CHANGES SYSTEM WALLPAPER
        call by passing in a resource id
        changeWallpaper(R.drawable.hello);
        parameters: int resource, this NEEDS to be an ID for a DRAWABLE
        picture.
     */

    public void changeWallpaper(Uri uri, String location){
        Context main = MainActivity.getContext();
        try {
            /*Bitmap is one type of image, open the uri with bitmap*/
            Bitmap source = MediaStore.Images.Media.getBitmap(main.getContentResolver(), uri);
            /*Create a new canvas using the bitmap, because we cannot change directly on the bitmap so we need a new one*/
            Bitmap bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            /*Sert the text color be black*/
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setTextSize(20);
            /*Draw on the bitmap*/
            canvas.drawText(location, 10, bitmap.getHeight() - bitmap.getHeight()/5, paint);
            /*Change the wallpaper to the loaded bitmap*/
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ma.getContext());
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

}