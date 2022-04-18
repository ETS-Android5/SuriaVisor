package uci.suriavisor.presentation.CustomViews;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class ViewSixCameraActivity extends AppCompatActivity implements PlayerView.OnChangeListener, View.OnClickListener, IPlayerActivity
{

    MaterialDialog materialDialogProgressIndeterminate = null;
    private String url1 = "",url2 = "", url3 = "", url4 = "", url5 = "", url6 = "";
    private TextView tvBuffer1, tvBuffer2, tvBuffer3, tvBuffer4, tvBuffer5, tvBuffer6;
    private View rlLoading1, rlLoading2, rlLoading3, rlLoading4, rlLoading5, rlLoading6;
    private PlayerView mPlayerView1, mPlayerView2, mPlayerView3, mPlayerView4, mPlayerView5, mPlayerView6;
    private boolean isFullscreen = false;

    private ImageButton ibSize, ibMute, ibAddSensor, ibLockCamera, ibPopup, ibDeleteCamera,
            ibSnapshot61, ibSnapshots61, ibRecord61, ibAddCamera61, ibSetMark61, ibMenuCamera61,
            ibSnapshot62, ibSnapshots62, ibRecord62, ibAddCamera62, ibSetMark62, ibMenuCamera62,
            ibSnapshot63, ibSnapshots63, ibRecord63, ibAddCamera63, ibSetMark63, ibMenuCamera63,
            ibSnapshot64, ibSnapshots64, ibRecord64, ibAddCamera64, ibSetMark64, ibMenuCamera64,
            ibSnapshot65, ibSnapshots65, ibRecord65, ibAddCamera65, ibSetMark65, ibMenuCamera65,
            ibSnapshot66, ibSnapshots66, ibRecord66, ibAddCamera66, ibSetMark66, ibMenuCamera66;

    private CustomView customView;
    VisorController visorController;
    private int indexCustomView, lastIndexCustomView = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setTheme(Constants.THEME_ID);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_view_six_camera);

        visorController = VisorController.getInstance();

        visorController.changeContext(this);
        isFullscreen = false;

        ibMenuCamera61 = (ImageButton) findViewById(R.id.ib_menu_camera61);
        ibMenuCamera61.setOnClickListener(this);
        ibSetMark61 = (ImageButton) findViewById(R.id.ib_set_mark61);
        ibSetMark61.setOnClickListener(this);
        ibAddCamera61 = (ImageButton) findViewById(R.id.ib_add_camera61);
        ibAddCamera61.setOnClickListener(this);
        ibRecord61 = (ImageButton) findViewById(R.id.ib_record61);
        ibRecord61.setOnClickListener(this);
        ibSnapshot61 = (ImageButton) findViewById(R.id.ib_snapshot61);
        ibSnapshot61.setOnClickListener(this);
        ibSnapshots61 = (ImageButton) findViewById(R.id.ib_snapshots61);
        ibSnapshots61.setOnClickListener(this);

        ibMenuCamera62 = (ImageButton) findViewById(R.id.ib_menu_camera62);
        ibMenuCamera62.setOnClickListener(this);
        ibSetMark62 = (ImageButton) findViewById(R.id.ib_set_mark62);
        ibSetMark62.setOnClickListener(this);
        ibAddCamera62 = (ImageButton) findViewById(R.id.ib_add_camera62);
        ibAddCamera62.setOnClickListener(this);
        ibRecord62 = (ImageButton) findViewById(R.id.ib_record62);
        ibRecord62.setOnClickListener(this);
        ibSnapshot62 = (ImageButton) findViewById(R.id.ib_snapshot62);
        ibSnapshot62.setOnClickListener(this);
        ibSnapshots62 = (ImageButton) findViewById(R.id.ib_snapshots62);
        ibSnapshots62.setOnClickListener(this);

        ibMenuCamera63 = (ImageButton) findViewById(R.id.ib_menu_camera63);
        ibMenuCamera63.setOnClickListener(this);
        ibSetMark63 = (ImageButton) findViewById(R.id.ib_set_mark63);
        ibSetMark63.setOnClickListener(this);
        ibAddCamera63 = (ImageButton) findViewById(R.id.ib_add_camera63);
        ibAddCamera63.setOnClickListener(this);
        ibRecord63 = (ImageButton) findViewById(R.id.ib_record63);
        ibRecord63.setOnClickListener(this);
        ibSnapshot63 = (ImageButton) findViewById(R.id.ib_snapshot63);
        ibSnapshot63.setOnClickListener(this);
        ibSnapshots63 = (ImageButton) findViewById(R.id.ib_snapshots63);
        ibSnapshots63.setOnClickListener(this);

        ibMenuCamera64 = (ImageButton) findViewById(R.id.ib_menu_camera64);
        ibMenuCamera64.setOnClickListener(this);
        ibSetMark64 = (ImageButton) findViewById(R.id.ib_set_mark64);
        ibSetMark64.setOnClickListener(this);
        ibAddCamera64 = (ImageButton) findViewById(R.id.ib_add_camera64);
        ibAddCamera64.setOnClickListener(this);
        ibRecord64 = (ImageButton) findViewById(R.id.ib_record64);
        ibRecord64.setOnClickListener(this);
        ibSnapshot64 = (ImageButton) findViewById(R.id.ib_snapshot64);
        ibSnapshot64.setOnClickListener(this);
        ibSnapshots64 = (ImageButton) findViewById(R.id.ib_snapshots64);
        ibSnapshots64.setOnClickListener(this);

        ibMenuCamera65 = (ImageButton) findViewById(R.id.ib_menu_camera65);
        ibMenuCamera65.setOnClickListener(this);
        ibSetMark65 = (ImageButton) findViewById(R.id.ib_set_mark65);
        ibSetMark65.setOnClickListener(this);
        ibAddCamera65 = (ImageButton) findViewById(R.id.ib_add_camera65);
        ibAddCamera65.setOnClickListener(this);
        ibRecord65 = (ImageButton) findViewById(R.id.ib_record65);
        ibRecord65.setOnClickListener(this);
        ibSnapshot65 = (ImageButton) findViewById(R.id.ib_snapshot65);
        ibSnapshot65.setOnClickListener(this);
        ibSnapshots65 = (ImageButton) findViewById(R.id.ib_snapshots65);
        ibSnapshots65.setOnClickListener(this);

        ibMenuCamera66 = (ImageButton) findViewById(R.id.ib_menu_camera66);
        ibMenuCamera66.setOnClickListener(this);
        ibSetMark66 = (ImageButton) findViewById(R.id.ib_set_mark66);
        ibSetMark66.setOnClickListener(this);
        ibAddCamera66 = (ImageButton) findViewById(R.id.ib_add_camera66);
        ibAddCamera66.setOnClickListener(this);
        ibRecord66 = (ImageButton) findViewById(R.id.ib_record66);
        ibRecord66.setOnClickListener(this);
        ibSnapshot66 = (ImageButton) findViewById(R.id.ib_snapshot66);
        ibSnapshot66.setOnClickListener(this);
        ibSnapshots66 = (ImageButton) findViewById(R.id.ib_snapshots66);
        ibSnapshots66.setOnClickListener(this);

        llOverlay61 = findViewById(R.id.ll_overlay61);
        llOverlay62 = findViewById(R.id.ll_overlay62);
        llOverlay63 = findViewById(R.id.ll_overlay63);
        llOverlay64 = findViewById(R.id.ll_overlay64);
        llOverlay65 = findViewById(R.id.ll_overlay65);
        llOverlay66 = findViewById(R.id.ll_overlay66);

        tvBuffer1 = (TextView) findViewById(R.id.tv_buffer61);
        tvBuffer2 = (TextView) findViewById(R.id.tv_buffer62);
        tvBuffer3 = (TextView) findViewById(R.id.tv_buffer63);
        tvBuffer4 = (TextView) findViewById(R.id.tv_buffer64);
        tvBuffer5 = (TextView) findViewById(R.id.tv_buffer65);
        tvBuffer6 = (TextView) findViewById(R.id.tv_buffer66);

        rlLoading1 = findViewById(R.id.rl_loading61);
        rlLoading2 = findViewById(R.id.rl_loading62);
        rlLoading3 = findViewById(R.id.rl_loading63);
        rlLoading4 = findViewById(R.id.rl_loading64);
        rlLoading5 = findViewById(R.id.rl_loading65);
        rlLoading6 = findViewById(R.id.rl_loading66);

        //Code de Jose
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
            visorController.getUrlStreamingById(customView.getIdsCameras().get(0));
            ibAddCamera61.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(0));
        }
        else
            ibAddCamera61.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(1).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(1));
            ibAddCamera62.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(1));
        }
        else
            ibAddCamera62.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(2).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(2));
            ibAddCamera63.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(2));
        }
        else
            ibAddCamera63.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(3).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(3));
            ibAddCamera64.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(3));
        }
        else
            ibAddCamera64.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(4).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(4));
            ibAddCamera65.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(4));
        }
        else
            ibAddCamera65.setVisibility(View.VISIBLE);

        if(!customView.getIdsCameras().get(5).equals("-1"))
        {
            visorController.getUrlStreamingById(customView.getIdsCameras().get(5));
            ibAddCamera66.setVisibility(View.INVISIBLE);
            visorController.getCameraStatus(customView.getIdsCameras().get(5));
        }
        else
            ibAddCamera66.setVisibility(View.VISIBLE);

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
                            rlLoading1 = findViewById(R.id.rl_loading61);
                            tvBuffer1 = (TextView) findViewById(R.id.tv_buffer61);
                            mPlayerView1 = (PlayerView) findViewById(R.id.pv_video61);
                            mPlayerView1.widthCustom = 2;
                            mPlayerView1.heightCustom = 3;
                            mPlayerView1.initPlayer(url1);
                            mPlayerView1.start();
                        }else {
                            Toast.makeText(ViewSixCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
                            rlLoading2 = findViewById(R.id.rl_loading62);
                            tvBuffer2 = (TextView) findViewById(R.id.tv_buffer62);
                            mPlayerView2 = (PlayerView) findViewById(R.id.pv_video62);
                            mPlayerView2.widthCustom = 2;
                            mPlayerView2.heightCustom = 3;
                            mPlayerView2.initPlayer(url2);
                            mPlayerView2.start();
                        }else {
                            Toast.makeText(ViewSixCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
                            rlLoading3 = findViewById(R.id.rl_loading63);
                            tvBuffer3 = (TextView) findViewById(R.id.tv_buffer63);
                            mPlayerView3 = (PlayerView) findViewById(R.id.pv_video63);
                            mPlayerView3.widthCustom = 2;
                            mPlayerView3.heightCustom = 3;
                            mPlayerView3.initPlayer(url3);
                            mPlayerView3.start();
                        }else {
                            Toast.makeText(ViewSixCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
                            rlLoading4 = findViewById(R.id.rl_loading64);
                            tvBuffer4 = (TextView) findViewById(R.id.tv_buffer64);
                            mPlayerView4 = (PlayerView) findViewById(R.id.pv_video64);
                            mPlayerView4.widthCustom = 2;
                            mPlayerView4.heightCustom = 3;
                            mPlayerView4.initPlayer(url4);
                            mPlayerView4.start();
                        }else {
                            Toast.makeText(ViewSixCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
                            rlLoading5 = findViewById(R.id.rl_loading65);
                            tvBuffer5 = (TextView) findViewById(R.id.tv_buffer65);
                            mPlayerView5 = (PlayerView) findViewById(R.id.pv_video65);
                            mPlayerView5.widthCustom = 2;
                            mPlayerView5.heightCustom = 3;
                            mPlayerView5.initPlayer(url5);
                            mPlayerView5.start();
                        }else {
                            Toast.makeText(ViewSixCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
        } else if (customView.getIdsCameras().get(5).equals(idCamera))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        url6 = urlStreaming;
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        if (!url6.equals(""))
                        {
                            rlLoading6 = findViewById(R.id.rl_loading66);
                            tvBuffer6 = (TextView) findViewById(R.id.tv_buffer66);
                            mPlayerView6 = (PlayerView) findViewById(R.id.pv_video66);
                            mPlayerView6.widthCustom = 2;
                            mPlayerView6.heightCustom = 3;
                            mPlayerView6.initPlayer(url6);
                            mPlayerView6.start();
                        }else {
                            Toast.makeText(ViewSixCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 5;
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
            ibRecord61.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_24dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 1) {
            ibRecord62.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_24dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            ibRecord63.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_24dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 3) {
            ibRecord64.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_24dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 4) {
            ibRecord65.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_24dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 5) {
            ibRecord66.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_24dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startedRecord(String idCamera)
    {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0) {
            ibRecord61.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 1) {
            ibRecord62.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            // Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 2) {
            ibRecord63.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 3) {
            ibRecord64.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 4) {
            ibRecord65.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }else if (pos == 5) {
            ibRecord66.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
/*
    public void playCamera()
    {
        //String url1 = "rtsp://root:Admin.123@10.64.13.250:564/live.sdp";
        //String url1 = "rtsp://admin:admin@10.64.13.261:49276/test";

        //url1 = VisorController.getInstance().getCameraById(idTreeNode).getUrl();
        //url1 = "http://minternos.uci.cu/naruto_473.webm";
        //url1 = "http://10.8.61.127/1.webm";
        //Log.e("URL en la camara", url1);
        if(!customView.getIdsCameras().get(0).equals("-1"))
        {
            url1 = visorController.getCameraById(customView.getIdsCameras().get(0)).getUrl();
            if (materialDialogProgressIndeterminate != null)
            {
                materialDialogProgressIndeterminate.dismiss();
            }
            rlLoading1 = findViewById(R.id.rl_loading61);
            tvBuffer1 = (TextView) findViewById(R.id.tv_buffer61);
            mPlayerView1 = (PlayerView) findViewById(R.id.pv_video61);
            mPlayerView1.widthCustom = 1;
            mPlayerView1.heightCustom = 3;
            mPlayerView1.initPlayer(url1);
            mPlayerView1.setOnChangeListener(this);
            mPlayerView1.start();
        }
        if(!customView.getIdsCameras().get(1).equals("-1"))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //camera 2
                        //url2 = "http://minternos.uci.cu/naruto_474.webm";
                        //url2 = "http://10.8.61.127/2.webm";
                        url2 = visorController.getCameraById(customView.getIdsCameras().get(1)).getUrl();
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        rlLoading2 = findViewById(R.id.rl_loading62);
                        tvBuffer2 = (TextView) findViewById(R.id.tv_buffer62);
                        mPlayerView2 = (PlayerView) findViewById(R.id.pv_video62);
                        mPlayerView2.widthCustom = 1;
                        mPlayerView2.heightCustom = 3;
                        //mPlayerView2.setOnChangeListener((ViewThreeCameraActivity)getApplicationContext());
                        mPlayerView2.initPlayer(url2);
                        mPlayerView2.start();
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        if(!customView.getIdsCameras().get(2).equals("-1"))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //camera 2
                        //url2 = "http://minternos.uci.cu/naruto_474.webm";
                        //url3 = "http://10.8.61.127/3.webm";
                        url3 = visorController.getCameraById(customView.getIdsCameras().get(2)).getUrl();
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        rlLoading3 = findViewById(R.id.rl_loading63);
                        tvBuffer3 = (TextView) findViewById(R.id.tv_buffer63);
                        mPlayerView3 = (PlayerView) findViewById(R.id.pv_video63);
                        mPlayerView3.widthCustom = 1;
                        mPlayerView3.heightCustom = 3;
                        //mPlayerView3.setOnChangeListener((ViewThreeCameraActivity)getApplicationContext());
                        mPlayerView3.initPlayer(url3);
                        mPlayerView3.start();
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        if(!customView.getIdsCameras().get(3).equals("-1"))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //camera 2
                        //url2 = "http://minternos.uci.cu/naruto_474.webm";
                        //url3 = "http://10.8.61.127/3.webm";
                        url4 = visorController.getCameraById(customView.getIdsCameras().get(3)).getUrl();
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        rlLoading4 = findViewById(R.id.rl_loading64);
                        tvBuffer4 = (TextView) findViewById(R.id.tv_buffer64);
                        mPlayerView4 = (PlayerView) findViewById(R.id.pv_video64);
                        mPlayerView4.widthCustom = 1;
                        mPlayerView4.heightCustom = 3;
                        //mPlayerView3.setOnChangeListener((ViewThreeCameraActivity)getApplicationContext());
                        mPlayerView4.initPlayer(url4);
                        mPlayerView4.start();
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        if(!customView.getIdsCameras().get(4).equals("-1"))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //camera 2
                        //url2 = "http://minternos.uci.cu/naruto_474.webm";
                        //url3 = "http://10.8.61.127/3.webm";
                        url5 = visorController.getCameraById(customView.getIdsCameras().get(4)).getUrl();
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        rlLoading5 = findViewById(R.id.rl_loading65);
                        tvBuffer5 = (TextView) findViewById(R.id.tv_buffer65);
                        mPlayerView5 = (PlayerView) findViewById(R.id.pv_video65);
                        mPlayerView5.widthCustom = 1;
                        mPlayerView5.heightCustom = 3;
                        //mPlayerView3.setOnChangeListener((ViewThreeCameraActivity)getApplicationContext());
                        mPlayerView5.initPlayer(url5);
                        mPlayerView5.start();
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        if(!customView.getIdsCameras().get(5).equals("-1"))
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //camera 2
                        //url2 = "http://minternos.uci.cu/naruto_474.webm";
                        //url3 = "http://10.8.61.127/3.webm";
                        url6 = visorController.getCameraById(customView.getIdsCameras().get(5)).getUrl();
                        if (materialDialogProgressIndeterminate != null)
                        {
                            materialDialogProgressIndeterminate.dismiss();
                        }
                        rlLoading6 = findViewById(R.id.rl_loading66);
                        tvBuffer6 = (TextView) findViewById(R.id.tv_buffer66);
                        mPlayerView6 = (PlayerView) findViewById(R.id.pv_video66);
                        mPlayerView6.widthCustom = 1;
                        mPlayerView6.heightCustom = 3;
                        //mPlayerView3.setOnChangeListener((ViewThreeCameraActivity)getApplicationContext());
                        mPlayerView6.initPlayer(url6);
                        mPlayerView6.start();
                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
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
        tvBuffer1.setText("Cargando..." + (int) buffer + "%");
    }

    private void showLoading()
    {
        rlLoading1.setVisibility(View.VISIBLE);
        rlLoading2.setVisibility(View.VISIBLE);
        rlLoading3.setVisibility(View.VISIBLE);
        rlLoading4.setVisibility(View.VISIBLE);
        rlLoading5.setVisibility(View.VISIBLE);
        rlLoading6.setVisibility(View.VISIBLE);
    }

    public void hideLoading()
    {
        rlLoading1.setVisibility(View.GONE);
        rlLoading2.setVisibility(View.GONE);
        rlLoading3.setVisibility(View.GONE);
        rlLoading4.setVisibility(View.GONE);
        rlLoading5.setVisibility(View.GONE);
        rlLoading6.setVisibility(View.GONE);
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
                return mPlayerView1.getUrl();
            case 1:
                return mPlayerView2.getUrl();
            case 2:
                return mPlayerView3.getUrl();
            case 3:
                return mPlayerView4.getUrl();
            case 4:
                return mPlayerView4.getUrl();
            case 5:
                return mPlayerView5.getUrl();
            case 6:
                return mPlayerView6.getUrl();
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
            case R.id.ib_snapshot61:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot62:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot63:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url3, customView.getIdsCameras().get(2));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot64:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url4, customView.getIdsCameras().get(3));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot65:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url5, customView.getIdsCameras().get(4));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot66:
                if(!customView.getIdsCameras().get(5).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url6, customView.getIdsCameras().get(5));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots61:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots62:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots63:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url3, customView.getIdsCameras().get(2));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots64:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url4, customView.getIdsCameras().get(3));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots65:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url5, customView.getIdsCameras().get(4));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots66:
                if(!customView.getIdsCameras().get(5).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url6, customView.getIdsCameras().get(5));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_popup:
                visorController.setCameraPlaying(urlByPosCamera(posMenuCameraPress));
                //startService(new Intent(ViewSixCameraActivity.this, ServiceFloating.class));
                mPopupWindow.dismiss();
                visorController.startServiceFloating();
                break;
            case R.id.ib_record61:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(0))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(0));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record62:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(1))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(1));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record63:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(2))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(2));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(2));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record64:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(3))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(3));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(3));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record65:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(4))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(4));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(4));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record66:
                if(!customView.getIdsCameras().get(5).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(5))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(5));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(5));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            //code jose
            case R.id.ib_add_camera61:
                addCamera(0);
                break;
            case R.id.ib_add_camera62:
                addCamera(1);
                break;
            case R.id.ib_add_camera63:
                addCamera(2);
                break;
            case R.id.ib_add_camera64:
                addCamera(3);
                break;
            case R.id.ib_add_camera65:
                addCamera(4);
                break;
            case R.id.ib_add_camera66:
                addCamera(5);
                break;
            case R.id.ib_delete_camera:
                deleteCamera();
                break;
            case R.id.ib_set_mark61:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(0)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(0), String.valueOf(mPlayerView1.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark62:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(1)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(1), String.valueOf(mPlayerView2.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark63:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(2)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(2), String.valueOf(mPlayerView3.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(2));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark64:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(3)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(3), String.valueOf(mPlayerView4.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(3));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark65:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(4)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(3), String.valueOf(mPlayerView5.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(4));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark66:
                if(!customView.getIdsCameras().get(5).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(5)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(3), String.valueOf(mPlayerView6.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(5));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_menu_camera61:
                if(!customView.getIdsCameras().get(0).equals("-1"))
                {
                    posMenuCameraPress = 0;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera62:
                if(!customView.getIdsCameras().get(1).equals("-1"))
                {
                    posMenuCameraPress = 1;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera63:
                if(!customView.getIdsCameras().get(2).equals("-1"))
                {
                    posMenuCameraPress = 2;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera64:
                if(!customView.getIdsCameras().get(3).equals("-1"))
                {
                    posMenuCameraPress = 3;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera65:
                if(!customView.getIdsCameras().get(4).equals("-1"))
                {
                    posMenuCameraPress = 4;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera66:
                if(!customView.getIdsCameras().get(5).equals("-1"))
                {
                    posMenuCameraPress = 5;
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
        if (posMenuCameraPress == 0 && mPlayerView4.isPlaying() && mPlayerView4 != null)
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
        if (posMenuCameraPress == 0 && mPlayerView5.isPlaying() && mPlayerView5 != null)
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
        if (posMenuCameraPress == 0 && mPlayerView6.isPlaying() && mPlayerView6 != null)
        {
            if (!mPlayerView6.isMute)
            {
                mPlayerView6.setVolume(0);
                mPlayerView6.isMute = true;
                ibMute.setBackgroundResource(R.drawable.ic_action_volume_up);
            }
            else
            {
                mPlayerView6.setVolume(100);
                mPlayerView6.isMute = false;
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

    private View llOverlay61, llOverlay62, llOverlay63, llOverlay64, llOverlay65, llOverlay66;

    private void showOverlay()
    {
        if(llOverlay61!=null)
        {
            llOverlay61.setVisibility(View.VISIBLE);
        }
        if(llOverlay62!=null)
        {
            llOverlay62.setVisibility(View.VISIBLE);
        }
        if(llOverlay63!=null)
        {
            llOverlay63.setVisibility(View.VISIBLE);
        }
        if(llOverlay64!=null)
        {
            llOverlay64.setVisibility(View.VISIBLE);
        }
        if(llOverlay65!=null)
        {
            llOverlay65.setVisibility(View.VISIBLE);
        }
        if(llOverlay66!=null)
        {
            llOverlay66.setVisibility(View.VISIBLE);
        }
    }

    private void hideOverlay()
    {
        if(llOverlay61!=null)
        {
            llOverlay61.setVisibility(View.GONE);
        }
        if(llOverlay62!=null)
        {
            llOverlay62.setVisibility(View.GONE);
        }
        if(llOverlay63!=null)
        {
            llOverlay63.setVisibility(View.GONE);
        }
        if(llOverlay64!=null)
        {
            llOverlay64.setVisibility(View.GONE);
        }
        if(llOverlay65!=null)
        {
            llOverlay65.setVisibility(View.GONE);
        }
        if(llOverlay66!=null)
        {
            llOverlay66.setVisibility(View.GONE);
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
            if (llOverlay61.getVisibility() != View.VISIBLE || llOverlay62.getVisibility() != View.VISIBLE ||
                    llOverlay63.getVisibility() != View.VISIBLE || llOverlay64.getVisibility() != View.VISIBLE ||
                    llOverlay65.getVisibility() != View.VISIBLE || llOverlay66.getVisibility() != View.VISIBLE)
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
                ibAddCamera61.setVisibility(View.VISIBLE);
                if(mPlayerView1 != null)
                {
                    mPlayerView1.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView1.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 1)
            {
                ibAddCamera62.setVisibility(View.VISIBLE);
                if(mPlayerView2 != null)
                {
                    mPlayerView2.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView2.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 2)
            {
                ibAddCamera63.setVisibility(View.VISIBLE);
                if(mPlayerView3 != null)
                {
                    mPlayerView3.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView3.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 3)
            {
                ibAddCamera64.setVisibility(View.VISIBLE);
                if(mPlayerView4 != null)
                {
                    mPlayerView4.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView4.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 4)
            {
                ibAddCamera65.setVisibility(View.VISIBLE);
                if(mPlayerView5 != null)
                {
                    mPlayerView5.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView5.releasePlayer();
                }
            }
            else if(posMenuCameraPress == 5)
            {
                ibAddCamera66.setVisibility(View.VISIBLE);
                if(mPlayerView6 != null)
                {
                    mPlayerView6.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView6.releasePlayer();
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
        if(mPlayerView6!= null)
            mPlayerView6.releasePlayer();
    }

    @Override
    public void finish()
    {
        visorController.setCameraPlaying(null);
        releasePlayers();
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
