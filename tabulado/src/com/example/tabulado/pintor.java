package com.example.tabulado;

import libreria.item;

import android.os.AsyncTask;
import android.util.Log;

class pintor extends AsyncTask<Void, Integer, Void> {


	//Context micontext;
	private boolean funciona = true;

	protected Void doInBackground(Void... params) {
		int c = 0;
		funciona=true;
		while (funciona) {
			try {
				c =c+1;
				//if (MainActivity.modoComunicacion) {
				if (MainActivity.modoComunicacion){
				publishProgress((int) c);
				}
				if (isCancelled())
					funciona = false;
				Thread.sleep(2000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.e("pintor", " int " + e.getMessage());
				this.cancel(true);
			}
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
			
		if (MainActivity.modoComunicacion) {
				
			int posicion =MainActivity.mViewPager.getCurrentItem();
			plc miPlc = servidor.plcs.get(posicion);
			
			for( int i = 0 ; i < miPlc.variables.size() ; i++ ){

				item variable=(item)miPlc.variables.get(i);

				if (variable.hayquepintar) {
					variable.hayquepintar=false;
						if (variable.view !=null){
							Log.e("pintor", "hayque pintar");
							dibuja.actualiza(variable);
						}

					
				}
			}
		}	
			
		/*	MainActivity.texto.setText(progress.toString());
			ArrayAdapter<item> a = (ArrayAdapter<item>) MainActivity.listaVariables
					.getAdapter();
			a.notifyDataSetChanged();
		
		}*/
	}

    protected void onPostExecute(Void result) {
    	Log.e("pintor", "se ha terminado el pintor");
    }

}
