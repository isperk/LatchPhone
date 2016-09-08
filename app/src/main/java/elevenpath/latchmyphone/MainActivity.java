package elevenpath.latchmyphone;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import layout.AboutFragment;
import layout.PasswordFragment;
import layout.ServiceFragment;
import layout.SettingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button buttonStart;
    Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ContextLatchWimp.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        //initializeViewButtons();

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

        displayView(R.id.nav_default);

        ContextLatchWimp.getInstance().ActivityContext = this;
        ContextLatchWimp.getInstance().service = new WimpService();
    }

    private void initializeViewButtons() {
        String isRun = ContextLatchWimp.getInstance().loadDataPreferenceString("isRun");

        if (isRun.equals("true"))
        {
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
        }
        else
        {
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the   Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId)
    {
        try {


            Fragment fragment = null;
            String title = getString(R.string.app_name);

            switch (viewId) {
                case R.id.nav_settings:
                    fragment = new SettingFragment();
                    title = "Settings";
                    viewIsAtHome = false;
                    break;
                case R.id.nav_password:
                    fragment = new PasswordFragment();
                    title = "Password";
                    viewIsAtHome = false;
                    break;
                case R.id.nav_about:
                    fragment = new AboutFragment();
                    title = "About";
                    viewIsAtHome = false;
                    break;
                case R.id.nav_default:
                    fragment = null;
                    title = "Latch my phone";
                    viewIsAtHome = true;
                    break;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FrameLayout layout = (FrameLayout) findViewById(R.id.content_setting);
            RelativeLayout relativeService = (RelativeLayout) findViewById(R.id.relativeService);

            if (fragment != null) {
                ft.replace(R.id.content_setting, fragment);
                layout.setVisibility(View.VISIBLE);
                relativeService.setVisibility(View.INVISIBLE);
                ft.commit();
            }
            else
            {
                layout.setVisibility(View.INVISIBLE);
                relativeService.setVisibility(View.VISIBLE);
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        catch(Exception ex)
        {
            Log.e("ErrorApp", ex.getMessage());
        }
    }

    private boolean viewIsAtHome;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displayView(R.id.nav_default); //display the News fragment
        } else {
            moveTaskToBack(true);  //If view is in News fragment, exit application
        }
    }

    // Method to start the service
    public void startService(View view) {

        try {

            ContextLatchWimp.getInstance().initializeLatch();

            String isPair = ContextLatchWimp.getInstance().loadDataPreferenceString("isPair");

            if (isPair.equals("true")) {
                startService(new Intent(getBaseContext(), WimpService.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Service is not Pair.", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception err)
        {
            Log.e("ErrorAppStartService", err.getMessage());
        }
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MainActivity.class));
    }

    public void saveSettings(View view) {

        try{


        }
        catch(Exception ex)
        {
            Log.e("AppError", ex.getMessage());
        }

    }
}
