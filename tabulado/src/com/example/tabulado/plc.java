package com.example.tabulado;


import java.util.ArrayList;
import java.util.Vector;

import libreria.item;
import libreria.variableEscribir;


public class plc {
	public String id;
	public String nombre;
	public String ip;
	public int refresco;
	public java.util.ArrayList<item> variables;
	public Vector<variableEscribir> ListaEscribir;
	public java.util.ArrayList<alarma> ListaAlarmas;
	public java.util.ArrayList<panel> paneles;
	public comunicacion_asinc hilo_comunicacion;

	
	public plc() {
		int ind =servidor.plcs.size();

		this.id = Integer.toString(ind);
		this.nombre = "";
		this.refresco = 1000;
		this.ip = "0.0.0.0";
		this.variables = new ArrayList<item>();
		this.paneles = new ArrayList<panel>();
		this.ListaEscribir =  new Vector<variableEscribir>();
		this.ListaAlarmas= new ArrayList<alarma> ();
	}
	
	
	// Constructor de la clase
	public plc(String id, String nombre, String ip, int refresco,java.util.ArrayList<item> lista) {
		this.id = id;
		this.nombre = nombre;
		this.ip = ip;
		this.refresco = refresco;
		this.variables = new ArrayList<item>();
		this.paneles = new ArrayList<panel>();
		this.ListaEscribir =  new Vector<variableEscribir>();
		this.ListaAlarmas= new ArrayList<alarma> ();
		variables = lista;
 
	}

	// Metodos Setters y Getters
	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	public void setnombre(String enombre) {
		this.nombre = enombre;
	}

	public String getnombre() {
		return this.nombre;
	}

	public void setip(String ip) {
		this.ip = ip;
	}

	public String getip() {
		return this.ip;
	}

	public void setrefresco(int refresco) {
		this.refresco = refresco;
	}

	public int getrefresco() {
		return this.refresco;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Datos de descarga " + getID());
		sb.append("; nombre: " + getnombre());
		sb.append("; ruta: " + getip());
		return sb.toString();
	}
	
	public item buscar (String nombre){	
	boolean buscar = true;
	int		cont=0;
	while (buscar){
		if (this.variables.get(cont).nombre.equals(nombre)) buscar =false;
		else cont=cont+1;
	}
	return this.variables.get(cont);
}

}
