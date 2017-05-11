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

public class MainActivity extends AppCompatActivity {

    private static Context sContext;
    /* This will tell the different between choose or release, 1 is for choose, 0 for release*/
    static final int PICK_CHOOSE = 1 ;
    static final int PICK_RELEASE = 0 ;

    //constructor
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        /*Ask the permission to read the images*/
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        /*Button that links to the Choose*/
        Button load_image = (Button) findViewById(R.id.choose);
        /*onClick Event*/
        load_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  /* Ask user to pick a image and save its uri, make the result become intent*/
                  Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  /* pass the intent with the option of choose button beign clicked*/
                  startActivityForResult(intent, PICK_CHOOSE);
              }
        }
        );

        /* Same thing but for the release button*/
        Button release_image = (Button) findViewById(R.id.release);
        release_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_RELEASE);
            }
        });


    }

    /*required for other classes to be able to access MainActivity*/
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*If user press back while picking images, exit the method */
        if(data == null)
            return;

        /*The choose button being clicked*/
        if(requestCode == PICK_CHOOSE) {
            /*Get the data as type of Uri*/
            Uri uri = data.getData();
            try {
                /*Bitmap is one type of image, open the uri with bitmap*/
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                /*Change the wallpaper to the loaded bitmap*/
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /* Release button being clicked*/
        if(requestCode == PICK_RELEASE){
            Uri uri = data.getData();
            /*pathOfFifle will give you the path of the file*/
            String pathOfFile = uri.getPath();

            //TODO Realse the path from the queue

            /*Log is for debuging purpose*/
            Log.v("Path", uri.getPath());
        }
    }

    /*Get from piazza, original from codepath :
        https://guides.codepath.com/android/Managing-Runtime-Permissions-with-PermissionsDispatcher */
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
