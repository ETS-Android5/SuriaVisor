package uci.suriavisor.presentation.CustomViews;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.playerlibrary.PlayerView;
import com.xilema.suriavisor.R;

import org.videolan.libvlc.util.AndroidUtil;

public class ServiceFloating extends Service implements IPlayerActivity, ScaleGestureDetector.OnScaleGestureListener
{

	private double mScaleFactor = 1.d;
	private int mPopupWidth, mPopupHeight;
	private int mScreenWidth, mScreenHeight;
	WindowManager.LayoutParams params;

	public void playCamera(String idCamera, String urlStreaming)
	{

	}

	public void showMessage(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	public void hideLoading()
	{
		//rlLoading.setVisibility(View.GONE);
	}

	public void stoppedRecord(String idCamera)
	{
		//actualizar el visual
	}

	public void startedRecord(String idCamera)
	{
		//actualizar el visual
	}

	public static  int ID_NOTIFICATION = 2018;

	private WindowManager windowManager;
	//private ImageView chatHead;
	private PlayerView playerView;
	//private PopupWindow pwindo;
	private

	boolean mHasDoubleClicked = false;
	long lastPressTime;
	private String url ="" ;

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void updateWindowSize()
	{
		if( AndroidUtil.isHoneycombMr2OrLater )
		{
			Point size = new Point();
			windowManager.getDefaultDisplay().getSize(size);
			mScreenWidth = size.x;
			mScreenHeight = size.y;
		}
		else
		{
			mScreenWidth = windowManager.getDefaultDisplay().getWidth();
			mScreenHeight = windowManager.getDefaultDisplay().getHeight();
		}
	}


	@Override 
	public void onCreate()
	{
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		//chatHead = new ImageView(this);
		playerView = new PlayerView(this);

		//chatHead.setImageResource(R.drawable.ic_launcher);

		/*final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);*/
		params = new WindowManager.LayoutParams(
				500,
				250,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		//params.gravity = Gravity.TOP | Gravity.RIGHT;
		params.x = 0;
		params.y = 100;

		//windowManager.addView(chatHead, params);
		windowManager.addView(playerView, params);

		try
		{
			playerView.setOnTouchListener(new View.OnTouchListener()
			//chatHead.setOnTouchListener(new View.OnTouchListener()
			{
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override public boolean onTouch(View v, MotionEvent event)
				{
					switch (event.getAction())
					{
					case MotionEvent.ACTION_DOWN:

						// Get current time in nano seconds.
						long pressTime = System.currentTimeMillis();


						// If double click...
						if (pressTime - lastPressTime <= 300)
						{
							//createNotification();
							ServiceFloating.this.stopSelf();
							playerView.releasePlayer();
							VisorController.getInstance().showCustomView(VisorController.getInstance().getCurrentView());
							stopService(new Intent(ServiceFloating.this, ServiceFloating.class));
							mHasDoubleClicked = true;
						}
						else
						{     // If not double click....
							mHasDoubleClicked = false;
						}
						lastPressTime = pressTime; 
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						updateWindowSize();
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_MOVE:
						paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
						//windowManager.updateViewLayout(chatHead, paramsF);
						containInScreen(paramsF.width, paramsF.height);
						windowManager.updateViewLayout(playerView, paramsF);
						break;
					}
					return false;
				}
			});
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		//chatHead.setOnClickListener(new View.OnClickListener()
		/*playerView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				//initiatePopupWindow(chatHead);
				if (!playerView.isPlaying())
					initiatePopupWindow(playerView);
				_enable = false;
				//				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				//				getApplicationContext().startActivity(intent);
			}
		});*/
		initiatePopupWindow(playerView);
	}

	private void containInScreen(int width, int height)
	{
		//params.x = Math.max(params.x, 0);
		//params.y = Math.max(params.y, 0);
		if (params.x + width > mScreenWidth)
			params.x = mScreenWidth - width;
		if (params.y + height > mScreenHeight)
			params.y = mScreenHeight - height;
	}

	private void initiatePopupWindow(View anchor)
	{
		try
		{
			//playerView.initPlayer("http://10.0.2.2/1.webm");
			//playerView.initPlayer(VisorController.getInstance().getCameraPlaying().getUrl());
			url = VisorController.getInstance().getUrlPlaying();
			if(!url.equals(""))
			{
				playerView.initPlayer(url);
				playerView.widthCustom = 1;
				playerView.heightCustom = 1;
				//quitar esto
				playerView.setSize(640, 480);
				playerView.start();
				Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				ListPopupWindow popup = new ListPopupWindow(this);
				popup.setAnchorView(anchor);
				popup.setWidth((int) (display.getWidth() / (1.5)));
				popup.show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector)
	{
		mScaleFactor *= detector.getScaleFactor();

		mScaleFactor = Math.max(0.1d, Math.min(mScaleFactor, 5.0d));
		mPopupWidth = (int) (playerView.getWidth()*mScaleFactor);
		mPopupHeight = (int) (playerView.getHeight()*mScaleFactor);
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector)
	{
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector)
	{
		setViewSize(mPopupWidth, mPopupHeight);
		mScaleFactor = 1.0d;
	}
	/*
     * Update layout dimensions and apply layout params to window manager
     */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void setViewSize(int width, int height)
	{
		if (width > mScreenWidth) {
			height = height * mScreenWidth / width;
			width = mScreenWidth;
		}
		if (height > mScreenHeight){
			width = width * mScreenHeight / height;
			height = mScreenHeight;
		}
		containInScreen(width, height);
		params.width = width;
		params.height = height;
		windowManager.updateViewLayout(playerView, params);
		if (playerView.getVLCVout() != null)
			playerView.getVLCVout().setWindowSize(mPopupWidth, mPopupHeight);
	}

	public void createNotification()
	{
		Intent notificationIntent = new Intent(getApplicationContext(), ServiceFloating.class);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, 0);

		Notification notification = new Notification(R.drawable.ic_launcher, "Click to start launcher",System.currentTimeMillis());
		//notification.setLatestEventInfo(getApplicationContext(), "Start launcher" ,  "Click to start launcher", pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;

		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(ID_NOTIFICATION,notification);
	}

	@Override
	public void onDestroy()
	{
		stopService(new Intent(ServiceFloating.this, ServiceFloating.class));
		super.onDestroy();
		//if (chatHead != null) windowManager.removeView(chatHead);
		if (playerView != null) windowManager.removeView(playerView);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		stopService(new Intent(ServiceFloating.this, ServiceFloating.class));
		//if (chatHead != null) windowManager.removeView(chatHead);
		if (playerView != null) windowManager.removeView(playerView);
		super.onTaskRemoved(rootIntent);
	}
}