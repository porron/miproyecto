package com.mipaquete;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import android.view.ViewGroup;
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
		LinearLayout.LayoutParams lp_wrap = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_wrap.gravity = Gravity.CENTER_VERTICAL;
		LinearLayout.LayoutParams lp_fill = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams layaout_milayout = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		milayout.setLayoutParams(layaout_milayout);
//		milayout.setBackgroundColor(Color.BLUE);
		for (int i = 0; i < mp.paneles.size(); i++)
		{
			mp.paneles.get(i).colocado=false;
			mp.paneles.get(i).cinta.removeAllViews();
			if (mp.paneles.get(i).getParent() != null)
			((ViewGroup)mp.paneles.get(i).getParent()).removeView(mp.paneles.get(i));
		}

		for (int i = 0; i < mp.variables.size(); i++) {


			item variable = (item) mp.variables.get(i);
			LinearLayout mil = new LinearLayout(MainActivity.ctx);
			mil.setPadding(8, 0, 0, 8);
			TextView t = new TextView(MainActivity.ctx);
			switch (variable.representacion) {

			case 4:// boton

				// mil.setOrientation(LinearLayout.HORIZONTAL);
				// t.setText(variable.nombre);
				final Number valor = variable.valor;
				ToggleButton b = new ToggleButton(MainActivity.ctx);
				b.setId(i);
				b.setText(variable.nombre);
				b.setGravity(Gravity.CENTER);
				// b.setLayoutParams(LinearLayout.)
				// Drawable myI = MainActivity.ctx.getResources().getDrawable(
				// android.R.drawable.button_onoff_indicator_off);
				// b.setBackground(myI);

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
				mil.addView(b);
				mil.setLayoutParams(lp_wrap);
				variable.view = b;

				break;
			case 2:// texto

				// mil.setOrientation(LinearLayout.HORIZONTAL);
				// t.setText(variable.nombre);
				TextView titulo = new TextView(MainActivity.ctx);
				TextView valor1 = new TextView(MainActivity.ctx);
				titulo.setGravity(Gravity.CENTER);
				valor1.setGravity(Gravity.CENTER);

				titulo.setText(variable.nombre+" :");
				titulo.setLines(1);
				titulo.setTextColor(Color.GRAY);
				valor1.setText(variable.valor+" "+variable.dim);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(i, i);
				
				mil.addView(titulo);
				mil.addView(valor1);
				mil.setLayoutParams(lp_wrap);
				variable.view = valor1;

				break;
			case 3:// editable
				mil.setOrientation(LinearLayout.HORIZONTAL);
				t.setText(variable.nombre);
				t.setTextColor(Color.DKGRAY);
				t.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						0.4f));

				final EditText valorEdit = new EditText(MainActivity.ctx);
				valorEdit.setLayoutParams(new LinearLayout.LayoutParams(
						120, LayoutParams.WRAP_CONTENT,
						0.4f));

				// valorEdit.setMinimumWidth(200);

				Button aceptar = new Button(MainActivity.ctx);
				aceptar.setId(i);
				Resources res = MainActivity.ctx.getResources();
				Drawable myImage = res.getDrawable(android.R.drawable.btn_default_small);
				aceptar.setBackground(myImage);
				aceptar.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						0.2f));

				botones.put(aceptar.getId(), variable.nombre);
				aceptar.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (!(valorEdit.getText().equals(""))) {
							int n = v.getId();
							double valor = Double.parseDouble(valorEdit
									.getText().toString());
							String nombre = botones.get(n);
							int pagina = MainActivity.mViewPager
									.getCurrentItem();
							servidor.plcs.get(pagina).ListaEscribir
									.add(new variableEscribir(nombre, valor));
							Toast toast = Toast.makeText(MainActivity.ctx,
									"Valor enviado",
									Toast.LENGTH_SHORT);
						} else {
							Toast toast = Toast.makeText(MainActivity.ctx,
									"Debe escribir un valor",
									Toast.LENGTH_SHORT);
						}
						// MainActivity.mViewPager.getWindowToken().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					}
				});

				mil.addView(t);
				mil.addView(valorEdit);
				mil.addView(aceptar);
				if (!variable.panel.equals("")) mil.setLayoutParams(lp_wrap);
				else mil.setLayoutParams(lp_fill);
				variable.view = valorEdit;

				break;
			case 1:// barra deslizable
				final String varnom = variable.nombre;

				final TextView consigna = new TextView(MainActivity.ctx);
				consigna.setText(varnom + "  " );

				SeekBar sb = new SeekBar(MainActivity.ctx);
				sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					int progressChanged = 0;

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						progressChanged = progress;
						consigna.setText(varnom + "  " + progress);

					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						seekBar.setSecondaryProgress(seekBar.getProgress());
						int pagina = MainActivity.mViewPager.getCurrentItem();
						servidor.plcs.get(pagina).ListaEscribir
								.add(new variableEscribir(varnom, seekBar
										.getProgress()));

					}
				});

				sb.setLayoutParams(new LinearLayout.LayoutParams(
						MainActivity.width, LayoutParams.WRAP_CONTENT));
				mil.setOrientation(LinearLayout.VERTICAL);
				mil.addView(consigna);
				mil.addView(sb);
				mil.setLayoutParams(lp_wrap);
				variable.view = sb;

				break;
			case 5:// plot

				if (variable.historial.marca>=0) {

					XYPlot miplot = new XYPlot(MainActivity.ctx,
							variable.nombre);
					plotserie serie = new plotserie(variable);
					miplot.getLegendWidget().setVisible(false);
					miplot.setGridPadding(0, 10, 10, 0);
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
					// ponemos el tamaño del plot a la anchura de la pantalla
					miplot.setLayoutParams(new LinearLayout.LayoutParams(
							MainActivity.width,MainActivity.width,
							0.5f));
					mil.setLayoutParams(lp_wrap);
					mil.addView(miplot);
					mil.setPadding(0, 0, 0, 0);
					variable.view = miplot;
				}
				break;

			default:
				break;
			}

			if (!variable.panel.equals("")) {
				int cont = 0;
				while (!mp.paneles.get(cont).titulo.getText().toString()
						.equals(variable.panel)) {
					cont = cont + 1;
				}
				// panel mipanel=mp.paneles.get(cont);
				LinearLayout.LayoutParams lp=(android.widget.LinearLayout.LayoutParams) mil.getLayoutParams();
				lp.gravity=Gravity.CENTER;
				mp.paneles.get(cont).meter(mil);
				if (!mp.paneles.get(cont).colocado) {
					mp.paneles.get(cont).colocado = true;
					milayout.addView(mp.paneles.get(cont));
				}
				
			} else
				milayout.addView(mil);
		}

		milayout.requestFocus();
		return milayout;
	}

	public static void actualiza(item variable) {

		switch (variable.representacion) {

		case 4:// es de escritura
			break;
		case 2:
			TextView t = (TextView) (variable.view);
			t.setText(Double.toString(variable.valor)+ " " + variable.dim);
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
