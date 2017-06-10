package comv1nnycdejaphoto.github.dejaphoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by ShirleyLam on 5/24/17.
 */

public class ViewShareOption extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPreferences();
        editor = sharedPreferences.edit();
        setContentView(R.layout.view_share);


        // set values so checkboxes know what they were previously
        CheckBox mine = (CheckBox)findViewById(R.id.viewMine);
        mine.setChecked(sharedPreferences.getBoolean("ViewMySelf", true));

        CheckBox frd = (CheckBox)findViewById(R.id.viewFrds);
        frd.setChecked(sharedPreferences.getBoolean("ViewFriend", false));

        CheckBox share = (CheckBox)findViewById(R.id.share);
        share.setChecked(sharedPreferences.getBoolean("Share", false));


        mine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(sharedPreferences.getBoolean("ViewFriend",false))
                        loadBoth();
                    else
                        loadMyself();
                    editor.putBoolean("ViewMySelf", true);
                }
                else {
                    if(sharedPreferences.getBoolean("ViewFriend",false))
                        loadFriend();
                    editor.putBoolean("ViewMySelf", false);
                }
                editor.apply();
            }
        });
        frd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(sharedPreferences.getBoolean("ViewMySelf",false))
                        loadBoth();
                    else
                        loadFriend();
                    editor.putBoolean("ViewFriend", true);
                }
                else {
                    if (sharedPreferences.getBoolean("ViewMySelf", false))
                        loadMyself();
                    editor.putBoolean("ViewFriend", false);
                }
                editor.apply();
            }
        });
        share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(user == null)
                        return;
                    else{
                        upload_Gallery_Thread uploadGalleryThread = new upload_Gallery_Thread();
                        uploadGalleryThread.run();
                    }
                    editor.putBoolean("Share", true);
                }
                else {
                    user.uploadDatabase();
                    editor.putBoolean("Share", false);
                }
                editor.apply();
            }

        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, Setting.class));
        finish();
    }


    @Override
    protected void onDestroy(){
        sharedPreferences = getSharedPreferences("ViewShareOption", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("Share", false)){
            loadMyself();
            upload_Gallery_Thread thread = new upload_Gallery_Thread();
            thread.start();
        }
        super.onDestroy();
    }

    public void loadMyself(){
        Gson gson = new Gson();
        Default_Gallery defaultGallery= new Default_Gallery();
        defaultGallery.Load_All(BackgroundService.getContext());
        String json = gson.toJson(defaultGallery);
        sharedPreferences.edit().putString("Gallery", json).apply();
    }
    public void loadFriend(){
        Gson gson = new Gson();
        Default_Gallery defaultGallery= new Default_Gallery();
        defaultGallery.Load_Friend(BackgroundService.getContext());
        String json = gson.toJson(defaultGallery);
        sharedPreferences.edit().putString("Gallery", json).apply();
    }
    public void loadBoth(){
        Gson gson = new Gson();
        Default_Gallery defaultGallery= new Default_Gallery();
        defaultGallery.Load_Friend(BackgroundService.getContext());
        defaultGallery.Load_All(BackgroundService.getContext());
        String json = gson.toJson(defaultGallery);
        sharedPreferences.edit().putString("Gallery", json).apply();
    }
    public void readPreferences() {
        Gson gson = new Gson();
        sharedPreferences = BackgroundService.getContext().getSharedPreferences("DejaPhoto", MODE_PRIVATE);
        String json = sharedPreferences.getString("User","");
        if(json == ""){
            Log.d("Json fro user in add friend ", "Not found");
            return;
        }
        else{
            user = gson.fromJson(json, User.class);
        }
    }


}

