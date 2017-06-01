package comv1nnycdejaphoto.github.dejaphoto;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.provider.ContactsContract.CommonDataKinds;
import static android.provider.ContactsContract.Contacts;

/**
 * Created by ShirleyLam on 5/24/17.
 */

@RuntimePermissions
public class AddFrd extends AppCompatActivity {

    User user = new User();

    private final int PICK_CONTACT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends);

        Button connect = (Button)findViewById(R.id.connect);
        Button request = (Button)findViewById(R.id.request);
        final EditText email = (EditText)findViewById(R.id.email);
        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            @NeedsPermission(android.Manifest.permission.READ_CONTACTS)
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);

                startActivityForResult(intent, PICK_CONTACT);

            }

        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.add(email.getText().toString());
            }
        });


    }
    @NeedsPermission(android.Manifest.permission.READ_CONTACTS)
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);

        ArrayList<String> emailAddr = new ArrayList<String>();
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
            Cursor emailCursor = resolver.query(
                    CommonDataKinds.Email.CONTENT_URI, null,
                    CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);

            while (emailCursor.moveToNext()) {
                String email = emailCursor.getString(emailCursor.getColumnIndex(CommonDataKinds.Email.DATA));

                Log.v("getEmail", email);

                if (email != null) {
                    emailAddr.add(email);
                }
            }
            emailCursor.close();
        }
        cursor.close();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }



}


