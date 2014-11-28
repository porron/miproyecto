package com.mipaquete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import com.mipaquete.R;
import libreria.item;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends FragmentActivity {

	public static SectionsPagerAdapter mSectionsPagerAdapter;
	public static ViewPager mViewPager;
	public static android.support.v4.app.FragmentManager fragmentManager;

	public static plc miPlc;
	public static Context ctx;
	public static int indvariable;
	// public static int indalarma;
	public static int pagina;
	public static ListView listaVariables;
	// public static TextView texto;
	public static boolean modoComunicacion;
	public static Socket misocket;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	boolean clickvariable = false;
	static ActionMode mActionMode;
	public static boolean cliente_conectado = false;
	private static final int RESULT_SETTINGS = 1;
	public static String nombre_servidor = "";
	public static String pass_servidor = "";
	public static String dir_espejo = "";
	public static boolean comunicando = false;
	public static int width;
	public static int height;
	static boolean aplicacionInicializada = false;
	public static NotificationCompat.Builder mBuilder;
	public static ArrayList<LinearLayout> scrolls = new ArrayList<LinearLayout>();

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		aplicacionInicializada = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		aplicacionInicializada = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		aplicacionInicializada = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ctx = this;
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if (!aplicacionInicializada) {
			xml.crearDocumento();
			if (xml.leerXml(ctx)) {
				xml.parsearDocumento();
				alarma.apuntarAlarmas();
			}
		}

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		nombre_servidor = sharedPref.getString("NOMBRE_SERVIDOR", "");
		pass_servidor = sharedPref.getString("PASS_SERVIDOR", "");
		dir_espejo = sharedPref.getString("DIR_ESPEJO", "");
		// por si no hay nada en preferencias....
		if (nombre_servidor.equals(""))
			nombre_servidor = "miservidor";
		if (pass_servidor.equals(""))
			pass_servidor = "abcd";
		if (dir_espejo.equals(""))
			dir_espejo = "82.223.78.55";

		fragmentManager = getSupportFragmentManager();
		setContentView(R.layout.activity_main);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.getCurrentItem();

		scrolls.clear();
		Iterator<plc> it = servidor.plcs.iterator();
		while (it.hasNext()) {
			plc p = (plc) it.next();
			scrolls.add(dibuja.rellena(p));
		}

		if (!aplicacionInicializada) {
			int c = servidor.plcs.size();
			if (c == 0) {
				EditarPlc dialogo = new EditarPlc(false);
				dialogo.show(fragmentManager, "dialog");
			} else {
				it = servidor.plcs.iterator();
				while (it.hasNext()) {
					plc p = (plc) it.next();
					p.hilo_comunicacion = new comunicacion_asinc(p);
					p.hilo_comunicacion
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		}

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.getCurrentItem();

		// FragmentActivity android.support.v4.app.Fragment main_activity=
		// getActivity() ;

		// -------------------------------------------------------------------------------------------------------------
		// creamos notificacion de
		// alarmas--------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------------------

		Intent resultIntent = new Intent(this, panelAlarmas.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack
		stackBuilder.addParentStack(panelAlarmas.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

				
		mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.icono)
        .setContentTitle(getString(R.string.app_name))
        .setDefaults(Notification.DEFAULT_SOUND);
//        .setAutoCancel(true);
		mBuilder.setContentIntent(resultPendingIntent);
		

		// NotificationManager mNotificationManager =
		// (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationManager.notify(1, mBuilder.build());

		// int mId = 1;
		// NotificationManager mNotifyMgr = (NotificationManager)
		// getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotifyMgr.notify(mId, mBuilder.build());
		// --------------------------------------------------------------------------------------------------------------------

		if (!aplicacionInicializada) {
			pintor mipintor = (pintor) new pintor()
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

		// guardian miguardian = new guardian();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.activity_main, menu);
		// MenuItem i = menu.findItem(R.id.menu_editar);
		// i.setIcon(R.drawable.ic_action_name);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_editar:
			EditarPlc dialogo = new EditarPlc(true);
			dialogo.show(fragmentManager, "crear Plc");
			return true;
		case R.id.Modo_comunicacion:
			if (item.isChecked()) {
				modoComunicacion = false;
				item.setChecked(false);
			} else {
				modoComunicacion = true;
				item.setChecked(true);
			}
			PagerAdapter a = MainActivity.mViewPager.getAdapter();
			a.notifyDataSetChanged();
			return true;

		case R.id.Modo_local:
			// poner_password dialogo_pass = new poner_password();
			// dialogo_pass.show(getFragmentManager(), "Conectar el servidor");
			if (item.isChecked()) {
				comunicando = false;
				item.setChecked(false);
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast toast = Toast.makeText(MainActivity.ctx, "espejo "
						+ MainActivity.dir_espejo, Toast.LENGTH_SHORT);
				toast.show();
				comunicando = true;
				item.setChecked(true);
				sock comunicacion = new sock();
				comunicacion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			return true;
		case R.id.menu_settings:
			Intent i = new Intent(this, preferencias.class);
			startActivityForResult(i, RESULT_SETTINGS);
			return true;
		case R.id.item1:
			Intent inten = new Intent(this, panelAlarmas.class);
			startActivity(inten);
			return true;
		case R.id.menuvariable:
			editarVariables dia = new editarVariables(null,
					servidor.plcs.get(MainActivity.pagina));
			dia.show(getSupportFragmentManager(), "crear");

			return true;
		case R.id.menupanel:

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			Bundle args = new Bundle();
			Fragment fragment;

			miPlc = servidor.plcs.get(position);
			if (!modoComunicacion) {
				fragment = new DummySectionFragment();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
				fragment.setArguments(args);
			} else {
				fragment = new comunicacionFragment();
				args.putInt(comunicacionFragment.ARG_SECTION_NUMBER, position);
				fragment.setArguments(args);
			}

			return fragment;
		}

		public int getItemPosition(Object item) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return servidor.plcs.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			plc p = servidor.plcs.get(position);
			return p.nombre;
		}
	}

	/**
	 * pagina para mostrar variables alrmas etc
	 */

	public class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment newInstance(int sectionNumber) {
			DummySectionFragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public DummySectionFragment() {
			int a = 0;
			a = 2;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			plc mp = servidor.plcs.get(getArguments()
					.getInt(ARG_SECTION_NUMBER));
			View rootView = inflater.inflate(R.layout.fragment_item_detail,
					container, false);

			TextView textoip = (TextView) rootView
					.findViewById(R.id.thistorial);
			textoip.setFocusable(false);
			TextView textorefresco = (TextView) rootView
					.findViewById(R.id.textorefresco);
			Button botonAlarmas = (Button) rootView
					.findViewById(R.id.botonAlarmas);
			botonAlarmas.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					EditarAlarmas Crearalarma = new EditarAlarmas(null,
							servidor.plcs.get(MainActivity.pagina));
					Crearalarma.show(getSupportFragmentManager(), "editar");

				}
			});

			Button botonVariables = (Button) rootView
					.findViewById(R.id.botonVariables);
			botonVariables.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editarVariables dialogo = new editarVariables(null,
							servidor.plcs.get(MainActivity.pagina));
					dialogo.show(getSupportFragmentManager(), "crear");
				}
			});

			textorefresco.setFocusable(false);
			textoip.setText(mp.ip);
			textorefresco.setText(Integer.toString(mp.refresco));

			ListView listaVariables = (ListView) rootView
					.findViewById(R.id.listVariables);
			listaVariables.setScrollbarFadingEnabled(false);
			// listaVariables.setScrollBarSize(10);
			ListView listaAlarmas = (ListView) rootView
					.findViewById(R.id.listAlarmas);

			registerForContextMenu(listaVariables);
			registerForContextMenu(listaAlarmas);

			variableadapter va = new variableadapter(ctx, R.layout.variables2,
					mp.variables);
			listaVariables.setAdapter(va);
			listaVariables.setItemsCanFocus(true);
			/*
			 * listaVariables.setOnItemLongClickListener(new
			 * AdapterView.OnItemLongClickListener() { // Called when the user
			 * long-clicks on someView public boolean
			 * onItemLongClick(AdapterView parent, View view, int position, long
			 * id) { if (mActionMode != null) { return false; } indvariable =
			 * position; clickvariable=true; mActionMode =
			 * getActivity().startActionMode( mActionModeCallback);
			 * view.setSelected(true); return true; } });
			 */
			String[] items = alarma.rellenar_lista(mp);
			ArrayAdapter<String> alarmasAdapter = new ArrayAdapter<String>(ctx,
					android.R.layout.simple_list_item_1, items);
			listaAlarmas.setAdapter(alarmasAdapter);
			/*
			 * listaAlarmas.setOnItemLongClickListener(new
			 * AdapterView.OnItemLongClickListener() { // Called when the user
			 * long-clicks on someView public boolean
			 * onItemLongClick(AdapterView parent, View view, int position, long
			 * id) { if (mActionMode != null) { return false; } indalarma =
			 * position; clickvariable=false; mActionMode =
			 * getActivity().startActionMode( mActionModeCallback);
			 * view.setSelected(true); return true; } });
			 */
			return rootView;
		}
	}

	public class comunicacionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		// public int plcId =getArguments().getInt(ARG_SECTION_NUMBER);
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int section = getArguments().getInt(ARG_SECTION_NUMBER);
			plc mp = servidor.plcs.get(section);

			View rootView = inflater.inflate(R.layout.comunicacion_item_detail,
					container, false);

			ScrollView miscroll = (ScrollView) rootView
					.findViewById(R.id.miscroll);
			miscroll.setScrollbarFadingEnabled(false);
			miscroll.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
			LinearLayout milayout;
			milayout = scrolls.get(section);
			if (milayout.getParent() != null)
				((ViewGroup) milayout.getParent()).removeView(milayout);
			miscroll.addView(milayout);

			// ((ViewGroup) rootView).addView(milayout);

			return rootView;
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// ------------------ MENU contextual
	// ----------------------------------------------------------------
	/*
	 * public ActionMode.Callback mActionModeCallback = new
	 * ActionMode.Callback() {
	 * 
	 * // Called when the action mode is created; startActionMode() was called
	 * 
	 * @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	 * // Inflate a menu resource providing context menu items MenuInflater
	 * inflater = mode.getMenuInflater();
	 * inflater.inflate(R.menu.menu_variables, menu); return true; }
	 * 
	 * // Called each time the action mode is shown. Always called after //
	 * onCreateActionMode, but // may be called multiple times if the mode is
	 * invalidated.
	 * 
	 * @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu)
	 * { return false; // Return false if nothing is done }
	 * 
	 * // Called when the user selects a contextual menu item
	 * 
	 * @Override public boolean onActionItemClicked(ActionMode mode, MenuItem
	 * item) {
	 * 
	 * switch (item.getItemId()) {
	 * 
	 * case R.id.item3: AlertDialog.Builder alertDialogBuilder = new
	 * AlertDialog.Builder( ctx); alertDialogBuilder.setTitle("Atencion");
	 * alertDialogBuilder .setMessage("Va a eliminar una variable!")
	 * .setCancelable(false) .setPositiveButton("Aceptar", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int id) { servidor.plcs.get(MainActivity.pagina).variables
	 * .remove(MainActivity.indvariable); xml.generarServidor();
	 * dialog.cancel(); }; }) .setNegativeButton("Cancelar", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int id) { dialog.cancel(); } }); AlertDialog alertDialog =
	 * alertDialogBuilder.create(); alertDialog.show(); mode.finish(); // Action
	 * picked, so close the CAB return true; case R.id.item2: //
	 * shareCurrentItem(); pagina = mViewPager.getCurrentItem(); if
	 * (clickvariable) { item variable =
	 * servidor.plcs.get(MainActivity.pagina).variables
	 * .get(MainActivity.indvariable); editarVariables dialogo = new
	 * editarVariables(variable, servidor.plcs.get(MainActivity.pagina));
	 * dialogo.show(getSupportFragmentManager(), "editar"); } else { alarma
	 * mialarma = servidor.plcs.get(MainActivity.pagina).ListaAlarmas
	 * .get(MainActivity.indalarma); EditarAlarmas dialogo = new
	 * EditarAlarmas(mialarma, servidor.plcs.get(MainActivity.pagina));
	 * dialogo.show(getSupportFragmentManager(), "editar"); } mode.finish(); //
	 * Action picked, so close the CAB return true;
	 * 
	 * case R.id.subir: java.util.Collections.swap(
	 * servidor.plcs.get(MainActivity.pagina).variables,
	 * MainActivity.indvariable, MainActivity.indvariable - 1);
	 * xml.generarServidor(); xml.escribirXml(MainActivity.ctx); return true;
	 * case R.id.bajar: java.util.Collections.swap(
	 * servidor.plcs.get(MainActivity.pagina).variables,
	 * MainActivity.indvariable, MainActivity.indvariable + 1);
	 * xml.generarServidor(); xml.escribirXml(MainActivity.ctx); return true;
	 * 
	 * default: return false; } }
	 * 
	 * // Called when the user exits the action mode
	 * 
	 * @Override public void onDestroyActionMode(ActionMode mode) { mActionMode
	 * = null; } };
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_variables, menu);
		if (v.getId() == R.id.listVariables)
			clickvariable = true;
		else
			clickvariable = false;
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		indvariable = info.position;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.item3:
			if (clickvariable) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						ctx);
				alertDialogBuilder.setTitle("Atencion");
				alertDialogBuilder
						.setMessage("Va a eliminar una variable!")
						.setCancelable(false)
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										servidor.plcs.get(MainActivity.pagina).variables
												.remove(MainActivity.indvariable);
										xml.generarServidor();
										dialog.cancel();
									};
								})
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						ctx);
				alertDialogBuilder.setTitle("Atencion");
				alertDialogBuilder
						.setMessage("Va a eliminar una alarma!")
						.setCancelable(false)
						.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										servidor.plcs.get(MainActivity.pagina).ListaAlarmas
												.remove(MainActivity.indvariable);
										xml.generarServidor();
										dialog.cancel();
									};
								})
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}

			return true;
		case R.id.item2:
			// shareCurrentItem();
			pagina = mViewPager.getCurrentItem();
			if (clickvariable) {
				item variable = servidor.plcs.get(MainActivity.pagina).variables
						.get(MainActivity.indvariable);
				editarVariables dialogo = new editarVariables(variable,
						servidor.plcs.get(MainActivity.pagina));
				dialogo.show(getSupportFragmentManager(), "editar");
			} else {
				alarma mialarma = servidor.plcs.get(MainActivity.pagina).ListaAlarmas
						.get(MainActivity.indvariable);
				EditarAlarmas dialogo = new EditarAlarmas(mialarma,
						servidor.plcs.get(MainActivity.pagina));
				dialogo.show(getSupportFragmentManager(), "editar");
			}
			return true;

		case R.id.subir:
			if (clickvariable) {
				java.util.Collections.swap(
						servidor.plcs.get(MainActivity.pagina).variables,
						MainActivity.indvariable, MainActivity.indvariable - 1);
				xml.generarServidor();
				xml.escribirXml(MainActivity.ctx);
			} else {
				java.util.Collections.swap(
						servidor.plcs.get(MainActivity.pagina).ListaAlarmas,
						MainActivity.indvariable, MainActivity.indvariable - 1);
				xml.generarServidor();
				xml.escribirXml(MainActivity.ctx);
			}
			return true;
		case R.id.bajar:
			if (clickvariable) {
				java.util.Collections.swap(
						servidor.plcs.get(MainActivity.pagina).variables,
						MainActivity.indvariable, MainActivity.indvariable + 1);
				xml.generarServidor();
				xml.escribirXml(MainActivity.ctx);
			} else {
				java.util.Collections.swap(
						servidor.plcs.get(MainActivity.pagina).ListaAlarmas,
						MainActivity.indvariable, MainActivity.indvariable + 1);
				xml.generarServidor();
				xml.escribirXml(MainActivity.ctx);
			}
			return true;

		case R.id.enviarMail:
			if (clickvariable) {
				item variable = servidor.plcs.get(MainActivity.pagina).variables
						.get(MainActivity.indvariable);
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				// emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"historico de variable :" + variable.nombre);

				String texto = "";
				for (int i = 0; i < variable.historial.buffer.length; i++) {
					texto = texto + Double.toString(variable.historial.Gety(i))
							+ ";" + Double.toString(variable.historial.Getx(i))
							+ "\n";
				}
				emailIntent.putExtra(Intent.EXTRA_TEXT, texto);

				MainActivity.ctx.startActivity(Intent.createChooser(
						emailIntent, "Send mail..."));
			} else {
				Toast toast = Toast.makeText(MainActivity.ctx,
						"No Exiete historico para las alarmas ",
						Toast.LENGTH_SHORT);
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
