package uci.suriavisor.logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Miguel on 07/03/2016.
 */
public class Camera extends TreeElement
{
    private String ip;
    private String user;
    private String password;
    private String model;
    private String spefic_info;
    private String protocol;
    private String endpoint;
    private String port;
    private LinkedList<Status> status;

    public Camera(){}

    public void changeStatus(String toString)
    {
        status = new LinkedList<>();
        if(toString.contains("UNAVAILABLE"))
            status.add(Status.UNAVAILABLE);
        else if(toString.contains("AVAILABLE"))
            status.add(Status.AVAILABLE);
        if(toString.contains("RECORDING"))
            status.add(Status.RECORDING);
        if(toString.contains("STREAMING"))
            status.add(Status.STREAMING);
        if(toString.contains("LOCKED"))
            status.add(Status.LOCKED);
    }

    public boolean isLock()
    {
        return status.contains(Status.LOCKED);
    }

    public void setLock(boolean lock)
    {
        if(lock)
        {
            if (!status.contains(Status.LOCKED))
                status.add(Status.LOCKED);
        }
        else
            status.remove(Status.LOCKED);
    }
    /*

    private boolean lock = false;*/

    //private boolean active;

    public LinkedList<String> getIdsSensorActivated()
    {
        return idsSensorActivated;
    }

    private LinkedList<String> idsSensorActivated = new LinkedList<>();
/*
    public void setActiveSensorById(String idSensor, Boolean active)
    {
        for (int i = 0; i < sensors.size(); i++)
        {
            if (sensors.get(i).getId().equals(idSensor))
                sensors.get(i).setActivated(active);
        }
    }*/

    public void setActiveSensorById(String idSensor, Boolean active)
    {
        if(idsSensorActivated.contains(idSensor) && !active)
            idsSensorActivated.remove(idSensor);
        else if (!idsSensorActivated.contains(idSensor) && active)
            idsSensorActivated.add(idSensor);
    }

    public void clearActivedSensor()
    {
        /*
        for (int i = 0; i < sensors.size(); i++)
        {
            sensors.get(i).setActivated(false);
        }*/
        idsSensorActivated = new LinkedList<>();
    }

    public boolean isRecoding()
    {
        return status.contains(Status.RECORDING);
    }

    public boolean isStreaming()
    {
        return status.contains(Status.STREAMING);
    }

    public void setRecoding(boolean recoding)
    {
        if(recoding)
        {
            if (!status.contains(Status.RECORDING))
                status.add(Status.RECORDING);
        }
        else
            status.remove(Status.RECORDING);
    }

    public boolean isActive()
    {
        return !status.contains(Status.UNAVAILABLE);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    private String url;

    public Camera(String id, String zone_id, String ip, String user, String password, String model, String spefic_info,
                  String url, String name, LinkedList<Status> status)
    {
        this.id = id;
        this.parent_id = zone_id;
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.model = model;
        this.spefic_info = spefic_info;
        this.url = url;
        this.name = name;
        this.status = status;
    }

    public Camera(JSONObject jsonObjectCamera)
    {
        try
        {
            this.id = jsonObjectCamera.getString("Id");
            this.parent_id = jsonObjectCamera.getString("zoneId");
            this.name = jsonObjectCamera.getString("name");
            this.description = jsonObjectCamera.getString("description");
            //this.model = jsonObjectCamera.getString("model");
            //this.spefic_info = jsonObjectCamera.getString("spefic_info");
            this.endpoint = jsonObjectCamera.getString("endpoint");
            this.protocol = jsonObjectCamera.getString("protocol");
            this.password = jsonObjectCamera.getString("password");
            this.user = jsonObjectCamera.getString("userName");
            this.ip = jsonObjectCamera.getString("ip");
            this.port = jsonObjectCamera.getString("port");
            //this.status = new Status(jsonObjectCamera.getString("status"));
            this.status = new LinkedList<>();
            changeStatus(jsonObjectCamera.getString("status"));
            //"rtsp://admin:admin@10.54.13.251:49276/test"
            this.url = protocol+"://"+user+":"+password+"@"+ip+":"+port+"/"+endpoint;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
}
