package comv1nnycdejaphoto.github.dejaphoto;

/**
 * Created by Ken on 5/11/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.provider.FirebaseInitProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Ref;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import comv1nnycdejaphoto.github.dejaphoto.Picture;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.decode;

/**
 * Created by Ken on 5/9/2017.
 */

/* Got basic idea of how code will be like from
http://stackoverflow.com/questions/30777023/diplaying-all-images-from-device-inside-my-app
*/
public class Default_Gallery{
    Default_Gallery defaultGallery;
    boolean no_show = false;
   // SharedPreferences sharedPreferences = null;

    int index;
    private Vector<Picture> pictures = new Vector<Picture>();
    private int num_photos;


    public Default_Gallery(){};


    public Vector<Picture> getPictures(){
        return pictures;
    }


    // download each friend's array of photos then populates the friend array
    public Default_Gallery download_Friends(User user, Context context, FirebaseDatabase data){
        pictures = new Vector<Picture>();
        num_photos = 0;
        ArrayList<String> friends = user.getFriendsList(); // get the friends
        int size = friends.size();

        SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // empties out friends
        editor.putString("Gallery","" );
        // for every friend
        for(int i =0; i < size; i++){

            // get the friend name and store it temporary

            editor.putString("Temp", friends.get(i));

            editor.apply();

            // get database location of the friend
            data = FirebaseDatabase.getInstance();
            DatabaseReference ref = data.getReference(friends.get(i) + "/gallery/");
            Query query = ref.child("");

            Log.v("Starting this", "Hi");

            // gets data at creation of listener and only once
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // get the json gallery
                    String val = (String) dataSnapshot.getValue();
                    SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);

                    // get the friends
                    String friend = sharedPreferences.getString("Temp","");

                    // get the array from the json gallery
                    Gson gson = new Gson();
                    Database_Picture[]  data_val = gson.fromJson(val, Database_Picture[].class);
                    Log.v("Gallery of Pictures", val);

                    // builds a gallery based on the json gallery (array of database photos
                    defaultGallery = Populate_Gallery(data_val, friend);

                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    if(sharedPreferences.getString("Gallery", "") != ""){
                        // create function to merge galleries
                    }

                        // converts current gallery to json to store as sharedPreference
                    String json = gson.toJson(defaultGallery);
                    Log.v("Gallery inputted is", json);
                    edit.putString("Gallery", json);
                    Log.v("Finished Uploading Friends Gallery", "" + defaultGallery.get_photos());
                    edit.apply();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });/*
            String json = sharedPreferences.getString("Temp", "");
            Log.v("Temp what", json + "Hello");

            Gson gson = new Gson();
            Database_Picture[]  data_val = gson.fromJson(json, Database_Picture[].class);

            Populate_Gallery(data_val, friends.get(i));*/
        }

        return this;
    }

    // Given an array of photos from database, stores them in a directory and puts them in current Gallery
    public Default_Gallery Populate_Gallery(Database_Picture[] imgs, String friend){

        // Placed Photos in a DCIM directory, can be changed
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File mydir = new File(root + "/MyFriends/");

        mydir.mkdirs();

        // for every database photo
        for(int i = 0; i < imgs.length; i++){
            // decodes the Bitmap
            Bitmap image = decode64(imgs[i].getImage());

            // Gets the directory and picture ready
            File file = new File(mydir, friend + i);
            Database_Picture pic = imgs[i];

            if (file.exists())
                file.delete();
            try {
                // puts file in directory
                FileOutputStream out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // basically creates new picture with the path used and copies the data
            pictures.add(num_photos, new Picture(mydir + "/" + friend + i, pic.getDate(), pic.getTime(), pic.getLocatio()));
            num_photos++;
        }
        Log.v("Populated some photos from 1 friend", "" +num_photos);
        return this;
    }

    // encodes a bitmap in a string
    public static String encode64(Bitmap image, Bitmap.CompressFormat compressFormat){
        int quality = 0; // how good the picture is encoded (more blocky at lower value) goes from 0 to 100
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteOS);
        return Base64.encodeToString(byteOS.toByteArray(), Base64.DEFAULT);
    }

    // Decodes an encoded Bitmap string back to its original format
    public static Bitmap decode64(String compressed_bitmap){
        byte[] Bytes = Base64.decode(compressed_bitmap, 0); // 0 is just start place/offset in array
        return BitmapFactory.decodeByteArray(Bytes, 0, Bytes.length);
    }


    // Uploads user default gallery to share
    public FirebaseDatabase upload_Gallery(User user, Context context, FirebaseDatabase data){
        SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);

        // counts all the non-removed photos in album
        int not_removed = 0;
        for(int i = 0; i < num_photos; i++){
            if(pictures.elementAt(i).getDisplay() == false){
                continue;
            }
            not_removed++;
        }

        // only adds upto the amount of non-removed photos
        int added = 0;

        String files [] = new String[not_removed];
        Database_Picture imgs [] = new Database_Picture[not_removed];
        for(int i = 0; i < num_photos; i++){
            //File file = new File(pictures.elementAt(i).getImage());
            //files[i] = file;
            if(pictures.elementAt(i).getDisplay() == false){
                continue;
            }

            Bitmap image = BitmapFactory.decodeFile(pictures.elementAt(i).getImage());
            String file = encode64(image, Bitmap.CompressFormat.JPEG );
            files[i] = file;
            imgs[i] = new Database_Picture(file, pictures.elementAt(i));
            added++;
        }

        data = FirebaseDatabase.getInstance();
        DatabaseReference ref = data.getReference(user.getName());
        DatabaseReference temp = ref.child("/gallery");
        String json = "";
        Gson gson = new Gson();

        json = gson.toJson(imgs, Database_Picture[].class);
        temp.setValue(json);

        temp = ref.child("/user_info");
        ref.setValue("Update All Others");



        Log.v("loaded an image", " Hello");
        //ref.setValue(json);
        return data;
    }


    public void Load_All(Context context){
        Log.v("Loading","ALL");
        load(context, "USER");
    }

    public void Load_Friend(Context context){
        Log.v("Loading", "Friend");
        load(context,"Friend");
    }
    public void load(Context context,String path){
        Geocoder decoder = new Geocoder(context);            // one time creation for location finding
        Uri internal_storage = null;
        if(path == "USER") {
            internal_storage = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()); // storage of all photos\
            Log.v("Path of user", ""+internal_storage.getPath());
        }
        if(path == "Friend"){
            PackageManager m = context.getPackageManager();
            String dir = context.getPackageName();
            PackageInfo p = null;
            try {
                p = m.getPackageInfo(dir, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dir = p.applicationInfo.dataDir;
            dir = dir + "/Photos/DejaPhotoFriends";
            internal_storage = Uri.parse(dir);
            Log.v("Path of friend", ""+internal_storage.getPath());
        }
        if(internal_storage == null){
            Log.d("Loading Error", "Null path");
        }
        File directory = new File(internal_storage.getPath());
        /* Filter to read only images*/
        File[] files = directory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
            }
        });
        if(files != null){
            /* Loop through the  images*/
            for(File f : files){
                String fileName = f.getName(); // this is file name
                try {
                    /* Read the file with exifinterface*/
                    ExifInterface exifInterface = new ExifInterface(f.getAbsolutePath());
                    String time = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
                    String date = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
                    String latitude =  null;
                    String longitude = null;
                    String latitudeRef =  null;
                    String longitudeRef = null;
                    String location = "No Location";
                    /* The format will be xx/x,xx/x,xxxx/xxxx*/
                    latitude= exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    /* It will be either 'W' 'E or 'N' 'S' */
                    latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                    longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                    /*If they are all not null, it means they have location infromation*/
                    if(latitude != null && longitude != null && latitudeRef != null && longitudeRef != null) {
                        double latiString = conversion(latitude,latitudeRef);
                        double longString = conversion(longitude,longitudeRef);
                        Log.i("Lati", Double.toString(latiString));
                        Log.i("Long",Double.toString(longString));
                        location = get_Location(context,latiString,longString,decoder);
                    }
                    Log.i("FIle name", fileName);
                    Log.i("Location" , location);
                    pictures.add(new Picture(f.getPath(), date, time, location));
                    num_photos++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//    public void load(Context context, String path){
//        Log.v("Loading","ALL");
//

//
//    }

    public double conversion(String converThis,String ref){
        /* Format : xx/yy,aa/bb,cccc/dddd or something like this*/
        String temp = converThis;
        double ans = 0;
        /* temp now will be xx*/
        temp = converThis.substring(0, converThis.indexOf('/'));
        ans = Double.parseDouble(temp);
        /* converThis will be ,aa/bb,cccc/dddd*/
        converThis = converThis.substring(converThis.indexOf(','));
        /* temp = aa*/
        temp = converThis.substring(1, converThis.indexOf('/'));
        ans = ans +  Double.parseDouble(temp)/60;
        /*converThis = aa/bb,cccc/dddd*/
        converThis = converThis.substring(1);
        /*converThis = ,cccc/dddd*/
        converThis = converThis.substring(converThis.indexOf(','));
        /* temp = cccc*/
        temp = converThis.substring(1,converThis.indexOf('/'));
        /* converThis = /dddd*/
        converThis = converThis.substring(converThis.indexOf('/'));
        /* converThis = dddd */
        converThis = converThis.substring(1);
        ans = ans + Double.parseDouble(temp)/3600 / Double.parseDouble(converThis);
        if(ref.compareTo("S") == 0 || ref.compareTo("W") == 0)
            ans = ans* -1;
        return ans;
    }

    public File retrieve_Photo(String Path) {
        File retrieved_image = new File(Path);  // finds image through file
        if(retrieved_image.exists()){
            return retrieved_image;
        }
        else{
            return null;
        }

    }

    public int get_photos(){
        return num_photos;
    }

    public String get_Location(Context context, double latitude, double longitude, Geocoder decoder){
        decoder = new Geocoder(context);  // using the geocoder
        List<Address> address = null;
        try { // try and get the address location from gps
            address = decoder.getFromLocation(latitude, longitude, 1);
        }
        catch(Exception e) {
            // ignore (bad code style) but used to fix case of adjusting all methods to throw exception
        }

        // using the address data type retrieved from gps
        // check various location types and outputs the most suitable name first
        if(address == null)
            return "No Location";
        Address add = address.get(0);

        //obtain address details
        return getAddressDetails(add);
    }

    //this method get detail information of the address
    public String getAddressDetails(Address add) {
        // names are made sure that they aren't entirely just numbers
        if(add.getFeatureName() != null && !add.getFeatureName().matches("[0-9]+")) {
            return add.getFeatureName();
        }
        else if(add.getAddressLine(0)!= null && !add.getAddressLine(0).matches("[0-9]+")) {
            return add.getAddressLine(0);
        }
        else if(add.getSubLocality() != null && !add.getSubLocality().matches("[0-9]+")) {
            return add.getSubLocality();
        }
        else if(add.getLocality() != null && !add.getLocality().matches("[0-9]+")) {
            return add.getLocality();
        }
        else if(add.getSubAdminArea() != null && !add.getSubAdminArea().matches("[0-9]+")) {
            return add.getSubAdminArea();
        }
        else if(add.getAdminArea() != null && !add.getAdminArea().matches("[0-9]+")){
            return add.getAdminArea();
        }
        else {
            return "No Discernible Location Name";
        }
    }

    public void next(){
        readPreferences();
        wallpaper wp = new wallpaper();
        if (defaultGallery.get_photos() != 0) {
            int last = index;
            SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            while ((index + 1) != last) {
                if (index == (defaultGallery.get_photos() - 1))
                    index = -1;
                Log.d("# photos", "" + defaultGallery.get_photos());
                Log.d("index" , "" + index);
                if ( defaultGallery.getPictures().elementAt(index + 1).getDisplay()) {
                    Picture picture = defaultGallery.getPictures().elementAt(index+1);
                    File file = new File(picture.getImage());
                    Uri uriFromGallery = Uri.fromFile(file);
                    wp.changeWallpaper(uriFromGallery, picture.getLocatio(),picture.getLikes());
                    editor.putInt("Index", index + 1);
                    break;
                }
                index = index + 1;
            }
            Log.v("Displaying",Integer.toString(index));
            editor.apply();
        }
        else wp.emptyPicture();
    }

    public void readPreferences(){
        /* Read the shared preferences*/

        SharedPreferences sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();

        /*gson is a way to put the object into shared preferences*/


        Gson gson = new Gson();
        String json = sharedPreferences.getString("Gallery","");
        //defaultGallery = new Default_Gallery();
        //defaultGallery.Load_All(BackgroundService.getContext());
        defaultGallery = gson.fromJson(json, Default_Gallery.class);


        index = sharedPreferences.getInt("Index",1);
    }

    /*
    /* Used to choose one of 3 galleries we store in shared preferences
    public static Default_Gallery Choose_Gallery(Context context){
        boolean friend = false;
        boolean user = true;
        Gson gson = new Gson();
       // Gson gson = new GsonBuilder().registerTypeAdapter(SharedPreferences.class, new InterfaceAdapter<SharedPreferences>()).create();
        SharedPreferences sharedPreference = context.getSharedPreferences("DejaPhoto",MODE_PRIVATE);
        SharedPreferences sharedPref_View = context.getSharedPreferences("ViewShareOption",MODE_PRIVATE);
        String json;
        Default_Gallery gall = null;

        //Log.v("Check Box Values", sharedPref_View.getString("Frd", "null") + " " + sharedPref_View.getString("View","null"));
        friend = sharedPref_View.getBoolean("ViewFriend",false);
        user = sharedPref_View.getBoolean("ViewMySelf",true);
        //  SharedPreferences.Editor editor = sharedPreferences.edit();
        json = sharedPreference.getString("Gallery","");
        gall = gson.fromJson(json, Default_Gallery.class);

        if(friend && user){
            //json = sharedPreference.getString("All", "");       // change names of galleries if need to
            //gall = gson.fromJson(json, Default_Gallery.class);
            Log.v("Uploaded Both friends and user galleries", "ALL");
        }
        else if(user){
            //json = sharedPreference.getString("Gallery","");
            //gall = gson.fromJson(json, Default_Gallery.class);
            Log.v("Uploaded just user gallery", "User");
        }
        else if(friend) {
            json = sharedPreference.getString("Friends", "");

            gall = gson.fromJson(json, Default_Gallery.class);

            Log.v("Uploaded just friend gallery", json + gall.get_photos());
        }
        else{
            Log.v("Uploaded no gallery", "None");
        }
        //json = sharedPreference.getString("Gallery","");
        //gall = gson.fromJson(json, Default_Gallery.class);

        return gall;

    }*/

    public void sortByTime(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("hh:mm:ss");
        readPreferences();
        int token = 0;
        /* If the time is within 2 hours, move them to the front*/
        for(int i = 0; i<defaultGallery.get_photos();++i){
            if(defaultGallery.getPictures().elementAt(i).timeWithinBounds()) {
                if (token == defaultGallery.get_photos() - 2) {
                    break;
                }
                else{
                    Collections.swap(defaultGallery.pictures,i,token);
                    token++;
                }
            }
        }
        /*For the photo with the same condiction, move the one with karma to the front*/
        int karmaCount = 0;
        for(int i = 0; i < token; ++i){
            if(defaultGallery.getPictures().elementAt(i).getKarma()){
                Collections.swap(defaultGallery.pictures,i,karmaCount);
                karmaCount++;
            }
        }
        /*Compare two picture's day, move the one took earlier to the back, for non karma pictures*/
        for(int i = 0; i < karmaCount-1; ++i){
            for(int j = 1; j <karmaCount; ++j) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        /* do the same thing for karma pictures*/
        for(int i = karmaCount; i < (token -1); i++){
            for(int j = karmaCount + 1; j < token ; j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        /*Do the same for match mode condition*/
        for(int i = token ; i < defaultGallery.get_photos()-1; ++i) {
            for (int j = token + 1; j < defaultGallery.get_photos(); j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*Same as sort By time*/
    public void sortByDay(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("MM:dd");
        Date today = Calendar.getInstance().getTime();
        today.setYear(0);
        readPreferences();
        int token  = 0;
        for(int i = 0; i<defaultGallery.get_photos(); ++i){
            try {
                Date dateToken = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getDate());
                dateToken.setYear(0);
                if(today.equals(dateToken)){
                    Collections.swap(defaultGallery.pictures, i, token);
                    token++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int karmaCount = 0;
        for(int i = 0; i < token; ++i){
            if(defaultGallery.getPictures().elementAt(i).getKarma()){
                Collections.swap(defaultGallery.pictures,i,karmaCount);
                karmaCount++;
            }
        }
        for(int i = 0; i < karmaCount-1; ++i){
            for(int j = 1; j <karmaCount; ++j) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = karmaCount; i < (token -1); i++){
            for(int j = karmaCount + 1; j < token ; j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = token ; i < defaultGallery.get_photos()-1; ++i) {
            for (int j = token + 1; j < defaultGallery.get_photos(); j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void sortByWeek(){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy:MM:dd");
        Calendar today = Calendar.getInstance();
        today.setTime(today.getTime());
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int token = 0;
        for(int i = 0; i < defaultGallery.get_photos() ; ++i){
            try {
                Calendar weekToken = Calendar.getInstance();
                Date took = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getDate());
                weekToken.setTime(took);
                if(dayOfWeek == weekToken.get(Calendar.DAY_OF_WEEK)) {
                    Collections.swap(defaultGallery.pictures, i, token);
                    token++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }int karmaCount = 0;
        for(int i = 0; i < token; ++i){
            if(defaultGallery.getPictures().elementAt(i).getKarma()){
                Collections.swap(defaultGallery.pictures,i,karmaCount);
                karmaCount++;
            }
        }
        for(int i = 0; i < karmaCount-1; ++i){
            for(int j = 1; j <karmaCount; ++j) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = karmaCount; i < (token -1); i++){
            for(int j = karmaCount + 1; j < token ; j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = token ; i < defaultGallery.get_photos()-1; ++i) {
            for (int j = token + 1; j < defaultGallery.get_photos(); j++) {
                try {
                    Date date1 = simpleDateFormat.parse(defaultGallery.getPictures().get(i).getTime());
                    Date date2 = simpleDateFormat.parse(defaultGallery.getPictures().get(j).getDate());
                    if (date1.before(date2))
                        Collections.swap(defaultGallery.pictures, i, j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}