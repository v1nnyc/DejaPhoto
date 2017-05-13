package comv1nnycdejaphoto.github.dejaphoto;

import java.io.IOException;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


public class wallpaper extends Activity {

    private MainActivity ma = new MainActivity();

    public wallpaper(){}

    /*METHOD CHANGES SYSTEM WALLPAPER
        call by passing in a resource id
        changeWallpaper(R.drawable.hello);
        parameters: int resource, this NEEDS to be an ID for a DRAWABLE
        picture.
     */

    public void changeWallpaper(Uri uri){
        Context main = MainActivity.getContext();
        try {
                /*Bitmap is one type of image, open the uri with bitmap*/
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(main.getContentResolver(), uri);
                /*Change the wallpaper to the loaded bitmap*/
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(ma.getContext());
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeWallpaper(String string){
        Context main = MainActivity.getContext();
        Uri uri = Uri.parse(string);
        try {
                /*Bitmap is one type of image, open the uri with bitmap*/
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(main.getContentResolver(), uri);
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