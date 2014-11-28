package com.mipaquete;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import libreria.ItemHistorico;
import libreria.item;
import libreria.paquete;
import libreria.variableEscribir;

import android.app.Notification;
import android.app.NotificationManager;
//import android.app.DialogFragment;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusLocator;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import android.support.v4.app.DialogFragment;

//class comunicacion_asinc extends AsyncTask<plc, Integer, Void> {

//class comunicacion_asinc extends Thread implements Runnable {

public class comunicacion_asinc extends AsyncTask<Void, String, Void> {

	public static final String[] tipos = { "BINARY", "TWO_BYTE_INT_UNSIGNED",
			"TWO_BYTE_INT_SIGNED", "FOUR_BYTE_INT_UNSIGNED",
			"FOUR_BYTE_INT_SIGNED", "FOUR_BYTE_INT_UNSIGNED_SWAPPED",
			"FOUR_BYTE_INT_SIGNED_SWAPPED", "FOUR_BYTE_FLOAT",
			"FOUR_BYTE_FLOAT_SWAPPED", "EIGHT_BYTE_INT_UNSIGNED",
			"EIGHT_BYTE_INT_SIGNED", "EIGHT_BYTE_INT_UNSIGNED_SWAPPED",
			"EIGHT_BYTE_INT_SIGNED_SWAPPED", "EIGHT_BYTE_FLOAT",
			"EIGHT_BYTE_FLOAT_SWAPPED", "TWO_BYTE_BCD", "FOUR_BYTE_BCD" };

	public static boolean salida_bloqueada = false;

	Context micontext;
	private boolean funciona = true;
	plc miPlc;
	paquete mipaquete;

	comunicacion_asinc(plc Plc) {
		miPlc = Plc;
	}

	@Override
	// protected Void doInBackground(plc... parametro) {
	protected Void doInBackground(Void... params) {
		Date date = new Date();
		ModbusFactory factory = new ModbusFactory();

		IpParameters param = new IpParameters();
		param.setHost(miPlc.ip);
		param.setPort(502);
		param.setEncapsulated(false);
		ModbusMaster master = factory.createTcpMaster(param, true);
		master.setTimeout(500);
		master.setRetries(2);
		

		try {
						
			master.init();
			Log.e("conexion", "inicio el master ");

			BatchRead<String> batch = new BatchRead<String>();

			while (funciona) {
				// android.os.Debug.waitForDebugger();
				// lee las variables y si la diferencia con el valor anterior es
				// mayor que el granulado la cambia
				// si la variable tiene historial la incluye
				// pone la variable a modificado y para que el el pintor la
				// dibuje
				// pone pintado a no
				// coloca la variable en el buffer de salida si hay cliente
				// conectado

				if (isCancelled())
					funciona = false;
				Thread.sleep(miPlc.refresco);

				if (miPlc.variables.size() > 0) {
					if (miPlc.plc_modificado == true) {
						for (int i = 0; i < miPlc.variables.size(); i++) {
							item variable = (item) miPlc.variables.get(i);
							ModbusLocator locator = new ModbusLocator(1,variable.rango, variable.offset,variable.tipoDato);
							batch.addLocator(variable.nombre, locator);
						}
						miPlc.plc_modificado = false;
					}

					BatchResults<String> br = master.send(batch);
					date = new Date();

					for (int i = 0; i < miPlc.variables.size(); i++) {
						item variable = (item) miPlc.variables.get(i);
						Number d = (Number) br.getValue(variable.nombre);
						double mivalor = d.doubleValue();
						variable.calidad = 1;
						double dd = Math.abs(variable.valor - mivalor);
						if (dd > variable.granulado) {
							// Log.e("conexion", "detecta actualizar ");
							variable.valor = mivalor;
							variable.tiempo = date;
							if (variable.historial != null) {
								ItemHistorico mitem = new ItemHistorico(
										variable.valor, variable.tiempo);
								variable.historial.meter(mitem);

							}

							variable.hayquepintar = true;
							if (MainActivity.cliente_conectado) {
								mipaquete = new paquete(true, 2,
										variable.nombre, variable.valor,
										variable.calidad,
										variable.tiempo.getTime(), null, null);
								escribe_socket.buffer_salida.put(mipaquete);
							}
						}

					}
					for (int i = 0; i < miPlc.ListaAlarmas.size(); i++) {
						alarma mialarma = miPlc.ListaAlarmas.get(i);
						alarma.estados estadoanterior = mialarma.estado; 
						mialarma.procesar();
						if (estadoanterior!= alarma.estados.disparada && mialarma.estado== alarma.estados.disparada){
							mialarma.hora=new Date();
							publishProgress("alarma"+mialarma.nombre);
						}
					}

					// ------------------------------------------------------------
					// escritura de variables en plc
					// --------------------------------------
					// Obtenemos un Iterador y recorremos la lista

					// Iterator<String> keySetIterator =
					// ListaEscribir.keySet().iterator();
					if (miPlc.ListaEscribir != null) {
						// Set s = miPlc.ListaEscribir.entrySet();
						Iterator it = miPlc.ListaEscribir.iterator();
						item mivariable;
						while (it.hasNext()) {
							variableEscribir v = (variableEscribir) it.next();

							mivariable = miPlc.buscar(v.nombre);
							mivariable.valor = v.valor;
							ModbusLocator locator = new ModbusLocator(1,
									mivariable.rango, mivariable.offset,
									mivariable.tipoDato);
							master.setValue(locator, mivariable.valor);
							miPlc.ListaEscribir.clear();
						}
						miPlc.ListaEscribir.clear();
					}
				}
			}
		} catch (ModbusTransportException e) {
			funciona = false;
			publishProgress(e.getMessage());
		} catch (ErrorResponseException e) {
			publishProgress(e.getMessage());
			funciona = false;
		} catch (InterruptedException e) {
			publishProgress(e.getMessage());
			funciona = false;
		} catch (ModbusInitException e) {
			funciona = false;
			publishProgress(e.getMessage());
		}

		master.destroy();
		return null;
	}

	protected void onProgressUpdate(String... progress) {

		if (progress[0].substring(0, 6).equals("alarma")){
			int mId = 1;
			NotificationManager mNotifyMgr = (NotificationManager) MainActivity.ctx.getSystemService(Context.NOTIFICATION_SERVICE);
			MainActivity.mBuilder.setContentTitle("Kerbero alarma");
			MainActivity.mBuilder.setContentText(progress[0].substring(6));
			mNotifyMgr.notify(1, MainActivity.mBuilder.build());
		}
		else {
		alerta_conexion_plc newFragment = new alerta_conexion_plc(miPlc.nombre
				+ "\nError  : " + progress[0]);
		newFragment.show(MainActivity.fragmentManager, "missiles");
		}



	}
}