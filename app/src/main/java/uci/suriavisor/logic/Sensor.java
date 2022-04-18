package uci.suriavisor.logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Miguel on 26/10/2016.
 */
public class Sensor implements Serializable
{
    public Sensor(String id, boolean activated)
    {
        this.id = id;
        this.activated = activated;
    }

    public Sensor(JSONObject jsonObject)
    {
        try
        {
            this.id = jsonObject.getString("id");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    private String id;

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    private boolean activated = false;
}
