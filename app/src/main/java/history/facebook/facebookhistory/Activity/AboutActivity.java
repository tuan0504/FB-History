package history.facebook.facebookhistory.Activity;


import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import history.facebook.Util.MultiAssetInputStream;

public class AboutActivity extends AppCompatPreferenceActivity {

    /*************************** VARIABLES AND CONSTANT ******************************************/
    private  static  Toolbar bar;
    /*************************** OVERRIDE FUNCTION ******************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(history.facebook.facebookhistory.R.layout.toolbar_about, root, false);
            bar.setTitle(getResources().getString(history.facebook.facebookhistory.R.string.title_activity_about));
            root.addView(bar, 0); // insert at top
        } catch (Exception e) {
            this.setTheme(history.facebook.facebookhistory.R.style.AppTheme);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(history.facebook.facebookhistory.R.xml.preferences_about, target);
    }

    /*************************** COMMON FUNCTION ******************************************/
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || CreaditPreferenceFragment.class.getName().equals(fragmentName)
                || TextFragment.class.getName().equals(fragmentName);
    }

    /*************************** FRAGMENT CLASS ******************************************/
    public static class CreaditPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(bar != null) {
                bar.setTitle(getResources().getString(history.facebook.facebookhistory.R.string.title_developer_credit));
            }
            addPreferencesFromResource(history.facebook.facebookhistory.R.xml.preferences_credits);
        }
    }

    public static class TextFragment extends Fragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            String textTitle = getArguments().getString(getResources().getString(history.facebook.facebookhistory.R.string.text_extra));
            if(textTitle == null) {
                return;
            }

            if(bar != null) {
                bar.setTitle(textTitle);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(history.facebook.facebookhistory.R.layout.fragment_text_about_view, null);
            TextView txtView = (TextView) view.findViewById(history.facebook.facebookhistory.R.id.txt_extra);

            String s = getArguments().getString(getResources().getString(history.facebook.facebookhistory.R.string.text_extra));

            if(s == null) {
                return view;
            }

            if(s.equals(getResources().getString(history.facebook.facebookhistory.R.string.title_google_services))) {
                String text = readInput("License.txt").toString();
                txtView.setText(text);
            } else if(s.equals(getResources().getString(history.facebook.facebookhistory.R.string.title_app_summary))) {
                String text = readInput("Summary.txt").toString();
                txtView.setText(text);
            } else if(s.equals(getResources().getString(history.facebook.facebookhistory.R.string.title_privacy))) {
                String text = readInput("Privacy.txt").toString();
                txtView.setText(text);
            }

            return view;
        }

        private StringBuilder readInput(String filename) {
            InputStream input = new MultiAssetInputStream(getActivity().getAssets(),
                    new String[] { filename});
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String s = null;
            StringBuilder builder = new StringBuilder();
            try {
                while ((s = reader.readLine()) != null) {
                    builder.append(s + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return builder;
        }
    }
}