package uci.suriavisor.presentation.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.xilema.suriavisor.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Constants
{
	public static String IS_HTTP ="is_http";
	public static String PLAY = "play_inside";
	public static String CUALITY = "cuality";
	public static String SCREEN_MODE="screen_mode";
	public static String SECTION_COUNT="section_count";
	public static String SECTION_ID="section_id";
	public static String FIRST_TIME="first_time";
	public static String LAST_POSITION="last_position";
	public static String SHOW_NOTIFICATION="show_notification";
	public static final int ERROR =-1;
	public static final int SUCCES =0;
	public static final int MORE_ELEMNTS =1;
	public static final int NOT_MORE_ELEMNTS =2;
	public static String  green_grass="#009688";
	public static String light_grey ="#eeeeee";
	public static String grey ="#424242";
	public static String blue ="#01579b";
	public static String green = "#2e7d32";
	public static String yellow ="#f57f17";
	public static String dark ="#212121";

	public static final int COLOR_LIGHT=1;
	public static final int COLOR_DARK=2;
	public static String THEME = "theme_color";
	public static boolean changeTheme = false;
	public static int THEME_ID = R.style.AppTheme;
	public static String PATH_TO_SAVE = Environment.getExternalStorageDirectory().getPath()+ File.separator+"Suria"+ File.separator+"Snapshots";
	public static String EXTENSION_OF_SNAPSHOT = "png";
	public static int NUMBER_OF_SNAPSHOTS = 10;
	public static final int INTERNAL=1;
	public static final int EXTERNAL=2;
	public static List<String> arrayItmsDrawer()
	{
		List<String> arrayItms = new LinkedList<>();
		arrayItms.add("Vistas");
		arrayItms.add("Árbol de Cámaras");
		arrayItms.add("Búsqueda");
		arrayItms.add("Configuración");
		arrayItms.add("Salir");
		return arrayItms;
	}

	public static List<Integer> arrayItmsDrawerIcon()
	{
		List<Integer> arrayItmsLigth = new LinkedList<>();
		arrayItmsLigth.add(R.drawable.ic_views_black);
		arrayItmsLigth.add(R.drawable.ic_tree_black);
		arrayItmsLigth.add(R.drawable.ic_search_black);
		arrayItmsLigth.add(R.drawable.ic_settings_black);
		arrayItmsLigth.add(R.drawable.ic_exit_app_black);
		List<Integer> arrayItmsDark = new LinkedList<>();
		arrayItmsDark.add(R.drawable.ic_views_white);
		arrayItmsDark.add(R.drawable.ic_tree_white);
		arrayItmsDark.add(R.drawable.ic_search_white);
		arrayItmsDark.add(R.drawable.ic_settings_white);
		arrayItmsDark.add(R.drawable.ic_exit_app_white);
		if(THEME_ID == R.style.AppTheme)
			return arrayItmsLigth;
		return arrayItmsDark;
	}

	public static final class CONFIG {
		public static final boolean DEBUG = false;
	}
}