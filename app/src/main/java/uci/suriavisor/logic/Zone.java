package uci.suriavisor.logic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Miguel on 04/04/2016.
 */
public class Zone extends TreeElement
{
    public Zone(String id, String parent_id, String name, String description)
    {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.description = description;
    }

    public Zone(JSONObject jsonObjectServer)
    {
        try
        {
            this.id = jsonObjectServer.getString("Id");
            this.name = jsonObjectServer.getString("name");
            this.parent_id = jsonObjectServer.getString("parentZoneId");
            this.description = jsonObjectServer.getString("description");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
