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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("ViewShareOption", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.view_share);

        CheckBox mine = (CheckBox)findViewById(R.id.viewMine);
        CheckBox frd = (CheckBox)findViewById(R.id.viewFrds);
        CheckBox share = (CheckBox)findViewById(R.id.share);

        if(sharedPreferences.getBoolean("ViewMySelf",true) == true)
            mine.setChecked(true);
        else
            mine.setChecked(false);

        if(sharedPreferences.getBoolean("ViewFriend",false) == true)
            frd.setChecked(true);
        else
            frd.setChecked(false);

        if(sharedPreferences.getBoolean("Share",false) == true)
            share.setChecked(true);
        else
            share.setChecked(false);

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
                if (isChecked)
                    editor.putBoolean("Share",true);
                else
                    editor.putBoolean("Share",false);
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

}

