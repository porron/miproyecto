package com.example.tabulado;

import android.view.View;
import android.widget.TextView;

class cachevariables {

      private View baseView;
      private TextView nombre;
      private TextView tipo;
      private TextView posicion;

      public cachevariables ( View baseView ) {
            this.baseView = baseView;
      }

      public View getViewBase ( ) {
            return baseView;
      }

      public TextView getNombre (int resource) {
          if ( nombre == null ) {
                nombre = ( TextView ) baseView.findViewById(R.id.titulo);
          }
          return nombre;
    }


     public TextView getTipo (int resource) {
            if ( tipo == null ) {
                  tipo = ( TextView ) baseView.findViewById(R.id.t2);
            }
            return tipo;
      }

      public TextView getPosicion (int resource) {
            if ( posicion == null ) {
                  posicion = ( TextView ) baseView.findViewById(R.id.textView3);
            }
            return posicion;
      }
}