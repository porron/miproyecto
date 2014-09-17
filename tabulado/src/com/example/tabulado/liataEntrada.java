package com.example.tabulado;

public class liataEntrada {
	private alarma.estados estado;
	private String texto; 

	public liataEntrada (alarma.estados estado, String texto) { 
	    this.estado = estado; 
	    this.texto = texto; 

	}

	public String get_texto() { 
	    return texto; 
	}

	public int get_idImagen() {
		int idImagen=0;
		if (estado== alarma.estados.cargada) idImagen= R.drawable.btn_toggle_on;
		if (estado== alarma.estados.disparada) idImagen= R.drawable.indicator_input_error;
		if (estado== alarma.estados.revisada) idImagen= R.drawable.btn_check_buttonless_on;
	    return idImagen; 
	} 
}
