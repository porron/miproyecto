package com.mipaquete;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import com.mipaquete.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
		String texto="";
		String fecha="";
		ArrayList<liataEntrada> miarray = new ArrayList<liataEntrada>();
		for (int i = 0; i < servidor.plcs.size(); i++) {
			plc miplc = servidor.plcs.get(i);
			for (int ii = 0; ii < miplc.ListaAlarmas.size(); ii++) {
				alarma mialarma = miplc.ListaAlarmas.get(ii);
				texto = miplc.nombre + "." + mialarma.nombre;
				if (mialarma.estado == alarma.estados.disparada) {
					DateFormat df = DateFormat.getTimeInstance();
					fecha=df.format(mialarma.hora);
				}
				else fecha="";

				liataEntrada entrada = new liataEntrada(mialarma.estado, texto,fecha);
				miarray.add(entrada);
			}

		}

		// ListView listView = (ListView) findViewById(R.id.lista_paneles);
		Button marcar = (Button) findViewById(R.id.b_edit_panel);
		marcar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mNotifyMgr.cancel(1);
				for (int i = 0; i < servidor.plcs.size(); i++) {
					plc miplc = servidor.plcs.get(i);
					for (int ii = 0; ii < miplc.ListaAlarmas.size(); ii++) {
						alarma mialarma = miplc.ListaAlarmas.get(ii);
						if (mialarma.estado == alarma.estados.disparada)
							mialarma.estado = alarma.estados.revisada;
					}
				}
			}
		});

		ListView lista = (ListView) findViewById(R.id.lista_paneles);
		lista.setAdapter(new lista_adaptador(this, R.layout.entradalarma,
				miarray) {
			@Override
			public void onEntrada(Object entrada, View view) {
				TextView texto = (TextView) view.findViewById(R.id.textentrada);
				texto.setText(((liataEntrada) entrada).get_texto());
				TextView fecha = (TextView) view.findViewById(R.id.textfecha);
				fecha.setText(((liataEntrada) entrada).get_fecha());

				
				ImageView imagen_entrada = (ImageView) view
						.findViewById(R.id.imageentrada);
				imagen_entrada.setImageResource(((liataEntrada) entrada)
						.get_idImagen());
			}

		});

	}

	public panelAlarmas() {
		// TODO Auto-generated constructor stub
	}

}
