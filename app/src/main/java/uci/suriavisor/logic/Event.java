package uci.suriavisor.logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Miguel on 22/02/2017.
 */

public class Event implements Serializable
{
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    private String id;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private String name;

    public Event(JSONObject jsonObject)
    {
        try
        {
            this.id = jsonObject.getString("Id");
            this.name = jsonObject.getString("name");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
