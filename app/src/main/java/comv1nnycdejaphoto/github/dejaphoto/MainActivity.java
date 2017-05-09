package comv1nnycdejaphoto.github.dejaphoto;

import android.app.WallpaperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button cameraRoll;
    Button customAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}
