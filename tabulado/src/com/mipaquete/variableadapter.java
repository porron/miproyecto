package com.mipaquete;


import java.util.List;

import libreria.item;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class variableadapter extends ArrayAdapter<item>{

      private int resource;
      private LayoutInflater inflater;
      private Context context;
      private java.util.ArrayList<item> misvariables;

      public variableadapter ( Context ctx, int resourceId, java.util.ArrayList<item> variables) {
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

            TextView nombre = viewCache.getNombre(resource);
            nombre.setText(variable.getNombre());
                      
            String texto= "";
            TextView posicion = viewCache.getTipo(resource);
			switch (variable.rango) {
			case 0:texto= "Coil out.";
			break;
			case 1:texto ="Dig.inp.";
			break;
			case 2:texto ="Ana.inp.";
			break;
			case 3:texto ="Hold.reg.";
			break;
			case 4:texto ="Ext.reg.";
			break;
			default:
				break;
			}
			texto=texto+" "+Integer.toString(variable.offset);
            posicion.setText(texto);
            
     
            TextView representacion = viewCache.getPosicion(resource);
            
			switch (variable.representacion) {
			case 0:texto= "Sin";
			break;
			case 1:texto ="Barra";
			break;
			case 2:texto ="Texto";
			break;
			case 3:texto ="Editable";
			break;
			case 4:texto ="Boton";
			break;
			case 5:texto ="Grafico";
			break;
			default:
				break;
			}
            representacion.setText(texto);
            


            return convertView;
      }
}