package tests;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;

import comv1nnycdejaphoto.github.dejaphoto.AddFrd;
import comv1nnycdejaphoto.github.dejaphoto.GoogleSignInActivity;
import comv1nnycdejaphoto.github.dejaphoto.MainActivity;
import comv1nnycdejaphoto.github.dejaphoto.R;
import comv1nnycdejaphoto.github.dejaphoto.RateActivity;
import comv1nnycdejaphoto.github.dejaphoto.ViewShareOption;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;


/**
 * Created by ShirleyLam on 5/14/17.
 */


public class JUnitTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);
    //@Rule
    public ActivityTestRule<RateActivity> RateActivity = new ActivityTestRule<>(RateActivity.class);
    @Rule
    public ActivityTestRule<ViewShareOption> viewShareOption = new ActivityTestRule<>(ViewShareOption.class);
    @Rule
    public ActivityTestRule<AddFrd> addFrd = new ActivityTestRule<>(AddFrd.class);
    @Rule
    public ActivityTestRule<GoogleSignInActivity> signIn = new ActivityTestRule<>(GoogleSignInActivity.class);


    /* test on the start screen Choose Album button */
    @Test
    //GOOD
    public void testChooseButton() {
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.choose);
        button.callOnClick();
    }

    /* test on the start screen Release button */
    @Test
    //GOOD
    public void testReleaseButton() {
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.release);
        button.callOnClick();
    }
    

    /* test on the start screen Setting button */
    @Test
    //GOOD
    public void testSettingButton() {
        AppCompatImageButton button = (AppCompatImageButton) mainActivity.getActivity().findViewById(R.id.setting);
        button.callOnClick();
    }

    /* test if clicking the setting button is going to the correct activity */
    @Test
    //BAD CLASS CAST EXCEPTION
    public void testSettingButtonActivity() throws Throwable {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor
                (RateActivity.class.getName(), null, false);

        // open current activity.
        final AppCompatImageButton button = (AppCompatImageButton) mainActivity.getActivity().findViewById(R.id.setting);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        RateActivity rateActivity = (RateActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        // next activity is opened and captured.
        assertNotNull(rateActivity);
        //rateActivity.finish();
    }

    /* test whether the TextView defined in the layout has Display Rate */
    @Test
    //GOOD
    public void testDisplayRate() {
        RateActivity = new ActivityTestRule<>(RateActivity.class);
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.textView);
        String value = textView.getText().toString();
        assertEquals("Display Rate", value);
    }

    /* test whether the TextView defined in the layout has Display Mode */
    @Test
    //GOOD
    public void testDisplayMode(){
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.mode);
        String value = textView.getText().toString();
        assertEquals("Display Mode", value);
    }

    /* test whether the TextView defined in the layout has 5 sec */
    @Test
    //GOOD
    public void testDisplayDurationMin() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.min);
        String value = textView.getText().toString();
        assertEquals("5 sec", value);
    }

    /* test whether the TextView defined in the layout has 1 min */
    @Test
    //GOOD
    public void testDisplayDurationMax() {
        TextView textView = (TextView) RateActivity.getActivity().findViewById(R.id.max);
        String value = textView.getText().toString();
        assertEquals("1 min", value);
    }

    /* test if the first radio button is checked */
    @Test
    //GOOD
    public void testTimeCheckbox() throws Exception {
        RateActivity = new ActivityTestRule<>(RateActivity.class);
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        assertTrue("The first radio button should be checked", radioButton.isChecked());

        RadioGroup radioGroup = (RadioGroup) RateActivity.getActivity().findViewById(R.id.radioGroup);
        assertEquals("The first radio button should be checked", R.id.time,
                radioGroup.getCheckedRadioButtonId());
    }

    /* test if the second radio button is checked */
    @Test
    //GOOD
    public void testDayCheckbox() throws Throwable {
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.day);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        assertTrue(radioButton.isChecked());
    }

    /* test if the third radio button is checked */
    @Test
    //GOOD
    public void testWeekCheckbox() throws Throwable {
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.week);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        assertTrue(radioButton.isChecked());
    }

    /* check if the radio button option is correct for the first button */
    @Test
    //GOOD
    public void testTimeButton() {
        RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.time);
        String value = radioButton.getText().toString();
        assertEquals("Time", value);
    }

    /* check if the radio button option is correct for the second button */
    @Test
    //GOOD
    public void testDayButton() throws Throwable{
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.day);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        String value = radioButton.getText().toString();
        assertEquals("Day", value);
    }

    /* check if the radio button option is correct for the third button */
    @Test
    //GOOD
    public void testWeekButton() throws Throwable {
        final RadioButton radioButton = (RadioButton) RateActivity.getActivity().findViewById(R.id.week);
        RateActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click the second radio button
                radioButton.performClick();
            }
        });
        String value = radioButton.getText().toString();
        assertEquals("Week", value);
    }

    @Test
    //GOOD
    public void testViewMyselfButton() {
        Button button = (Button) viewShareOption.getActivity().findViewById(R.id.viewMine);
        String value = button.getText().toString();
        assertEquals("ViewMySelf", value);
    }

    @Test
    //GOOD
    public void testViewFrdButton() {
        Button button = (Button) viewShareOption.getActivity().findViewById(R.id.viewFrds);
        String value = button.getText().toString();
        assertEquals("ViewFrd", value);
    }

    @Test
    //GOOD
    public void testShareButton() {
        Button button = (Button) viewShareOption.getActivity().findViewById(R.id.share);
        String value = button.getText().toString();
        assertEquals("Share", value);
    }

    @Test
    //BAD CALLED FROM WRONG THREAD EXCEPTION
    public void testPhotoPickerButton() {
        Button button = (Button) mainActivity.getActivity().findViewById(R.id.picker);
        button.performClick();
        String value = button.getText().toString();
        assertEquals("Photos Picker", value);
    }

    @Test
    //BAD CLASS CAST EXCEPTION
    public void testAddFriendButton() throws Throwable {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor
                (AddFrd.class.getName(), null, false);

        // open current activity.
        final Button button = (Button) mainActivity.getActivity().findViewById(R.id.addFrd);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        AddFrd add = (AddFrd) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        // next activity is opened and captured.
        assertNotNull(add);
        add.finish();
    }

    @Test
    //BAD CLASS CAST EXCEPTION
    public void testSignInButton() throws Throwable {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor
                (GoogleSignInActivity.class.getName(), null, false);

        // open current activity.
        final Button button = (Button) mainActivity.getActivity().findViewById(R.id.signin);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        GoogleSignInActivity googleSignIn = (GoogleSignInActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

        // next activity is opened and captured.
        assertNotNull(googleSignIn);
        googleSignIn.finish();
    }
}
