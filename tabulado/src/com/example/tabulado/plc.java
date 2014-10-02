package com.example.tabulado;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import libreria.item;
import libreria.variableEscribir;


public class plc {
	// Variables
	public String id;
	public String nombre;
	public String ip;
	public int refresco;
//	public Map <String,item> variables;
	public java.util.ArrayList<item> variables;
	public Vector<variableEscribir> ListaEscribir;
	public java.util.ArrayList<alarma> ListaAlarmas;
	public comunicacion_asinc hilo_comunicacion;
//	public  Map <String,Double> ListaEscribir ;

	

	// Constructor de la clase
	public void plc(String id, String nombre, String ip, int refresco,java.util.ArrayList<item> lista) {
		this.id = id;
		this.nombre = nombre;
		this.ip = ip;
		this.refresco = refresco;
//		this.variables = new HashMap <String,item> ();
		this.variables = new ArrayList<item>();
		this.ListaEscribir =  new Vector<variableEscribir>();
//		this.ListaEscribir = new HashMap <String,Double> ();
		variables = lista;

	}

	// Metodos Setters y Getters
	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	public void setnombre(String titulo) {
		this.nombre = nombre;
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
