package cs246.groupApp.dndapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Settings page. Allows the user to edit preferences about the app.
 * @author Alex Kearns
 */
//  https://alvinalexander.com/android/android-tutorial-preferencescreen-preferenceactivity-preferencefragment
public class PreferencesActivity extends PreferenceActivity {

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
    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.prefs);

            // set texts correctly
            onSharedPreferenceChanged(null, "");
        }

        /*@Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }*/

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            /*ListPreference lp = (ListPreference) findPreference("themeStyle");

            switch (lp.getValue())
            {
                case "1":
                    getActivity().setTheme(R.style.AppThemeLight);
                    break;

                case "2":
                    getActivity().setTheme(R.style.AppThemeDark);
                    break;

                case "3":
                    getActivity().setTheme(R.style.DnDark);
                    break;
            }
            setPreferenceScreen(null);
            addPreferencesFromResource(R.xml.prefs);*/
        }
    }
}