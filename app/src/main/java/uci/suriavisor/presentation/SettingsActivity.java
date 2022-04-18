package uci.suriavisor.presentation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.materialdialogs.DialogAction;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.materialdialogs.folderselector.FolderChooserDialog;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;


public class SettingsActivity extends AppCompatActivity implements FolderChooserDialog.FolderCallback
{

    protected int color;
    private Toolbar toolbar;
    private TextView txtTheme;
    private TextView pathToSave;
    private TextView twCountSnapshot;
    private LinearLayout layout;
    private final static int STORAGE_PERMISSION_RC = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(Constants.THEME_ID);
        setContentView(R.layout.activity_settings);
        VisorController.getInstance().changeContext(this);

        layout = (LinearLayout) findViewById(R.id.settings_layout) ;
        pathToSave = (TextView) findViewById(R.id.settings_activity_storageSelected);
        pathToSave.setText(VisorController.getInstance().getPreferences().getPathToSave());
        switch (Constants.THEME_ID){
            case R.style.AppTheme:
                layout.setBackgroundColor(getResources().getColor(R.color.background_light));
                break;
            case R.style.AppThemeDark:
                layout.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
        }

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
        actionBar.setTitle("Configuración");
        actionBar.show();

        txtTheme = (TextView) findViewById(R.id.settings_activity_themeSelected);
        switch (Constants.THEME_ID)
        {
            case R.style.AppTheme:
            {
                txtTheme.setText("Light");
                break;
            }
            case R.style.AppThemeDark:
            {
                txtTheme.setText("Dark");
                break;
            }
        }

        LinearLayout themeLayout = (LinearLayout) findViewById(R.id.settings_activity_theme_layout);
        themeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                showThemeDialog();
            }
        });

        LinearLayout storageLayout = (LinearLayout) findViewById(R.id.settings_activity_storage_layout);
        if(storageLayout!=null)
            storageLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //chooserDialog = R.id.folder_chooser;
                    if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_RC);
                        return;
                    }
                    //<string name="md_choose_label">Choose</string>
                    int color = getResources().getColor(R.color.background_dark);
                    switch (Constants.THEME_ID)
                    {
                        case R.style.AppTheme:
                        {
                            color =  getResources().getColor(R.color.background_light);
                            break;
                        }
                        case R.style.AppThemeDark:
                        {
                            color =  getResources().getColor(R.color.background_dark);
                            break;
                        }
                    }
                    FolderChooserDialog a= new FolderChooserDialog.Builder(SettingsActivity.this)
                            .chooseButton(R.string.md_choose_label)
                            .allowNewFolder(true,0)
                            .build();
                    a.backgroundColor(color);
                    a.show(SettingsActivity.this);
                }
            });

        LinearLayout  countSnapshotLayout= (LinearLayout) findViewById(R.id.settings_activity_count_snapshot_layout);
        countSnapshotLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showCountSnapshotsDialog();
            }
        });
        twCountSnapshot = (TextView) findViewById(R.id.settings_activity_count_snapshotNumber);
        twCountSnapshot.setText(String.valueOf(VisorController.getInstance().getPreferences().getNumberOfSnapshots()));

        LinearLayout  muteAlarmsLayout= (LinearLayout) findViewById(R.id.settings_activity_mute_alarms_layout);
        final TextView twMuteAlarms = (TextView) findViewById(R.id.settings_activity_mute_alarms_description);
        final CheckBox muteAlarmsCheckBox = (CheckBox) findViewById(R.id.settings_activity_mute_alarms);
        muteAlarmsCheckBox.setChecked(VisorController.getInstance().getPreferences().isMuteAlarms());
        if(muteAlarmsCheckBox.isChecked())
        {
            twMuteAlarms.setText("Desactive este valor para que las alarmas emitan un sonido");
        }
        else
        {
            twMuteAlarms.setText("Active este valor para silenciar las alarmas");
        }
        muteAlarmsCheckBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisorController.getInstance().setMuteAlarms( muteAlarmsCheckBox.isChecked());
                if(muteAlarmsCheckBox.isChecked())
                {
                    twMuteAlarms.setText("Desactive este valor para que las alarmas emitan un sonido");
                }
                else
                {
                    twMuteAlarms.setText("Active este valor para silenciar las alarmas");
                }
            }
        });
        muteAlarmsLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                muteAlarmsCheckBox.setChecked(!muteAlarmsCheckBox.isChecked());
                VisorController.getInstance().setMuteAlarms(muteAlarmsCheckBox.isChecked());
                if(muteAlarmsCheckBox.isChecked())
                {
                    twMuteAlarms.setText("Desactive este valor para que las alarmas emitan un sonido");
                }
                else
                {
                    twMuteAlarms.setText("Active este valor para silenciar las alarmas");
                }
            }
        });
        twCountSnapshot = (TextView) findViewById(R.id.settings_activity_count_snapshotNumber);
        twCountSnapshot.setText(String.valueOf(VisorController.getInstance().getPreferences().getNumberOfSnapshots()));
    }

    public void showThemeDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeDark));
        if(Constants.THEME_ID == R.style.AppTheme)
            builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));

        LayoutInflater inflater = getLayoutInflater();
        View custom_dialog = inflater.inflate(R.layout.dialog_config, null);

        final RadioButton rbLight = (RadioButton) custom_dialog.findViewById(R.id.rbLight);
        final RadioButton rbDark = (RadioButton) custom_dialog.findViewById(R.id.rbDark);

        builder.setView(custom_dialog)
                .setTitle("Temas")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (rbLight.isChecked())
                        {
                            txtTheme.setText("Light");
                            Constants.THEME_ID = R.style.AppTheme;
                            VisorController.getInstance().getPreferences().setThemId(Constants.THEME_ID);
                            recreate();
                        }
                        else if (rbDark.isChecked())
                        {
                            txtTheme.setText("Dark");
                            Constants.THEME_ID = R.style.AppThemeDark;
                            VisorController.getInstance().getPreferences().setThemId(Constants.THEME_ID);
                            recreate();
                        }
                    }
                });
        switch (Constants.THEME_ID)
        {
            case R.style.AppTheme:
            {
                rbLight.setChecked(true);
                break;
            }
            case R.style.AppThemeDark:
            {
                rbDark.setChecked(true);
                break;
            }
        }

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private View positiveAction;
    private EditText count_snapshot_edit;
    public void showCountSnapshotsDialog()
    {
        int color = getResources().getColor(R.color.background_dark);
        switch (Constants.THEME_ID)
        {
            case R.style.AppTheme:
            {
                color =  getResources().getColor(R.color.background_light);
                break;
            }
            case R.style.AppThemeDark:
            {
                color =  getResources().getColor(R.color.background_dark);
                break;
            }
        }
        MaterialDialog dialog = new MaterialDialog.Builder(SettingsActivity.this)
                .backgroundColor(color)
                .title("Cantidad de instantáneas a realizar")
                .customView(R.layout.dialog_count_snapshot, true)
                .positiveText(R.string.action_accept)
                .negativeText(R.string.action_cancell)
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        Constants.NUMBER_OF_SNAPSHOTS = Integer.parseInt(count_snapshot_edit.getText().toString());
                        VisorController.getInstance().getPreferences().setNumberOfSnapshots(Constants.NUMBER_OF_SNAPSHOTS);
                        twCountSnapshot.setText(String.valueOf(VisorController.getInstance().getPreferences().getNumberOfSnapshots()));
                        Toast.makeText(getApplicationContext(), "Cantidad de instantáneas establecida correctamente.", Toast.LENGTH_LONG).show();
                    }
                }).build();
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        count_snapshot_edit = (EditText) dialog.getCustomView().findViewById(R.id.edittext_count_snapshots);
        if(count_snapshot_edit!=null)
        {
            count_snapshot_edit.setText(String.valueOf(Constants.NUMBER_OF_SNAPSHOTS));
            count_snapshot_edit.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    positiveAction.setEnabled(s.toString().trim().length() > 0 && Integer.parseInt(s.toString().trim())>2);
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                }
            });
        }
        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        //int current = Integer.parseInt(getIntent().getStringExtra("position"));
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        /*MainActivity.createViewPager(getSupportFragmentManager(),MainActivity.getViewPager());
        MainActivity.notifyTabLayout();
        MainActivity.getViewPager().setCurrentItem(current);*/
        this.finish();
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder)
    {
        try
        {
            pathToSave.setText(folder.getCanonicalPath());
            Constants.PATH_TO_SAVE = folder.getCanonicalPath();
            VisorController.getInstance().getPreferences().setPathToSave(folder.getCanonicalPath());
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }
}
