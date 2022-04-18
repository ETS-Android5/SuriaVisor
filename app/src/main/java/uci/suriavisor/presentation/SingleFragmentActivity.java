package uci.suriavisor.presentation;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class SingleFragmentActivity extends AppCompatActivity
{
    public final static String FRAGMENT_PARAM = "fragment";
    private Toolbar toolbar;
    private Fragment f;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setTheme(Constants.THEME_ID);
        setContentView(R.layout.activity_single_fragment);
        VisorController.getInstance().changeContext(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Árbol de Cámaras");
        actionBar.show();

        Bundle b = getIntent().getExtras();
        Class<?> fragmentClass = (Class<?>) b.get(FRAGMENT_PARAM);
        if (bundle == null)
        {
            f = Fragment.instantiate(this, fragmentClass.getName());
            f.setArguments(b);
            getFragmentManager().beginTransaction().replace(R.id.fragment, f, fragmentClass.getName()).commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        //int current = Integer.parseInt(getIntent().getStringExtra("position"));
        startActivity(new Intent(SingleFragmentActivity.this, MainActivity.class));
        /*MainActivity.createViewPager(getSupportFragmentManager(),MainActivity.getViewPager());
        MainActivity.notifyTabLayout();
        MainActivity.getViewPager().setCurrentItem(current);*/
        this.finish();
    }
/*
    @Override
    protected void onResume()
    {
        VisorController.getInstance().changeContext(this);
        super.onResume();
    }*/
}
