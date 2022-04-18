package uci.suriavisor.presentation.CustomViews;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.CustomView;
import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.MainActivity;
import uci.suriavisor.presentation.SearchActivity;
import uci.suriavisor.presentation.SingleFragmentActivity;
import uci.suriavisor.presentation.ViewsActivity;
import uci.suriavisor.presentation.fragment.FolderStructureFragment;
import uci.suriavisor.presentation.materialdialogs.DialogAction;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.playerlibrary.PlayerView;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

import java.util.LinkedList;

public class ViewOneCameraActivity extends Activity implements PlayerView.OnChangeListener, View.OnClickListener, IPlayerActivity
{

    MaterialDialog materialDialogProgressIndeterminate = null;
    private ImageButton ibSize, ibMute, ibSnapshot, ibSnapshots, ibPopup, ibRecord, ibAddCamera,
            ibDeleteCamera, ibSetMark, ibMenuCamera, ibAddSensor, ibLockCamera;

    public String getUrl()
    {
        return url;
    }

    private int indexCustomView, lastIndexCustomView = -1;

    public void setUrl(String url)
    {
        this.url = url;
    }

    private String url = "";
    private TextView tvBuffer;
    private View rlLoading;
    private PlayerView mPlayerView;
    private CustomView customView;
    //private PopupMenu popupMenu;
    RecyclerView recyclerView;
    VisorController visorController;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(Constants.THEME_ID);
        //setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_view_one_camera);

        visorController = VisorController.getInstance();
        visorController.changeContext(this);
        isFullscreen = false;

        //android:visibility="gone" pone invisible un Layout
        ibMenuCamera = (ImageButton) findViewById(R.id.ib_menu_camera);
        ibMenuCamera.setOnClickListener(this);
        ibSetMark = (ImageButton) findViewById(R.id.ib_set_mark);
        ibSetMark.setOnClickListener(this);
        ibAddCamera = (ImageButton) findViewById(R.id.ib_add_camera);
        ibAddCamera.setOnClickListener(this);
        ibRecord = (ImageButton) findViewById(R.id.ib_record);
        ibRecord.setOnClickListener(this);
        ibSnapshot = (ImageButton) findViewById(R.id.ib_snapshot);
        ibSnapshot.setOnClickListener(this);
        ibSnapshots = (ImageButton) findViewById(R.id.ib_snapshots);
        ibSnapshots.setOnClickListener(this);
        /*ibDeleteCamera = (ImageButton) findViewById(R.id.ib_delete_camera);
        ibDeleteCamera.setOnClickListener(this);
        ibMute = (ImageButton) findViewById(R.id.ib_mute);
        ibMute.setOnClickListener(this);*/
        llOverlay = findViewById(R.id.ll_overlay);

        //showIndeterminateProgressDialog(false);

        //
        if(getIntent().getExtras().get("lastIndexCustomView") != null)
            lastIndexCustomView = (int) getIntent().getExtras().get("lastIndexCustomView");

        indexCustomView = Integer.parseInt(getIntent().getExtras().getString("indexCustomView"));
        visorController.setCurrentView(indexCustomView);
        if(indexCustomView >= 0 && visorController.getCustomViews().size()> indexCustomView)
            customView = visorController.getCustomViews().get(indexCustomView);
        else
            customView = visorController.customViewTemporal;
        if(!customView.getIdsCameras().get(0).equals("-1"))
        {
            //mostrar el video
            ibAddCamera.setVisibility(View.INVISIBLE);
            visorController.getUrlStreamingById(customView.getIdsCameras().get(0));
            visorController.getCameraStatus(customView.getIdsCameras().get(0));
        }
        else
        {
            ibAddCamera.setVisibility(View.VISIBLE);
        }
    }

    public void playCamera(String idCamera, String urlStreaming){
        if (customView.getIdsCameras().get(0).equals(idCamera)){
            if (!urlStreaming.equals("")){
                mPlayerView = (PlayerView) findViewById(R.id.pv_video);
                rlLoading = findViewById(R.id.rl_loading);
                mPlayerView.initPlayer(urlStreaming);
                mPlayerView.widthCustom = 1;
                mPlayerView.heightCustom = 1;
                mPlayerView.start();
                setUrl(urlStreaming);
            }else {
                Toast.makeText(this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                deleteCamera();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_custom_view, menu);
        return true;
    }

    private void setMute()
    {
        if (mPlayerView !=null && mPlayerView.isPlaying())
        {
            if (!mPlayerView.isMute)
            {
                mPlayerView.setVolume(0);
                mPlayerView.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView.setVolume(100);
                mPlayerView.isMute = false;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnMute:
                /*
                if(!visorController.isMuted())
                {
                    visorController.setMute(false);
                    ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
                }
                else
                {
                    visorController.setMute(true);
                    ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
                }*/
                setMute();
                break;
            case R.id.mnPopup:
                showMessage("Me llamo popup... :-)");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void stoppedRecord(String idCamera)
    {
        //actualizar el visual
        ibRecord.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
        //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
    }

    public void startedRecord(String idCamera)
    {
        //actualizar el visual
        ibRecord.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
        //Toast.makeText(this, "Se ha iniciado la grabación.", Toast.LENGTH_SHORT).show();
    }
    public void showIndeterminateProgressDialog(boolean horizontal)
    {
        materialDialogProgressIndeterminate = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
    }
    private boolean isFullscreen = false;
    /*
    private void toggleFullscreen(boolean fullscreen)
    {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullscreen)
        {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        else
        {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(attrs);
    }*/

    @Override
    public void onError()
    {
        Toast.makeText(getApplicationContext(), "Inténtelo más tarde.", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onEnd()
    {
        this.finish();
    }

    @Override
    public void onBufferChanged(float buffer)
    {
        if (buffer >= 100) {
            hideLoading();
        } else {
            showLoading();
        }
        tvBuffer.setText("Cargando..." + (int) buffer + "%");
    }

    private void showLoading()
    {
        rlLoading.setVisibility(View.VISIBLE);

    }

    public void hideLoading()
    {
        rlLoading.setVisibility(View.GONE);
        //quitar esto
        //mPlayerView.seek(11000);
    }

    @Override
    public void onLoadComplet()
    {
        // mHandler.sendEmptyMessage(ON_LOADED);
    }

    private PopupWindow mPopupWindow;
    private View popupView;
    private void showMenuCamera(View view)
    {
        /*
        popupMenu = new PopupMenu(ViewOneCameraActivity.this, view);
        popupMenu.setOnDismissListener(new OnDismissListener());
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
        popupMenu.inflate(R.menu.popup_menu_camera);
        popupMenu.show();*/
        popupView = getLayoutInflater().inflate(R.layout.popup_window_menu_camera, null);


        mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        ibAddSensor = (ImageButton) popupView.findViewById(R.id.ib_add_sensor);
        ibAddSensor.setOnClickListener(this);

        if (lastIndexCustomView != -1){
            ibSize = (ImageButton) popupView.findViewById(R.id.ib_size);
            ibSize.setBackgroundResource(R.drawable.ic_normalscreen_white);
            ibSize.setOnClickListener(this);
        }else {
            ibSize = (ImageButton) popupView.findViewById(R.id.ib_size);
            ibSize.setEnabled(false);
            ibSize.setOnClickListener(this);
        }

        ibPopup = (ImageButton) popupView.findViewById(R.id.ib_popup);
        ibPopup.setOnClickListener(this);
        ibDeleteCamera = (ImageButton) popupView.findViewById(R.id.ib_delete_camera);
        ibDeleteCamera.setOnClickListener(this);
        ibMute = (ImageButton) popupView.findViewById(R.id.ib_mute);
        ibMute.setOnClickListener(this);
        ibLockCamera = (ImageButton) popupView.findViewById(R.id.ib_lock_camera);
        if(visorController.getCameraById(customView.getIdsCameras().getFirst()).isLock())
            ibLockCamera.setBackgroundResource(R.drawable.ic_lock_open_white_24dp);
        else
            ibLockCamera.setBackgroundResource(R.drawable.ic_lock_outline_white_24dp);
        ibLockCamera.setOnClickListener(this);
        //mPopupWindow.showAsDropDown(view, 150, 150);
        //int [] coor = new int [2];
        //view.getLocationInWindow(coor);
        //mPopupWindow.showAsDropDown(ibMenuCamera, coor[0], coor[1]);
        //mPopupWindow.showAsDropDown(ibMenuCamera);
        //mPopupWindow.showAtLocation(view, Gravity.RIGHT, 238, 18);
        int [] coor = new int [2];
        view.getLocationInWindow(coor);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, coor[0], coor[1]);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ib_mute:
                if (mPlayerView.isPlaying())
                {
                    if (!mPlayerView.isMute)
                    {
                        mPlayerView.setVolume(0);
                        mPlayerView.isMute = true;
                    } else
                    {
                        mPlayerView.setVolume(100);
                        mPlayerView.isMute = false;
                    }
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.ib_size:
                if(lastIndexCustomView != -1)
                {
                    visorController.showCustomView(lastIndexCustomView);
                    finish();
                }
                else
                    toggleFullscreen();
                break;
            case R.id.ib_snapshot:
                //snapshot(1);
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots:
                //snapshot(Constants.NUMBER_OF_SNAPSHOTS);
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_popup:
                //visorController.setCameraPlaying(visorController.getCameraById(customView.getIdsCameras().getFirst()));
                visorController.setCameraPlaying(urlByPosCamera(0));
                startService(new Intent(ViewOneCameraActivity.this, ServiceFloating.class));
                break;
            case R.id.ib_record:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    if (!customView.getIdsCameras().getFirst().equals("-1") && visorController.getCameraById((customView.getIdsCameras().getFirst())).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().getFirst());
                    else
                        visorController.startRecord(customView.getIdsCameras().getFirst());
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_add_camera:
                addCamera();
                break;
            case R.id.ib_delete_camera:
                deleteCamera();
                mPopupWindow.dismiss();
                break;
            case R.id.ib_set_mark:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    if (!customView.getIdsCameras().getFirst().equals("-1") && visorController.getCameraById(customView.getIdsCameras().getFirst()).isRecoding())
                        visorController.getCurrentTime(customView.getIdsCameras().getFirst());
                        //visorController.setMark(customView.getIdsCameras().getFirst(),String.valueOf(mPlayerView.getTime()));
                    else
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_menu_camera:
                if(!customView.getIdsCameras().getFirst().equals("-1") )
                {
                    showMenuCamera(view);
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_add_sensor:
                /*if (!customView.getIdsCameras().getFirst().equals("-1"))
                    visorController.getCurrentTime(customView.getIdsCameras().getFirst());*/
                showSensors(view);
                break;
            case R.id.ib_lock_camera:
                visorController.lockCamera(customView.getIdsCameras().getFirst());
                if(mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                break;
            default:
                break;
        }
    }

    private String urlByPosCamera(int pos)
    {
        switch (pos)
        {
            case 0:
                return mPlayerView.getUrl();
        }
        return "";
    }

    public void showSensors(View view)
    {
        mPopupWindow.dismiss();
        String[] words = new String[visorController.getSensors().size()];
        final LinkedList<Integer> activatedList = new LinkedList<>();
        for (int i = 0; i < words.length; i++)
        {
            words[i] = visorController.getSensors().get(i).getId();
            if(visorController.getCameraById(customView.getIdsCameras().getFirst()).getIdsSensorActivated().contains(visorController.getSensors().get(i).getId()))
                activatedList.add(i);
        }
        Integer[] activated = new Integer[activatedList.size()];
        for (int i = 0; i < activatedList.size(); i++)
        {
            activated[i] = activatedList.get(i);
        }
        new MaterialDialog.Builder(this)
                .title("Sensores")
                .items(words)
                .itemsCallbackMultiChoice(activated, new MaterialDialog.ListCallbackMultiChoice()
                {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text)
                    {
                        visorController.getCameraById(customView.getIdsCameras().getFirst()).clearActivedSensor();
                        for (int i = 0; i < which.length; i++)
                        {
                            Log.e("sensor name:",visorController.getSensors().get(which[i]).getId());
                            String idSensor = visorController.getSensors().get(which[i]).getId();
                            visorController.getCameraById(customView.getIdsCameras().getFirst()).getIdsSensorActivated().add(idSensor);
                        }
                        return true; // allow selection
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        if(visorController.getCameraById(customView.getIdsCameras().getFirst()).getIdsSensorActivated().size()>0)
                        {
                            dialog.clearSelectedIndices();
                            visorController.getCameraById(customView.getIdsCameras().getFirst()).clearActivedSensor();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        for (int i = 0; i < visorController.getSensors().size(); i++)
                        {
                            String idSensor = visorController.getSensors().get(i).getId();
                            if(!visorController.getCameraById(customView.getIdsCameras().getFirst()).getIdsSensorActivated().contains(idSensor) && activatedList.contains(i))
                                VisorController.getInstance().setActiveSensor(customView.getIdsCameras().getFirst(),idSensor,false);
                            else if(visorController.getCameraById(customView.getIdsCameras().getFirst()).getIdsSensorActivated().contains(idSensor) && !activatedList.contains(i))
                                VisorController.getInstance().setActiveSensor(customView.getIdsCameras().getFirst(), idSensor, true);
                        }
                        dialog.dismiss();
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .positiveText(R.string.action_accept)
                .autoDismiss(false)
                .neutralText(R.string.clear_selection)
                .show();
    }

    public void deleteCamera()
    {
        if (!customView.getIdsCameras().isEmpty())
        {
            customView.getIdsCameras().removeFirst();
            customView.getIdsCameras().add(String.valueOf(-1));
            ibAddCamera.setVisibility(View.VISIBLE);
            mPopupWindow.dismiss();
            if(mPlayerView!= null)
            {
                mPlayerView.getmSurface().setVisibility(View.INVISIBLE);
                mPlayerView.releasePlayer();
            }
        }
        else
        {
            showMessage("No existe ninguna cámara para eliminar.");
        }
    }

    public void addCamera ()
    {
        String [] array = {"Árbol","Búsqueda"};
        new MaterialDialog.Builder(this)
                .title("Seleccionar desde")
                .items(array)
                .itemsCallback(new MaterialDialog.ListCallback()
                {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text)
                    {
                        Intent i;
                        if (text.equals("Árbol"))
                        {
                            i = new Intent(getApplicationContext(), SingleFragmentActivity.class);
                            i.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, FolderStructureFragment.class);
                        }
                        else
                        {
                            i = new Intent(getApplicationContext(), SearchActivity.class);
                        }
                        i.putExtra("entryType", "addCamera");
                        i.putExtra("posArea", 0);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
    }
/*
    private void snapshot(int cant)
    {
        FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
        String fileName;
        int cantProcced = 0;
        for (int i = 0; i < cant; i++)
        {
            try
            {
                fmmr = new FFmpegMediaMetadataRetriever();
                fmmr.setDataSource(customView.getIdsCameras().getFirst().getUrl(), new HashMap<String, String>());
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
            Bitmap thumbnail = fmmr.getFrameAtTime();
            fileName = Constants.PATH_TO_SAVE+"/"+String.valueOf(mPlayerView.getTime())+"."+Constants.EXTENSION_OF_SNAPSHOT;
            File fileObj =new File(fileName);
            if(!fileObj.exists())
            {
                if (Constants.saveSnapshotInFile(fileName, thumbnail))
                    cantProcced++;
            }
            else
                i--;
        }
        if (cantProcced == cant && cantProcced != 1)
        {
            showMessage("Ráfaga de instantáneas guardadas correctamente.");
        }
        else if(cantProcced == 1)
            showMessage("Instantánea guardada correctamente.");
        //ibSnapshots.setBackgroundColor(Color.TRANSPARENT);
    }*/

    private void toggleFullscreen()
    {
        View decorView = getWindow().getDecorView();
        int uiOptions;
        if(isFullscreen)
        {
            isFullscreen = false;
            getActionBar().show();
        }
        else
        {
            getActionBar().hide();
            isFullscreen = true;
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
            hideOverlay();
        }
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags ^= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private View llOverlay;

    private void showOverlay()
    {
        if(llOverlay!=null)
        {
            llOverlay.setVisibility(View.VISIBLE);
        }
    }

    private void hideOverlay()
    {
        if(llOverlay!=null)
        {
            llOverlay.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (llOverlay.getVisibility() != View.VISIBLE)
            {
                showOverlay();
            }
            else
            {
                hideOverlay();
            }
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        releasePlayers();
        startActivity(new Intent(this, ViewsActivity.class));
        super.onBackPressed();
    }

    public void releasePlayers()
    {
        if(mPlayerView!= null)
            mPlayerView.releasePlayer();
    }

    @Override
    public void finish()
    {
        releasePlayers();
        visorController.setCameraPlaying(null);
        super.finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        releasePlayers();
        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        recreate();
        super.onRestart();
    }
}
