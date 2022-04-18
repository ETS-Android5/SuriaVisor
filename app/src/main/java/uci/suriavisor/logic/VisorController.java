package uci.suriavisor.logic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;
import uci.suriavisor.presentation.CustomViews.IPlayerActivity;
import uci.suriavisor.presentation.CustomViews.ViewFiveCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewFourCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewSixCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewThreeCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewTwoCameraActivity;
import uci.suriavisor.presentation.MainActivity;
import uci.suriavisor.presentation.CustomViews.ViewOneCameraActivity;
import uci.suriavisor.presentation.SingleFragmentActivity;
import uci.suriavisor.presentation.ViewsActivity;
import uci.suriavisor.presentation.adapter.RecyclerAlarmAdapter;
import uci.suriavisor.presentation.fragment.FolderStructureFragment;
import uci.suriavisor.presentation.materialdialogs.DialogAction;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import uci.suriavisor.presentation.CustomViews.ServiceFloating;
import uci.suriavisor.presentation.util.Constants;
import uci.suriavisor.presentation.util.Preferences;
import com.xilema.suriavisor.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class VisorController
{
	private LinkedList<TreeElement> treeElements;

	public LinkedList<LinkedList<Integer>> getAdyacents()
	{
		return adyacents;
	}

	public LinkedList<TreeElement> getTreeElements()
	{
		return treeElements;
	}

	private LinkedList<LinkedList<Integer>> adyacents;

	public CustomView customViewTemporal = new CustomView("View Temporal", "-1", 1);

	private LinkedList<CustomView> customViews =new LinkedList<>();

	public int getCurrentView()
	{
		return currentView;
	}

	public void setCurrentView(int currentView)
	{
		this.currentView = currentView;
	}

	private int currentView = -1;

	public boolean permissionGranted = false;

	public LinkedList<CustomView> getCustomViews()
	{
		return customViews;
	}

	public void addCustomView(CustomView customView)
	{
		if(customView.isDefaultView())
		{
			disableDefault();
		}
		customViews.add(customView);
	}

	public void setDefaultView(int pos)
	{
		if(customViews.size()>pos)
		{
			disableDefault();
			customViews.get(pos).setDefaultView(true);
			showMessage("La vista "+customViews.get(pos).getName()+" se ha establecido como predeterminada.");
		}
	}

	public void deleteCustomView(int pos)
	{
		if (pos<customViews.size())
		{
			String nameView = customViews.get(pos).getName();
			customViews.remove(pos);
			if (context instanceof ViewsActivity)
			{
				((ViewsActivity)context).recreate();
			}
			Toast.makeText(context, "Vista "+ nameView +" eliminada correctamente.", Toast.LENGTH_SHORT).show();
		}
	}

	public void lockCamera(String idCamera)
	{
		servers.getFirst().lockCamera(idCamera);
	}

	public void cameraLocked(JSONObject json)
	{
		try
		{
			JSONObject json2 = json.getJSONObject("data");
			String idCamera = json2.getString("idCamera");
			Camera c = getCameraById(idCamera);
			c.setLock(true);
			showMessage("La cámara "+c.getName()+" ha sido bloqueda.");
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}

	public void cameraUnLocked(JSONObject json)
	{
		try
		{
			JSONObject json2 = json.getJSONObject("data");
			String idCamera = json2.getString("idCamera");
			Camera c = getCameraById(idCamera);
			c.setLock(false);
			showMessage("La camara "+c.getName()+" ha sido desbloqueda.");
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}

	public LinkedList<TreeElement> childOfZoneById(String idZone)
	{
		LinkedList<TreeElement> treeElementsFoundZ = new LinkedList<>();
		for (int i = 0; i < treeElements.size(); i++)
		{
			if(treeElements.get(i).getParentZoneId().equals(idZone))
				treeElementsFoundZ.add(treeElements.get(i));
		}
		return treeElementsFoundZ;
	}

	public LinkedList<TreeElement> searchZone(String parameter)
	{
		LinkedList<TreeElement> treeElementsFoundZ = new LinkedList<>();
		for (int i = 0; i < treeElements.size(); i++)
		{
			String tempName = treeElements.get(i).getName().toLowerCase();
			if(tempName.contains(parameter.toLowerCase()) && treeElements.get(i) instanceof Zone)
			{
				treeElementsFoundZ.add(treeElements.get(i));
			}
		}
		return treeElementsFoundZ;
	}

	public LinkedList<TreeElement> searchCamera(String parameter)
	{
		LinkedList<TreeElement> treeElementsFoundC = new LinkedList<>();
		for (int i = 0; i < treeElements.size(); i++)
		{
			String tempName = treeElements.get(i).getName().toLowerCase();
			if(tempName.contains(parameter.toLowerCase()) && treeElements.get(i) instanceof Camera)
			{
				treeElementsFoundC.add(treeElements.get(i));
			}
		}
		return treeElementsFoundC;
	}

	public LinkedList<TreeElement> searchAll(String parameter)
	{
		LinkedList<TreeElement> treeElementsFoundZ = new LinkedList<>();
		LinkedList<TreeElement> treeElementsFoundC = new LinkedList<>();
		for (int i = 0; i < treeElements.size(); i++)
		{
			String tempName = treeElements.get(i).getName().toLowerCase();
			if(tempName.contains(parameter.toLowerCase()))
			{
				if(treeElements.get(i) instanceof Zone)
					treeElementsFoundZ.add(treeElements.get(i));
				else
					treeElementsFoundC.add(treeElements.get(i));
			}
		}
		treeElementsFoundZ.addAll(treeElementsFoundC);
		return treeElementsFoundZ;
	}

	public void setMuteAlarms(Boolean mute)
	{
		preferences.setMuteAlarms(mute);
		if(r!= null && r.isPlaying() && mute)
			r.stop();
		else if(!mute && alarms.size()>0)
		{
			if(notification== null)
				notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if(r== null)
				r = RingtoneManager.getRingtone(context, notification);
			r.play();
		}
	}

	public void deleteAlarm(int pos)
	{
		if (pos<alarms.size())
		{
			alarms.remove(pos);
			if(alarms.size()==0)
			{
				if (mPopupWindow!= null && mPopupWindow.isShowing())
				{
					mPopupWindow.dismiss();
				}
				if(r!= null && r.isPlaying())
					r.stop();
				if(context instanceof Activity)
					((Activity)context).recreate();
			}
			else
				//if (context instanceof Activity)
				//{
				showAlarm(String.valueOf(-1),String.valueOf(-1));
			//}
		}
	}

	public void webSocketTimeOut()
	{
		servers.getFirst().webSocketTimeOut();
	}

	public boolean isInPopupMode = false;

	public void showCustomView(int pos)
	{
		if (pos<customViews.size() && pos> -1)
		{
			int canCameras = customViews.get(pos).getCantAreas();
			switch (canCameras)
			{
				case 1:
				{
//					Intent i = new Intent();
//					i.setClass(context.getApplicationContext(), ViewOneCameraActivity.class);
//					i.putExtra("indexCustomView", String.valueOf(pos));
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.getApplicationContext().startActivity(i);
//					break;
					Intent i = new Intent(context, ViewOneCameraActivity.class);
					i.putExtra("indexCustomView", String.valueOf(pos));
					context.startActivity(i);
					break;
				}
				case 2:
				{
					Intent i = new Intent(context, ViewTwoCameraActivity.class);
					i.putExtra("indexCustomView", String.valueOf(pos));
					context.startActivity(i);
					break;
				}
				case 3:
				{
					Intent i = new Intent(context, ViewThreeCameraActivity.class);
					i.putExtra("indexCustomView", String.valueOf(pos));
					context.startActivity(i);
					break;
				}
				case 4:
				{
					Intent i = new Intent(context, ViewFourCameraActivity.class);
					i.putExtra("indexCustomView", String.valueOf(pos));
					context.startActivity(i);
					break;
				}
				case 5:
				{
					Intent i = new Intent(context, ViewFiveCameraActivity.class);
					i.putExtra("indexCustomView", String.valueOf(pos));
					context.startActivity(i);
					break;
				}
				case 6:
				{
					Intent i = new Intent(context, ViewSixCameraActivity.class);
					i.putExtra("indexCustomView", String.valueOf(pos));
					context.startActivity(i);
					break;
				}
				default:
				{
					break;
				}
			}
			if(context instanceof Activity)
				((Activity) context).finish();
		}
	}

	public void showEditDialog(int pos)
	{
		if (pos<customViews.size())
		{
			if (context instanceof ViewsActivity)
			{
				((ViewsActivity)context).showDialog(customViews.get(pos));
			}
		}
	}

	public void disableDefault()
	{
		for (CustomView customView: customViews)
		{
			customView.setDefaultView(false);
		}
	}

	private static VisorController ourInstance = new VisorController();

	private LinkedList<Server> servers;
	private int countServersConected = 0;
	public void aServerConected()
	{
		countServersConected++;
	}
	public void aServerDisconected()
	{
		countServersConected--;
	}

	public String getUser()
	{
		return user;
	}

	private String user, password;

	public boolean isReadyAllCameras()
	{
		return readyAllCameras;
	}

	public boolean isReadyAllZones()
	{
		return readyAllZones;
	}

	private boolean readyAllCameras = false, readyAllZones= false;

	public Context getContext()
	{
		return context;
	}

	private Context context;

	private VisorController()
	{
		super();
		servers = new LinkedList<>();
		treeElements =  new LinkedList<>();
		adyacents = new LinkedList<> ();
		sensors = new LinkedList<> ();
		events = new LinkedList<> ();
	}

	public static VisorController getInstance()
	{
		return ourInstance;
	}

	public void setCameraPlaying(String urlPlaying)
	{
		this.urlPlaying = urlPlaying;
	}

	public void startServiceFloating()
	{
		if(context.stopService(new Intent(context, ServiceFloating.class)))
			context.startService(new Intent(context, ServiceFloating.class));
		else
			context.startService(new Intent(context, ServiceFloating.class));
	}

	private String urlPlaying = null;

	public String getUrlPlaying()
	{
		return urlPlaying;
	}



	// Método para reproducir el vídeo
	//rtsp://root:Admin.123@10.54.13.250:554/live.sdp
	public void playCamera(String ruta, VideoView video)
	{
		StringBuilder url = new StringBuilder(ruta);
		Log.e("AVISO", url.toString());
		Uri uri = Uri.parse(url.toString());
		video.setVideoURI(uri);
		video.requestFocus();
		video.start();
	}
	/*
	public void playCamera(LinkedList<String> idsCameras)
	{
		for (int i = 0; i < servers.size(); i++)
		{
			if (servers.get(i).isLogged())
			{
				servers.addFirst(servers.get(i));
				servers.remove(i+1);
				servers.get(0).getURLCamerasByIds(idsCameras);
				i = servers.size();
			}
		}
	}*/

	public void requestSensors()
	{
		servers.getFirst().getSensors();
	}

	private LinkedList<Sensor> sensors;

	public LinkedList<Sensor> getSensors()
	{
		return sensors;
	}

	public Sensor getSensorById(String idSensor)
	{
		for (int i = 0; i < sensors.size(); i++)
		{
			if (sensors.get(i).getId().equals(idSensor))
				return sensors.get(i);
		}
		return null;
	}

	public void requestEvents()
	{
		servers.getFirst().getEvents();
	}

	private LinkedList<Event> events;

	public LinkedList<Event> getEvents()
	{
		return events;
	}

	public Event getEventById(String idEvent)
	{
		for (int i = 0; i < events.size(); i++)
		{
			if (events.get(i).getId().equals(idEvent))
				return events.get(i);
		}
		return null;
	}

	public void changedCameraStatus(String idCamera, String status)
	{
		Camera c = getCameraById(idCamera);
		boolean oldActive = c.isActive();
		//boolean oldRecoding= c.isRecoding();
		c.changeStatus(status);
		if(oldActive != c.isActive())
			if(c.isActive())
				showMessage("La cámara "+ c.getName() + " está activa.");
			else
				showMessage("La cámara "+ c.getName() + " se ha desactivado.");
		if(/*oldRecoding != c.isRecoding() &&*/ context instanceof IPlayerActivity)
		{
			if(c.isRecoding())
				((IPlayerActivity) context).startedRecord(idCamera);
			else
				((IPlayerActivity) context).stoppedRecord(idCamera);
		}
	}

	public String getCurrentTimeString()
	{
		Time time = new Time();
		time.setToNow();
		String hour = String.valueOf(time.hour);
		if(hour.length()==1)
			hour = String.valueOf(0)+hour;
		String minute = String.valueOf(time.minute);
		if(minute.length()==1)
			minute = String.valueOf(0)+minute;
		String second = String.valueOf(time.second);
		if(second.length()==1)
			second = String.valueOf(0)+second;
		return hour + ":" + minute + ":" +second;
	}

	public String getCurrentDateString()
	{
		Time time = new Time();
		time.setToNow();
		String day = String.valueOf(time.monthDay);
		if(day.length()==1)
			day = String.valueOf(0)+day;
		String month = String.valueOf(time.month);
		if(month.length()==1)
			month = String.valueOf(0)+month;
		String year = String.valueOf(time.year);
		/*if(second.length()==1)
			second = String.valueOf(0)+second;*/
		return day + "-" + month + "-" +year;
	}

	private LinkedList<JSONObject> alarms = new LinkedList<>();
	private PopupWindow mPopupWindow;
	private View popupView;
	private RecyclerView recyclerView;
	private Uri notification ;
	private Ringtone r ;

	public void showAlarm(String idCamera, String idSensor)
	{
		try
		{
			if (!(idCamera.equals(String.valueOf(-1)) && idSensor.equals(String.valueOf(-1))))
			{
				JSONObject jsonAlarms = new JSONObject();

				jsonAlarms.put("time", getCurrentTimeString());
				jsonAlarms.put("idSensor", idSensor);
				jsonAlarms.put("idCamera", idCamera);
				alarms.add(jsonAlarms);
				//emit sound of alarm
				try
				{
					if(notification== null && r== null && !preferences.isMuteAlarms())
					{
						notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						r = RingtoneManager.getRingtone(context, notification);
						r.play();
					}
					else if(!r.isPlaying() && !preferences.isMuteAlarms())
						r.play();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			if(context instanceof Activity)
			{
				popupView = ((Activity)context).getLayoutInflater().inflate(R.layout.popup_window_alarms, null);
				Button button = (Button) popupView.findViewById(R.id.button_deleteAllAlarm);
				button.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						mPopupWindow.dismiss();
						if(r!= null && r.isPlaying())
							r.stop();
						alarms = new LinkedList<>();
						((Activity)context).recreate();
					}
				});

				//Mute alarma
				final Button btAlarmaMute= (Button) popupView.findViewById(R.id.button_MuteAlarm);
				if (VisorController.getInstance().getPreferences().isMuteAlarms())
					btAlarmaMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
				else
					btAlarmaMute.setBackgroundResource(R.drawable.ic_action_volume_up);
				btAlarmaMute.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						if (!VisorController.getInstance().getPreferences().isMuteAlarms()){
							setMuteAlarms(true);
							btAlarmaMute.setBackgroundResource(R.drawable.ic_action_volume_mute);
						}else {
							setMuteAlarms(false);
							btAlarmaMute.setBackgroundResource(R.drawable.ic_action_volume_up);
						}

					}
				});

				recyclerView = (RecyclerView) popupView.findViewById(R.id.popup_windows_alarms);
				LinearLayoutManager llm = new LinearLayoutManager(context);
				recyclerView.setLayoutManager(llm);
				recyclerView.setHasFixedSize(true);
				RecyclerAlarmAdapter adapter = new RecyclerAlarmAdapter(alarms);
				recyclerView.setAdapter(adapter);
				mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, false);
				mPopupWindow.setTouchable(true);
				mPopupWindow.setOutsideTouchable(true);
				//mPopupWindow.setBackgroundDrawable(new BitmapDrawable((context).getResources(), (Bitmap) null));
				View vv = ((Activity)context).findViewById(R.id.toolbar);
				mPopupWindow.showAtLocation(vv, Gravity.RIGHT, 0, 0);
			}
		}
		catch (Exception e)
		{
			Log.e("showAlarm()",e.toString());
		}
	}

	public void updateChange (String nameEntity, LinkedList<Camera> cameras){
		//Update the Tree
		if (context instanceof SingleFragmentActivity){
			showTree();
			Toast.makeText(context, "El árbol de Zonas y Cámaras se ha actualizado.", Toast.LENGTH_SHORT).show();
		}
		//Update View of Camera
		if (context instanceof ViewOneCameraActivity && nameEntity.equals("Camera")){

			Camera camera0 = cameras.get(0);
			Camera camera1 = new Camera();
			for (int i = 0; i < treeElements.size(); i++)
			{
				String id = treeElements.get(i).getId();
				if (camera0.getId().equals(id)){
					camera1 = getCameraById(treeElements.get(i).getId());
					break;
				}
			}
			if (camera0.isStreaming() && camera1 != null){
				if (camera0.isRecoding() != camera1.isRecoding() || camera0.isLock() != camera1.isLock()){
					((ViewOneCameraActivity) context).recreate();
				}
			}

		}else if (context instanceof ViewTwoCameraActivity && nameEntity.equals("Camera")){

			Camera camera0, camera1 = new Camera();
			for (int j = 0; j < cameras.size(); j++)
			{
				camera0 = cameras.get(j);
				if (camera0 != null)
				{
					for (int i = 0; i < treeElements.size(); i++)
					{
						String id = treeElements.get(i).getId();
						if (camera0.getId().equals(id))
						{
							camera1 = getCameraById(treeElements.get(i).getId());
							break;
						}
					}
					if (camera0.isStreaming() && camera1 != null)
					{
						if (camera0.isRecoding() != camera1.isRecoding() || camera0.isLock() != camera1.isLock())
						{
							((ViewTwoCameraActivity) context).recreate();
						}
					}
				}
			}

		}else if (context instanceof ViewThreeCameraActivity && nameEntity.equals("Camera")){

			// Ver por que no sale esta de tres....
			Camera camera0, camera1 = new Camera();
			for (int j = 0; j < cameras.size(); j++)
			{
				camera0 = cameras.get(j);
				if (camera0 != null)
				{
					for (int i = 0; i < treeElements.size(); i++)
					{
						String id = treeElements.get(i).getId();
						if (camera0.getId().equals(id))
						{
							camera1 = getCameraById(treeElements.get(i).getId());
							break;
						}
					}
					if (camera0.isStreaming() && camera1 != null)
					{
						if (camera0.isRecoding() != camera1.isRecoding() || camera0.isLock() != camera1.isLock())
						{
							((ViewThreeCameraActivity) context).recreate();
							break;
						}
					}
				}
			}
		}else if (context instanceof ViewFourCameraActivity && nameEntity.equals("Camera")){
			Camera camera0, camera1 = new Camera();
			for (int j = 0; j < cameras.size(); j++)
			{
				camera0 = cameras.get(j);
				if (camera0 != null)
				{
					for (int i = 0; i < treeElements.size(); i++)
					{
						String id = treeElements.get(i).getId();
						if (camera0.getId().equals(id))
						{
							camera1 = getCameraById(treeElements.get(i).getId());
							break;
						}
					}
					if (camera0.isStreaming() && camera1 != null)
					{
						if (camera0.isRecoding() != camera1.isRecoding() || camera0.isLock() != camera1.isLock())
						{
							((ViewFourCameraActivity) context).recreate();
							break;
						}
					}
				}
			}
		}else if (context instanceof ViewFiveCameraActivity && nameEntity.equals("Camera")){
			Camera camera0, camera1 = new Camera();
			for (int j = 0; j < cameras.size(); j++)
			{
				camera0 = cameras.get(j);
				if (camera0 != null)
				{
					for (int i = 0; i < treeElements.size(); i++)
					{
						String id = treeElements.get(i).getId();
						if (camera0.getId().equals(id))
						{
							camera1 = getCameraById(treeElements.get(i).getId());
							break;
						}
					}
					if (camera0.isStreaming() && camera1 != null)
					{
						if (camera0.isRecoding() != camera1.isRecoding() || camera0.isLock() != camera1.isLock())
						{
							((ViewFiveCameraActivity) context).recreate();
							break;
						}
					}
				}
			}
		}else if (context instanceof ViewSixCameraActivity && nameEntity.equals("Camera")){
			Camera camera0, camera1 = new Camera();
			for (int j = 0; j < cameras.size(); j++)
			{
				camera0 = cameras.get(j);
				if (camera0 != null)
				{
					for (int i = 0; i < treeElements.size(); i++)
					{
						String id = treeElements.get(i).getId();
						if (camera0.getId().equals(id))
						{
							camera1 = getCameraById(treeElements.get(i).getId());
							break;
						}
					}
					if (camera0.isStreaming() && camera1 != null)
					{
						if (camera0.isRecoding() != camera1.isRecoding() || camera0.isLock() != camera1.isLock())
						{
							((ViewSixCameraActivity) context).recreate();
							break;
						}
					}
				}
			}
		}
	}

	public void getUrlStreamingById(String idCamera)
	{
		for (int i = 0; i < servers.size(); i++)
		{
			//pedir lista de camaras y zonas a cada servdior
			if (servers.get(i).isLogged())
			{
				servers.get(i).getStreaming(idCamera);
			}
		}
	}

	public void setStreaming(String idCamera, String urlStreaming)
	{
		if(context instanceof IPlayerActivity)
		{
			((IPlayerActivity) context).playCamera(idCamera, urlStreaming);
		}
	}
/*
	public void showDialogMark(String idCamera, String currentTime)
	{
		if(context instanceof IPlayerActivity)
		{
			((IPlayerActivity) context).showDialogMark(idCamera, currentTime);
		}
	}*/

	private EditText etNameMark, etDetailsMark;
	private Spinner spinner;
	public void showDialogMark(final String idCamera, final String currentTime)
	{
		final CharSequence [] array = new String[getEvents().size()];
		for (int i=0; i < getEvents().size(); i++)
		{
			array[i] = getEvents().get(i).getName();
		}
		MaterialDialog dialog = new MaterialDialog.Builder(context)
				.customView(R.layout.dialog_add_marcador, true)
				.title("Detalles del marcador")
				.positiveText(R.string.action_accept)
				.negativeText(R.string.action_cancell)
				.onPositive(new MaterialDialog.SingleButtonCallback()
				{
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
					{
						String idEvent = getEvents().get(spinner.getSelectedItemPosition()).getId();
						setMark(idCamera, etNameMark.getText().toString(), etDetailsMark.getText().toString(), idEvent, currentTime);
						//showMessage("Nombre: " + etNameMark.getText().toString() + "Detalles: " + etDetailsMark.getText().toString());
					}
				}).build();
		etNameMark = (EditText) dialog.findViewById(R.id.et_name_mark);
		etDetailsMark = (EditText) dialog.findViewById(R.id.et_detail_mark);
		spinner = (Spinner) dialog.findViewById(R.id.sp_events);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, android.R.id.text1, array);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		dialog.show();
	}

	public void showPopupViews(final int posCustomView, final Camera c)
	{
		try
		{
			if(context instanceof Activity)
			{
				CustomView customView = customViews.get(posCustomView);
				View popupView = null;
				switch (customView.getCantAreas())
				{
					case 1:
						popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.popup_window_view_one_camera, null);
						if (!customView.getIdsCameras().get(0).equals("-1")){
							ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
							camera1.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						break;
					case 2:
						popupView = ((Activity)context).getLayoutInflater().inflate(R.layout.popup_window_view_two_camera, null);
						if (!customView.getIdsCameras().get(0).equals("-1")){
							ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
							camera1.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(1).equals("-1")){
							ImageButton camera2 = (ImageButton) popupView.findViewById(R.id.camera2);
							camera2.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						break;
					case 3:
						popupView = ((Activity)context).getLayoutInflater().inflate(R.layout.popup_window_view_three_camera, null);
						if (!customView.getIdsCameras().get(0).equals("-1")){
							ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
							camera1.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(1).equals("-1")){
							ImageButton camera2 = (ImageButton) popupView.findViewById(R.id.camera2);
							camera2.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(2).equals("-1")){
							ImageButton camera3 = (ImageButton) popupView.findViewById(R.id.camera3);
							camera3.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						break;
					case 4:
						popupView = ((Activity)context).getLayoutInflater().inflate(R.layout.popup_window_view_four_camera, null);
						if (!customView.getIdsCameras().get(0).equals("-1")){
							ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
							camera1.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(1).equals("-1")){
							ImageButton camera2 = (ImageButton) popupView.findViewById(R.id.camera2);
							camera2.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(2).equals("-1")){
							ImageButton camera3 = (ImageButton) popupView.findViewById(R.id.camera3);
							camera3.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(3).equals("-1")){
							ImageButton camera4 = (ImageButton) popupView.findViewById(R.id.camera4);
							camera4.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						break;
					case 5:
						popupView = ((Activity)context).getLayoutInflater().inflate(R.layout.popup_window_view_five_camera, null);
						if (!customView.getIdsCameras().get(0).equals("-1")){
							ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
							camera1.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(1).equals("-1")){
							ImageButton camera2 = (ImageButton) popupView.findViewById(R.id.camera2);
							camera2.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(2).equals("-1")){
							ImageButton camera3 = (ImageButton) popupView.findViewById(R.id.camera3);
							camera3.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(3).equals("-1")){
							ImageButton camera4 = (ImageButton) popupView.findViewById(R.id.camera4);
							camera4.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(4).equals("-1")){
							ImageButton camera5 = (ImageButton) popupView.findViewById(R.id.camera5);
							camera5.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						break;
					case 6:
						popupView = ((Activity)context).getLayoutInflater().inflate(R.layout.popup_window_view_six_camera, null);
						if (!customView.getIdsCameras().get(0).equals("-1")){
							ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
							camera1.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(1).equals("-1")){
							ImageButton camera2 = (ImageButton) popupView.findViewById(R.id.camera2);
							camera2.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(2).equals("-1")){
							ImageButton camera3 = (ImageButton) popupView.findViewById(R.id.camera3);
							camera3.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(3).equals("-1")){
							ImageButton camera4 = (ImageButton) popupView.findViewById(R.id.camera4);
							camera4.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(4).equals("-1")){
							ImageButton camera5 = (ImageButton) popupView.findViewById(R.id.camera5);
							camera5.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						if (!customView.getIdsCameras().get(5).equals("-1")){
							ImageButton camera6 = (ImageButton) popupView.findViewById(R.id.camera6);
							camera6.setBackgroundResource(R.drawable.ic_videocam_white_48dp);
						}
						break;
					default:
						break;
				}

				final PopupWindow mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
				mPopupWindow.setTouchable(true);
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
				View vv = ((Activity)context).findViewById(R.id.toolbar);

				if(popupView!=null)
				{
					ImageButton camera1 = (ImageButton) popupView.findViewById(R.id.camera1);
					if(camera1!=null)
						camera1.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View view)
							{
								setCameraToArea(0, posCustomView, c);
								if (mPopupWindow != null)
									mPopupWindow.dismiss();
							}
						});
					ImageButton camera2 = (ImageButton) popupView.findViewById(R.id.camera2);
					if(camera2!=null)
						camera2.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View view)
							{
								setCameraToArea(1, posCustomView, c);
								if (mPopupWindow != null)
									mPopupWindow.dismiss();
							}
						});
					ImageButton camera3 = (ImageButton) popupView.findViewById(R.id.camera3);
					if(camera3!=null)
						camera3.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View view)
							{
								setCameraToArea(2, posCustomView, c);
								if (mPopupWindow != null)
									mPopupWindow.dismiss();
							}
						});
					ImageButton camera4 = (ImageButton) popupView.findViewById(R.id.camera4);
					if(camera4!=null)
						camera4.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View view)
							{
								setCameraToArea(3, posCustomView, c);
								if (mPopupWindow != null)
									mPopupWindow.dismiss();
							}
						});
					ImageButton camera5 = (ImageButton) popupView.findViewById(R.id.camera5);
					if(camera5!=null)
						camera5.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View view)
							{
								setCameraToArea(4, posCustomView, c);
								if (mPopupWindow != null)
									mPopupWindow.dismiss();
							}
						});
					ImageButton camera6 = (ImageButton) popupView.findViewById(R.id.camera6);
					if(camera6!=null)
						camera6.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View view)
							{
								setCameraToArea(5, posCustomView, c);
								if (mPopupWindow != null)
									mPopupWindow.dismiss();
							}
						});
				}

				mPopupWindow.showAtLocation(vv, Gravity.CENTER, 0, 0);
			}
		}
		catch (Exception e)
		{
			Log.e("showPopupViews()",e.toString());
		}
	}

	private void setCameraToArea(int posArea, int posCustomView, Camera c)
	{
		CustomView customView = customViews.get(posCustomView);
		while (customView.getIdsCameras().size()<=posArea)
			customView.getIdsCameras().add(null);
		customView.getIdsCameras().remove(posArea);
		customView.getIdsCameras().add(posArea, c.getId());
	}

	public void setSensors(LinkedList<Sensor> sensors)
	{
		this.sensors = sensors;
	}

	private Boolean muted = false;

	public Boolean isMuted()
	{
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		Log.e("vol", String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
		if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)>0)
			muted = false;
		else
			muted = true;
		return muted;
	}

	private AudioManager audioManager = null;
	public void setMute(Boolean flagMute)
	{
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if(flagMute)
		{
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		}
		else
		{
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
		}
	}

	public void getCurrentTime(String idCamera)
	{
		servers.getFirst().getCurrentTime(idCamera);
	}

	public void getActivedSensorsByIdCamera(String idCamera)
	{
		servers.getFirst().getActivedSensorsByIdCamera(idCamera);
	}

	public void setMark(String idCamera, String nameMark, String detailsMark, String idEvent, String time)
	{
		servers.getFirst().setMark(idCamera, nameMark, detailsMark, idEvent, time);
	}

	public void startedRecord(JSONObject jsonObjectI)
	{
		try
		{
			String idCamera = jsonObjectI.getString("data");
			if(idCamera != null)
			{
				LinkedList<String> cameras = customViews.get(currentView).getIdsCameras();
				for (String c : cameras)
				{
					if (getCameraById(c) != null)
					{
						if (!getCameraById(c).isRecoding() && c.equals(idCamera))
						{
							getCameraById(c).setRecoding(true);
							showMessage("Se ha comenzado la grabación en la cámara " + getCameraById(c).getName());
						}
					}
				}
				if (context instanceof IPlayerActivity)
					((IPlayerActivity) context).startedRecord(idCamera);
			}
			else
				showMessage("No se pudo inciar la grabación en la cámara, inténtelo más tarde.");
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}

	public void setActiveSensor(String idCamera, String idSensor, Boolean active)
	{
		servers.getFirst().setActiveSensor(idCamera,idSensor, active);
	}

	public void activedSensor(String idCamera, String idSensor)
	{
		try
		{
			Camera c = getCameraById(idCamera);
			c.setActiveSensorById(idSensor, true);
			showMessage("El sensor "+getSensorById(idSensor).getId()+" ha sido activado.");
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}

	public void deactivedSensor(String idCamera, String idSensor)
	{
		try
		{
			Camera c = getCameraById(idCamera);
			c.setActiveSensorById(idSensor, false);
			showMessage("El sensor "+getSensorById(idSensor).getId()+" ha sido desactivado.");
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}

	public void stoppedRecord(JSONObject jsonObjectI)
	{
		try
		{
			String idCamera = jsonObjectI.getString("data");
			getCameraById(idCamera).setRecoding(false);
			showMessage("Se ha detenido la grabación en la cámara " + getCameraById(idCamera).getName());
			if(context instanceof IPlayerActivity)
				((IPlayerActivity)context).stoppedRecord(idCamera);
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}
/*
	public void setURLsToCameras(JSONArray jsonArray)
	{
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObjectI = jsonArray.getJSONObject(i);
				Log.e("Lista de ids recibida",jsonObjectI.getString("url"));
				getCameraById(jsonObjectI.getString("id")).setUrl(jsonObjectI.getString("url"));
			}
			if (context instanceof ViewOneCameraActivity)
			{
				((ViewOneCameraActivity)context).playCamera();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/

	private Server getServerByIdCamera(String idCamera)
	{
		for (Server s:servers)
		{
			if(s.getPosCameraById(idCamera)!= -1)
			{
				return s;
			}
		}
		return null;
	}

	public void prepareTree()
	{
		getCameras();
		getZones();
	}

	public void getCameras()
	{
		for (int i = 0; i < servers.size(); i++)
		{
			//pedir lista de camaras y zonas a cada servdior
			if (servers.get(i).isLogged())
			{
				servers.get(i).getCameras();
			}
		}
	}

	public void getCameraStatus(String idCamera)
	{
		servers.getFirst().getCameraStatus(idCamera);
	}

	public void getZones()
	{
		for (int i = 0; i < servers.size(); i++)
		{
			//pedir lista de camaras y zonas a cada servdior
			if (servers.get(i).isLogged())
			{
				servers.get(i).getZones();
			}
		}
	}

	MaterialDialog materialDialogProgressIndeterminate;
	public void showIndeterminateProgressDialog(boolean horizontal)
	{
		materialDialogProgressIndeterminate = new MaterialDialog.Builder(context)
				.title(R.string.progress_dialog)
				.content(R.string.please_wait)
				.progress(true, 0)
				.progressIndeterminateStyle(horizontal)
				.show();
	}

	public void newCameras()
	{
		for (int i = 0; i < servers.size(); i++)
		{
			readyAllCameras = true;
			if(servers.get(i).isLogged() && !servers.get(i).isReadyCameras())
			{
				readyAllCameras = false;
				i = servers.size();
			}
		}
		if(readyAllCameras && readyAllZones)
		{
			buildTree();
		}
	}

	public void newZones()
	{
		for (int i = 0; i < servers.size(); i++)
		{
			readyAllZones = true;
			if(servers.get(i).isLogged() && !servers.get(i).isReadyZones())
			{
				readyAllZones = false;
				i = servers.size();
			}
		}
		if(readyAllCameras && readyAllZones)
		{
			buildTree();
		}
	}
	public Boolean toShow = false;

	public void buildTree()
	{
		treeElements = new LinkedList<>();
		adyacents = new LinkedList<>();
		for (int i = 0; i < servers.size(); i++)
		{
			for (int j = 0; j < servers.get(i).getTreeElements().size(); j++)
			{
				TreeElement element = servers.get(i).getTreeElements().get(j);
				if(element instanceof Zone && getPosZoneById(element.getId())!=-1)
				{}
				else
				{
					treeElements.add(servers.get(i).getTreeElements().get(j));
					adyacents.add(new LinkedList<Integer>());
				}
			}
		}
		for (int i = 0; i < treeElements.size(); i++)
		{
			Log.i("id padre zona ", String.valueOf(treeElements.get(i).getParentZoneId()));
			int pos = getPosZoneById(treeElements.get(i).getParentZoneId());
			Log.i("int pos", String.valueOf(pos));
			if(pos != -1)
			{
				adyacents.get(pos).add(i);
			}
		}
	}

	public void showTree()
	{
		if (context instanceof SingleFragmentActivity)
		{
			//actualizar el arbol cuando llega una nueva camara o una nueva zona
			((SingleFragmentActivity) context).finish();
		}
		if (context instanceof SingleFragmentActivity || context instanceof MainActivity)
		{
			Intent i = new Intent(context, SingleFragmentActivity.class);
			i.putExtra(SingleFragmentActivity.FRAGMENT_PARAM, FolderStructureFragment.class);
			context.startActivity(i);
			if (materialDialogProgressIndeterminate != null && materialDialogProgressIndeterminate.isShowing())
				materialDialogProgressIndeterminate.dismiss();
			((Activity) context).finish();
		}
	}

	public int getPosZoneById(String idZone)
	{
		for (int i = 0; i < treeElements.size(); i++)
		{
			if (treeElements.get(i) instanceof Zone && treeElements.get(i).getId().equals(idZone))
			{
				return i;
			}
		}
		return -1;
	}

	public Camera getCameraById(String idCamera)
	{
		for (int i = 0; i < treeElements.size(); i++)
		{
			if (treeElements.get(i) instanceof Camera && treeElements.get(i).getId().equals(idCamera))
			{
				return (Camera) treeElements.get(i);
			}
		}
		return null;
	}

	public int getPosElementById(String id)
	{
		for (int i = 0; i < treeElements.size(); i++)
		{
			if (treeElements.get(i).getId().equals(id))
			{
				return i;
			}
		}
		return -1;
	}

	public LinkedList<Server> getServersList()
	{
		return servers;
	}

	//"getServers: server:ip, port, path; server:ip, port, path"
	//"getServers: server:10.54.13.110, 8802, ; server:10.54.13.10, 8802, ;"
	public void addServers(JSONObject jsonObjectServers)
	{
		//Log.i("Servers....", message);
		try
		{
			JSONArray jsonArrayServers = jsonObjectServers.getJSONArray("data");
			for (int i = 0; i < jsonArrayServers.length(); i++)
			{
				JSONObject jsonObjectServer = jsonArrayServers.getJSONObject(i);
				Log.i("Servers....", jsonObjectServer.toString());
				servers.add(new Server(jsonObjectServer, user, password, context));
			}
			//connectAllServers();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		/*String[] serverListTemp = message.split("server:");
		for (int i = 1; i < serverListTemp.length; i++)
		{
			String[] parameters = serverListTemp[i].split(",");
			String path = "";
			if(parameters.length==3 && parameters[2].trim().split(";").length>0)
			{
				path = parameters[2].trim().split(";")[0];
			}
			servers.add(new Server(parameters[0].trim(), parameters[1].trim(), path, user, password, context));
			//Log.i("Server"+String.valueOf(i), parameters[0].trim()+ parameters[1].trim()+ path);
		}
		connectAllServers();*/
		//Log.i("ServerCount", String.valueOf(servers.size()));
	}

	public void getServers()
	{
		for (int i = 0; i < servers.size(); i++)
		{
			if (servers.get(i).isServerWSConnected())
			{
				servers.get(i).getServers();
				i = servers.size();
			}
		}
	}

	public void login()
	{
		servers.get(0).login();
	}

	public void loginSucceed()
	{
		readViews();
		if (getPreferences() == null)
		{
			readPreferences();
			if (getPreferences() == null)
				setdefaultPreferences();
		}
		if(getPreferences().getThemId() != Constants.THEME_ID)
			Constants.THEME_ID = getPreferences().getThemId();
		if(getPreferences().getPathToSave()!= Constants.PATH_TO_SAVE)
			Constants.PATH_TO_SAVE = getPreferences().getPathToSave();

		/*
        esto es para pedir la lista de sensores y eventos disponibles*/
		requestSensors();
		requestEvents();
		if(getAdyacents().size()==0)
			prepareTree();

		if(!showDefaultView())
		{
			Intent i = new Intent(context, MainActivity.class);
			context.startActivity(i);
		}
		((Activity) context).finish();
	}

	public void changeContext(Context context)
	{
		this.context = context;
		for (int i = 0; i < servers.size(); i++)
		{
			servers.get(i).setContext(context);
		}
	}

	public void addServerMaster(String user, String password, Context context)
	{
		this.user = user;
		this.password = password;
		this.context = context;
		final SharedPreferences setting = context.getSharedPreferences("datos_cfg_cnx_gestor", Context.MODE_PRIVATE);
		String ip = setting.getString("dir_ip", "");
		String port = setting.getString("puerto", "");
		String path = setting.getString("ruta", "");
		if(servers.size()>0)
			servers.removeFirst();
		servers.addFirst(new Server(ip, port, path, user, password, context));
	}

	public void connectAllServers()
	{
		for (int i = 0; i < servers.size(); i++)
		{
			if (!servers.get(i).isLogged())
			{
				servers.get(i).login();
			}
		}
	}

	public void startRecord(String idCamera)
	{
		servers.getFirst().startRecord(idCamera);
	}

	public void stopRecord(String idCamera)
	{
		servers.getFirst().stopRecord(idCamera);
	}

	public void snapshot(final int cant, final String url, final String idCamera)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				FFmpegMediaMetadataRetriever fmmr;
				String fileName;
				int cantProcced = 0;
				//String urlCamera = mPlayerView.getUrl();
				//String urlCamera = "rtsp://visor:ABi343VF1Eg=@10.54.13.19:1794/ComplejoC1_Cam_1";
				String urlCamera = url;
				try
				{
					fmmr = new FFmpegMediaMetadataRetriever();
					//fmmr.setDataSource(customView.getIdsCameras().getFirst().getUrl(), new HashMap<String, String>());
					fmmr.setDataSource(urlCamera, new HashMap<String, String>());
				} catch (Exception e)
				{
					e.getStackTrace();
					return;
				}
				for (int i = 0; i < cant; i++)
				{

					Bitmap thumbnail = fmmr.getFrameAtTime();
					//fileName = Constants.PATH_TO_SAVE+"/"+String.valueOf(mPlayerView.getTime())+"."+Constants.EXTENSION_OF_SNAPSHOT;
					fileName = Constants.PATH_TO_SAVE + "/" + idCamera + "_" + getCurrentTimeString() + "_" + getCurrentDateString() + "." + Constants.EXTENSION_OF_SNAPSHOT;
					File fileObj = new File(fileName);
					if (!fileObj.exists())
					{
						if (DataToFile.saveSnapshotInFile(fileName, thumbnail))
							cantProcced++;
						if (!(fileObj.length() > 0))
						{
							cantProcced--;
							fileObj.delete();
							i--;
						}
					} else
						i--;
				}
				if (context instanceof Activity)
				{
					if (cantProcced == cant && cantProcced != 1)
					{
						((Activity) context).runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								showMessage("Ráfaga de instantáneas guardadas correctamente.");
							}
						});
					} else if (cantProcced == 1)
						((Activity) context).runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								showMessage("Instantánea guardada correctamente.");
							}
						});
				}
				//ibSnapshots.setBackgroundColor(Color.TRANSPARENT);
			}
		}).start();
	}

	public boolean showDefaultView()
	{
		for (int i = 0; i < customViews.size(); i++)
		{
			if(customViews.get(i).isDefaultView())
			{
				showCustomView(i);
				return true;
			}
		}
		return false;
	}

	public void showMessage(String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	//to save views in local file
	public void writeViews()
	{
		/*if(customViews.size()>0)
		{*/
		String filename = "views.srl";
		ObjectOutput objectOutput = null;
		try
		{
			//Log.e("fileeeeeee", new File(context.getFilesDir(),"")+File.separator+filename);
			objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir(), "") + File.separator + filename));
			objectOutput.writeObject(customViews);
			objectOutput.close();
			//Log.e("saving views", "saved");
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		//}
	}
	//to read views from local file
	public void readViews()
	{
		ObjectInputStream input;
		String filename = "views.srl";
		try
		{
			//Log.e("fileeeeeee", new File(context.getFilesDir(),"")+File.separator+filename);
			input = new ObjectInputStream(new FileInputStream(new File(new File(context.getFilesDir(),"")+File.separator+filename)));
			customViews = (LinkedList<CustomView>) input.readObject();
			input.close();
			//Log.e("reading views", "read "+String.valueOf(customViews.size()));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	private Preferences preferences = null;
	public Preferences getPreferences()
	{
		/*if(preferences == null)
			setdefaultPreferences();*/
		return preferences;
	}

	public void readPreferences()
	{
		DataToFile dataToFile = new DataToFile();
		setPreferences(dataToFile.readPreferences());
	}

	public void writePreferences()
	{
		DataToFile dataToFile = new DataToFile();
		dataToFile.writePreferences();
	}

	public void setPreferences(Preferences preferences)
	{
		this.preferences = preferences;
		if(preferences!=null)
		{
			Constants.PATH_TO_SAVE = preferences.getPathToSave();
			Constants.NUMBER_OF_SNAPSHOTS = preferences.getNumberOfSnapshots();
		}
	}

	public void setdefaultPreferences()
	{
		preferences = new Preferences(Constants.PATH_TO_SAVE, R.style.AppTheme, Constants.NUMBER_OF_SNAPSHOTS, false);
	}

	public void onClosed()
	{
		context.stopService(new Intent(context, ServiceFloating.class));
		for (int i = 0; i < treeElements.size(); i++)
		{
			if (treeElements.get(i) instanceof Camera )
			{
				((Camera) treeElements.get(i)).clearActivedSensor();
			}
		}
		writeViews();
		writePreferences();
		ourInstance = null;
		System.exit(0);
	}
}