package com.example.tabulado;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import libreria.item;
import libreria.variableEscribir;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/*
 nada=0
 barra =1
 texto =2
 editable =3
 boton =4
 plot =5
 */

public class dibuja extends View {

	public dibuja(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	

	public static LinearLayout rellena(plc mp) {

		final Map<Integer, String> botones = new HashMap<Integer, String>();
		LinearLayout milayout = new LinearLayout(MainActivity.ctx);
		// final List <item> lasvariables = new ArrayList () ;
		milayout.setOrientation(LinearLayout.VERTICAL);
		panel mipanel= new panel(MainActivity.ctx);
		mipanel.tiltular("gagagagaag");
		milayout.addView(mipanel);
		
		for (int i = 0; i < mp.variables.size(); i++) {

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			

			item variable = (item) mp.variables.get(i);
//			if (variable.valor != null) {
			LinearLayout mil = new LinearLayout(MainActivity.ctx);
			TextView t = new TextView(MainActivity.ctx);
			switch (variable.representacion) {

			case 4:// boton

		//		mil.setOrientation(LinearLayout.HORIZONTAL);
		//		t.setText(variable.nombre);
				final Number valor = variable.valor;
				ToggleButton b = new ToggleButton(MainActivity.ctx);
				b.setId(i);
				b.setText(variable.nombre);
				b.setGravity(Gravity.RIGHT);
//				b.setLayoutParams(LinearLayout.)
				Drawable myI = MainActivity.ctx.getResources().getDrawable(android.R.drawable.button_onoff_indicator_off);
				b.setBackground(myI);

				
				botones.put(b.getId(), variable.nombre);
				b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int n = buttonView.getId();
						String nombre = botones.get(n);
						int pagina = MainActivity.mViewPager.getCurrentItem();
						if (isChecked) {
							servidor.plcs.get(pagina).ListaEscribir
									.add(new variableEscribir(nombre, 1));
						} else {

							servidor.plcs.get(pagina).ListaEscribir
									.add(new variableEscribir(nombre, 0));
						}
					}
				});
		//		mil.addView(t);
		//		mil.addView(b);
				mipanel.meter(b);
				variable.view = b;

				break;
			case 2://texto

				// mil.setOrientation(LinearLayout.HORIZONTAL);
				// t.setText(variable.nombre);
				TextView valor1 = new TextView(MainActivity.ctx);
				
				if (variable.valor != null)
					valor1.setText(variable.nombre + "kkkk   "
							+ variable.valor.toString());
				// mil.addView(t);
				// mil.addView(valor1);
			//	milayout.addView(valor1);
				mipanel.meter(valor1);
				milayout.setLayoutParams(lp);
				variable.view = valor1;

				break;
			case 3://editable
				mil.setOrientation(LinearLayout.HORIZONTAL);
				t.setText(variable.nombre);
				t.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 40f));

				final EditText valorEdit = new EditText(MainActivity.ctx);
				valorEdit.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 50f));
				
//				valorEdit.setMinimumWidth(200);

				Button aceptar = new Button(MainActivity.ctx);
				aceptar.setId(i);
				Resources res = MainActivity.ctx.getResources();
				Drawable myImage = res.getDrawable(android.R.drawable.btn_star);
				aceptar.setBackground(myImage);
				aceptar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 10f));

				botones.put(aceptar.getId(), variable.nombre);
				aceptar.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (!(valorEdit.getText().equals(""))) {
						int n = v.getId();
						double valor = Double.parseDouble(valorEdit.getText()
								.toString());
						String nombre = botones.get(n);
						int pagina = MainActivity.mViewPager.getCurrentItem();
						servidor.plcs.get(pagina).ListaEscribir.add(new variableEscribir(nombre, valor));
						}
						else {
							Toast toast = Toast.makeText(MainActivity.ctx, "Debe escribir un valor",
									Toast.LENGTH_SHORT);
						}
				//		 MainActivity.mViewPager.getWindowToken().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
									}
				});

				mil.addView(t);
				mil.addView(valorEdit);
				mil.addView(aceptar);
				milayout.addView(mil);
				milayout.setLayoutParams(lp);
				variable.view = valorEdit;

				break;
			case 1://barra deslizable
				  final String varnom =variable.nombre;

				  final TextView consigna = new  TextView(MainActivity.ctx);
				  consigna.setText(variable.valor.toString());
				  
				  SeekBar sb = new SeekBar(MainActivity.ctx);
				  sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				  
					  int progressChanged = 0;
				  
				  public void onProgressChanged(SeekBar seekBar, int progress,
				  boolean fromUser) { 
					  progressChanged = progress; 
					  consigna.setText(varnom +"  "+progress);

					  }				  
				  public void onStartTrackingTouch(SeekBar seekBar) {
				  }
				  
				  public void onStopTrackingTouch(SeekBar seekBar) {
				  seekBar.setSecondaryProgress(seekBar.getProgress());
				  int pagina = MainActivity.mViewPager.getCurrentItem();
				  servidor.plcs.get(pagina).ListaEscribir.add(new variableEscribir(varnom, seekBar.getProgress()));
				  
				  }
				  });
				  
				  mil.setOrientation(LinearLayout.VERTICAL); 
				  mil.addView(consigna);
				  mil.addView(sb); 
				  milayout.addView(mil); 
				  variable.view = sb;
				 
				break;
			case 5://plot

				if (variable.historial.buffer.length > 0) {

					XYPlot miplot = new XYPlot(MainActivity.ctx,
							variable.nombre);
					plotserie serie = new plotserie(variable);
					// Plot.RenderMode.USE_BACKGROUND_THREAD
					miplot.setDomainStepMode(XYStepMode.SUBDIVIDE);
					miplot.setDomainStepValue(variable.plotlong);

					// miplot.setRangeStepMode(XYStepMode.SUBDIVIDE);
					// miplot.setRangeStepValue(5);

					miplot.setRangeValueFormat(new DecimalFormat("#######.#"));
					miplot.setDomainValueFormat(new DecimalFormat("#######"));

					PointLabelFormatter plf = new PointLabelFormatter(
							Color.rgb(0, 0, 0));
					LineAndPointFormatter formatter1 = new LineAndPointFormatter(
							Color.LTGRAY, Color.rgb(0, 0, 0), null, plf);

					miplot.addSeries(serie, formatter1);
					milayout.addView(miplot);

					variable.view = miplot;
				}
				break;

			default:
				break;
			}

		}
		milayout.requestFocus();
		return milayout;
	}

	public static void actualiza(item variable) {
		if (variable.valor != null) {

			switch (variable.representacion) {

			case 4:// es de escritura
				break;
			case 2:
				TextView t = (TextView) (variable.view);
				t.setText(variable.nombre + "   " + variable.valor.toString()+ " "+variable.dim);
				variable.view.invalidate();
				break;

			case 1:// es de escritura
				break;
			case 5:
				XYPlot miplot = (XYPlot) variable.view;
				miplot.redraw();
				break;

			default:
				break;
			}
		}

	}

}
