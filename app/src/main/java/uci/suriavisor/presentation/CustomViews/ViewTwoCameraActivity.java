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


public class ViewTwoCameraActivity extends Activity implements PlayerView.OnChangeListener, View.OnClickListener, IPlayerActivity
{

    MaterialDialog materialDialogProgressIndeterminate = null;
    private String url1 = "", url2 = "";
    private TextView tvBuffer1, tvBuffer2;
    private View rlLoading1, rlLoading2;
    private PlayerView mPlayerView1, mPlayerView2;
    private boolean isFullscreen = false;
    private ImageButton ibSize, ibMute, ibAddSensor, ibLockCamera, ibPopup, ibDeleteCamera,
            ibSnapshot21, ibSnapshots21, ibRecord21, ibAddCamera21, ibSetMark21, ibMenuCamera21,
            ibSnapshot22, ibSnapshots22, ibRecord22, ibAddCamera22, ibSetMark22, ibMenuCamera22;
    private CustomView customView;
    VisorController visorController;
    private int indexCustomView;
    //private FrameLayout mSurfaceFrame21, mSurfaceFrame22;
    //public int widthCustom21, heightCustom21, widthCustom22, heightCustom22;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setTheme(Constants.THEME_ID);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_view_two_camera);
        visorController = VisorController.getInstance();

        visorController.changeContext(this);
        isFullscreen = false;

        ibMenuCamera21 = (ImageButton) findViewById(R.id.ib_menu_camera21);
        ibMenuCamera21.setOnClickListener(this);
        ibSetMark21 = (ImageButton) findViewById(R.id.ib_set_mark21);
        ibSetMark21.setOnClickListener(this);
        ibAddCamera21 = (ImageButton) findViewById(R.id.ib_add_camera21);
        ibAddCamera21.setOnClickListener(this);
        ibRecord21 = (ImageButton) findViewById(R.id.ib_record21);
        ibRecord21.setOnClickListener(this);
        ibSnapshot21 = (ImageButton) findViewById(R.id.ib_snapshot21);
        ibSnapshot21.setOnClickListener(this);
        ibSnapshots21 = (ImageButton) findViewById(R.id.ib_snapshots21);
        ibSnapshots21.setOnClickListener(this);

        ibMenuCamera22 = (ImageButton) findViewById(R.id.ib_menu_camera22);
        ibMenuCamera22.setOnClickListener(this);
        ibSetMark22 = (ImageButton) findViewById(R.id.ib_set_mark22);
        ibSetMark22.setOnClickListener(this);
        ibAddCamera22 = (ImageButton) findViewById(R.id.ib_add_camera22);
        ibAddCamera22.setOnClickListener(this);
        ibRecord22 = (ImageButton) findViewById(R.id.ib_record22);
        ibRecord22.setOnClickListener(this);
        ibSnapshot22 = (ImageButton) findViewById(R.id.ib_snapshot22);
        ibSnapshot22.setOnClickListener(this);
        ibSnapshots22 = (ImageButton) findViewById(R.id.ib_snapshots22);
        ibSnapshots22.setOnClickListener(this);

        llOverlay21 = findViewById(R.id.ll_overlay21);
        llOverlay22 = findViewById(R.id.ll_overlay22);

        tvBuffer1 = (TextView) findViewById(R.id.tv_buffer21);
        tvBuffer2 = (TextView) findViewById(R.id.tv_buffer22);

        rlLoading1 = findViewById(R.id.rl_loading21);
        rlLoading2 = findViewById(R.id.rl_loading22);

        indexCustomView = Integer.parseInt(getIntent().getExtras().getString("indexCustomView"));
        visorController.setCurrentView(indexCustomView);
        if (indexCustomView >= 0)
            customView = visorController.getCustomViews().get(indexCustomView);
        else
            customView = visorController.customViewTemporal;

        if (!customView.getIdsCameras().get(0).equals("-1"))
        {
            ibAddCamera21.setVisibility(View.INVISIBLE);
            visorController.getUrlStreamingById(customView.getIdsCameras().get(0));
            visorController.getCameraStatus(customView.getIdsCameras().get(0));
        } else
            ibAddCamera21.setVisibility(View.VISIBLE);

        if (!customView.getIdsCameras().get(1).equals("-1"))
        {
            ibAddCamera22.setVisibility(View.INVISIBLE);
            visorController.getUrlStreamingById(customView.getIdsCameras().get(1));
            visorController.getCameraStatus(customView.getIdsCameras().get(1));
        } else
            ibAddCamera22.setVisibility(View.VISIBLE);

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
                            rlLoading1 = findViewById(R.id.rl_loading21);
                            tvBuffer1 = (TextView) findViewById(R.id.tv_buffer21);
                            mPlayerView1 = (PlayerView) findViewById(R.id.pv_video21);
                            mPlayerView1.widthCustom = 2;
                            mPlayerView1.heightCustom = 1;
                            mPlayerView1.initPlayer(url1);
                            //mPlayerView1.setOnChangeListener(this);
                            mPlayerView1.start();
                            mPlayerView1.setVolume(100);
                        }else {
                            Toast.makeText(ViewTwoCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
                            posMenuCameraPress = 0;
                            deleteCamera();
                        }
                    } catch (Exception e)
                    {
                        e.fillInStackTrace();
                    }
                }
            });
        }
        else if (customView.getIdsCameras().get(1).equals(idCamera))
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
                            rlLoading2 = findViewById(R.id.rl_loading22);
                            tvBuffer2 = (TextView) findViewById(R.id.tv_buffer22);
                            mPlayerView2 = (PlayerView) findViewById(R.id.pv_video22);
                            mPlayerView2.widthCustom = 2;
                            mPlayerView2.heightCustom = 1;
                            mPlayerView2.initPlayer(url2);
                            mPlayerView2.start();
                            mPlayerView2.setVolume(100);
                        }else {
                            Toast.makeText(ViewTwoCameraActivity.this, "La cámara insertada tiene problema, la URL esta vacía.", Toast.LENGTH_SHORT).show();
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
        }
    }

    public void startedRecord(String idCamera)
    {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0)
        {
            ibRecord21.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha iniciado la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 1)
        {
            ibRecord22.setBackgroundResource(R.drawable.ic_fiber_manual_record_red);
            //Toast.makeText(this, "Se ha iniciado la grabación.", Toast.LENGTH_SHORT).show();
        }
    }

    public void stoppedRecord(String idCamera)
    {
        //actualizar el visual
        int pos = customView.getIdsCameras().indexOf(idCamera);
        if (pos == 0)
        {
            ibRecord21.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        } else if (pos == 1)
        {
            ibRecord22.setBackgroundResource(R.drawable.ic_fiber_manual_record_white_36dp);
            //Toast.makeText(this, "Se ha detenido la grabación.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private void setMute()
    {
        if (posMenuCameraPress == 0 && mPlayerView1!=null && mPlayerView1.isPlaying())
        {
            if (!mPlayerView1.isMute)
            {
                mPlayerView1.setVolume(0);
                mPlayerView1.isMute = true;
            } else
            {
                mPlayerView1.setVolume(100);
                mPlayerView1.isMute = false;
            }
        }
        else if (posMenuCameraPress == 1 && mPlayerView2!=null && mPlayerView2.isPlaying())
        {
            if (!mPlayerView2.isMute)
            {
                mPlayerView2.setVolume(0);
                mPlayerView2.isMute = true;
            } else
            {
                mPlayerView2.setVolume(100);
                mPlayerView2.isMute = false;
            }
        }
    }

    private String urlByPosCamera(int pos)
    {
        switch (pos)
        {
            case 0:
                return url1;
            case 1:
                return url2;
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
                toggleFullscreen();
                break;
            case R.id.ib_snapshot21:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshot22:
                if (!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(1, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots21:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url1, customView.getIdsCameras().get(0));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_snapshots22:
                if (!customView.getIdsCameras().get(1).equals("-1"))
                {
                    Toast.makeText(this, "Espere por favor...", Toast.LENGTH_SHORT).show();
                    visorController.snapshot(Constants.NUMBER_OF_SNAPSHOTS, url2, customView.getIdsCameras().get(1));
                }else {
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_popup:
                visorController.setCameraPlaying(urlByPosCamera(posMenuCameraPress));
                startService(new Intent(ViewTwoCameraActivity.this, ServiceFloating.class));
                break;
            case R.id.ib_record21:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(0))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(0));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_record22:
                if (!customView.getIdsCameras().get(1).equals("-1"))
                    if (visorController.getCameraById((customView.getIdsCameras().get(1))).isRecoding())
                        visorController.stopRecord(customView.getIdsCameras().get(1));
                    else
                        visorController.startRecord(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_add_camera21:
                addCamera(0);
                break;
            case R.id.ib_add_camera22:
                addCamera(1);
                break;
            case R.id.ib_delete_camera:
                deleteCamera();
                mPopupWindow.dismiss();
                break;
            case R.id.ib_set_mark21:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(0)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(0), String.valueOf(mPlayerView1.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(0));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_set_mark22:
                if (!customView.getIdsCameras().get(1).equals("-1"))
                    if (!visorController.getCameraById(customView.getIdsCameras().get(1)).isRecoding())
                        showMessage("La cámara debe estar grabando para poder incorporar un marcador.");
                    else
                        //visorController.setMark(customView.getIdsCameras().get(1), String.valueOf(mPlayerView2.getTime()));
                        visorController.getCurrentTime(customView.getIdsCameras().get(1));
                else
                    Toast.makeText(this, "Necesita tener una cámara insertada.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_menu_camera21:
                if (!customView.getIdsCameras().get(0).equals("-1"))
                {
                    posMenuCameraPress = 0;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_menu_camera22:
                if (!customView.getIdsCameras().get(1).equals("-1"))
                {
                    posMenuCameraPress = 1;
                    showMenuCamera(view);
                }
                break;
            case R.id.ib_add_sensor:
                showSensors(view);
                break;
            case R.id.ib_lock_camera:
                visorController.lockCamera(customView.getIdsCameras().get(posMenuCameraPress));
                if (mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                break;
            default:
                break;
        }
    }

    public void showSensors(View view)
    {
        mPopupWindow.dismiss();
        final Camera c = visorController.getCameraById(customView.getIdsCameras().get(posMenuCameraPress));
        String[] words = new String[visorController.getSensors().size()];
        final LinkedList<Integer> activatedList = new LinkedList<>();
        for (int i = 0; i < words.length; i++)
        {
            words[i] = visorController.getSensors().get(i).getId();
            if (c.getIdsSensorActivated().contains(visorController.getSensors().get(i).getId()))
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
                            Log.e("sensor name:", visorController.getSensors().get(which[i]).getId());
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
                        if (c.getIdsSensorActivated().size() > 0)
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
                            if (!c.getIdsSensorActivated().contains(idSensor) && activatedList.contains(i))
                                VisorController.getInstance().setActiveSensor(c.getId(), idSensor, false);
                            else if (c.getIdsSensorActivated().contains(idSensor) && !activatedList.contains(i))
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
        if (buffer >= 100)
        {
            hideLoading();
        } else
        {
            showLoading();
        }
        tvBuffer1.setText("Cargando..." + (int) buffer + "%");
    }

    private void showLoading()
    {
        rlLoading1.setVisibility(View.VISIBLE);
    }

    public void hideLoading()
    {
        rlLoading1.setVisibility(View.GONE);
    }

    @Override
    public void onLoadComplet()
    {
        // mHandler.sendEmptyMessage(ON_LOADED);
    }

    public void deleteCamera()
    {
        if (customView.getIdsCameras().size() > posMenuCameraPress)
        {
            customView.getIdsCameras().remove(posMenuCameraPress);
            customView.getIdsCameras().add(posMenuCameraPress, String.valueOf(-1));
            if (posMenuCameraPress == 0)
            {
                ibAddCamera21.setVisibility(View.VISIBLE);
                if (mPlayerView1 != null)
                {
                    mPlayerView1.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView1.releasePlayer();
                }
            } else
            {
                ibAddCamera22.setVisibility(View.VISIBLE);
                if (mPlayerView2 != null)
                {
                    mPlayerView2.getmSurface().setVisibility(View.INVISIBLE);
                    mPlayerView2.releasePlayer();
                }
            }
            mPopupWindow.dismiss();
        } else
        {
            Toast.makeText(this, "No existe la cámara que desea eliminar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCamera(final int posArea)
    {
        final String[] array = {"Árbol", "Búsqueda"};
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

                        } else
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

    private View llOverlay21, llOverlay22;

    private void showOverlay()
    {
        if (llOverlay21 != null)
        {
            llOverlay21.setVisibility(View.VISIBLE);
        }
        if (llOverlay22 != null)
        {
            llOverlay22.setVisibility(View.VISIBLE);
        }
    }

    private void hideOverlay()
    {
        if (llOverlay21 != null)
        {
            llOverlay21.setVisibility(View.GONE);
        }
        if (llOverlay22 != null)
        {
            llOverlay22.setVisibility(View.GONE);
        }
        if (isFullscreen)
        {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (llOverlay21.getVisibility() != View.VISIBLE || llOverlay22.getVisibility() != View.VISIBLE)
            {
                showOverlay();
            } else
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
        ibLockCamera = (ImageButton) popupView.findViewById(R.id.ib_lock_camera);
        if (visorController.getCameraById(customView.getIdsCameras().get(posMenuCameraPress)).isLock())
            ibLockCamera.setBackgroundResource(R.drawable.ic_lock_open_white_24dp);
        else
            ibLockCamera.setBackgroundResource(R.drawable.ic_lock_outline_white_24dp);
        ibLockCamera.setOnClickListener(this);
        //mPopupWindow.showAsDropDown(view, 150, 150);
        //mPopupWindow.showAsDropDown(ibMenuCamera, 500, 500);
        int[] coor = new int[2];
        view.getLocationInWindow(coor);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, coor[0], coor[1]);
    }

    @Override
    public void onPause()
    {
        visorController.setCameraPlaying(null);
        if(mPlayerView1!=null)
            mPlayerView1.releasePlayer();
        if(mPlayerView2!=null)
            mPlayerView2.releasePlayer();
        super.onPause();
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
    }

    @Override
    public void finish()
    {
        releasePlayers();
        visorController.setCameraPlaying(null);
        super.finish();
    }

    @Override
    protected void onRestart()
    {
        recreate();
        super.onRestart();
    }
}
