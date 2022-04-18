package uci.suriavisor.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.CustomView;
import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;
import uci.suriavisor.presentation.adapter.RecyclerViewAdapter;
import uci.suriavisor.presentation.materialdialogs.DialogAction;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;

public class ViewsActivity extends AppCompatActivity
{

    // custom view dialog
    private EditText viewNameInput;
    private SeekBar seekBarCountScreen;
    private TextView textViewCountScreen;
    private View positiveAction;
    private boolean defaultView = false;
    private int countScreen = -1;
    RecyclerView recyclerView;
    private VisorController visorController;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(Constants.THEME_ID);
        setContentView(R.layout.layout_recyclerview_views);
        visorController = VisorController.getInstance();
        visorController.changeContext(this);

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
        actionBar.setTitle("Vistas");
        actionBar.show();

        defaultView = false;
        loadViewList();
        FloatingActionButton btn_addView = (FloatingActionButton) findViewById(R.id.btn_addView);
        btn_addView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //poner esto
                showDialog(null);
                //quitar esto
                //visorController.showAlarm("0","0");
            }
        });
    }

    public void loadViewList()
    {
        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView_Views);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(VisorController.getInstance().getCustomViews());
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() > 0){
            TextView textView = (TextView)findViewById(R.id.tvViewEmpty);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public void showDialog(final CustomView customView)
    {
        if(customView!=null)
        {
            countScreen = customView.getCantAreas();
            defaultView = customView.isDefaultView();
        }
        else
        {
            countScreen = 1;
            defaultView = false;
        }
        int color = VisorController.getInstance().getContext().getResources().getColor(R.color.background_dark);
        if(Constants.THEME_ID == R.style.AppTheme)
            color =  VisorController.getInstance().getContext().getResources().getColor(R.color.background_light);
        MaterialDialog dialog = new MaterialDialog.Builder(ViewsActivity.this)
                .backgroundColor(color)
                .title("Datos de la Vista")
                .customView(R.layout.dialog_addcustomview, true)
                .positiveText(R.string.action_accept)
                .negativeText(R.string.action_cancell)
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        if(countScreen != 0)
                        {
                            if(customView!=null)
                            {
                                if(defaultView)
                                    visorController.disableDefault();
                                customView.setDefaultView(defaultView);
                                customView.setName(viewNameInput.getText().toString());
                                customView.setCantAreas(countScreen);
                                customView.setIdsCameras(countScreen);
                                visorController.showMessage("Vista editada correctamente.");
                            }
                            else
                            {
                                if (!viewNameInput.getText().toString().equals(""))
                                {
                                    CustomView customView1 = new CustomView(viewNameInput.getText().toString(), "0", countScreen);
                                    customView1.setDefaultView(defaultView);
                                    visorController.addCustomView(customView1);
                                    visorController.showMessage("Vista agregada correctamente.");
                                }else {
                                    //Toast.makeText(ViewsActivity.this, "Escriba un nombre a la vista por favor.", Toast.LENGTH_SHORT).show();
                                    viewNameInput.setError("Campo obligatorio");
                                }
                            }
                            loadViewList();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Debe seleccionar al menos un área de visualización.", Toast.LENGTH_LONG).show();
                        }


                    }
                }).build();
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        viewNameInput = (EditText) dialog.getCustomView().findViewById(R.id.edittext_viewname);
        if(customView!=null)
            viewNameInput.setText(customView.getName());
        viewNameInput.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // Toggling the show password CheckBox will mask or unmask the password input EditText
        CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.checkbox_dafaultview);
        if(customView!=null)
            checkbox.setChecked(customView.isDefaultView());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                defaultView = isChecked;
                positiveAction.setEnabled(!viewNameInput.getText().toString().equals(""));

            }
        });
        textViewCountScreen = (TextView) dialog.getCustomView().findViewById(R.id.TextView_CountScreen);
        if(customView!=null)
            textViewCountScreen.setText("Cantidad de áreas: "+String.valueOf(customView.getCantAreas()));
        seekBarCountScreen = (SeekBar) dialog.getCustomView().findViewById(R.id.seekBar_CountScreen);
        if(customView!=null)
            seekBarCountScreen.setProgress(customView.getCantAreas());
        seekBarCountScreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser)
            {
                countScreen = progresValue;
                String s ="Cantidad de areas: "+String.valueOf(countScreen);
                textViewCountScreen.setText(s);
                positiveAction.setEnabled(viewNameInput.getText().toString().trim().length() > 0);

                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

    @Override
    protected void onResume()
    {
        VisorController.getInstance().changeContext(this);
        super.onResume();
    }

    private long backPressedTime = 0;
    @Override
    public void onBackPressed()
    {        // to prevent irritating accidental logouts
        visorController.writeViews();
        startActivity(new Intent(this,MainActivity.class));
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
