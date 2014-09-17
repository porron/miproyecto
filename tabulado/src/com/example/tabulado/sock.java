package com.example.tabulado;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import org.w3c.dom.Element;


import libreria.historico;
import libreria.item;
import libreria.paquete;
import libreria.variableEscribir;

import android.os.AsyncTask;
import android.util.Log;

import android.widget.Toast;

public class sock extends AsyncTask<Void, Integer, Void> {

	
	
//	public ArrayBlockingQueue<item> cola_de_salida ;
	@Override
	protected Void doInBackground(Void... params) {
		MainActivity.misocket = null;
		MainActivity.out = null;
		MainActivity.in = null;
		paquete mipaquete = null;

		try {
//			android.os.Debug.waitForDebugger();

			Log.e("sock", "arranco el thread ");
//			cola_de_salida = new ArrayBlockingQueue<item>(10);
			MainActivity.misocket = new Socket(MainActivity.dir_espejo, 4444);
			// MainActivity.misocket = new Socket("192.168.1.10", 4444);
			// MainActivity.misocket = new Socket("87.216.111.185", 4444);
			Log.e("sock", "conectado");

			OutputStream os = MainActivity.misocket.getOutputStream();
			MainActivity.out = new ObjectOutputStream(os);

			InputStream buffer = new BufferedInputStream(
					MainActivity.misocket.getInputStream());
			MainActivity.in = new ObjectInputStream(buffer);
			String nomf = String.format("%-50s", MainActivity.nombre_servidor);
			String passf = String.format("%-10s", MainActivity.pass_servidor);
			mipaquete = new paquete(true, 11, "espejo", 0.0, 0, 0L, null,
					nomf + passf);
			MainActivity.out.writeObject(mipaquete);
			MainActivity.out.flush();
			publishProgress(11);
			
			escribe_socket escritor =new escribe_socket();
			escritor.start();
			while (true) {
				mipaquete = (paquete) MainActivity.in.readObject();
				procesar (mipaquete);
			}
		} catch (IOException e) {
			Log.e("conexion", "no conecta :" + e.getMessage());
	//		System.err.println("no conecta :" + e.getMessage());
			System.exit(1);
			publishProgress(mipaquete.tipo);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
		int tipo = progress[0];
		Toast toast;
		switch (tipo) {
		case 11:
			Log.e("sock", "servidor recibe 11 ");
			toast = Toast.makeText(MainActivity.ctx, "solicitando conexion",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		case 12:
			Log.e("sock", "servidor recibe 12");
			
			toast = Toast.makeText(MainActivity.ctx, "conectado",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		case 13:
			Log.e("sock", "servidor recibe 13 ");

			toast = Toast.makeText(MainActivity.ctx, "conexion imposible",
					Toast.LENGTH_LONG);
			toast.show();
			break;
		case 15:
			Log.e("sock", "servidor recibe 15 ");

			toast = Toast.makeText(MainActivity.ctx, "No existe cliente",
					Toast.LENGTH_LONG);
			toast.show();
			break;

			
		default:
			break;
		}
	}

	void enviar_servidor() {
		item mi;
		alarma ma;
		paquete mipaquete;
		Iterator<plc> it = servidor.plcs.iterator();
		while (it.hasNext()) {
			plc miplc = (plc) it.next();
			for (int i = 0; i < miplc.variables.size(); i++) {
				mi = (item) miplc.variables.get(i);

				if (mi.valor != null) {
					try {
						mipaquete = new paquete(true, 2, mi.nombre,mi.valor.doubleValue(), mi.calidad, mi.tiempo.getTime(),
								null, null);
						MainActivity.out.writeObject(mipaquete);
						MainActivity.out.flush();
						Log.e("sock", "envio 2 ");
						if ( mi.historial!=null){
							mipaquete = new paquete(true, 3, mi.nombre,0.0, 0,0L,mi.historial,null);
							MainActivity.out.writeObject(mipaquete);
							MainActivity.out.flush();
						}

					} catch (IOException e) {
						Log.e("sock", e.getMessage());
					}
				}
			}
			/*
			 * for (int i = 0; i < miplc.ListaAlarmas.size(); i++) { ma =
			 * (alarma) miplc.ListaAlarmas.get(i); mipaquete = new paquete(true,
			 * 6, ma.nombre, null, 1, null, null, null); try {
			 * MainActivity.out.writeObject(mipaquete);
			 * MainActivity.out.flush(); } catch (IOException e) { Log.e("sock",
			 * e.getMessage()); } }
			 */
		}
		mipaquete = new paquete(true, 9, null, 0.0, 0,0L, null, null);
		try {
			MainActivity.out.writeObject(mipaquete);
			MainActivity.out.flush();
			Log.e("sock", "termino el envio de datos ");
			MainActivity.cliente_conectado=true;
		} catch (IOException e) {
			Log.e("sock", e.getMessage());
		}
	}
	
	
	void procesar (paquete paq) {

		switch (paq.tipo) {
		
		// peticion de datos desde un cliente
		case 1:
			Log.e("sock", "servidor recibe 1 ");
	//		escribe_socket.salida_permitida=false;
			String s=xml.toString(xml.documentoXML);
			Log.e("sock", "creo el xml");

			paquete mipaquete = new paquete(true, 1, MainActivity.nombre_servidor, 0.0, 0,
					0L, null, s);
			Log.e("sock", "creo el paquete");
			
			try {
				MainActivity.out.writeObject(mipaquete);
				Log.e("sock", "mando el paquete");

				MainActivity.out.flush();
				Log.e("sock", "devuelvo 1 ");
			} catch (IOException e) {
				Log.e("sock", e.getMessage());
			}
			enviar_servidor();
	//		escribe_socket.salida_permitida=true;
			break;
			
			// recepsion de cambio de variable
		case 2:
			Log.e("sock", "servidor recibe 2 ");
			int idplc=servidor.plcDeVariable (paq.nombre);
			servidor.plcs.get(idplc).ListaEscribir.add(new variableEscribir(paq.nombre, paq.valor));
			break;
			

			// conexion a espejo aceptada
		case 12:
			publishProgress(12);
			break;

			// conexion a espejo rechazada
		case 13:
			publishProgress(13);
			break;
			
			// no hay socket para el cliente en el espejo
		case 15:
			publishProgress(15);
			MainActivity.cliente_conectado=false;
			break;

		default:
			break;
		}
	}
}
