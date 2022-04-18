package uci.suriavisor.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import uci.suriavisor.logic.TreeElement;
import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.adapter.RecyclerAlarmAdapter;
import uci.suriavisor.presentation.adapter.RecyclerTreeElementAdapter;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

import java.util.LinkedList;

/*
import cu.uci.agoravmobile.logic.entities.Entity;
import cu.uci.agoravmobile.presentation.adapters.ContentItemListAdapter;
import cu.uci.agoravmobile.presentation.adapters.VideoItemListAdapter;*/

public class SearchActivity extends AppCompatActivity {
    
    private EditText txtSearch;
    private ImageButton buttonSearch;
    CheckBox boxZone, boxCamera;
    private RelativeLayout layout;
    public VisorController visorController =  VisorController.getInstance();
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(Constants.THEME_ID);
        setContentView(R.layout.activity_search);
        VisorController.getInstance().changeContext(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Buscar");
        actionBar.show();

        txtSearch = (EditText) findViewById(R.id.editTxt_search);
        buttonSearch = (ImageButton) findViewById(R.id.button_search);

        if (Constants.THEME_ID == R.style.AppTheme)
        {
            buttonSearch.setBackgroundResource(R.drawable.ic_search_black);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_tree_elements);

        layout = (RelativeLayout) findViewById(R.id.search_layout);

        switch (Constants.THEME_ID)
        {
            case R.style.AppTheme:
                layout.setBackgroundColor(getResources().getColor(R.color.background_light));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.background_light));
                break;
            case R.style.AppThemeDark:
                layout.setBackgroundColor(getResources().getColor(R.color.background_dark));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
        }
        
        buttonSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String busqueda =  txtSearch.getText().toString();
                if (!busqueda.isEmpty())
                {
                    boxZone = (CheckBox)findViewById(R.id.cbZone);
                    boxCamera = (CheckBox)findViewById(R.id.cbCamera);
                    if (!boxZone.isChecked() && !boxCamera.isChecked()){
                        Toast.makeText(SearchActivity.this, "Seleccione al menos una opción de búsqueda", Toast.LENGTH_SHORT).show();
                    }else {
                        if (boxZone.isChecked() && !boxCamera.isChecked()){
                            LinkedList<TreeElement> treeElementsFound = visorController.searchZone(busqueda);
                            showSearchResult(treeElementsFound);
                        }else if (!boxZone.isChecked() && boxCamera.isChecked()){
                            LinkedList<TreeElement> treeElementsFound = visorController.searchCamera(busqueda);
                            showSearchResult(treeElementsFound);
                        }else if (boxZone.isChecked() && boxCamera.isChecked()){
                            LinkedList<TreeElement> treeElementsFound = visorController.searchAll(busqueda);
                            showSearchResult(treeElementsFound);
                        }
                    }
                    txtSearch.setText("");
                }
                else
                {
                    Toast.makeText(SearchActivity.this, "Introduzca un criterio de búsqueda", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                }
            }
        });
    }

    public void showSearchResult(LinkedList<TreeElement> treeElementsFound)
    {
        if (!treeElementsFound.isEmpty())
        {
            txtSearch.clearFocus();
            LinearLayoutManager llm = new LinearLayoutManager(VisorController.getInstance().getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new RecyclerTreeElementAdapter(treeElementsFound));
        }
        else
        {
            recyclerView.setAdapter(null);
            Toast.makeText(SearchActivity.this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {        // to prevent irritating accidental logouts
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
        /*
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000)
        {    // 2 secs
            backPressedTime = t;
            Toast.makeText(getApplicationContext(), "Presione de nuevo atras para salir.", Toast.LENGTH_LONG).show();
        }
        else
        {    // this guy is serious
            // clean up
            //super.onBackPressed();       // bye
            visorController.writeViews();
            stopService(new Intent(ViewsActivity.this, ServiceFloating.class));
            System.exit(0);
        }*/
    }
}
