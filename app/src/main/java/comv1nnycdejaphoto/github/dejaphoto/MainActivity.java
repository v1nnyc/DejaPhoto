package comv1nnycdejaphoto.github.dejaphoto;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    private static Context sContext;

    //constructor
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        Button load_image = (Button) findViewById(R.id.choose);
        load_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  /*The intent will ask to pick something, and it should be type of image that has uri*/
                  Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  /*Since the action is pick, we need a result from the action */
                  startActivityForResult(intent, 1);
                  /*This will give the path of system album*/
                  String path = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
                  /*For debug*/
                  Log.v("DCIM",path);
              }
        }
        );

    }

    /*required for other classes to be able to access MainActivity
     */
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            return;
        /* Uri is a type of path ,extract the data from intent that was loaded with image*/
        Uri uri = data.getData();
        try {
            /*Bitmap is a type of image, the following line loads the file with the uri into a bitmap(picture)*/
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            /* This is same as the wallpapaer class, but I have to set the wallpaper with a bitmap, so I cannot use wallpapaer
                        class. CAN BE SUBSTITUTE LATER  */
            WallpaperManager wallpaperManager=WallpaperManager.getInstance(this);
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*Get from piazza, original from codepath :
         https://guides.codepath.com/android/Managing-Runtime-Permissions-with-PermissionsDispatcher
       */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    //mymethod(); //a sample method called

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }
}
