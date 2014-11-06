package com.mipaquete;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import libreria.paquete;

public class escribe_socket extends Thread implements Runnable {
	public static  ArrayBlockingQueue<paquete> buffer_salida= new ArrayBlockingQueue<paquete>(100);
	public static boolean salida_permitida = true;

	public void run() {
		paquete mipaquete;
		while (true) {
			mipaquete = buffer_salida.poll();
			if (salida_permitida)
				if (mipaquete != null) {
					try {
						MainActivity.out.writeObject(mipaquete);
						MainActivity.out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

		}

	}
}
