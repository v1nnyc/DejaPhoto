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

        final CheckBox mine = (CheckBox)findViewById(R.id.viewMine);
        final CheckBox frd = (CheckBox)findViewById(R.id.viewFrds);
        final CheckBox share = (CheckBox)findViewById(R.id.share);

        mine.setChecked(true);
        frd.setChecked(false);
        share.setChecked(false);
        if(sharedPreferences.getString("View","self").equals("null")){
            mine.setChecked(false);
        }
        if(sharedPreferences.getString("Frd","null").equals("frd")){
            frd.setChecked(true);
        }
        if(sharedPreferences.getString("Share","null").equals("yes")){
            share.setChecked(true);
        }
        Log.v("Check Box Values", sharedPreferences.getString("Frd","") + " " +
                sharedPreferences.getString("View", "") +  " " + sharedPreferences.getString("Share", ""));


        mine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString("View", "self");
                    mine.setChecked(true);
                }
                else{
                    editor.putString("View", "null");
                    mine.setChecked(false);
                }
                editor.apply();
                Log.v("User Check Box Value", sharedPreferences.getString("View",""));

            }
        });
        frd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString("Frd", "frd");
                    frd.setChecked(true);
                }
                else {
                    editor.putString("Frd", "null");
                    frd.setChecked(false);
                }
                editor.apply();
                Log.v("Friend Check Box Value", sharedPreferences.getString("Frd",""));
            }
        });
        share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString("Share", "yes");
                    share.setChecked(true);
                }
                else {
                    editor.putString("Share", "null");
                    share.setChecked(false);
                }
                editor.apply();
                Log.v("Share Check Box Value", sharedPreferences.getString("Share",""));

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
}

