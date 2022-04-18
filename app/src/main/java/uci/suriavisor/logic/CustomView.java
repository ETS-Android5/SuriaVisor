package uci.suriavisor.logic;

import android.util.Log;

import com.xilema.suriavisor.R;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Miguel on 25/05/2016.
 */
public class CustomView implements Serializable
{
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String name;
    String id;
    private int photoId;

    public String getId(){
        return id;
    }
    public LinkedList<String> getIdsCameras()
    {
        return idsCameras;
    }

    private LinkedList<String> idsCameras;

    public void addCamera(String idC)
    {
        idsCameras.add(idC);
    }

    public void setCantAreas(int cantAreas)
    {
        this.cantAreas = cantAreas;
        if (cantAreas == 1)
        {
            this.photoId = R.drawable.view1;
        }
        else if(cantAreas == 2)
        {
            this.photoId = R.drawable.view2;
        }
        else if(cantAreas == 3)
        {
            this.photoId = R.drawable.view3;
        }
        else if(cantAreas == 4)
        {
            this.photoId = R.drawable.view4;
        }
        else if(cantAreas == 5)
        {
            this.photoId = R.drawable.view5;
        }
        else if(cantAreas == 6)
        {
            this.photoId = R.drawable.view6;
        }
    }

    int cantAreas;

    public boolean isDefaultView()
    {
        return defaultView;
    }

    public void setDefaultView(boolean defaultView)
    {
        this.defaultView = defaultView;
    }

    boolean defaultView;

    public CustomView(String name, String id, int cantAreas)
    {
        this.name = name;
        this.id = id;
        this.setCantAreas(cantAreas);
        this.setIdsCameras(cantAreas);
    }

    public int getCantAreas()
    {
        return cantAreas;
    }

    public int getPhotoId()
    {
        return photoId;
    }

    public void setIdsCameras (int cantAreas){
        idsCameras = new LinkedList<>();
        for (int i = 0; i < cantAreas; i++)
        {
            idsCameras.add("-1");
        }
    }
}
