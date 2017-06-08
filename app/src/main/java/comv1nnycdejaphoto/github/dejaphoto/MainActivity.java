
package comv1nnycdejaphoto.github.dejaphoto;

 /* This class used third party library Gson made by google.
        * Obtained the library from https://github.com/google/gson.
        */
import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    User user = new User();

    SharedPreferences sharedPreferences;
    private Default_Gallery defaultGallery;
    private static Context sContext;
    private BackgroundService backgroundService;
    private Boolean isBound;
    private boolean mReturningWithResult = false;

    /* This will tell the different between choose or release, 1 is for choose, 0 for release*/
    final int CAPTURE_PICTURE = 2;
    static final int PICK_CHOOSE = 1;
    static final int PICK_RELEASE = 0;
    static final int MY_REQUEST_CODE = 3;

    //constructor
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        /* Check is the photo directory exist, if not, create them*/
        if(checkFolderExist(sContext))
            Log.v("File:","Exist");
        else {
            Log.v("File", "Does Not Exist, Have to Create them");
            createDirectory(sContext);
        }
        setContentView(R.layout.select_album);


        /*Ask the permission to read the images*/
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        setContentView(R.layout.start_screen);
        /*Button that links to the Choose*/
        Button load_image = (Button) findViewById(R.id.choose);
        /*onClick Event*/
        load_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*Ask user to pick a image and save its uri, make the result become intent*/
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              /* pass the intent with the option of choose button beign clicked*/
                startActivityForResult(intent, PICK_CHOOSE);
            }
        });
         /* Same thing but for the release button*/
        Button release_image = (Button) findViewById(R.id.release);
        release_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*Ask user to pick a image and save its uri, make the result become intent*/
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              /* pass the intent with the option of choose button beign clicked*/
                startActivityForResult(intent, PICK_CHOOSE);
            }
        });
        /* link to the camera for users to take pictures */
        ImageButton camera = (ImageButton) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            /* onClick Event */
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_REQUEST_CODE);
                }
                startActivityForResult(camera, CAPTURE_PICTURE);
            }


        });
        releasePictures();
        setDisplayRate();

        //start background service
        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Gson gson = new Gson();
        sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        String json = sharedPreferences.getString("Gallery", "");
        defaultGallery = new Default_Gallery();
        defaultGallery.Load_All(BackgroundService.getContext());
        defaultGallery = gson.fromJson(json, Default_Gallery.class);
        if(data == null){
            return;
        }
        /*If user press back while picking images, exit the method */
        /*The choose button being clicked*/

        switch(requestCode) {
            case CAPTURE_PICTURE: {
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    Savefile(getResizedBitmap(bmp));
                    defaultGallery = new Default_Gallery();
                    defaultGallery.Load_All(getContext());
                    Log.v("Number of photo beinng loaded", Integer.toString(defaultGallery.get_photos()));
                    json = gson.toJson(defaultGallery);
                    sharedPreferences.edit().putString("Gallery", json).apply();
                }
                return;
            }
            case PICK_CHOOSE: {
                if (data != null) {
                    //TODO  choose album
                    /*https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery*/
            /*Get the data as type of Uri*/
                    Uri uri = data.getData();
                    Log.v("Choosed Path", uri.getPath());
                }
                return;
            }
            case PICK_RELEASE: {
                /*Only one picture is selected*/
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    for (int i = 0; i < defaultGallery.get_photos(); ++i) {
                        Picture picture = defaultGallery.getPictures().elementAt(i);
                        //if (picture.isEqual(uri))
                        //  defaultGallery.getPictures().elementAt(i).hide();
                    }
                }

            /*Multiple pictures were selected*/
                else if (data.getClipData() != null) {
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
                        for (int j = 0; j < defaultGallery.get_photos(); ++j) {
                        /*load the picture from the gallery*/
                            Picture picture = defaultGallery.getPictures().elementAt(j);
                            //if (picture.isEqual(uriList.get(i)))
                            //  defaultGallery.getPictures().elementAt(j).hide();
                        }
                    }
                }
                return;
            }

        }
        /*json = gson.toJson(defaultGallery);
        sharedPreferences.edit().putString("Gallery", json).apply();*/

        mReturningWithResult = true;

    }

    //this method let users select the duration of each picture being displayed
    public void setDisplayRate() {
    /* link to the setting page for users to set display rate */
        ImageButton setting = (ImageButton) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener(){
            /* onClick Event */
            @Override
            public void onClick(View view){
                /* setContentView(R.layout.rate); */
                Intent intent = new Intent(getBaseContext(),RateActivity.class);
                startActivity(intent);
            }
        });
    }

    //this method is for users release pictures
    public void releasePictures() {
    /* Same thing but for the release button*/
        Button release_image = (Button) findViewById(R.id.release);
        release_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_RELEASE);
            }
        });
        /* link to the add friend page for users to send friend request */
        ImageButton addFrd = (ImageButton) findViewById(R.id.addFrd);
        addFrd.setOnClickListener(new View.OnClickListener() {
            /* onClick Event */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddFrd.class);
                startActivity(intent);
            }
        });

        /* link to the setting page for users to set display rate */
        ImageButton setting = (ImageButton) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            /* onClick Event */
            @Override
            public void onClick(View view) {
                /* setContentView(R.layout.rate); */
                Intent intent = new Intent(getBaseContext(), Setting.class);
                startActivity(intent);
            }
        });

        /* link to the sign in page for users to sign in */
        ImageButton signIn = (ImageButton) findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            /* onClick Event */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GoogleSignInActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        startService(intent);
    }
    /*required for other classes to be able to access MainActivity*/
    public static Context getContext() {
        return sContext;
    }

    public void Savefile(Bitmap bm) {
        Uri internal_storage = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        Toast.makeText(sContext, ""+root, Toast.LENGTH_SHORT).show();
        //File myDir = new File(root + "/DejaPhoto");
        File myDir = new File(root);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        Log.i("saved", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
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

                }
                else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }

                return;

            }
            // add other cases for more permissions
        }
    }

    /*Check is the photo directory within app exists, return true if exist*/
    public boolean checkFolderExist(Context context){
        String path = context.getPackageCodePath() + "/Photos";
        File photo_path = new File(path);
        if(photo_path.exists())
            return true;
        else
            return false;
    }

    public Bitmap getResizedBitmap(Bitmap bm) {
        WindowManager windowManager = (WindowManager) MainActivity.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        /* get size of the screen */
        Point size = new Point();
        display.getSize(size);
        int newWidth = size.x;
        int newHeight = size.y;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /*Called if directories does not exists, create them*/
    public void createDirectory(Context context){
        String path = context.getPackageCodePath() + "/Photos";
        File photo_path = new File(path);
        if(photo_path.mkdir())
            Log.v("Create Directory Photos","Failed");
        else
            Log.v("Create Directory Photos","Success");
        photo_path = new File(path + "/MyFriends");
        if(photo_path.mkdir())
            Log.v("Create Directory MyFriends","Failed");
        else
            Log.v("Create Directory MyFriends","Success");
        photo_path = new File(path + "/MyFriendsAndMe");
        if(photo_path.mkdir())
            Log.v("Create Directory MyFriendsAndMe","Failed");
        else
            Log.v("Create Directory MyFriendsAndMe","Success");
    }
}
