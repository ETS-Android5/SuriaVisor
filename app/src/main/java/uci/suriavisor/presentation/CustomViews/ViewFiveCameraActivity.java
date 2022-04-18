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
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.playerlibrary.PlayerView;
import uci.suriavisor.presentation.util.Constants;
import com.xilema.suriavisor.R;

import java.util.LinkedList;

public class ViewFiveCameraActivity extends Activity implements PlayerView.OnChangeListener, View.OnClickListener, IPlayerActivity
{

    MaterialDialog materialDialogProgressIndeterminate = null;
    private String url1 = "",url2 = "", url3 = "", url4 = "", url5 = "";
    private TextView tvBuffer1, tvBuffer2, tvBuffer3, tvBuffer4, tvBuffer5;
    private View rlLoading1, rlLoading2, rlLoading3, rlLoading4, rlLoading5;
    private PlayerView mPlayerView1, mPlayerView2, mPlayerView3, mPlayerView4, mPlayerView5;
    private boolean isFullscreen = false;

    private ImageButton ibSize, ibMute, ibAddSensor, ibLockCamera, ibPopup, ibDeleteCamera,
            ibSnapshot51, ibSnapshots51, ibRecord51, ibAddCamera51, ibSetMark51, ibMenuCamera51,
            ibSnapshot52, ibSnapshots52, ibRecord52, ibAddCamera52, ibSetMark52, ibMenuCamera52,
            ibSnapshot53, ibSnapshots53, ibRecord53, ibAddCamera53, ibSetMark53, ibMenuCamera53,
            ibSnapshot54, ibSnapshots54, ibRecord54, ibAddCamera54, ibSetMark54, ibMenuCamera54,
            ibSnapshot55, ibSnapshots55, ibRecord55, ibAddCamera55, ibSetMark55, ibMenuCamera55;

    private CustomView customView;
    VisorController visorController;
    private int indexCustomView, lastIndexCustomView = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(Constants.THEME_ID);
        setContentView(R.layout.activity_view_five_camera);

        visorController = VisorController.getInstance();

        visorController.changeContext(this);
        visorController.setCurrentView(2);
        isFullscreen = false;

        ibMenuCamera51 = (ImageButton) findViewById(R.id.ib_menu_camera51);
        ibMenuCamera51.setOnClickListener(this);
        ibSetMark51 = (ImageButton) findViewById(R.id.ib_set_mark51);
        ibSetMark51.setOnClickListener(this);
        ibAddCamera51 = (ImageButton) findViewById(R.id.ib_add_camera51);
        ibAddCamera51.setOnClickListener(this);
        ibRecord51 = (ImageButton) findViewById(R.id.ib_record51);
        ibRecord51.setOnClickListener(this);
        ibSnapshot51 = (ImageButton) findViewById(R.id.ib_snapshot51);
        ibSnapshot51.setOnClickListener(this);
        ibSnapshots51 = (ImageButton) findViewById(R.id.ib_snapshots51);
        ibSnapshots51.setOnClickListener(this);

        ibMenuCamera52 = (ImageButton) findViewById(R.id.ib_menu_camera52);
        ibMenuCamera52.setOnClickListener(this);
        ibSetMark52 = (ImageButton) findViewById(R.id.ib_set_mark52);
        ibSetMark52.setOnClickListener(this);
        ibAddCamera52 = (ImageButton) findViewById(R.id.ib_add_camera52);
        ibAddCamera52.setOnClickListener(this);
        ibRecord52 = (ImageButton) findViewById(R.id.ib_record52);
        ibRecord52.setOnClickListener(this);
        ibSnapshot52 = (ImageButton) findViewById(R.id.ib_snapshot52);
        ibSnapshot52.setOnClickListener(this);
        ibSnapshots52 = (ImageButton) findViewById(R.id.ib_snapshots52);
        ibSnapshots52.setOnClickListener(this);

        ibMenuCamera53 = (ImageButton) findViewById(R.id.ib_menu_camera53);
        ibMenuCamera53.setOnClickListener(this);
        ibSetMark53 = (ImageButton) findViewById(R.id.ib_set_mark53);
        ibSetMark53.setOnClickListener(this);
        ibAddCamera53 = (ImageButton) findViewById(R.id.ib_add_camera53);
        ibAddCamera53.setOnClickListener(this);
        ibRecord53 = (ImageButton) findViewById(R.id.ib_record53);
        ibRecord53.setOnClickListener(this);
        ibSnapshot53 = (ImageButton) findViewById(R.id.ib_snapshot53);
        ibSnapshot53.setOnClickListener(this);
        ibSnapshots53 = (ImageButton) findViewById(R.id.ib_snapshots53);
        ibSnapshots53.setOnClickListener(this);

        ibMenuCamera54 = (ImageButton) findViewById(R.id.ib_menu_camera54);
        ibMenuCamera54.setOnClickListener(this);
        ibSetMark54 = (ImageButton) findViewById(R.id.ib_set_mark54);
        ibSetMark54.setOnClickListener(this);
        ibAddCamera54 = (ImageButton) findViewById(R.id.ib_add_camera54);
        ibAddCamera54.setOnClickListener(this);
        ibRecord54 = (ImageButton) findViewById(R.id.ib_record54);
        ibRecord54.setOnClickListener(this);
        ibSnapshot54 = (ImageButton) findViewById(R.id.ib_snapshot54);
        ibSnapshot54.setOnClickListener(this);
        ibSnapshots54 = (ImageButton) findViewById(R.id.ib_snapshots54);
        ibSnapshots54.setOnClickListener(this);

        ibMenuCamera55 = (ImageButton) findViewById(R.id.ib_menu_camera55);
        ibMenuCamera55.setOnClickListener(this);
        ibSetMark55 = (ImageButton) findViewById(R.id.ib_set_mark55);
        ibSetMark55.setOnClickListener(this);
        ibAddCamera55 = (ImageButton) findViewById(R.id.ib_add_camera55);
        ibAddCamera55.setOnClickListener(this);
        ibRecord55 = (ImageButton) findViewById(R.id.ib_record55);
        ibRecord55.setOnClickListener(this);
        ibSnapshot55 = (ImageButton) findViewById(R.id.ib_snapshot55);
        ibSnapshot55.setOnClickListener(this);
        ibSnapshots55 = (ImageButton) findViewById(R.id.ib_snapshots55);
        ibSnapshots55.setOnClickListener(this);

        llOverlay51 = findViewById(R.id.ll_overlay51);
        llOverlay52 = findViewById(R.id.ll_overlay52);
        llOverlay53 = findViewById(R.id.ll_overlay53);
        llOverlay54 = findViewById(R.id.ll_overlay54);
        llOverlay55 = findViewById(R.id.ll_overlay55);

        tvBuffer1 = (TextView) findViewById(R.id.tv_buffer51);
        tvBuffer2 = (TextView) findViewById(R.id.tv_buffer52);
        tvBuffer3 = (TextView) findViewById(R.id.tv_buffer53);
        tvBuffer4 = (TextView) findViewById(R.id.tv_buffer54);
        tvBuffer5 = (TextView) findViewById(R.id.tv_buffer55);

        rlLoading1 = findViewById(R.id.rl_loading51);
        rlLoading2 = findViewById(R.id.rl_loading52);
        rlLoading3 = findViewById(R.id.rl_loading53);
        rlLoading4 = findViewById(R.id.rl_loading54);
        rlLoading5 = findViewById(R.id.rl_loading55);

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
            visorController.getUrlStreamingById(customView.getIdsCameras().get(0));
            ibAddCamera51.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(0));
        } else
            ibAddCamera51.setVisibility(View.VISIBLE);

        if (!customView.getIdsCameras().get(1).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(1));
            ibAddCamera52.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(1));
        } else
            ibAddCamera52.setVisibility(View.VISIBLE);

        if (!customView.getIdsCameras().get(2).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(2));
            ibAddCamera53.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(2));
        }
        else
            ibAddCamera53.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(3).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(3));
            ibAddCamera54.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(3));
        }
        else
            ibAddCamera54.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(4).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(4));
            ibAddCamera55.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(4));
        }
        else
            ibAddCamera55.setVisibility(View.VISIBLE);

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
                        if (!url1.equals(""))
                        {
                            rlLoading1 = findViewById(R.id.rl_loading51);
                            tvBuffer1 = (TextView) findViewById(R.id.tv_buffer51);
                            mPlayerView1 = (PlayerView) findViewById(R.id.pv_video51);
                            mPlayerView1.widthCustom = 1;
                            mPlayerView1.heightCustom = 3;
                            mPlayerView1.initPlayer(url1);
                            mPlayerView1.start();
                        }else {
                            Toast.makeText(ViewFiveCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
                        if (!url2.equals(""))
                        {
                            rlLoading2 = findViewById(R.id.rl_loading52);
                            tvBuffer2 = (TextView) findViewById(R.id.tv_buffer52);
                            mPlayerView2 = (PlayerView) findViewById(R.id.pv_video52);
                            mPlayerView2.widthCustom = 2;
                            mPlayerView2.heightCustom = 3;
                            mPlayerView2.initPlayer(url2);
                            mPlayerView2.start();
                        }else {
                            Toast.makeText(ViewFiveCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
                        if (!url3.equals(""))
                        {
                            rlLoading3 = findViewById(R.id.rl_loading53);
                            tvBuffer3 = (TextView) findViewById(R.id.tv_buffer53);
                            mPlayerView3 = (PlayerView) findViewById(R.id.pv_video53);
                            mPlayerView3.widthCustom = 2;
                            mPlayerView3.heightCustom = 3;
                            mPlayerView3.initPlayer(url3);
                            mPlayerView3.start();
                        }else {
                            Toast.makeText(ViewFiveCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
        } else if (customView.getIdsCameras().get(3).equals(idCamera))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        url4 = urlStreaming;
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        if (!url4.equals(""))
                        {
                            rlLoading4 = findViewById(R.id.rl_loading54);
                            tvBuffer4 = (TextView) findViewById(R.id.tv_buffer54);
                            mPlayerView4 = (PlayerView) findViewById(R.id.pv_video54);
                            mPlayerView4.widthCustom = 2;
                            mPlayerView4.heightCustom = 3;
                            mPlayerView4.initPlayer(url4);
                            mPlayerView4.start();
                        }else {
                            Toast.makeText(ViewFiveCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 3;
                            deleteCamera();
                        }
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } else if (customView.getIdsCameras().get(4).equals(idCamera))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        url5 = urlStreaming;
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        if (!url5.equals(""))
                        {
                            rlLoading5 = findViewById(R.id.rl_loading55);
                            tvBuffer5 = (TextView) findViewById(R.id.tv_buffer55);
                            mPlayerView5 = (PlayerView) findViewById(R.id.pv_video55);
                            mPlayerView5.widthCustom = 2;
                            mPlayerView5.heightCustom = 3;
                            mPlayerView5.initPlayer(url5);
                            mPlayerView5.start();
                        }else {
                            Toast.makeText(ViewFiveCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 4;
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

    public void stoppedRecord(String idCamera) {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0) {
            ibRecord51.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 1) {
            ibRecord52.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            ibRecord53.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 3) {
            ibRecord54.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 4) {
            ibRecord55.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startedRecord(String idCamera)
    {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0) {
            ibRecord51.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 1) {
            ibRecord52.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            // Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            ibRecord53.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 3) {
            ibRecord54.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 4) {
            ibRecord55.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
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
        rlLoading4.setVisibility(View.VISIBLE);
        rlLoading5.setVisibility(View.VISIBLE);
    }

    public void hideLoading()
    {
        rlLoading1.setVisibility(View.GONE);
        rlLoading2.setVisibility(View.GONE);
        rlLoading3.setVisibility(View.GONE);
        rlLoading4.setVisibility(View.GONE);
        rlLoading5.setVisibility(View.GONE);
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
            case 3:
                return url4;
            case 4:
                return url5;
            default:
                break;
        }
        return "";
    }

    int posMenuCameraPress = -1;

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
            case R.id.ib_snapshot51:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot52:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot53:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url3, customView.getIdsCameras().get(2));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot54:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url4, customView.getIdsCameras().get(3));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot55:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url5, customView.getIdsCameras().get(4));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots51:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots52:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots53:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url3, customView.getIdsCameras().get(2));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots54:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url4, customView.getIdsCameras().get(3));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots55:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url5, customView.getIdsCameras().get(4));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_popup:
                visorController.setCameraPlaying(urlByPosCamera(posMenuCameraPress));
                startService(new Intent(ViewFiveCameraActivity.this, ServiceFloating.class));
                break;
            case R.id.ib_record51:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(0))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(0));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record52:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(1))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(1));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record53:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(2))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(2));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(2));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record54:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(3))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(3));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(3));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record55:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(4))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(4));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(4));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            //code jose
            case R.id.ib_add_camera51:
                addCamera(0);
                break;
            case R.id.ib_add_camera52:
                addCamera(1);
                break;
            case R.id.ib_add_camera53:
                addCamera(2);
                break;
            case R.id.ib_add_camera54:
                addCamera(3);
                break;
            case R.id.ib_add_camera55:
                addCamera(4);
                break;

            case R.id.ib_delete_camera:
                deleteCamera();
                break;
            case R.id.ib_set_mark51:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(0)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(0), String.valueOf(mPlayerView1.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark52:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(1)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(1), String.valueOf(mPlayerView2.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark53:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(2)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(2), String.valueOf(mPlayerView3.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(2));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark54:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(3)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(3), String.valueOf(mPlayerView4.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(3));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark55:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(4)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(3), String.valueOf(mPlayerView5.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(4));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_menu_camera51:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    posMenuCameraPress = 0;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera52:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    posMenuCameraPress = 1;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera53:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    posMenuCameraPress = 2;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera54:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                {
                    posMenuCameraPress = 3;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera55:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                {
                    posMenuCameraPress = 4;
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

    private void setMute()
    {
        if (posMenuCameraPress == 0 && mPlayerView1 != null && mPlayerView1.isPlaying())
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
        if (posMenuCameraPress == 1 && mPlayerView2 != null && mPlayerView2.isPlaying())
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
        if (posMenuCameraPress == 2 && mPlayerView3 != null && mPlayerView3.isPlaying())
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
        if (posMenuCameraPress == 3 && mPlayerView4 != null && mPlayerView4.isPlaying())
        {
            if (!mPlayerView4.isMute)
            {
                mPlayerView4.setVolume(0);
                mPlayerView4.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView4.setVolume(100);
                mPlayerView4.isMute = false;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
            }
        }
        if (posMenuCameraPress == 4 && mPlayerView5 != null && mPlayerView5.isPlaying())
        {
            if (!mPlayerView5.isMute)
            {
                mPlayerView5.setVolume(0);
                mPlayerView5.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView5.setVolume(100);
                mPlayerView5.isMute = false;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
            }
        }
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

    private View llOverlay51, llOverlay52, llOverlay53, llOverlay54, llOverlay55;

    private void showOverlay()
    {
        if(llOverlay51!=null)
        {
            llOverlay51.setVisibility(View.VISIBLE);
        }
        if(llOverlay52!=null)
        {
            llOverlay52.setVisibility(View.VISIBLE);
        }
        if(llOverlay53!=null)
        {
            llOverlay53.setVisibility(View.VISIBLE);
        }
        if(llOverlay54!=null)
        {
            llOverlay54.setVisibility(View.VISIBLE);
        }
        if(llOverlay55!=null)
        {
            llOverlay55.setVisibility(View.VISIBLE);
        }
    }

    private void hideOverlay()
    {
        if(llOverlay51!=null)
        {
            llOverlay51.setVisibility(View.GONE);
        }
        if(llOverlay52!=null)
        {
            llOverlay52.setVisibility(View.GONE);
        }
        if(llOverlay53!=null)
        {
            llOverlay53.setVisibility(View.GONE);
        }
        if(llOverlay54!=null)
        {
            llOverlay54.setVisibility(View.GONE);
        }
        if(llOverlay55!=null)
        {
            llOverlay55.setVisibility(View.GONE);
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
            if (llOverlay51.getVisibility() != View.VISIBLE || llOverlay52.getVisibility() != View.VISIBLE ||
                    llOverlay53.getVisibility() != View.VISIBLE || llOverlay54.getVisibility() != View.VISIBLE ||
                    llOverlay55.getVisibility() != View.VISIBLE)
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

    public void deleteCamera()
    {
        if (customView.getIdsCameras().size()>posMenuCameraPress)
        {
            customView.getIdsCameras().remove(posMenuCameraPress);
            customView.getIdsCameras().add(posMenuCameraPress,String.valueOf(-1));
            if(posMenuCameraPress == 0)
            {
                ibAddCamera51.setVisibility(View.VISIBLE);
                if(mPlayerView1 != null)
                {
                    mPlayerView1.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView1.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 1)
            {
                ibAddCamera52.setVisibility(View.VISIBLE);
                if(mPlayerView2 != null)
                {
                    mPlayerView2.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView2.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 2)
            {
                ibAddCamera53.setVisibility(View.VISIBLE);
                if(mPlayerView3 != null)
                {
                    mPlayerView3.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView3.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 3)
            {
                ibAddCamera54.setVisibility(View.VISIBLE);
                if(mPlayerView4 != null)
                {
                    mPlayerView4.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView4.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 4)
            {
                ibAddCamera55.setVisibility(View.VISIBLE);
                if(mPlayerView5 != null)
                {
                    mPlayerView5.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView5.releasePlayer();
                }
            }
            mPopupWindow.dismiss();
        }
        else
        {
            showMessage("No existe la cámara que desea eliminar.");
        }
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

        int [] coor = new int [2];
        view.getLocationInWindow(coor);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, coor[0], coor[1]);

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
        if(mPlayerView4!= null)
            mPlayerView4.releasePlayer();
        if(mPlayerView5!= null)
            mPlayerView5.releasePlayer();
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
