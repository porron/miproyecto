package com.mipaquete;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libreria.item;


public class  servidor {
	
	public static List<plc> plcs = new ArrayList<plc>();
	//public static HashMap<String, plc> plcs = new HashMap<String, plc>();
/*
    public static void servidor(){
    	super();
		xml.parsearDocumento();		
    }

*/	

	public static class listaplc {
		public String id;
		public String nombre;

		public listaplc(String id, String content) {
			this.id = id;
			this.nombre = content;
		}

		@Override
		public String toString() {
			return nombre;
		}
	}
	static item buscar_variable (String nombre) {
		boolean buscando =true ;
		int cont1=0;
		int cont2=0;
		plc miplc ;
		item mitem=null;
		 while (buscando){
			 miplc =servidor.plcs.get(cont1);
		 	cont2=0;
		 	while (buscando){
				if (miplc.variables.get(cont2).nombre.equals(nombre)) {
					buscando =false;
					mitem=miplc.variables.get(cont2);
				}
				else cont2=cont2+1;
		 	}
		 	cont1=cont1+1;	 	
		 }
		return mitem; 
	}
	 public static int plcDeVariable (String nombre) {
		boolean buscando =true ;
		int cont1=0;
		int cont2=0;
		plc miplc ;
		item mitem=null;
		 while (buscando){
			 miplc =servidor.plcs.get(cont1);
		 	cont2=0;
		 	while (buscando){
				if (miplc.variables.get(cont2).nombre.equals(nombre)) {
					buscando =false;
					mitem=miplc.variables.get(cont2);
				}
				else cont2=cont2+1;
		 	}
		 	cont1=cont1+1;	 	
		 }
		 int devuelto ;
		 if (!buscando) devuelto=cont1-1;
		 else devuelto=-1;
		return devuelto ;
	}

}
