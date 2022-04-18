package uci.suriavisor.presentation.CustomViews;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import uci.suriavisor.logic.Camera;
import uci.suriavisor.logic.CustomView;
import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.MainActivity;
import uci.suriavisor.presentation.SearchActivity;
import uci.suriavisor.presentation.SingleFragmentActivity;
import uci.suriavisor.presentation.ViewsActivity;
import uci.suriavisor.presentation.fragment.FolderStructureFragment;
import uci.suriavisor.presentation.materialdialogs.DialogAction;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.playerlibrary.PlayerView;

import java.util.LinkedList;

public class ViewThreeCameraActivity extends Activity implements PlayerView.OnChangeListener, View.OnClickListener, IPlayerActivity
{

    MaterialDialog materialDialogProgressIndeterminate = null;
    private String url1 = "",url2 = "", url3 = "";
    private TextView tvBuffer1, tvBuffer2, tvBuffer3;
    private View rlLoading1, rlLoading2, rlLoading3;
    private PlayerView mPlayerView1, mPlayerView2, mPlayerView3;
    private boolean isFullscreen = false;

    private ImageButton ibSize, ibMute, ibAddSensor, ibLockCamera, ibPopup, ibDeleteCamera,
            ibSnapshot31, ibSnapshots31, ibRecord31, ibAddCamera31, ibSetMark31, ibMenuCamera31,
            ibSnapshot32, ibSnapshots32, ibRecord32, ibAddCamera32, ibSetMark32, ibMenuCamera32,
            ibSnapshot33, ibSnapshots33, ibRecord33, ibAddCamera33, ibSetMark33, ibMenuCamera33;
    private CustomView customView;
    VisorController visorController;
    private int indexCustomView, lastIndexCustomView = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(Constants.THEME_ID);
        setContentView(R.layout.activity_view_three_camera);

        visorController = VisorController.getInstance();

        visorController.changeContext(this);
        isFullscreen = false;

        ibMenuCamera31 = (ImageButton) findViewById(R.id.ib_menu_camera31);
        ibMenuCamera31.setOnClickListener(this);
        ibSetMark31 = (ImageButton) findViewById(R.id.ib_set_mark31);
        ibSetMark31.setOnClickListener(this);
        ibAddCamera31 = (ImageButton) findViewById(R.id.ib_add_camera31);
        ibAddCamera31.setOnClickListener(this);
        ibRecord31 = (ImageButton) findViewById(R.id.ib_record31);
        ibRecord31.setOnClickListener(this);
        ibSnapshot31 = (ImageButton) findViewById(R.id.ib_snapshot31);
        ibSnapshot31.setOnClickListener(this);
        ibSnapshots31 = (ImageButton) findViewById(R.id.ib_snapshots31);
        ibSnapshots31.setOnClickListener(this);

        ibMenuCamera32 = (ImageButton) findViewById(R.id.ib_menu_camera32);
        ibMenuCamera32.setOnClickListener(this);
        ibSetMark32 = (ImageButton) findViewById(R.id.ib_set_mark32);
        ibSetMark32.setOnClickListener(this);
        ibAddCamera32 = (ImageButton) findViewById(R.id.ib_add_camera32);
        ibAddCamera32.setOnClickListener(this);
        ibRecord32 = (ImageButton) findViewById(R.id.ib_record32);
        ibRecord32.setOnClickListener(this);
        ibSnapshot32 = (ImageButton) findViewById(R.id.ib_snapshot32);
        ibSnapshot32.setOnClickListener(this);
        ibSnapshots32 = (ImageButton) findViewById(R.id.ib_snapshots32);
        ibSnapshots32.setOnClickListener(this);

        ibMenuCamera33 = (ImageButton) findViewById(R.id.ib_menu_camera33);
        ibMenuCamera33.setOnClickListener(this);
        ibSetMark33 = (ImageButton) findViewById(R.id.ib_set_mark33);
        ibSetMark33.setOnClickListener(this);
        ibAddCamera33 = (ImageButton) findViewById(R.id.ib_add_camera33);
        ibAddCamera33.setOnClickListener(this);
        ibRecord33 = (ImageButton) findViewById(R.id.ib_record33);
        ibRecord33.setOnClickListener(this);
        ibSnapshot33 = (ImageButton) findViewById(R.id.ib_snapshot33);
        ibSnapshot33.setOnClickListener(this);
        ibSnapshots33 = (ImageButton) findViewById(R.id.ib_snapshots33);
        ibSnapshots33.setOnClickListener(this);

        llOverlay31 = findViewById(R.id.ll_overlay31);
        llOverlay32 = findViewById(R.id.ll_overlay32);
        llOverlay33 = findViewById(R.id.ll_overlay33);

        tvBuffer1 = (TextView) findViewById(R.id.tv_buffer31);
        tvBuffer2 = (TextView) findViewById(R.id.tv_buffer32);
        tvBuffer3 = (TextView) findViewById(R.id.tv_buffer33);

        rlLoading1 = findViewById(R.id.rl_loading31);
        rlLoading2 = findViewById(R.id.rl_loading32);
        rlLoading3 = findViewById(R.id.rl_loading33);

        //Code de Jose
        if(getIntent().getExtras().get("lastIndexCustomView") != null)
            lastIndexCustomView = (int) getIntent().getExtras().get("lastIndexCustomView");

        indexCustomView = Integer.parseInt(getIntent().getExtras().getString("indexCustomView"));
        visorController.setCurrentView(indexCustomView);

        if(indexCustomView >= 0 && visorController.getCustomViews().size()> indexCustomView)
            customView = visorController.getCustomViews().get(indexCustomView);
        else
            customView = visorController.customViewTemporal;

        if (!customView.getIdsCameras().get(0).equals("-1"))
        {
            ibAddCamera31.setVisibility(View.INVISIBLE);
            visorController.getUrlStreamingById(customView.getIdsCameras().get(0));
            visorController.getCameraStatus(customView.getIdsCameras().get(0));
        }
        else
            ibAddCamera31.setVisibility(View.VISIBLE);

        if (!customView.getIdsCameras().get(1).equals("-1"))
        {
            ibAddCamera32.setVisibility(View.INVISIBLE);
            visorController.getUrlStreamingById(customView.getIdsCameras().get(1));
            visorController.getCameraStatus(customView.getIdsCameras().get(1));
        }
        else
            ibAddCamera32.setVisibility(View.VISIBLE);

        if (!customView.getIdsCameras().get(2).equals("-1"))
        {
            ibAddCamera33.setVisibility(View.INVISIBLE);
            visorController.getUrlStreamingById(customView.getIdsCameras().get(2));
            visorController.getCameraStatus(customView.getIdsCameras().get(2));
        }
        else
            ibAddCamera33.setVisibility(View.VISIBLE);

        //playCamera();
    }

    public void playCamera(String idCamera, final String urlStreaming)
    {
        if (customView.getIdsCameras().get(0).equals(idCamera))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        url1 = urlStreaming;
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        if (!url1.equals("")){
                            rlLoading1 = findViewById(R.id.rl_loading31);
                            tvBuffer1 = (TextView) findViewById(R.id.tv_buffer31);
                            mPlayerView1 = (PlayerView) findViewById(R.id.pv_video31);
                            mPlayerView1.widthCustom = 1;
                            mPlayerView1.heightCustom = 3;
                            mPlayerView1.initPlayer(url1);
                            mPlayerView1.start();
                        }else {
                            Toast.makeText(ViewThreeCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 0;
                            deleteCamera();
                        }
                    } catch (Exception e)
                    {
                        e.fillInStackTrace();
                    }
                }
            });
        } else if (customView.getIdsCameras().get(1).equals(idCamera))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        url2 = urlStreaming;
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        if (!url2.equals("")){
                            rlLoading2 = findViewById(R.id.rl_loading32);
                            tvBuffer2 = (TextView) findViewById(R.id.tv_buffer32);
                            mPlayerView2 = (PlayerView) findViewById(R.id.pv_video32);
                            mPlayerView2.widthCustom = 1;
                            mPlayerView2.heightCustom = 3;
                            mPlayerView2.initPlayer(url2);
                            mPlayerView2.start();
                        }else {
                            Toast.makeText(ViewThreeCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 1;
                            deleteCamera();
                        }
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } else if (customView.getIdsCameras().get(2).equals(idCamera))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        url3 = urlStreaming;
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        if (!url3.equals("")){
                            rlLoading3 = findViewById(R.id.rl_loading33);
                            tvBuffer3 = (TextView) findViewById(R.id.tv_buffer33);
                            mPlayerView3 = (PlayerView) findViewById(R.id.pv_video33);
                            mPlayerView3.widthCustom = 1;
                            mPlayerView3.heightCustom = 3;
                            mPlayerView3.initPlayer(url3);
                            mPlayerView3.start();
                        }else {
                            Toast.makeText(ViewThreeCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 2;
                            deleteCamera();
                        }
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void showDialogMark(String idCamera, String currentTime)
    {

    }

    public void stoppedRecord(String idCamera) {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0) {
            ibRecord31.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación...", Toast.LENGTH_SHORT).show();
        } else if (pos == 1) {
            ibRecord32.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación...", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            ibRecord33.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación...", Toast.LENGTH_SHORT).show();
        }
    }

    public void startedRecord(String idCamera)
    {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0) {
            ibRecord31.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación...", Toast.LENGTH_SHORT).show();
        } else if (pos == 1) {
            ibRecord32.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación...", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            ibRecord33.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación...", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

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
        tvBuffer1.setText("Cargando..." + (int) buffer + "%");
    }

    private void showLoading()
    {
        rlLoading1.setVisibility(View.VISIBLE);
        rlLoading2.setVisibility(View.VISIBLE);
        rlLoading3.setVisibility(View.VISIBLE);
    }

    public void hideLoading()
    {
        rlLoading1.setVisibility(View.GONE);
        rlLoading2.setVisibility(View.GONE);
        rlLoading3.setVisibility(View.GONE);
    }

    @Override
    public void onLoadComplet()
    {
        // mHandler.sendEmptyMessage(ON_LOADED);
    }

    private String urlByPosCamera(int pos)
    {
        switch (pos)
        {
            case 0:
                return url1;
            case 1:
                return url2;
            case 2:
                return url3;
            default:
                break;
        }
        return "";
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ib_mute:
                setMute();
                if (mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
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
            case R.id.ib_snapshot31:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot32:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot33:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url3, customView.getIdsCameras().get(2));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots31:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots32:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots33:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url3, customView.getIdsCameras().get(2));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_popup:
                visorController.setCameraPlaying(urlByPosCamera(posMenuCameraPress));
                startService(new Intent(ViewThreeCameraActivity.this, ServiceFloating.class));
                break;
            case R.id.ib_record31:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(0))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(0));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record32:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(1))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(1));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record33:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(2))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(2));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(2));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_add_camera31:
                addCamera(0);
                break;
            case R.id.ib_add_camera32:
                addCamera(1);
                break;
            case R.id.ib_add_camera33:
                addCamera(2);
                break;
            case R.id.ib_delete_camera:
                deleteCamera();
                break;
            case R.id.ib_set_mark31:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(0)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(0), String.valueOf(mPlayerView1.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark32:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(1)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(1), String.valueOf(mPlayerView2.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark33:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(2)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(2), String.valueOf(mPlayerView3.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(2));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_menu_camera31:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    posMenuCameraPress = 0;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera32:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    posMenuCameraPress = 1;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera33:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    posMenuCameraPress = 2;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_add_sensor:
                showSensors(view);
                break;
            case R.id.ib_lock_camera:
                visorController.lockCamera(customView.getIdsCameras().get(posMenuCameraPress));
                if(mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                break;
            default:
                break;
        }
    }


    int posMenuCameraPress = -1;

    private void setMute()
    {
        if (posMenuCameraPress == 0 && mPlayerView1.isPlaying() && mPlayerView1 != null)
        {
            if (!mPlayerView1.isMute)
            {
                mPlayerView1.setVolume(0);
                mPlayerView1.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView1.setVolume(100);
                mPlayerView1.isMute = false;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
            }
        }
        if (posMenuCameraPress == 0 && mPlayerView2.isPlaying() && mPlayerView2 != null)
        {
            if (!mPlayerView2.isMute)
            {
                mPlayerView2.setVolume(0);
                mPlayerView2.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView2.setVolume(100);
                mPlayerView2.isMute = false;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
            }
        }
        if (posMenuCameraPress == 0 && mPlayerView3.isPlaying() && mPlayerView3 != null)
        {
            if (!mPlayerView3.isMute)
            {
                mPlayerView3.setVolume(0);
                mPlayerView3.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView3.setVolume(100);
                mPlayerView3.isMute = false;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
            }
        }
    }

    public void showSensors(View view)
    {
        mPopupWindow.dismiss();
        final Camera c= visorController.getCameraById(customView.getIdsCameras().get(posMenuCameraPress));
        String[] words = new String[visorController.getSensors().size()];
        final LinkedList<Integer> activatedList = new LinkedList<>();
        for (int i = 0; i < words.length; i++)
        {
            words[i] = visorController.getSensors().get(i).getId();
            if(c.getIdsSensorActivated().contains(visorController.getSensors().get(i).getId()))
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
                        c.clearActivedSensor();
                        for (int i = 0; i < which.length; i++)
                        {
                            Log.e("sensor name:",visorController.getSensors().get(which[i]).getId());
                            String idSensor = visorController.getSensors().get(which[i]).getId();
                            c.getIdsSensorActivated().add(idSensor);
                        }
                        return true; // allow selection
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        if(c.getIdsSensorActivated().size()>0)
                        {
                            dialog.clearSelectedIndices();
                            c.clearActivedSensor();
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
                            if(!c.getIdsSensorActivated().contains(idSensor) && activatedList.contains(i))
                                VisorController.getInstance().setActiveSensor(c.getId(),idSensor,false);
                            else if(c.getIdsSensorActivated().contains(idSensor) && !activatedList.contains(i))
                                VisorController.getInstance().setActiveSensor(c.getId(), idSensor, true);
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
        if (customView.getIdsCameras().size()>posMenuCameraPress)
        {
            customView.getIdsCameras().remove(posMenuCameraPress);
            customView.getIdsCameras().add(posMenuCameraPress,String.valueOf(-1));
            if(posMenuCameraPress == 0)
            {
                ibAddCamera31.setVisibility(View.VISIBLE);
                if(mPlayerView1 != null)
                {
                    mPlayerView1.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView1.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 1)
            {
                ibAddCamera32.setVisibility(View.VISIBLE);
                if(mPlayerView2 != null)
                {
                    mPlayerView2.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView2.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 2)
            {
                ibAddCamera33.setVisibility(View.VISIBLE);
                if(mPlayerView3 != null)
                {
                    mPlayerView3.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView3.releasePlayer();
                }
            }
            mPopupWindow.dismiss();
        }
        else
        {
            showMessage("No existe la cámara que desea eliminar.");
        }
    }

    public void addCamera (final int posArea)
    {
        final String [] array = {"Árbol","Búsqueda"};
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
                        i.putExtra("posArea", posArea);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
    }

    private void toggleFullscreen()
    {
        Intent i = new Intent(this, ViewOneCameraActivity.class);
        VisorController.getInstance().customViewTemporal.getIdsCameras().add(0, customView.getIdsCameras().get(posMenuCameraPress));
        i.putExtra("indexCustomView", "-1");
        i.putExtra("lastIndexCustomView", indexCustomView);
        startActivity(i);
        finish();
    }

    private View llOverlay31, llOverlay32, llOverlay33;

    private void showOverlay()
    {
        if(llOverlay31!=null)
        {
            llOverlay31.setVisibility(View.VISIBLE);
        }
        if(llOverlay32!=null)
        {
            llOverlay32.setVisibility(View.VISIBLE);
        }
        if(llOverlay33!=null)
        {
            llOverlay33.setVisibility(View.VISIBLE);
        }
    }

    private void hideOverlay()
    {
        if(llOverlay31!=null)
        {
            llOverlay31.setVisibility(View.GONE);
        }
        if(llOverlay32!=null)
        {
            llOverlay32.setVisibility(View.GONE);
        }
        if(llOverlay33!=null)
        {
            llOverlay33.setVisibility(View.GONE);
        }/*
        if(isFullscreen)
        {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (llOverlay31.getVisibility() != View.VISIBLE || llOverlay32.getVisibility() != View.VISIBLE || llOverlay33.getVisibility() != View.VISIBLE)
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
        ibSize = (ImageButton) popupView.findViewById(R.id.ib_size);
        ibSize.setOnClickListener(this);
        ibPopup = (ImageButton) popupView.findViewById(R.id.ib_popup);
        ibPopup.setOnClickListener(this);
        ibDeleteCamera = (ImageButton) popupView.findViewById(R.id.ib_delete_camera);
        ibDeleteCamera.setOnClickListener(this);
        ibMute = (ImageButton) popupView.findViewById(R.id.ib_mute);
        ibMute.setOnClickListener(this);
        if(visorController.isMuted())
            ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
        else
            ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
        ibLockCamera = (ImageButton) popupView.findViewById(R.id.ib_lock_camera);
        if(visorController.getCameraById(customView.getIdsCameras().get(posMenuCameraPress)).isLock())
            ibLockCamera.setBackgroundResource(R.drawable.ic_lock_open_white_24dp);
        else
            ibLockCamera.setBackgroundResource(R.drawable.ic_lock_outline_white_24dp);
        ibLockCamera.setOnClickListener(this);
        //mPopupWindow.showAsDropDown(view, 150, 150);
        //mPopupWindow.showAsDropDown(ibMenuCamera, 500, 500);
        //mPopupWindow.showAtLocation(view, Gravity.RIGHT, 161, 30);
        int [] coor = new int [2];
        view.getLocationInWindow(coor);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, coor[0], coor[1]);
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
        if(mPlayerView1!= null)
            mPlayerView1.releasePlayer();
        if(mPlayerView2!= null)
            mPlayerView2.releasePlayer();
        if(mPlayerView3!= null)
            mPlayerView3.releasePlayer();
    }

    @Override
    public void finish()
    {
        releasePlayers();
        visorController.setCameraPlaying(null);
        super.finish();
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
