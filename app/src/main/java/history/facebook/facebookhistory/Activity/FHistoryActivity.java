package history.facebook.facebookhistory.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import history.facebook.UI.Factory.FactoryContentView;
import history.facebook.facebookhistory.Fragment.Interface.IFragmentContent;
import history.facebook.facebookhistory.R;

public class FHistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /************************** VARIABLES AND CONSTANTS *************************************/
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    private Toolbar toolbar;
    private View currentViewToolbar;
    private Map<Integer , Integer> typeFMap = new HashMap<>();

    private static Fragment fContent;
    /*************************** OVERRIDE FUNCTION ******************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //init data to show Fragment ContentView
        initData();

        //request permission
        if(Build.VERSION.SDK_INT >= 23) {
            insertDummny();
        }

        //Create View
        setContentView(R.layout.activity_main_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);


        //Show Fragment
        FragmentManager fg = getSupportFragmentManager();

        if(fContent == null) {
            //Open Guide User
            fContent = FactoryContentView.getInstance(typeFMap.get(R.id.nav_search));
        }
        fg.beginTransaction().replace(R.id.ContentView, fContent).commit();

        //ReDraw toolbar
        UpdateUIToolBar((IFragmentContent) fContent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //incase share url we don't need fragment
        if(typeFMap.get(item.getItemId()) == FactoryContentView.SHARE_CONTENT) {
            shareTextUrl();
            return true;
        } else if (typeFMap.get(item.getItemId()) == FactoryContentView.LICENSE_CONTENT) {
            Intent intent = new Intent(this , AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        } else if (typeFMap.get(item.getItemId()) == FactoryContentView.RATE_APP) {
            rateApp();
            return true;
        }

        //get TypeFragment
        int typeFragment = typeFMap.get(item.getItemId());
        //Show Fragment
        FragmentManager fg = getSupportFragmentManager();
        fContent = FactoryContentView.getInstance(typeFragment);
        fg.beginTransaction().replace(R.id.ContentView, fContent).commit();

        //ReDraw toolbar
        UpdateUIToolBar((IFragmentContent)fContent);

        //Draw Layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*************************** COMMON FUNCTION ******************************************/
    /**
     * init data for create View
     */
    private void initData() {
        initMap();
    }

    /**
     * init data for Map to Create Fragment
     */
    private void initMap() {
        typeFMap.put(R.id.nav_search , FactoryContentView.SEARCH_CONTENT);
        typeFMap.put(R.id.nav_rate , FactoryContentView.RATE_APP);
        typeFMap.put(R.id.nav_share , FactoryContentView.SHARE_CONTENT);
        typeFMap.put(R.id.nav_license, FactoryContentView.LICENSE_CONTENT);
    }

    /*************************** UI FUNCTION ******************************************/

    public void UpdateUIToolBar(IFragmentContent content) {
        View view = content.getViewToolBar((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE));
        if( view == null) {
            return;
        }
        if(currentViewToolbar != null && currentViewToolbar.getParent() == toolbar) {
            toolbar.removeView(currentViewToolbar);
        }
        currentViewToolbar = view;
        toolbar.addView(currentViewToolbar , new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /*************************** CHECK PERMISSION FUNCTION ******************************************/
    @SuppressLint("NewApi")
    private void insertDummny() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.INTERNET))
            permissionsNeeded.add("Internet");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
    }

    @SuppressLint("NewApi")
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(FHistoryActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    new AlertDialog.Builder(FHistoryActivity.this)
                            .setMessage("App Will Close Because Don't have Permission")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    insertDummny();
                                }
                            })
                            .create()
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Share Url of Andriod Application
     */
    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "");
        share.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(share, "Share link!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Share Url of Andriod Application
     */
    private void rateApp() {
        String url = "";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
