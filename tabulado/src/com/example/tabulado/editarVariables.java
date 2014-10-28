package com.example.tabulado;

import java.util.ArrayList;
import java.util.Iterator;

import libreria.item;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class editarVariables extends DialogFragment implements
		OnItemSelectedListener {

	boolean editando = true;
	item mivariable = null;
	plc miPlc;

	public editarVariables(item variable, plc Plc) {

		super();

		if (variable == null)
			editando = false;

		mivariable = variable;
		miPlc = Plc;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View rootView = inflater.inflate(R.layout.editar_variables, null);
		final EditText nombre = (EditText) rootView.findViewById(R.id.tnombre);
		final EditText posicion = (EditText) rootView
				.findViewById(R.id.trefresco);
		final EditText historico = (EditText) rootView
				.findViewById(R.id.textoip);
		final EditText plotlong = (EditText) rootView.findViewById(R.id.tlong);
		final EditText max = (EditText) rootView.findViewById(R.id.editmax);
		final EditText min = (EditText) rootView.findViewById(R.id.editmin);
		final EditText magnitud = (EditText) rootView
				.findViewById(R.id.editmagnitud);

		final Spinner spinnerTipo = (Spinner) rootView
				.findViewById(R.id.spinnerTipo);
		ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter
				.createFromResource(MainActivity.ctx, R.array.tiposdato,
						android.R.layout.simple_spinner_item);
		// adapterTipo.setDropDownViewResource(R.layout.layoutspiner);

		spinnerTipo.setAdapter(adapterTipo);
		// spinnerTipo.setOnItemSelectedListener(this);

		final Spinner spinnerRango = (Spinner) rootView
				.findViewById(R.id.spinnerRango);
		ArrayAdapter<CharSequence> adapterRango = ArrayAdapter
				.createFromResource(MainActivity.ctx, R.array.rangos,
						android.R.layout.simple_spinner_item);
		spinnerRango.setAdapter(adapterRango);
		// spinnerRango.setOnItemSelectedListener(this);

		final Spinner spinnerRepresentacion = (Spinner) rootView
				.findViewById(R.id.spinnerrepresentacion);
		ArrayAdapter<CharSequence> adapterRepre = ArrayAdapter
				.createFromResource(MainActivity.ctx, R.array.representaciones,
						android.R.layout.simple_spinner_item);
		spinnerRepresentacion.setAdapter(adapterRepre);
		// spinnerRepresentacion.setOnItemSelectedListener(this);

		// paneles--------------------------------------------------------------------

		final Spinner spinnerPanel = (Spinner) rootView
				.findViewById(R.id.spinnerPanel);
		plc miplc = servidor.plcs.get(MainActivity.pagina);
		ArrayList<String> items = new ArrayList<String>();
		items.add("");
		for (int i = 0; i < miplc.paneles.size(); i++) {
			String s = (String) miplc.paneles.get(i).titulo.getText()
					.toString();
			items.add(s);
		}
		final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
				MainActivity.ctx, android.R.layout.simple_list_item_1, items);
		spinnerPanel.setAdapter(adapter4);
		// spinnerPanel.setOnItemSelectedListener(this);
		// -----------------------------------------------------------------------------------------
		if (!editando) {
			mivariable = new item();
		}

		// mivariable =
		// servidor.plcs.get(MainActivity.pagina).variables.get(MainActivity.indvariable);
		nombre.setText(mivariable.nombre);
		posicion.setText(Integer.toString(mivariable.offset));

		if (mivariable.historial != null)
			historico.setText(Integer.toString(mivariable.historial.size()));
		else
			historico.setText("0");

		plotlong.setText(Integer.toString(mivariable.plotlong));
		max.setText(Double.toString(mivariable.max));
		min.setText(Double.toString(mivariable.min));
		magnitud.setText(mivariable.dim);

		spinnerTipo.setSelection(mivariable.tipoDato);
		spinnerRango.setSelection(mivariable.rango);
		spinnerRepresentacion.setSelection(mivariable.representacion);
		spinnerPanel.setSelection(items.indexOf(mivariable.panel));

		builder.setView(rootView);
		// Add action buttons
		builder.setPositiveButton(R.string.aceptar,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						if (spinnerRango.getSelectedItemId() == 1
								|| spinnerRango.getSelectedItemId() == 0) {
							if (spinnerTipo.getSelectedItemId() != 0) {
								spinnerTipo.setSelection(0);
								Toast toast = Toast
										.makeText(
												MainActivity.ctx,
												"Coil and Input ranges need Binary type",
												Toast.LENGTH_SHORT);
							}
						} else {

							if (validarNombre(nombre.getText().toString())
									|| editando) {

								mivariable.nombre = nombre.getText().toString();
								mivariable.offset = Integer.parseInt(posicion
										.getText().toString());
								mivariable.tipoDato = (int) spinnerTipo
										.getSelectedItemId();
								mivariable.rango = (int) spinnerRango
										.getSelectedItemId();
								mivariable.representacion = (int) spinnerRepresentacion
										.getSelectedItemId();
								mivariable.plotlong = Integer.parseInt(posicion
										.getText().toString());
								mivariable.granulado = Double
										.parseDouble(posicion.getText()
												.toString());
								mivariable.max = Double.parseDouble(max
										.getText().toString());
								mivariable.min = Double.parseDouble(min
										.getText().toString());
								mivariable.dim = magnitud.getText().toString();
								String panel = (String) spinnerPanel
										.getSelectedItem();

								// if (panel.equals("null"))
								// mivariable.panel="";
								// else mivariable.panel=panel;
								mivariable.panel = panel;

								if (Integer.parseInt(historico.getText()
										.toString()) > 0)
									mivariable.historial = new libreria.historico(
											Integer.parseInt(historico
													.getText().toString()));

								editarVariables.this.getDialog().dismiss();
								if (!editando) {
									miPlc.variables.add(mivariable);
								}
								miPlc.plc_modificado = true;
								xml.generarServidor();
								xml.escribirXml(MainActivity.ctx);
								MainActivity.mViewPager.getAdapter()
										.notifyDataSetChanged();
							} else {
								Toast toast = Toast.makeText(MainActivity.ctx,
										"El nombre de la variable ya existe",
										Toast.LENGTH_SHORT);
							}
						}

					}
				});
		builder.setNegativeButton(R.string.cancelar,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						editarVariables.this.getDialog().cancel();
					}
				});

		posicion.addTextChangedListener(new TextWatcher() {
			private void handleText() {
				// Grab the button
				final Button okButton = ((AlertDialog) editarVariables.this
						.getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
				if (nombre.getText().length() == 0
						| posicion.getText().length() == 0) {
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

		if (!editando) {
			nombre.addTextChangedListener(new TextWatcher() {
				private void handleText() {
					// Grab the button
					final Button okButton = ((AlertDialog) editarVariables.this
							.getDialog())
							.getButton(AlertDialog.BUTTON_POSITIVE);
					if (nombre.getText().length() == 0
							| posicion.getText().length() == 0) {
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
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// Nothing to do
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// Nothing to do
				}
			});
		}
		AlertDialog b = builder.create();
		b.show();
		Button okButton = b.getButton(AlertDialog.BUTTON_POSITIVE);
		if (!editando)
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
		/*
		 * miPlc.hilo_comunicacion.cancel(true); miPlc.hilo_comunicacion = new
		 * comunicacion_asinc().execute(miPlc);
		 */
		// miPlc.hilo_comunicacion = new comunicacion_asinc(miPlc);
		// miPlc.hilo_comunicacion.start();
	}

	boolean validarNombre(String minombre) {
		boolean noencontrado = true;

		Iterator<plc> it = servidor.plcs.iterator();
		while (it.hasNext()) {
			plc p = (plc) it.next();
			if (p.variables.size() > 0) {
				int cont = 0;
				while (noencontrado) {
					if (p.variables.get(cont).nombre.equals(minombre))
						noencontrado = false;
					else
						cont = cont + 1;
				}
			} else
				noencontrado = true;
		}
		;
		return noencontrado;
	}

}
