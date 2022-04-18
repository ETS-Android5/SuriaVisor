package uci.suriavisor.presentation;

import android.Manifest;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.adapter.NavigationItemListAdapter;
import uci.suriavisor.presentation.CustomViews.ServiceFloating;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

public class MainActivity extends AppCompatActivity
{

    private static Toolbar mToolbar;
    private static ViewPager mViewPager;
    private static TabLayout tabLayout;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView navList;
    private NavigationItemListAdapter navAdapter;

    VisorController visorController;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        visorController = VisorController.getInstance();
        /*
        if (visorController.getPreferences() == null)
        {
            visorController.readPreferences();
            if (visorController.getPreferences() == null)
                visorController.setdefaultPreferences();
        }
        if(visorController.getPreferences().getThemId() != Constants.THEME_ID)
            Constants.THEME_ID = visorController.getPreferences().getThemId();
        if(visorController.getPreferences().getPathToSave()!= Constants.PATH_TO_SAVE)
            Constants.PATH_TO_SAVE = visorController.getPreferences().getPathToSave();
            */

        setTheme(Constants.THEME_ID);
        setContentView(R.layout.activity_main_);
        navList = (ListView) findViewById(R.id.listDrawer);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                selectItemDrawer(position);
            }
        });
        mTitle = getTitle();

        visorController.changeContext(this);

        switch (Constants.THEME_ID)
        {
            case R.style.AppTheme:
                navList.setBackgroundColor(getResources().getColor(R.color.background_light));
                break;
            case R.style.AppThemeDark:
                navList.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);
            final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        createNav();

        /*
        esto es para pedir la lista de sensores y eventos disponibles
        visorController.requestSensors();
        visorController.requestEvents();*/

        //esto es para obtener la lista de servidores
        //visorController.getServers();
        if(Build.VERSION.SDK_INT > 21)
            checkPermission();

        //MySecurityManager secManager = new MySecurityManager();
        //System.setSecurityManager(secManager);
        //Para obtener las camaras y zonas del servidor
        /*if(visorController.getAdyacents().size()==0)
            visorController.prepareTree();*/
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId())
        {
            case R.id.action_settings:
                MainActivity.this.finish();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNav()
    {
        View header = getLayoutInflater().inflate(R.layout.header_drawer, null);
        navList.addHeaderView(header);
        navAdapter = new NavigationItemListAdapter(this, Constants.arrayItmsDrawer(), Constants.arrayItmsDrawerIcon());
        navList.setAdapter(navAdapter);
        TextView textView = (TextView)header.findViewById(R.id.userName_text);
        textView.setText(visorController.getUser());
        Button sessionButton = (Button)header.findViewById(R.id.session_button);
        sessionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                visorController.onClosed();
            }
        });
    }

/*
    public void treeReady()
    {
        Intent i = new Intent(MainActivity.this, SingleFragmentActivity.class);
        //i.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, SelectableTreeFragment.class);
        i.putExtra(SingleFragmentActivity.FRAGMENT_PARAM,FolderStructureFragment.class);
        this.startActivity(i);
        //this.finish();
        if (materialDialogProgressIndeterminate!=null && materialDialogProgressIndeterminate.isShowing())
            materialDialogProgressIndeterminate.dismiss();
    }*/

    /*@Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }*/

    private void selectItemDrawer(int position)
    {
        //Log.e("position>>>>>", String.valueOf(position));
        if(position == 1)
        {
            Intent i = new Intent(this, ViewsActivity.class);
            startActivity(i);
            finish();
        }
        else if (position == 2)
        {
            VisorController.getInstance().showIndeterminateProgressDialog(false);
            //VisorController.getInstance().prepareTree();
            VisorController.getInstance().showTree();
                    /*Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                    i.putExtra(SingleFragmentActivity.FRAGMENT_PARAM,FolderStructureFragment.class);
                    getActivity().startActivity(i);
                    getActivity().finish();*/
        }

        else if (position == 3)
        {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
            finish();
        }
        else if (position == 4)
        {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            finish();
        }
        else if (position == 5)
        {
            visorController.onClosed();
        }
    }

    public void onSectionAttached(int number)
    {
        switch (number)
        {
            case 1:
                mTitle = getString(R.string.section_Views);
                break;
            case 2:
                mTitle = getString(R.string.section_CamerasTree);
                break;
            case 3:
                mTitle = getString(R.string.section_Search);
                break;
            case 4:
                mTitle = getString(R.string.section_Config);
                break;
            case 5:
                mTitle = getString(R.string.section_exit);
                break;
        }
    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private long backPressedTime = 0;
    @Override
    public void onBackPressed()
    {        // to prevent irritating accidental logouts
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000)
        {    // 2 secs
            backPressedTime = t;
            Toast.makeText(getApplicationContext(), "Presione de nuevo atrás para salir.", Toast.LENGTH_LONG).show();
        }
        else
        {    // this guy is serious
            // clean up
            //super.onBackPressed();       // bye
            visorController.onClosed();
        }
    }

    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    public void checkPermission()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!Settings.canDrawOverlays(MainActivity.this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                //Log.v(TAG,"Permission is granted");
            }
            else
            {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE)
        {
            if(Build.VERSION.SDK_INT >= 23)
                if (!Settings.canDrawOverlays(this))
                {
                    VisorController.getInstance().permissionGranted = false;
                }
        }
    }

    @Override
    protected void onResume()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getString("LAUNCH").equals("YES"))
        {
            VisorController.getInstance().permissionGranted = true;
        }
        VisorController.getInstance().changeContext(this);
        super.onResume();
    }

    class MySecurityManager extends SecurityManager
    {
        @Override public void checkExit(int status)
        {
            stopService(new Intent(MainActivity.this, ServiceFloating.class));
            //throw new SecurityException();
        }
    }
}
