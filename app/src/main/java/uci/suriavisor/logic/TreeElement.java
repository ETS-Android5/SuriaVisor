package uci.suriavisor.logic;


import java.io.Serializable;

/**
 * Created by Miguel on 10/05/2016.
 */
public class TreeElement implements Serializable
{
    String id, name, parent_id, description;

    public String getId()
    {
        return id;
    }

    public void setId(String zoneId)
    {
        this.id = zoneId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getParentZoneId()
    {
        return parent_id;
    }

    public void setParentZoneId(String zoneId)
    {
        this.parent_id = zoneId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
