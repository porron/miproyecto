package com.example.tabulado;

import java.io.IOException;
import libreria.paquete;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;

public class poner_password extends DialogFragment implements
		OnItemSelectedListener {

	public poner_password() {
		super();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return null;
//		final AlertDialog.Builder builder = new AlertDialog.Builder(
//				getActivity());
//		LayoutInflater inflater = getActivity().getLayoutInflater();
//		View rootView = inflater.inflate(R.layout.dialog_signin, null);
//
//		final EditText tservidor = (EditText) rootView
//				.findViewById(R.id.username);
//		if (servidor.nombre != null)
//			tservidor.setText(servidor.nombre);
//		else
//			tservidor.setText("Nombre_servidor");
//
//		final EditText tpass = (EditText) rootView.findViewById(R.id.password);
//		if (servidor.password != null)
//			tpass.setText(servidor.password);
//		else
//			tpass.setText("password");
//
//		// ---------------------------------------------------------------------------------------------------------------------------------------------
//
//		builder.setView(rootView);
//		// Add action buttons
//		builder.setPositiveButton(R.string.aceptar,
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int id) {
//						boolean actualizar =false;
//					
//						if  (!servidor.nombre.equals(tservidor.getText().toString())){
//						servidor.nombre = tservidor.getText().toString();
//						actualizar=true;
//						}
//						if  (!servidor.password.equals(tpass.getText().toString())){
//						servidor.password = tpass.getText().toString();
//						actualizar=true;
//						}
//						if (actualizar) {
//						xml.generarServidor();
//						xml.escribirXml(MainActivity.ctx);
//						}
//				//		paquete mipaquete = new paquete(true, 11, null, null,0, null, null, null);
//						sock comunicacion = new sock ();
//						comunicacion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);		
//						poner_password.this.getDialog().dismiss();
//					}
//				});
//		builder.setNegativeButton(R.string.cancelar,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						poner_password.this.getDialog().cancel();
//					}
//				});
//		AlertDialog b = builder.create();
//		b.show();
//
//		return b;
//
//	}
//
//	public void onItemSelected(AdapterView<?> parent, View view, int pos,
//			long id) {
//		// An item was selected. You can retrieve the selected item using
//		/*
//		 * Spinner spinner = (Spinner) parent; if(spinner.getId() ==
//		 * R.id.spinnerDato1){
//		 * 
//		 * }
//		 */
//	}
//
//	public void onNothingSelected(AdapterView<?> parent) {
//		// Another interface callback
//	}
//
//	public void onDismiss(DialogInterface dialog) {
//
//	}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
