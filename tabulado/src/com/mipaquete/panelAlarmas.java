package com.mipaquete;

import java.util.ArrayList;

import com.mipaquete.R;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class panelAlarmas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panelalarmas);
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.cancel(1);
		ArrayList <liataEntrada> miarray = new ArrayList  <liataEntrada> ();
		for (int i = 0; i < servidor.plcs.size(); i++) {
			plc miplc= servidor.plcs.get(i);
			for (int ii = 0; ii < miplc.ListaAlarmas.size(); ii++) {
				alarma mialarma = miplc.ListaAlarmas.get(ii);
				String texto = miplc.nombre+"."+mialarma.nombre+"   ";
				liataEntrada entrada = new liataEntrada (mialarma.estado,texto);
				miarray.add(entrada);
			}
			
			
		}

		ListView listView = (ListView) findViewById(R.id.lista_paneles); 
       
        
        ListView lista = (ListView) findViewById(R.id.lista_paneles);
        lista.setAdapter(new lista_adaptador(this, R.layout.entradalarma, miarray){
			@Override
			public void onEntrada(Object entrada, View view) {
		            TextView texto = (TextView) view.findViewById(R.id.textentrada); 
		            texto.setText(((liataEntrada) entrada).get_texto()); 

		            ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageentrada); 
		            imagen_entrada.setImageResource(((liataEntrada) entrada).get_idImagen());
			}

		});
        
        
        
        
        
	}

	public panelAlarmas() {
		// TODO Auto-generated constructor stub
	}

}
