package com.mipaquete;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import libreria.item;

import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

public class alarma {
	public boolean disparada;
	public String nombre;
	public boolean activa;
	String fuenteDato1;
	String fuenteDato2;
	item dato1;
	item dato2;
	int operador;

	enum estados {
		disparada, revisada, cargada
	};

	estados estado;

	public alarma( ) {

		// TODO Auto-generated constructor stub
	}
	
	public alarma(String nombre, String fdato1, String fdato2, int ope ) {
		this.nombre =nombre ;
		this.fuenteDato1=fdato1;
		this.fuenteDato2=fdato2;
		this.operador=ope;
		// TODO Auto-generated constructor stub
	}

	public void procesar() {
		if (dato1 != null && dato2 != null) {
			boolean valor = false;
			switch (operador) {

			case 1:
				valor = dato1.valor > dato2.valor;
				break;
			case 2:
				valor = dato1.valor>= dato2.valor;
				break;
			case 3:
				valor = dato1.valor < dato2.valor;
				break;
			case 4:
				valor = dato1.valor <= dato2.valor;
				break;
			case 5:
				valor = dato1.valor == dato2.valor;
				break;
			default:
				break;
			}
			disparada = valor;
			if (valor)
				estado = estados.disparada;
			if (estado == estados.revisada && !valor)
				estado = estados.cargada;
		}

	}

	static public void apuntarAlarmas() {

		boolean buscar = true;
		for (int i = 0; i < servidor.plcs.size(); i++) {
			plc miplc = servidor.plcs.get(i);
			for (int ii = 0; ii < miplc.ListaAlarmas.size(); ii++) {
				alarma mialarma = miplc.ListaAlarmas.get(ii);

				buscar = true;
				int cont = 0;
				while (buscar) {
					if (miplc.variables.get(cont).nombre
							.equals(mialarma.fuenteDato1)) {
						buscar = false;
						mialarma.dato1 = servidor.plcs.get(i).variables
								.get(cont);
					} else
						cont = cont + 1;
				}
				buscar = true;
				cont = 0;
				while (buscar) {
					if (miplc.variables.get(cont).nombre
							.equals(mialarma.fuenteDato2)) {
						buscar = false;
						mialarma.dato2 = servidor.plcs.get(i).variables
								.get(cont);
					} else
						cont = cont + 1;
				}

			}

		}
	
	}
static public  String [] rellenar_lista (plc mp){
	String[] lista = new String[ mp.ListaAlarmas.size()];
	for (int i = 0; i < mp.ListaAlarmas.size(); i++) {
		String texto;
		texto=(String) (mp.ListaAlarmas.get(i).nombre + "    ");
		texto=(String) (texto+mp.ListaAlarmas.get(i).fuenteDato1+" ");
		String op="" ;
		switch (mp.ListaAlarmas.get(i).operador) {
		case 1:
			op = (String) ">"; 
			break;
		case 2:
			op = (String) ">="; 
			break;
		case 3:
			op = (String) "<"; 
			break;
		case 4:
			op = (String) "<="; 
			break;
		case 5:
			op = (String) "="; 
			break;
		default:
			break;
		}
		texto=(String) (texto+" "+op+" "+mp.ListaAlarmas.get(i).fuenteDato2);
		lista[i]=texto;
	}
	return lista;
}

}
