package com.mipaquete;

import java.util.ArrayList;
import java.util.Iterator;

import com.mipaquete.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.ToggleButton;

public class EditarPlc extends DialogFragment implements OnItemSelectedListener {

	boolean editando;
	plc miplc = null;
	int panel_seleccionado=-1;

	public EditarPlc(boolean edit) {
		super();
		editando = edit;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View rootView = inflater.inflate(R.layout.editar_plc, null);
		final EditText nombre = (EditText) rootView
				.findViewById(R.id.eplcnombre);
		final EditText refresco = (EditText) rootView
				.findViewById(R.id.eplcrefresco);
		final EditText ip = (EditText) rootView.findViewById(R.id.eplcip);
		
		
//		if (editando) miplc=servidor.plcs.get(MainActivity.pagina);
							
		ImageButton editar =(ImageButton)rootView.findViewById(R.id.edit_panel);
		ImageButton crear =(ImageButton)rootView.findViewById(R.id.crear_panel);
		ImageButton borrar =(ImageButton)rootView.findViewById(R.id.borrar_panel);
		
		editar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				EditarPaneles edpad = new EditarPaneles (servidor.plcs.get(MainActivity.pagina),panel_seleccionado,
						true);
				edpad.show(MainActivity.fragmentManager, "editar panel");
				EditarPlc.this.getDialog().cancel();

			}
		});
		
		crear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				EditarPaneles edpad = new EditarPaneles (servidor.plcs.get(MainActivity.pagina),panel_seleccionado,
						false);
				edpad.show(MainActivity.fragmentManager, "editar panel");
				EditarPlc.this.getDialog().cancel();
			}
		});
		
		borrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String nombreAntiguo=servidor.plcs.get(MainActivity.pagina).paneles.get(panel_seleccionado).titulo.getText().toString();
				servidor.plcs.get(MainActivity.pagina).paneles.remove(panel_seleccionado);
				
				for (int i = 0; i < servidor.plcs.get(MainActivity.pagina).variables.size(); i++) {
					String n = servidor.plcs
							.get(MainActivity.pagina).variables
							.get(i).panel;
					if (n.equals(nombreAntiguo))
						servidor.plcs.get(MainActivity.pagina).variables.get(i).panel = "";
				}
				xml.generarServidor();
				xml.escribirXml(MainActivity.ctx);
				
				EditarPlc.this.getDialog().cancel();
			}
		});
			
	 	
/*
		ToggleButton toggle = (ToggleButton) rootView
				.findViewById(R.id.toggleButton1);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					editando = false;
					miplc = new plc();
					miplc.id = "0";
					miplc.nombre = "";
					miplc.refresco = 1000;
					miplc.ip = "0.0.0.0";
					nombre.setText(miplc.nombre);
					refresco.setText(Integer.toString(miplc.refresco));
					ip.setText(miplc.ip);

				} else {
					editando = true;
					miplc = servidor.plcs.get(MainActivity.pagina);
					nombre.setText(miplc.nombre);
					refresco.setText(Integer.toString(miplc.refresco));
					ip.setText(miplc.ip);

				}
			}
		});
*/
		if (editando) {
			miplc = servidor.plcs.get(MainActivity.pagina);
			nombre.setText(miplc.nombre);
			refresco.setText(Integer.toString(miplc.refresco));
			ip.setText(miplc.ip);
			ArrayList<String> items = new ArrayList<String>();
			for (int i = 0; i < miplc.paneles.size(); i++) {
				String s=(String) miplc.paneles.get(i).titulo.getText().toString();
				items.add(s);
			}
			ListView lpaneles = (ListView) rootView.findViewById(R.id.listView1);	
			lpaneles.setAdapter(new ArrayAdapter<String>(MainActivity.ctx,
					android.R.layout.simple_list_item_multiple_choice, items));
				
			lpaneles.setOnItemClickListener(new OnItemClickListener (){
			     public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
			//         String selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
			    	 panel_seleccionado=myItemInt;
			       }                 			
			});

		} else {

			miplc = new plc();
			nombre.setText(miplc.nombre);
			refresco.setText(Integer.toString(miplc.refresco));
			ip.setText(miplc.ip);

		}
		builder.setView(rootView);
		// Add action buttons
		builder.setPositiveButton(R.string.aceptar,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						miplc.nombre = nombre.getText().toString();
						miplc.refresco = Integer.parseInt(refresco.getText()
								.toString());
						miplc.ip = ip.getText().toString();

						if (!editando) {
							servidor.plcs.add(miplc);
						}
						xml.generarServidor();
						xml.escribirXml(MainActivity.ctx);
						
// reconectar los plcs pare ver cambios						
						Iterator<plc> it = servidor.plcs.iterator();
						while (it.hasNext()) {
							plc p = (plc) it.next();
							// p.hilo_comunicacion= new comunicacion_asinc().execute(p);
							// p.hilo_comunicacion= new comunicacion_asinc(p);
							// p.hilo_comunicacion.start();
							if (p.hilo_comunicacion !=null) p.hilo_comunicacion.cancel(true);
							p.hilo_comunicacion = new comunicacion_asinc(p);
							p.hilo_comunicacion
									.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

						}
						MainActivity.mViewPager.getAdapter()
								.notifyDataSetChanged();

					}
				});
		builder.setNegativeButton(R.string.cancelar,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditarPlc.this.getDialog().cancel();
					}
				});

		// para activar o desactivar el boton

		nombre.addTextChangedListener(new TextWatcher() {
			private void handleText() {
				// Grab the button
				final Button okButton = ((AlertDialog) EditarPlc.this
						.getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
				if (nombre.getText().length() == 0) {
					okButton.setEnabled(false);
				} else {
					okButton.setEnabled(true);
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				handleText();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Nothing to do
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Nothing to do
			}
		});

		AlertDialog b = builder.create();
		b.show();
		Button okButton = b.getButton(AlertDialog.BUTTON_POSITIVE);
		okButton.setEnabled(false);

		return b;

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	public void onDismiss(DialogInterface dialog) {
		if (miplc.variables.size() == 0) {
			editarVariables dialogo2 = new editarVariables(null, miplc);
			// FragmentManager fragmentManager = getSupportFragmentManager();
			// dialogo2.show(fragmentManager, "Variables");
		} else {
			// miplc.hilo_comunicacion.cancel(true);
			// miplc.hilo_comunicacion = new comunicacion_asinc(miplc);
			// miplc.hilo_comunicacion.start();
		}
	}

}
