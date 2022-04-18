package uci.suriavisor.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xilema.suriavisor.R;

import uci.suriavisor.logic.VisorController;

public class ConfigConectionActivity extends Activity {
	VisorController visorController;

	public ConfigConectionActivity()
	{
		super();
	}

/*
	// Método que retorna la llamada al servicio Service_Test_Conexion
	public String testConexion(String ip, String puerto, String ruta)
	{
		return service.serviceTestConexion(ip, puerto, ruta);
	}*/

	// Método que muestra un diálogo para configurar la conexión
	public void configurarConexion(final Context context, SharedPreferences setting)
	{
		final SharedPreferences.Editor editor = setting.edit();
		LayoutInflater cfg = LayoutInflater.from(context);
		View v_cfg = cfg.inflate(R.layout.activity_cfg_gestor, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final EditText eip = (EditText) v_cfg.findViewById(R.id.gestor_ip);
		final EditText epuerto = (EditText) v_cfg.findViewById(R.id.gestor_puerto);
		final EditText eruta = (EditText) v_cfg.findViewById(R.id.gestor_ruta);
		if(!setting.getString("dir_ip", "").equals(""))
			eip.setText(setting.getString("dir_ip", ""));
		if(!setting.getString("puerto", "").equals(""))
			epuerto.setText(setting.getString("puerto", ""));
		if(!setting.getString("ruta", "").equals(""))
			eruta.setText(setting.getString("ruta", ""));


		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				});

		builder.setPositiveButton("Guardar",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});
		/*builder.setNeutralButton("Probar",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				});*/


		final AlertDialog dialogo = builder.create();
		dialogo.setView(v_cfg);
		dialogo.setTitle("Configurar conexión");
		dialogo.setIcon(R.drawable.ic_cfg_cnx);
		dialogo.show();

		// Acción del Botón Salir del Diálogo
		dialogo.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						dialogo.cancel();
					}
				});

		// Acción del Botón Guardar y Salir del Diálogo
		dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						String ip = eip.getText().toString();
						String puerto = epuerto.getText().toString();
						String ruta = eruta.getText().toString();
						if (ip.equals(""))
						{
							eip.setError("Campo obligatorio");
						} else if (puerto.equals("")){
							epuerto.setError("Campo obligatorio");
						}else if (ruta.equals("")){
							eruta.setError("Campo obligatorio");
						}
						else
						{
						editor.putString("dir_ip", ip);
						editor.putString("puerto", puerto);
						editor.putString("ruta", ruta);
						editor.commit();
						dialogo.cancel();
						}
					}
				});
	}
}
