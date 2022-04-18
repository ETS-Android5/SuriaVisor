package uci.suriavisor.presentation.util;

import java.io.Serializable;

/**
 * Created by Miguel on 6/4/2016.
 */
public class Preferences implements Serializable
{
    private int themId;
    private String pathToSave;

    public Boolean isMuteAlarms()
    {
        return muteAlarms;
    }

    public void setMuteAlarms(Boolean muteAlarms)
    {
        this.muteAlarms = muteAlarms;
    }

    private Boolean muteAlarms;

    public int getNumberOfSnapshots()
    {
        return numberOfSnapshots;
    }

    public void setNumberOfSnapshots(int numberOfSnapshots)
    {
        this.numberOfSnapshots = numberOfSnapshots;
    }

    public Preferences(String pathToSave, int themId, int numberOfSnapshots, Boolean muteAlarms)
    {
        this.pathToSave = pathToSave;
        this.themId = themId;
        this.numberOfSnapshots = numberOfSnapshots;
        this.muteAlarms = muteAlarms;
    }

    private int numberOfSnapshots;

    public int getThemId()
    {
        return themId;
    }

    public void setThemId(int themId)
    {
        this.themId = themId;
    }

    public String getPathToSave()
    {
        return pathToSave;
    }

    public void setPathToSave(String pathToSave)
    {
        this.pathToSave = pathToSave;
    }
}
