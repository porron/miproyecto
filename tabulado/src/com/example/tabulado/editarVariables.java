package com.example.tabulado;

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
		final EditText posicion = (EditText) rootView.findViewById(R.id.trefresco);
		final EditText historico = (EditText) rootView.findViewById(R.id.textoip);
		final EditText plotlong = (EditText) rootView.findViewById(R.id.tlong);

		final Spinner spinnerTipo = (Spinner) rootView.findViewById(R.id.spinnerTipo);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				MainActivity.ctx, R.array.tiposdato,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTipo.setAdapter(adapter);
		spinnerTipo.setOnItemSelectedListener(this);
		
		
		final Spinner spinnerRango = (Spinner) rootView.findViewById(R.id.spinnerRango);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(MainActivity.ctx,
		        R.array.rangos, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRango.setAdapter(adapter2);
		spinnerRango.setOnItemSelectedListener(this);
		
		final Spinner spinnerRepresentacion = (Spinner) rootView.findViewById(R.id.spinnerrepresentacion);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(MainActivity.ctx,
		        R.array.representaciones, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRepresentacion.setAdapter(adapter3);
		spinnerRepresentacion.setOnItemSelectedListener(this);
		

		if (editando) {
			// mivariable =
			// servidor.plcs.get(MainActivity.pagina).variables.get(MainActivity.indvariable);
			nombre.setText(mivariable.nombre);
			posicion.setText(Integer.toString(mivariable.offset));
			historico.setText(Integer.toString(mivariable.historial.size()));
			plotlong.setText(Integer.toString(mivariable.plotlong));

			spinnerTipo.setSelection(mivariable.tipoDato);
			spinnerRango.setSelection(mivariable.rango);
		} else
			mivariable = new item();

		builder.setView(rootView);
		// Add action buttons
		builder.setPositiveButton(R.string.aceptar,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						mivariable.nombre = nombre.getText().toString();
						mivariable.offset = Integer.parseInt(posicion.getText()
								.toString());
						mivariable.tipoDato = (int) spinnerTipo.getSelectedItemId();
						mivariable.rango= (int) spinnerRango.getSelectedItemId();
						mivariable.representacion=(int) spinnerRango.getSelectedItemId();
						mivariable.plotlong= Integer.parseInt(posicion.getText().toString());
						if (Integer.parseInt(historico.getText().toString())>0)
						mivariable.historial= new libreria.historico ( Integer.parseInt(historico.getText().toString()));
						
						editarVariables.this.getDialog().dismiss();
						if (!editando) {
							miPlc.variables.add(mivariable);
						}
						xml.generarServidor();
						xml.escribirXml(MainActivity.ctx);

						/*miPlc.hilo_comunicacion.cancel(true);
						miPlc.hilo_comunicacion = new comunicacion_asinc()
								.execute(miPlc);
						*/
	/*					miPlc.hilo_comunicacion = new comunicacion_asinc(miPlc);
						miPlc.hilo_comunicacion.start();*/

					}
				});
		builder.setNegativeButton(R.string.cancelar,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						editarVariables.this.getDialog().cancel();
					}
				});
		if (!editando)
			builder.setNeutralButton(R.string.anadir,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mivariable.nombre = nombre.getText().toString();
							mivariable.offset = Integer.parseInt(posicion.getText()
									.toString());
							mivariable.tipoDato = (int) spinnerTipo.getSelectedItemId();
							mivariable.rango= (int) spinnerRango.getSelectedItemId();
							mivariable.representacion=(int) spinnerRango.getSelectedItemId();
							mivariable.plotlong= Integer.parseInt(posicion.getText().toString());
							if (Integer.parseInt(historico.getText().toString())>0)
							mivariable.historial= new libreria.historico ( Integer.parseInt(historico.getText().toString()));
							
							miPlc.variables.add(mivariable);
							nombre.setText(null);
							posicion.setText(null);
							spinnerTipo.setSelection(0);
							Context context = MainActivity.ctx;
							CharSequence text = "Variable a�adida. Debe aun guardar al finalizar pulsando 'Aceptar'";
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text,
									duration);
							toast.show();
							editarVariables.this.getDialog().show();
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

		nombre.addTextChangedListener(new TextWatcher() {
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
	/*	miPlc.hilo_comunicacion.cancel(true);
		miPlc.hilo_comunicacion = new comunicacion_asinc().execute(miPlc);
	*/	
	//	miPlc.hilo_comunicacion = new comunicacion_asinc(miPlc);
	// miPlc.hilo_comunicacion.start();
	}

}