package comv1nnycdejaphoto.github.dejaphoto;

 /* This class used third party library Gson made by google.
        * Obtained the library from https://github.com/google/gson.
        */
import android.Manifest;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
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

import com.google.gson.Gson;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private Default_Gallery defaultGallery;
    Button cameraRoll;
    Button customAlbum;
    private static Context sContext;
    /* This will tell the different between choose or release, 1 is for choose, 0 for release*/
    static final int PICK_CHOOSE = 1 ;
    static final int PICK_RELEASE = 0 ;
    private final int UPDATE = 1;
    private final int SKIP = 0;
    //constructor
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        /*Check is the sharedPreferences exists*/
        initialize(SKIP);
        /*Read the data from the shared preferences*/
        readPreferences();
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_album);
                /*Ask the permission to read the images*/
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        /*final Button cameraRoll = (Button) findViewById(R.id.camera_roll_button);
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
        });*/

        setContentView(R.layout.start_screen);

        /*Button that links to the Choose*/
        Button load_image = (Button) findViewById(R.id.choose);
        /*onClick Event*/
        load_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           /*Ask user to pick a image and save its uri, make the result become intent*/
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_RELEASE);
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
        if(data != null && requestCode == PICK_CHOOSE) {
            //TODO  choose album
            /*Get the data as type of Uri*/
                Uri uri = data.getData();
                wallpaper wp = new wallpaper();
                wp.changeWallpaper(uri);
        }
        /* Release button being clicked*/
        if(requestCode == PICK_RELEASE){
            /*Only one picture is selected*/
            if(data.getData()!=null){
                Uri uri = data.getData();
                for(int i = 0; i < defaultGallery.get_photos() ; ++i) {
                    Picture picture = defaultGallery.getPictures().elementAt(i);
                    File file = new File(picture.getImage());
                        /*Turn the picture into uri*/
                    Uri uriFromGallery = Uri.fromFile(file);
                        /* Make the user selected picture and gallery picture into bitmap*/
                    Bitmap bitmapUser = null;
                    try {
                        bitmapUser = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        Bitmap bitmapGallery = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriFromGallery);
                            /*If they are same, hide the picture in Gallery*/
                        if (bitmapUser.sameAs(bitmapGallery))
                            defaultGallery.getPictures().elementAt(i).hide();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            /*Multiple pictures were selected*/
            else if(data.getClipData() != null) {
                /*ClipData is like Clipboard but with data instead of text,
                                Copying the intent data to clipdata because the data is only one data */
                ClipData mClipData = data.getClipData();
                /*Make an array to store all the uri (Path of Images select by user)*/
                ArrayList<Uri> uriList = new ArrayList<Uri>();
                for (int i = 0; i < mClipData.getItemCount(); ++i) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    uriList.add(uri);
                }
                /*Log is for debuging purpose*/
                /*To get the real path, uriList.get(i).getPath(); will do the job*/
                for (int i = 0; i < uriList.size(); ++i) {
                    for(int j = 0; j < defaultGallery.get_photos() ; ++j){
                        /*load the picture fromt the gallery*/
                        Picture picture = defaultGallery.getPictures().elementAt(j);
                        File file = new File(picture.getImage());
                        /*Turn the picture into uri*/
                        Uri uriFromGallery = Uri.fromFile(file);
                        try {
                            /* Make the user selected picture and gallery picture into bitmap*/
                            Bitmap bitmapUser = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriList.get(i));
                            Bitmap bitmapGallery = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uriFromGallery);
                            /*If they are same, hide the picture in Gallery*/
                            if(bitmapUser.sameAs(bitmapGallery))
                                defaultGallery.getPictures().elementAt(j).hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
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
    /*This method will check the preferences value exist or not, if not, initialize them*/
    public void initialize(int Must_Update){
        /*Create a editor to edit*/
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*Check for gallery*/
        if(!sharedPreferences.contains("Gallery")){
            /*Make a new gallery and load all the pictures*/
            Default_Gallery defaultGallery=new Default_Gallery();
            defaultGallery.Load_All(this);
            /*Gson is an object that make an object into a string*/
            Gson gson = new Gson();
            String json = gson.toJson(defaultGallery);
            editor.putString("Gallery", json);
            /*Save the value into shared preferences*/
            editor.apply();
        }
        /*Check is the preferences saved the correct thing*/
        if(sharedPreferences.contains("Gallery")){
            /* Read from the preferences and see is it empty*/
            Default_Gallery checkEmpty;
            Gson gson = new Gson();
            String json = sharedPreferences.getString("Gallery","");
            checkEmpty = gson.fromJson(json, Default_Gallery.class);
            /*Update it when it is empty or force it to update*/
            if(checkEmpty.get_photos() ==  0 || Must_Update == 1){
                checkEmpty.Load_All(this);
                json = gson.toJson(checkEmpty);
                editor.putString("Gallery", json);
                editor.apply();
            }
        }
        /*Check the rate that how long to change to another picture*/
        if(!sharedPreferences.contains("Rate")){
            int rate = 5000;
            editor.putInt("Rate", rate);
            editor.apply();
        }
        /*Check the last picture being shown*/
        if(!sharedPreferences.contains("Index")){
            editor.putInt("Index", 0);
            editor.apply();
        }
    }

    /*The method will read all the value from the shared preferences*/
    public void readPreferences(){
        /*Get the gallery data from the preferences, gson helps to turn a string into an object*/
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery","");
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
        /*Make another gallery that loads everything in the gallery*/
        Default_Gallery checkUpdate = new Default_Gallery();
        checkUpdate.Load_All(this);
        /*If the number of photos are different, it means the user has more/lease images*/
        if(checkUpdate.get_photos() != defaultGallery.get_photos()){
            /* make the gallery be the new one and update it into the shared preferences*/
            defaultGallery = checkUpdate;
            initialize(UPDATE);
        }
    }
}
