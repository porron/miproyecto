package com.example.tabulado;

import java.util.List;

import libreria.item;

import com.androidplot.xy.XYSeries;


public class plotserie  implements XYSeries {


	        private int si;
	        private String title;
	        private item mivariable;

	        public plotserie(item variable) {
	        	
	        	si=variable.plotlong;
	        	title = variable.nombre;
	        	mivariable= variable;
	        	
	           }

	        @Override
	        public String getTitle() {
	            return title;
	        }

	        @Override
	        public int size() {
	            return si;
	        }

	        @Override
	        public Number getX(int index) {
	        	Number x=mivariable.historial.Getx(index);
	            return x;
	        }

	        @Override
	        public Number getY(int index) {
	        	Number y=mivariable.historial.Gety(index);
	            return y;
	        }
	    }
