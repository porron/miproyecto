package com.mipaquete;


import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

	public class alerta_conexion_plc extends DialogFragment {
		String mtexto;
		
		alerta_conexion_plc(String texto) {
			mtexto = texto;
		}
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	        builder.setMessage(R.string.alerta_plc+"  "+mtexto)
	    mtexto=getResources().getString(R.string.alerta_plc)+"  "+mtexto;
        builder.setMessage(mtexto)
        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
