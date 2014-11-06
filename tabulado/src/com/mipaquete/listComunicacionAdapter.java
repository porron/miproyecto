package com.mipaquete;


import java.sql.Date;
import java.util.List;

import libreria.item;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class listComunicacionAdapter extends ArrayAdapter<item>{

      private int resource;
      private LayoutInflater inflater;
      private Context context;
      private java.util.ArrayList<item> misvariables;

      public listComunicacionAdapter ( Context ctx, int resourceId, java.util.ArrayList<item> variables) {
            super( ctx, resourceId, variables );
            resource = resourceId;
            inflater = LayoutInflater.from( ctx );
            context=ctx;
            misvariables =variables;
      }

      @Override
      public View getView ( int position, View convertView, ViewGroup parent ) {

            item variable = misvariables.get( position );  	   
            cachevariables viewCache;

            if ( convertView == null ) {
                  convertView = ( LinearLayout ) inflater.inflate( resource, null );
                  viewCache = new cachevariables( convertView );
                  convertView.setTag( viewCache );
            }
            else {
                  convertView = ( LinearLayout ) convertView;
                  viewCache = ( cachevariables ) convertView.getTag();
            }

            
            // usamos el mismo cache que para el modo sin comunicacion por eso el campo no cuadra
            // por ahora no importa
            
            TextView nombre = viewCache.getNombre(resource);
            nombre.setText(variable.getNombre());
            
   
            TextView tipo = viewCache.getTipo(resource);
      //     if (variable.valor != null) tipo.setText(Double.toString((Double)variable.valor));
            tipo.setText(Double.toString(variable.valor));
            
            TextView posicion = viewCache.getPosicion(resource);
            if (variable.calidad != 0) posicion.setText(Integer.toString(variable.calidad));
            


            return convertView;
      }
}
