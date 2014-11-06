package com.mipaquete;

import java.util.Date;
import java.util.Iterator;

import android.os.AsyncTask;
import android.util.Log;

public class guardian {

	guardian() {

		Thread t = new Thread() {
			public void run() {
				try {
					plc p;
			   // comentado para evitar la comunicacion con espejo en pruebas
				//	sock TareaSocket = new sock() ;
				//	TareaSocket.start();
					while (true) {
						Thread.sleep(5000);
						//Log.e("guardian", " entro en guardian  ");
						// arrancamos el socket de comunicacion externa
				/*		if (TareaSocket.getState()==Thread.State.TERMINATED || 
								TareaSocket.getState()==Thread.State.NEW ||
								!MainActivity.misocket.isConnected() ){
							TareaSocket.start();
						}*/
					
						// Se arrancan los hilos a los modbus
						

						Iterator<plc> it = servidor.plcs.iterator();
						while (it.hasNext()) {
							p = (plc) it.next();
								p.hilo_comunicacion = new comunicacion_asinc(p);
								p.hilo_comunicacion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);	

							
						}
						
						
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.e("guardian", " interrupidooo" + e.getMessage());
				}
				}
						
		};
		t.start();
	}
}
