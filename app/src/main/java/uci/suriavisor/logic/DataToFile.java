package uci.suriavisor.logic;

import android.graphics.Bitmap;
import android.util.Log;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.util.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Miguel on 6/4/2016.
 */
public class DataToFile
{

    private VisorController controller = VisorController.getInstance();

    public Preferences readPreferences()
    {
        ObjectInputStream input;
        String filename = "cachedPreferences.srl";

        try {
            input = new ObjectInputStream(new FileInputStream(new File(new File(controller.getContext().getFilesDir(),"")+File.separator+filename)));
            Preferences object = (Preferences) input.readObject();
            input.close();
            Log.e("Preferencias", "leidas");
            return object;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //public void writePreferences(Activity activity)
    public void writePreferences()
    {
        String filename = "cachedPreferences.srl";
        ObjectOutput out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(new File(controller.getContext().getFilesDir(),"")+File.separator+filename));
            out.writeObject(controller.getPreferences());
            out.close();
            Log.e("Preferencias", "guardadas");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveSnapshotInFile(String fileName, Bitmap pictureBitmap)
    {
        try
        {
            File directory = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));
            if(directory.exists() || directory.mkdirs())
            {
                File file = new File(fileName); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                OutputStream fOut = new FileOutputStream(file);
                pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
