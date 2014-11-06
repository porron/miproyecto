package com.mipaquete;

import java.util.ArrayList;


import libreria.item;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class EditarAlarmas extends DialogFragment implements
		OnItemSelectedListener {

	boolean editando;
	plc miplc = null;
	Spinner spinnerDato1;
	Spinner spinnerDato2;
	Spinner spinnerOperador;
	ArrayAdapter<String> adaptervariables;
	alarma mialarma;
	String operadores[] = { ">", ">=", "<", "<=", "=" };

	public EditarAlarmas(alarma alar, plc Plc) {

		super();
		mialarma = alar;
		miplc = Plc;
		
		if (alar == null)
			editando = false;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View rootView = inflater.inflate(R.layout.editar_alarma, null);

		ArrayList<item> variables = miplc.variables;
		ArrayAdapter<String> adaptervariables1 = new ArrayAdapter<String>(
				MainActivity.ctx, android.R.layout.simple_list_item_1);
		for (int i = 0; i < variables.size(); i++) {
			String n = variables.get(i).nombre;
			adaptervariables1.add(n);
		}
		ArrayAdapter<String> adaptervariables2 = new ArrayAdapter<String>(
				MainActivity.ctx, android.R.layout.simple_list_item_1);
		for (int i = 0; i < variables.size(); i++) {
			String n = variables.get(i).nombre;
			adaptervariables2.add(n);
		}
		spinnerDato1 = (Spinner) rootView.findViewById(R.id.spinnerDato1);
		adaptervariables1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDato1.setAdapter(adaptervariables1);
		spinnerDato2 = (Spinner) rootView.findViewById(R.id.spinnerDato2);
		adaptervariables2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDato2.setAdapter(adaptervariables2);

		ArrayAdapter<String> adapterOperadores = new ArrayAdapter<String>(
				MainActivity.ctx, android.R.layout.simple_list_item_1,
				operadores);
		adapterOperadores
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOperador = (Spinner) rootView.findViewById(R.id.spinnerOperador);
		spinnerOperador.setAdapter(adapterOperadores);

		
		final EditText nombre = (EditText) rootView.findViewById(R.id.thistorial);
		if (editando) {
			// mivariable =
			// servidor.plcs.get(MainActivity.pagina).variables.get(MainActivity.indvariable);
			nombre.setText(mialarma.nombre);
			int spinnerPosition = adaptervariables1.getPosition(mialarma.fuenteDato1);
			spinnerDato1.setSelection(spinnerPosition);
			spinnerPosition = adaptervariables2.getPosition(mialarma.fuenteDato2);
			spinnerDato2.setSelection(spinnerPosition);
			spinnerOperador.setSelection(mialarma.operador);

		} else
			mialarma = new alarma();
		// ---------------------------------------------------------------------------------------------------------------------------------------------


		builder.setView(rootView);
		// Add action buttons
		builder.setPositiveButton(R.string.aceptar,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						String nom = nombre.getText().toString();
						String fuenteDato1 = (String) spinnerDato1
								.getSelectedItem();
						String fuenteDato2 = (String) spinnerDato2
								.getSelectedItem();
						int operador = (int) spinnerOperador
								.getSelectedItemId();
						if (!editando) {
							mialarma = new alarma(nom, fuenteDato1,
									fuenteDato2, operador);
							miplc.ListaAlarmas.add(mialarma);
						}
						xml.generarServidor();
						xml.escribirXml(MainActivity.ctx);
					}
				});
		builder.setNegativeButton(R.string.cancelar,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditarAlarmas.this.getDialog().cancel();
					}
				});

		// para activar o desactivar el boton

		nombre.addTextChangedListener(new TextWatcher() {
			private void handleText() {
				// Grab the button
				final Button okButton = ((AlertDialog) EditarAlarmas.this
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
		/*
		 * Spinner spinner = (Spinner) parent; if(spinner.getId() ==
		 * R.id.spinnerDato1){
		 * 
		 * }
		 */
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	public void onDismiss(DialogInterface dialog) {
		if (miplc.variables.size() == 0) {
//			editarVariables dialogo2 = new editarVariables(null, miplc);
//			dialogo2.show(getFragmentManager(), "Variables");
		} else {
			// miplc.hilo_comunicacion.cancel(true);
			// miplc.hilo_comunicacion = new comunicacion_asinc(miplc);
			// miplc.hilo_comunicacion.start();
		}
	}

}
