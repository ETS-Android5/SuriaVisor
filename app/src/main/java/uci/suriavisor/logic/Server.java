package uci.suriavisor.logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xilema.suriavisor.R;

import uci.suriavisor.presentation.CustomViews.ServiceFloating;
import uci.suriavisor.presentation.CustomViews.ViewFiveCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewFourCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewOneCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewSixCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewThreeCameraActivity;
import uci.suriavisor.presentation.CustomViews.ViewTwoCameraActivity;
import uci.suriavisor.presentation.LoginActivity;
import uci.suriavisor.presentation.MainActivity;
import uci.suriavisor.presentation.SingleFragmentActivity;
import uci.suriavisor.presentation.materialdialogs.Theme;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Miguel on 07/03/2016.
 */
public class Server implements Serializable
{
    private LinkedList<TreeElement> treeElements;

    /*public LinkedList<LinkedList<String>> getAdyacents()
    {
        return adyacents;
    }*/

    public LinkedList<TreeElement> getTreeElements()
    {
        return treeElements;
    }

    //    private LinkedList<LinkedList<String>> adyacents;
    int countCameras = 0;
    int countZones = 0;

    private int posCameraSelected;

    public boolean isLogged()
    {
        return logged;
    }

    private boolean logged= false, readyCameras = false, readyZones= false;

    String ip;
    String port;
    String path;

    public Server(String ip, String port, String path, String user, String password, Context context)
    {
        this.ip = ip;
        this.port = port;
        this.path = path;
        this.user = user;
        this.password = password;
        this.context = context;
        serverWSConnected = false;
        logged = false;
        treeElements = new LinkedList<>();
    }

    public Server(JSONObject jsonObjectServer, String user, String password, Context context)
    {
        try
        {
            this.ip = jsonObjectServer.getString("ip");
            this.port = jsonObjectServer.getString("port");
            this.path = jsonObjectServer.getString("path");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        this.user = user;
        this.password = password;
        this.context = context;
        serverWSConnected = false;
        logged = false;
        treeElements = new LinkedList<>();
    }
    /*public void addCamera(String url, int port, String user, String password, String id)
    {
        Camera camera =  new Camera();
        camera.setId(id);
        camera.setUrl(url);
        camera.setPort(String.valueOf(port));
        camera.setUser(user);
        camera.setPassword(password);
        cameras.add(camera);
    }*/

    public void setActiveSensor(String idCamera, String idSensor, Boolean active)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        parameters.add(idSensor);
        parameters.add(String.valueOf(active));
        if(serverWSConnected)
        {
            excutefunction("setActiveSensor", parameters);
        }
        else
        {
            connectWebSocket("setActiveSensor", parameters);
        }
    }

    private static Server ourInstance = new Server();

    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    private Context context;

    public static Server getInstance()
    {
        return ourInstance;
    }

    private Server()
    {
        serverWSConnected = false;
    }

    private WebSocketClient webSocketClient;

    public boolean isServerWSConnected()
    {
        return serverWSConnected;
    }

    private boolean serverWSConnected;

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    private String user;

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    private String password;

    public void login()
    {
        //online
        connectWebSocket("login", null);
        //processMessage("login,succeed");
    }

    public void webSocketTimeOut()
    {
        if(!webSocketClient.isOpen())
        {
            webSocketClient = null;
            serverWSConnected = false;
            if (context instanceof LoginActivity)
            {
                ((LoginActivity)context).loginFail("No se ha podido establecer conexión con el servidor, inténtelo más tarde.");
            }
        }
        timer.cancel();
    }

    private Timer timer;

    private void connectWebSocket(final String function, final LinkedList<String> parameters)
    {
        if (!serverWSConnected)
        {
            URI uri;
            try
            {
                uri = new URI("ws://"+ip+":"+port+"/"+path);
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
                return;
            }
            webSocketClient = new WebSocketClient(uri, new Draft_17(),null, 5000)
                    //webSocketClient = new WebSocketClient(uri, new Draft_17())
                    //webSocketClient = new WebSocketClient(uri)
            {
                @Override
                public void onOpen(ServerHandshake serverHandshake)
                {
                    Log.i("Websocket", "Opened");
                    serverWSConnected = true;
                    timer.cancel();
                    VisorController.getInstance().aServerConected();
                    excutefunction(function, parameters);
                    //webSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                }

                @Override
                public void onMessage(String s)
                {
                    final String message = s;
                    //mensaje("Message: "+ s);
                    ((Activity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                processMessage(message);
                            }
                            catch (Exception e)
                            //catch (JSONException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onClose(int i, String s, boolean b)
                {
                    Log.i("Websocket", "Closed " + s);
                    if(serverWSConnected && !(context instanceof LoginActivity))
                    {
                        ((Activity) context).runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                VisorController.getInstance().showMessage("Se ha perdido la conexión con el servidor.");
                                Intent i = new Intent(context, LoginActivity.class);
                                context.startActivity(i);
                                ((Activity) context).finish();
                            }
                        });
                    }
                    context.stopService(new Intent(context, ServiceFloating.class));
                    VisorController.getInstance().aServerDisconected();
                    serverWSConnected = false;
                }

                @Override
                public void onError(Exception e)
                {
                    Log.i("Websocket", "Error " + e.getMessage());
                    serverWSConnected = false;
                }
            };
            //webSocketClient.connect();
            try
            {
                timer = new Timer(5000,1000, this);//5 minutos de espera
                timer.start();
                webSocketClient.connect();
            }
            catch (Exception e)
            {
                if (!serverWSConnected)
                    VisorController.getInstance().showMessage("No se ha podido establecer la comunicación con el servidor, verifique la configuración.");
                e.fillInStackTrace();
            }
        }
        else
        {
            excutefunction(function, parameters);
        }
    }

    private void processMessage(final String message)
    {
        try
        {
            final JSONObject jsonObject= new JSONObject(message);
            String function = jsonObject.getString("function");
            if (function.equals("login"))
            {
                JSONObject jsonObject2= jsonObject.getJSONObject("data");
                if (jsonObject2.getString("message").equals("succeed"))
                {
                    logged = true;
                    if(context instanceof LoginActivity)
                    {
                        //((LoginActivity) context).loginSucceed();
                        VisorController.getInstance().loginSucceed();
                    }
                }
                else
                {
                    //autenticacion fallida
                    try
                    {
                        //JSONObject jsonObject= new JSONObject(message);
                        Log.e("autenticacion fallida", jsonObject2.getString("message"));
                        //mensaje(jsonObject2.getString("message"));
                        ((LoginActivity) context).loginFail(jsonObject2.getString("message"));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            //"getServers: server:ip, port, path; server:ip, port, path"
            //"getServers: server:10.54.13.110, 8802, ; server:10.54.13.110, 8802, ;"
            else if(function.equals("getServers"))
            {
                VisorController.getInstance().addServers(jsonObject);
            }
            //"getCameras: camera:ip, zoneId, password, user, port, id, url; ip, zoneId, password, user, port, id, url;"
            //"getCameras: camera:10.54.13.110, 3, password, user, 1234, 0, url;
            //             camera:10.54.13.110, 1, password, user, 1234, 0, url;
            //             camera:10.54.13.110, 0, password, user, 1234, 0, url;
            //             camera:10.54.13.110, 2, password, user, 1234, 0, url;
            //             camera:10.54.13.110, 3, password, user, 1234, 0, url;
            //             camera:10.54.13.110, 3, password, user, 1234, 0, url;"
            else if(function.equals("getCameras"))
            {
                addTreeElement(jsonObject,"getCameras");
                //addCameras(message);
            }
            //"getZones: zone: zoneId, name, parentZone, description; zone: zoneId, name, parentZone, description;"
            //"getZones: zone: 0, Zona1, -1, description de la zona1;
            //           zone: 1, Zona2, 0, description de la zona2;
            //           zone: 2, Zona3, -1, description de la zona3;
            //           zone: 3, Zona4, 0, description de la zona4;"
            else if(function.equals("getZones"))
            {
                addTreeElement(jsonObject,"getZones");
                //addZones(message);
            }
            else if(function.equals("getSensors"))
            {
                try
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.i("jsonArray....", jsonArray.toString());
                    VisorController.getInstance().getSensors().clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObjectI = jsonArray.getJSONObject(i);
                        Log.i("jsonObjectI....", jsonObjectI.toString());
                        VisorController.getInstance().getSensors().add(new Sensor(jsonObjectI));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            //getEvents
            else if(function.equals("getEvents"))
            {
                try
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.i("jsonArray....", jsonArray.toString());
                    VisorController.getInstance().getEvents().clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObjectI = jsonArray.getJSONObject(i);
                        Log.i("jsonObjectI....", jsonObjectI.toString());
                        VisorController.getInstance().getEvents().add(new Event(jsonObjectI));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            /*
            else if(message.contains("getURLCamerasByIds"))
            {
                JSONObject json = new JSONObject(message);
                JSONArray jsonArray = (JSONArray)json.get("getURLCamerasByIds");
                VisorController.getInstance().setURLsToCameras(jsonArray);
            }*/
            else if(function.equals("updateTree"))
            {
                excutefunction("getZones", null);
                excutefunction("getCameras", null);
            }
            else if(function.equals("startedRecord"))
            {
                VisorController.getInstance().startedRecord(jsonObject);
            }
            else if(function.equals("stoppedRecord"))
            {
                VisorController.getInstance().stoppedRecord(jsonObject);
            }
            else if(function.equals("noStoppedRecord"))
            {
                Toast.makeText(context, "No tiene permisos para detener la grabación.", Toast.LENGTH_SHORT).show();
            }
            else if(function.equals("noStartedRecord"))
            {
                Toast.makeText(context, "No tiene permisos de grabación sobre la cámara seleccionada.", Toast.LENGTH_SHORT).show();
            }
            else if(function.equals("activedSensor"))
            {
                JSONObject jsonSensor = jsonObject.getJSONObject("data");
                String idCamera = jsonSensor.getString("idCamera");
                String idSensor = jsonSensor.getString("idSensor");
                VisorController.getInstance().activedSensor(idCamera, idSensor);
            }
            else if(function.equals("deactivedSensor"))
            {
                JSONObject jsonSensor = jsonObject.getJSONObject("data");
                String idCamera = jsonSensor.getString("idCamera");
                String idSensor = jsonSensor.getString("idSensor");
                VisorController.getInstance().deactivedSensor(idCamera, idSensor);
            }
            else if(function.equals("cameraLocked"))
            {
                VisorController.getInstance().cameraLocked(jsonObject);
            }

            else if(function.equals("noCameraLocked")){
                Toast.makeText(context, "Usted no tiene permiso para bloquear la cámara.", Toast.LENGTH_SHORT).show();
            }
            else if(function.equals("nadaCameraUnLocked")){}

            else if(function.equals("cameraUnLocked"))
            {
                VisorController.getInstance().cameraUnLocked(jsonObject);
            }
            else if(function.equals("noCameraUnLocked"))
            {
                Toast.makeText(context, "Usted no tiene permiso para desbloquear la cámara.", Toast.LENGTH_SHORT).show();
            }
            else if(function.equals("nadaCameraUnLocked")){}

            else if(function.equals("changedCameraStatus"))
            {
                try
                {
                    JSONObject jsonObject2= jsonObject.getJSONObject("data");
                    String idCamera = jsonObject2.getString("idCamera");
                    String status = jsonObject2.getString("status");
                    VisorController.getInstance().changedCameraStatus(idCamera, status);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if(function.equals("getCameraStatus"))
            {
                try
                {
                    JSONObject jsonObject2= jsonObject.getJSONObject("data");
                    String idCamera = jsonObject2.getString("idCamera");
                    String status = jsonObject2.getString("status");
                    VisorController.getInstance().changedCameraStatus(idCamera, status);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if(function.equals("showAlarm"))
            {
                try
                {   //{"showAlarm":{"idCamera":"4","idSensor":"2"}}
                    JSONObject jsonObject2= jsonObject.getJSONObject("data");
                    String idCamera = jsonObject2.getString("idCamera");
                    String idSensor = jsonObject2.getString("idSensor");
                    VisorController.getInstance().showAlarm(idCamera, idSensor);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if(function.equals("change"))
            {
                try
                {   //{"showAlarm":{"idCamera":"4","idSensor":"2"}}
                    JSONObject jsonObjectChange = jsonObject.getJSONObject("data");
                    final String nameEntity = jsonObjectChange.getString("nameEntity");
                    final LinkedList<Camera> cameras = new LinkedList<>();

                    //Para actualizar los cambio en el area de visualizacion
                    if ((context instanceof ViewOneCameraActivity) || (context instanceof ViewTwoCameraActivity) || (context instanceof ViewThreeCameraActivity) || (context instanceof ViewFourCameraActivity) || (context instanceof ViewFiveCameraActivity) || (context instanceof ViewSixCameraActivity))
                    {
                        for (int i = 0; i < VisorController.getInstance().getCustomViews().getFirst().getIdsCameras().size(); i++)
                        {
                            cameras.add(VisorController.getInstance().getCameraById(VisorController.getInstance().getCustomViews().getFirst().getIdsCameras().get(i)));
                        }

                    }

                    //Para actualizar el arbol de zonas y camaras
                    treeElements = new LinkedList<>();
                    countCameras = 0;
                    countZones = 0;
                    cant = 0;
                    tag = true;
                    VisorController.getInstance().prepareTree();
                    threadUpdate = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            VisorController.getInstance().updateChange(nameEntity, cameras);
                        }
                    });

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            //{"getStreaming":{"idCamera":"3","urlStreaming":"rtsp://visor:AEByqFNH1Eg=@10.54.13.21:3237/ComplejoC1_Cam_1"}}
            else if(function.equals("getStreaming"))
            {
                try
                {
                    JSONObject jsonObject2= jsonObject.getJSONObject("data");
                    String idCamera = jsonObject2.getString("idCamera");
                    String urlStreaming = jsonObject2.getString("urlStreaming");
                    VisorController.getInstance().setStreaming(idCamera,urlStreaming);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if(function.equals("getCurrentTime"))
            {
                try
                {   //{"getCurrentTime":{"idCamera":"4","currentTime":"2546"}}
                    JSONObject jsonObject2= jsonObject.getJSONObject("data");
                    String idCamera = jsonObject2.getString("idCamera");
                    String currentTime = jsonObject2.getString("currentTime");
                    VisorController.getInstance().showDialogMark(idCamera,currentTime);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if(function.equals("getActivedSensorsByIdCamera"))
            {
                try
                {   //{"getActivedSensorsByIdCamera":{"idCamera":"4","currentTime":"2546"}}
                    JSONObject jsonObject2= jsonObject.getJSONObject("data");
                    String idCamera = jsonObject2.getString("idCamera");
                    JSONArray jsonArray = jsonObject.getJSONArray("sensors");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String idSensor = jsonArray.getString(i);
                        VisorController.getInstance().activedSensor(idCamera, idSensor);
                    }
                    //VisorController.getInstance().showDialogMark(idCamera,currentTime);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if(function.equals("showMessage"))
            {
                try
                {   //{"showMessage":"Mensaje"}
                    //JSONObject jsonObject2= jsonObject.getJSONObject("showMessage");
                    VisorController.getInstance().showMessage(jsonObject.getString("data"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    Thread threadUpdate;

    public void lockCamera(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("lockCamera", parameters);
        }
        else
        {
            connectWebSocket("lockCamera", parameters);
        }
    }

    /*public Zone getZoneById(String idZone)
    {
        for (int i = 0; i < treeElements.size(); i++)
        {
            if (treeElements.get(i) instanceof Zone && treeElements.get(i).getId().equals(idZone))
            {
                return (Zone)treeElements.get(i);
            }
        }
        return null;
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
    }*/

    public int getPosCameraById(String idCamera)
    {
        for (int i = 0; i < treeElements.size(); i++)
        {
            if (treeElements.get(i) instanceof Camera && treeElements.get(i).getId().equals(idCamera))
            {
                return i;
            }
        }
        return -1;
    }

    public void generateTreeTester()
    {
        /*String ip, String user, String password, String model, String spefic_info, Boolean active,
            String url, boolean recoding, String name*/
       /* treeElements.add(new Camera("0", "1" ,"10.1.2.3", "user", "pass", "model", "spefic_info", true,
                                    "http://minternos.uci.cu/naruto_481.webm", false, "naruto_481"));*/
        treeElements.add(new Camera("0", "1" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "http://192.168.137.1/1.webm","Entérate Primero", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Camera("1", "0" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "http://minternos.uci.cu/naruto_480.webm","naruto_480", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Camera("2", "3" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "http://minternos.uci.cu/naruto_479.webm", "naruto_479", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Camera("3", "1" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "http://minternos.uci.cu/naruto_478.webm", "naruto_478", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Camera("4", "3" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "http://minternos.uci.cu/naruto_477.webm",  "naruto_477", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Camera("5", "3" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "http://minternos.uci.cu/naruto_476.webm",  "naruto_476", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Camera("6", "2" ,"10.1.2.3", "user", "pass", "model", "spefic_info",
                "mms://ucimedia.uci.cu/e7dd297dch6",  "Canal 6", new LinkedList<Status>()));
        countCameras++;
        treeElements.add(new Zone("2","0","Zona 3", "descripción"));
        countZones++;
        treeElements.add(new Zone("3","-1","Zona 4", "descripción"));
        countZones++;
        treeElements.add(new Zone("0","1","Zona 1", "descripción"));
        countZones++;
        treeElements.add(new Zone("1","-1","Zona 2", "descripción"));
        countZones++;
        VisorController.getInstance().buildTree();
    }

    int cant = 0;
    boolean tag = false;

    public void addTreeElement(JSONObject jsonObject, String elementType)
    {
        // Log.i("addTreeElement....", message);
        Log.i("elementType....", elementType);
        try
        {
            //JSONObject jsonObject= new JSONObject(message);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Log.i("jsonArray....", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObjectI = jsonArray.getJSONObject(i);
                Log.i("jsonObjectI....", jsonObjectI.toString());
                if(elementType.equals("getCameras"))
                {
                    treeElements.add(new Camera(jsonObjectI));
                    countCameras++;
                }
                else if(elementType.equals("getZones"))
                {
                    treeElements.add(new Zone(jsonObjectI));
                    countZones++;
                }
            }
            if (countCameras>0)
            {
                readyCameras = true;
                VisorController.getInstance().newCameras();
            }
            if (countZones>0)
            {
                readyZones = true;
                VisorController.getInstance().newZones();
            }

            cant++;
            if (cant==2 && tag){
                threadUpdate.run();
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isReadyCameras()
    {
        return readyCameras;
    }

    public boolean isReadyZones()
    {
        return readyZones;
    }

    public void getCurrentTime(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("getCurrentTime", parameters);
        }
        else
        {
            connectWebSocket("getCurrentTime", parameters);
        }
    }

    public void getActivedSensorsByIdCamera(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("getActivedSensorsByIdCamera", parameters);
        }
        else
        {
            connectWebSocket("getActivedSensorsByIdCamera", parameters);
        }
    }

    public void setMark(String idCamera, String nameMark, String detailsMark, String idEvent, String time)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        parameters.add(time);
        parameters.add(nameMark);
        parameters.add(detailsMark);
        parameters.add(idEvent);
        if(serverWSConnected)
        {
            excutefunction("setMark", parameters);
        }
        else
        {
            connectWebSocket("setMark", parameters);
        }
    }

    public void startRecord(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("startRecord", parameters);
        }
        else
        {
            connectWebSocket("startRecord", parameters);
        }
    }

    public void stopRecord(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("stopRecord", parameters);
        }
        else
        {
            connectWebSocket("stopRecord", parameters);
        }
    }

    public void getStreaming(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("getStreaming", parameters);
        }
        else
        {
            connectWebSocket("getStreaming", parameters);
        }
    }

    private void excutefunction(String function, LinkedList<String> parameters)
    {
        try
        {
            JSONObject jsonObject= new JSONObject();
            if (function.equals("login"))
            {
                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.put("user", user);
                jsonObject0.put("password", password);

                jsonObject.put("data", jsonObject0);
                jsonObject.put("function", "login");
                Log.e("jsonObject1", jsonObject.toString());
                webSocketClient.send(jsonObject.toString());
            }
            else if (function.equals("getServers"))
            {
                jsonObject.put("function", "getServers");
                webSocketClient.send(jsonObject.toString());
                //webSocketClient.send("getServers:");
            }
            else if (function.equals("getCameras"))
            {
                jsonObject.put("function", "getCameras");
                webSocketClient.send(jsonObject.toString());
                //webSocketClient.send("getCameras:");
            }
            else if (function.equals("getZones"))
            {
                jsonObject.put("function", "getZones");
                webSocketClient.send(jsonObject.toString());
                //webSocketClient.send("getZones:");
            }
            else if (function.equals("getSensors"))
            {
                jsonObject.put("function", "getSensors");
                webSocketClient.send(jsonObject.toString());
                // webSocketClient.send("getSensors:");
            }
            else if (function.equals("getEvents"))
            {
                jsonObject.put("function", "getEvents");
                webSocketClient.send(jsonObject.toString());
                // webSocketClient.send("getSensors:");
            }
            else if (function.equals("startRecord"))
            {
                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.put("idCamera", parameters.getFirst());

                jsonObject.put("data", jsonObject0);
                jsonObject.put("function", "startRecord");
                webSocketClient.send(jsonObject.toString());
                //webSocketClient.send("startRecord:" + parameters.getFirst());
            }
            else if (function.equals("stopRecord"))
            {
                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.put("idCamera", parameters.getFirst());

                jsonObject.put("data", jsonObject0);
                jsonObject.put("function", "stopRecord");
                webSocketClient.send(jsonObject.toString());
                //webSocketClient.send("stopRecord:" + parameters.getFirst());
            }
            else if (function.equals("getCameraStatus"))
            {
                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.put("idCamera", parameters.getFirst());

                jsonObject.put("data", jsonObject0);
                jsonObject.put("function", "getCameraStatus");
                webSocketClient.send(jsonObject.toString());
                //webSocketClient.send("stopRecord:" + parameters.getFirst());
            }
            else if (function.equals("setMark"))
            {
                try
                {
                    JSONObject jsonObject0 = new JSONObject();

                    jsonObject0.put("idCamera", parameters.get(0));
                    jsonObject0.put("time", parameters.get(1));
                    jsonObject0.put("name", parameters.get(2));
                    jsonObject0.put("details", parameters.get(3));
                    jsonObject0.put("idEvent", parameters.get(4));

                    jsonObject.put("data", jsonObject0);
                    jsonObject.put("function", "setMark");
                    webSocketClient.send(jsonObject.toString());
                } catch (Exception e)
                {
                    e.getStackTrace();
                }
            }
            else if (function.equals("setActiveSensor"))
            {
                try
                {
                    JSONObject jsonObject0 = new JSONObject();

                    jsonObject0.put("idCamera", parameters.get(0));
                    jsonObject0.put("idSensor", parameters.get(1));
                    jsonObject0.put("active", parameters.get(2));

                    jsonObject.put("data", jsonObject0);
                    jsonObject.put("function", "setActiveSensor");
                    webSocketClient.send(jsonObject.toString());
                } catch (Exception e)
                {
                    e.getStackTrace();
                }
            }
            else if (function.equals("lockCamera"))
            {
                try
                {
                    JSONObject jsonObject0 = new JSONObject();

                    jsonObject0.put("idCamera", parameters.get(0));

                    jsonObject.put("data", jsonObject0);
                    jsonObject.put("function", "lockCamera");
                    webSocketClient.send(jsonObject.toString());
                }
                catch (Exception e)
                {
                    e.getStackTrace();
                }
            }
            else if (function.equals("getStreaming"))
            {
                //{"getStreaming":{"idCamera":"1"}}
                try
                {
                    JSONObject jsonObject0 = new JSONObject();

                    jsonObject0.put("idCamera", parameters.get(0));

                    jsonObject.put("data", jsonObject0);
                    jsonObject.put("function", "getStreaming");
                    webSocketClient.send(jsonObject.toString());
                } catch (Exception e)
                {
                    e.getStackTrace();
                }
            }
            else if (function.equals("getCurrentTime"))
            {
                //{"getCurrentTime":{"idCamera":"1"}}
                try
                {
                    JSONObject jsonObject0 = new JSONObject();

                    jsonObject0.put("idCamera", parameters.get(0));

                    jsonObject.put("data", jsonObject0);
                    jsonObject.put("function", "getCurrentTime");
                    webSocketClient.send(jsonObject.toString());
                } catch (Exception e)
                {
                    e.getStackTrace();
                }
            }
            else if (function.equals("getActivedSensorsByIdCamera"))
            {
                //{"getActivedSensorsByIdCamera":{"idCamera":"1"}}
                try
                {
                    JSONObject jsonObject0 = new JSONObject();
                    jsonObject0.put("idCamera", parameters.get(0));
                    jsonObject.put("data", jsonObject0);
                    jsonObject.put("function", "getActivedSensorsByIdCamera");
                    webSocketClient.send(jsonObject.toString());
                } catch (Exception e)
                {
                    e.getStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    public void getServers()
    {
        if(serverWSConnected)
        {
            excutefunction("getServers", null);
        }
        else
        {
            connectWebSocket("getServers", null);
        }
    }

    public void getZones()
    {
        if(serverWSConnected)
        {
            excutefunction("getZones", null);
        }
        else
        {
            connectWebSocket("getZones", null);
        }
    }

    public void getCameras()
    {
        if(serverWSConnected)
        {
            excutefunction("getCameras", null);
        }
        else
        {
            connectWebSocket("getCameras", null);
        }
    }

    public void getCameraStatus(String idCamera)
    {
        LinkedList<String> parameters = new LinkedList<>();
        parameters.addFirst(idCamera);
        if(serverWSConnected)
        {
            excutefunction("getCameraStatus", parameters);
        }
        else
        {
            connectWebSocket("getCameraStatus", parameters);
        }
    }

    public void getSensors()
    {
        if(serverWSConnected)
        {
            excutefunction("getSensors", null);
        }
        else
        {
            connectWebSocket("getSensors", null);
        }
    }

    public void getEvents()
    {
        if(serverWSConnected)
        {
            excutefunction("getEvents", null);
        }
        else
        {
            connectWebSocket("getEvents", null);
        }
    }

}
