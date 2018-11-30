package cs246.groupApp.dndapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Settings page. Allows the user to edit preferences about the app.
 * @author Alex Kearns
 */
//  https://alvinalexander.com/android/android-tutorial-preferencescreen-preferenceactivity-preferencefragment
public class PreferencesActivity extends PreferenceActivity  {

    /**
     * Loads the preference page
     * @author Alex Kearns
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    /**
     * Class to contain/load the preferences page
     * @author Alex Kearns
     */
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
        }
    }
}