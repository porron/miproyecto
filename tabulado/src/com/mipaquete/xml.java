package com.mipaquete;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import libreria.ItemHistorico;
import libreria.historico;
import libreria.item;
import libreria.variableEscribir;

import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class xml {
	public static Document documentoXML;
	public static Element nodoservidor;

	public static void crearDocumento() {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			documentoXML = docBuilder.newDocument();
			nodoservidor = documentoXML.createElement("Servidor");

		} catch (Exception e) {
			System.out.println("Error : " + e);
		}
	}

	public static void escribirXml(Context context) {
		try {

			TransformerFactory transformerfactory = TransformerFactory
					.newInstance();
			Transformer transformer;

			transformer = transformerfactory.newTransformer();

			DOMSource source = new DOMSource(documentoXML);
			FileOutputStream _stream;
			_stream = context.openFileOutput("confservidor.xml",
					Context.MODE_PRIVATE);

			StreamResult result = new StreamResult(_stream);
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean leerXml(Context context) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// File p =context.getFilesDir();
			// AssetManager am = context.getAssets();
			// InputStream is = am.open("confservidor.xml");
			FileInputStream fin = context.openFileInput("confservidor.xml");
			DocumentBuilder db = dbf.newDocumentBuilder();
			documentoXML = db.parse(fin);
			Log.e("leerxml", documentoXML.toString());
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			return false;
		} catch (SAXException se) {
			se.printStackTrace();
			return false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
		return true;
	}

	public static Element generarPlc(plc miplc) {
		
// forma el xml para escribirlo

		item mi;
		alarma ma;

		Element nodoplc = documentoXML.createElement("plc");
		nodoplc.setAttribute("id", miplc.getID());
		nodoplc.setAttribute("nombre", miplc.getnombre());
		nodoplc.setAttribute("ip", miplc.getip());
		nodoplc.setAttribute("refresco", Integer.toString(miplc.getrefresco()));

		for (int i = 0; i < miplc.variables.size(); i++) {
			mi = (item) miplc.variables.get(i);

			Element nodoitem = documentoXML.createElement("item");
			nodoitem.setAttribute("nombre", mi.nombre);
			// nodoitem.setAttribute("tipoProtocolo",Integer.toString(mi.));
			nodoitem.setAttribute("rango", Integer.toString(mi.rango));
			nodoitem.setAttribute("tipo", Integer.toString(mi.tipoDato));
			nodoitem.setAttribute("offset", Integer.toString(mi.offset));
			nodoitem.setAttribute("representacion",
					Integer.toString(mi.representacion));
			if (mi.historial != null)
				nodoitem.setAttribute("historico",
						Integer.toString(mi.historial.size()));
			else
				nodoitem.setAttribute("historico", "0");
			nodoitem.setAttribute("plotlong", Integer.toString(mi.plotlong));
			nodoitem.setAttribute("granulado",
					Double.toString(mi.granulado));
			nodoitem.setAttribute("max", Double.toString(mi.max));
			nodoitem.setAttribute("min", Double.toString(mi.min));
			if(!(mi.dim==null)) nodoitem.setAttribute("dim", mi.dim);
			if(!(mi.panel==null))nodoitem.setAttribute("panel", mi.panel);

			nodoplc.appendChild(nodoitem);
		}
		
		for (int i = 0; i < miplc.ListaAlarmas.size(); i++) {
			ma = (alarma) miplc.ListaAlarmas.get(i);
			Element nodoitem = documentoXML.createElement("alarma");
			nodoitem.setAttribute("nombre", ma.nombre);
			nodoitem.setAttribute("fuenteDato1", ma.fuenteDato1);
			nodoitem.setAttribute("fuenteDato2", ma.fuenteDato2);
			nodoitem.setAttribute("operador", Integer.toString(ma.operador));
			nodoplc.appendChild(nodoitem);
		}
		for (int i = 0; i < miplc.paneles.size(); i++) {
			Element nodopanel = documentoXML.createElement("panel");
			nodopanel.setAttribute("titulo", miplc.paneles.get(i).titulo
					.getText().toString());
			nodoplc.appendChild(nodopanel);
		}

		return nodoplc;
	}

	public static void generarServidor() {

		crearDocumento();
		Iterator it = servidor.plcs.iterator();
		// nodoservidor.setAttribute("nombre", servidor.nombre);
		// nodoservidor.setAttribute("password", servidor.password);

		while (it.hasNext()) {
			plc e = (plc) it.next();
			nodoservidor.appendChild(generarPlc(e));
		}
		documentoXML.appendChild(nodoservidor);
	}

	public static void parsearDocumento() {

		Element docEle = documentoXML.getDocumentElement();

		// servidor.nombre=docEle.getAttribute("nombre");
		// servidor.password=docEle.getAttribute("password");
		NodeList nl = docEle.getElementsByTagName("plc");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element elemento = (Element) nl.item(i);
				servidor.plcs.add(obtenerPlc(elemento));
			}
		}
	}

	// Devuelve un objeto plc generado con los datos de un elemento XML
	public static plc obtenerPlc(Element elemento) {
		plc miplc = new plc();
		miplc.id = elemento.getAttribute("id");
		miplc.nombre = elemento.getAttribute("nombre");
		miplc.ip = elemento.getAttribute("ip");
		miplc.refresco = Integer.parseInt(elemento.getAttribute("refresco"));

		java.util.ArrayList<item> variables = new ArrayList<item>();
		java.util.ArrayList<alarma> alarmas = new ArrayList<alarma>();
		java.util.ArrayList<panel> paneles = new ArrayList<panel>();

		// Map <String,item> variables = new HashMap <String,item> ();

		/*
		 * leer las variables
		 */
		NodeList nl = elemento.getElementsByTagName("item");
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			item variable = new item();
//			variable.plc = miplc.id;
			variable.nombre = el.getAttribute("nombre");
			variable.rango = Integer.parseInt(el.getAttribute("rango"));
			variable.offset = Integer.parseInt(el.getAttribute("offset"));
			variable.tipoDato = Integer.parseInt(el.getAttribute("tipo"));
			variable.granulado = Double.parseDouble(el
					.getAttribute("granulado"));


			variable.representacion = Integer.parseInt(el
					.getAttribute("representacion"));
			int size;
			if (!el.getAttribute("historico").equals("0")) {
				size = Integer.parseInt(el.getAttribute("historico"));
				variable.historial = new historico(size);
				ItemHistorico mitem = new ItemHistorico(new Double(0),
						new Date());
				variable.historial.meter(mitem);
		
			}

			
			if (el.getAttribute("plotlong")!= null && !el.getAttribute("plotlong").equals("")) {
				size = Integer.parseInt(el.getAttribute("plotlong"));
				variable.plotlong = size;
			}
				
			if (el.getAttribute("max")!= null &&  !el.getAttribute("max").equals("")) {
				double max = Double.parseDouble(el.getAttribute("max"));
				variable.max = max;
			}
			if (el.getAttribute("min")!= null && !el.getAttribute("min").equals("")) {
				double min = Double.parseDouble(el.getAttribute("min"));
				variable.min = min;
			}
			if (el.getAttribute("dim")!= null && !el.getAttribute("dim").equals("")) {
				variable.dim = el.getAttribute("dim");
			}
			if (el.getAttribute("panel")!= null && !el.getAttribute("panel").equals("")) {
				variable.panel = el.getAttribute("panel");
			}

			// variable.representacion=el.getAttribute("representacion");
			variables.add(variable);
		}

		miplc.variables = variables;
		miplc.ListaEscribir = new Vector<variableEscribir>();

		nl = elemento.getElementsByTagName("alarma");
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			int operador = Integer.parseInt(el.getAttribute("operador"));
			alarma mialarma = new alarma(el.getAttribute("nombre"),
					el.getAttribute("fuenteDato1"),
					el.getAttribute("fuenteDato2"), operador);
			alarmas.add(mialarma);
		}
		miplc.ListaAlarmas = alarmas;

		nl = elemento.getElementsByTagName("panel");
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			String titulo = el.getAttribute("titulo");
			panel mipanel = new panel(MainActivity.ctx);
			mipanel.tiltular(titulo);
			paneles.add(mipanel);
		}
		miplc.paneles = paneles;

		return miplc;

	}

	public static String toString(Document doc) {
		/*
		 * try { StringWriter sw = new StringWriter(); TransformerFactory tf =
		 * TransformerFactory.newInstance(); Transformer transformer =
		 * tf.newTransformer();
		 * transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
		 * "yes"); transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		 * transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		 * transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		 * 
		 * Log.e("xml", "empiezo a transformar");
		 * 
		 * transformer.transform(new DOMSource(doc), new StreamResult(sw));
		 * Log.e("xml", sw.toString());
		 * 
		 * return sw.toString(); } catch (Exception ex) { throw new
		 * RuntimeException("Error converting to String", ex); } }
		 */
		String s = "";
		try {
			//AssetManager am = MainActivity.ctx.getAssets();
			//InputStream is = am.open("confservidor.xml");
			 FileInputStream fin =
			 MainActivity.ctx.openFileInput("confservidor.xml");
						BufferedReader in = new BufferedReader(new InputStreamReader(fin));
			String Cadena;
			while ((Cadena = in.readLine()) != null) {
				s = s + Cadena;
			}
		} catch (Exception ex) {
			Log.e("xml", "Error al leer fichero desde memoria interna");
		}
		return s;
	}

}
