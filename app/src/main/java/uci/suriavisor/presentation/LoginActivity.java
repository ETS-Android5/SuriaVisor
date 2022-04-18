package uci.suriavisor.presentation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import uci.suriavisor.logic.VisorController;
import uci.suriavisor.presentation.materialdialogs.MaterialDialog;
import com.xilema.suriavisor.R;

public class LoginActivity extends AppCompatActivity
{
	private ImageView app_name, xilema_suria;
	private Button button_Login;
	private EditText user, pass;
	//private LoginController loginController = new LoginController();
	private VisorController visorController;
	private ConfigConectionActivity configConectionActivity = new ConfigConectionActivity();
	private String url;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		app_name = (ImageView) findViewById(R.id.ic_gris);
		xilema_suria = (ImageView) findViewById(R.id.imageView2);
		button_Login = (Button) findViewById(R.id.login);
		user = (EditText) findViewById(R.id.gestor_ip);
		pass = (EditText) findViewById(R.id.editText2);
		ActionBar actionBar = getActionBar();
		if(actionBar!=null)
		{
			actionBar.setTitle(com.xilema.suriavisor.R.string.app_name);
			actionBar.setSubtitle("Login");
		}
		presentacion();

		visorController = VisorController.getInstance();
		visorController.changeContext(LoginActivity.this);

		//Acción del Botón Login
		button_Login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//code jose
				SharedPreferences setting = getSharedPreferences("datos_cfg_cnx_gestor", Context.MODE_PRIVATE);
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

				if (actNetInfo == null){
					Toast.makeText(getApplicationContext(), "No está conectado el dispositivo a ninguna red Wi-Fi.", Toast.LENGTH_SHORT).show();
				}else if (setting.getString("dir_ip", "").equals("")){
					Toast.makeText(getApplicationContext(), "No tiene ningún parámetro de Configuración de Conexión guardado.", Toast.LENGTH_SHORT).show();
					configConectionActivity.configurarConexion(LoginActivity.this, setting);
				}else
				{

					if (user.getText().toString().equals(""))
					{
						//Toast.makeText(getApplicationContext(), "Verifique que no quede ningún campo vacío.", Toast.LENGTH_SHORT).show();
						user.setError("Campo obligatorio");
					} else if (pass.getText().toString().equals(""))
					{
						pass.setError("Campo obligatorio");
					} else
					{
						//final SharedPreferences setting = getSharedPreferences("datos_cfg_cnx_gestor", Context.MODE_PRIVATE);
						//String a = loginController.login(user.getText().toString(), pass.getText().toString(), setting);
				/*volver a poner esto para que pinche el autenticar*/
						showIndeterminateProgressDialog(false);
						visorController.addServerMaster(user.getText().toString(), pass.getText().toString(), LoginActivity.this);
						visorController.login();
						//quitar esto
				/*
				VisorController.getInstance().readViews();
				Intent i = new Intent(getBaseContext(), MainActivity.class);
				startActivity(i);
				finish();*/
					}

				}
			}
		});

	}

	@Override
	//Crear el Menú
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	//Acción de las opciones del Menú
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {

			//Opción Salir
			case R.id.item_salir:
				finish();
				return true;

			//Opción Configurar Conexión
			case R.id.item_cfg_cnx:
				final SharedPreferences setting = getSharedPreferences("datos_cfg_cnx_gestor", Context.MODE_PRIVATE);
				configConectionActivity.configurarConexion(LoginActivity.this, setting);
				return true;

		}
		return false;
	}

	//Método para la animación inicial de la aplicación
	private void presentacion()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						animar();
					}
				});
			}
		}).start();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(2500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mostrar();
					}
				});
			}
		}).start();

	}

	//Método que sube y baja las imágenes de suria y xilema respectivamente
	private void animar()
	{
		Animation subir = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.subir);
		Animation bajar = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.bajar);
		app_name.startAnimation(subir);
		xilema_suria.startAnimation(bajar);
	}

	//Método que muestra los campos User, Pass y el Botón Login 
	private void mostrar()
	{
		button_Login.setVisibility(View.VISIBLE);
		user.setVisibility(View.VISIBLE);
		pass.setVisibility(View.VISIBLE);
	}
	private MaterialDialog materialDialogProgressIndeterminate;
	public void loginFail(String msg)
	{
		if (materialDialogProgressIndeterminate!=null && materialDialogProgressIndeterminate.isShowing())
			materialDialogProgressIndeterminate.dismiss();
		//visorController.showMessage("No se ha podido establecer conexión con el servidor, inténtelo más tarde.");
		visorController.showMessage(msg);
	}

	/*public void loginSucceed()
	{
		visorController.readViews();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		if (materialDialogProgressIndeterminate!=null && materialDialogProgressIndeterminate.isShowing())
			materialDialogProgressIndeterminate.dismiss();
		finish();
	}*/

	public void showIndeterminateProgressDialog(boolean horizontal)
	{
		materialDialogProgressIndeterminate = new MaterialDialog.Builder(this)
				.title(R.string.progress_dialog)
				.content(R.string.please_wait)
				.progress(true, 0)
				.progressIndeterminateStyle(horizontal)
				.show();
	}

	//Método que muestra un mensaje en pantalla
	/*private void mensaje(String info)
	{
		Toast toast = Toast.makeText(this, info, Toast.LENGTH_LONG);
		toast.show();
	}*/
	@Override
	public void finish()
	{
		if (materialDialogProgressIndeterminate!=null && materialDialogProgressIndeterminate.isShowing())
			materialDialogProgressIndeterminate.dismiss();
		super.finish();
	}

}
