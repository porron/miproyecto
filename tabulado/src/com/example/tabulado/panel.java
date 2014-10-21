package com.example.tabulado;
import com.example.tabulado.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class panel extends LinearLayout {
	public TextView titulo;
	public boolean colocado;
	LinearLayout cinta;
	public panel(Context context) {

		super(context);
		this.setOrientation(VERTICAL);
		titulo= new TextView(context);
		cinta= new LinearLayout(context);
		this.addView(titulo);
		this.addView(cinta);
		cinta.setPadding(10, 0, 0, 0);
		titulo.setTextSize(12f);
		this.setBackground(getResources().getDrawable(R.drawable.panel));
		cinta.setOrientation(HORIZONTAL);
		colocado=false;
		
	}
	public void tiltular (String tit){
		this.titulo.setText(tit);
	}
	public void meter(View v){
		cinta.addView(v);
	}

}
