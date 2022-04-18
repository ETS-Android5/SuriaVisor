package uci.suriavisor.presentation.util;

import android.util.Log;

import java.io.File;

/**
 * Created by Orlando on 5/31/2016.
 */
public class SettingsUtil {


    public static String sd1path,sd0path;

    public static void generatePath(){


        if(new File("/storage/sdcard1/").exists())
        {
            sd1path="/storage/sdcard1/";
            Log.i("Sd Card1 Path",sd1path);
        }
        else if(new File("/storage/extSdCard/").exists())
        {
            sd1path="/storage/extSdCard/";
            Log.i("Sd Cardext Path",sd1path);
        }

        if(new File("/storage/emulated/legacy").exists())
        {
            sd0path="/storage/emulated/legacy";
            Log.i("Sd Card0 Path",sd0path);
        }
        else if(new File("/storage/sdcard0/").exists())
        {
            sd0path="/storage/sdcard0/";
            Log.i("Sd Card0 Path",sd0path);
        }
    }
}
