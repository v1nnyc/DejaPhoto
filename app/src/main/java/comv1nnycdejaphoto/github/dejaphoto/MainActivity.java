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

    Button cameraRoll;
    Button customAlbum;
    private static Context sContext;

    //constructor
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_album);

        final Button cameraRoll = (Button) findViewById(R.id.camera_roll_button);
        final Button customAlbum = (Button) findViewById(R.id.custom_album_button);

        cameraRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRoll.setBackgroundResource(R.drawable.button_green);
                customAlbum.setBackgroundResource(R.drawable.button_white);
            }
        });

        customAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRoll.setBackgroundResource(R.drawable.button_white);
                customAlbum.setBackgroundResource(R.drawable.button_green);
            }
        });

        setContentView(R.layout.start_screen);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        Button load_image = (Button) findViewById(R.id.choose);
        load_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  startActivityForResult(intent, 1);

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
        Uri uri = data.getData();
        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            WallpaperManager wallpaperManager=WallpaperManager.getInstance(this);
            Log.v("in",bitmap.toString());
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            Log.v("ERROR","ERROR");
            e.printStackTrace();
        }
//        WallpaperManager wallpaperManager= WallpaperManager.getInstance(this);
//        try {
//            wallpaperManager.setBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
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
>>>>>>> 18cfe83edde3f5278a1495aba18b5e23b53e2933
    }
}
