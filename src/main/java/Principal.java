import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Principal {

	private static ArrayList<Jugadores> listaJugadores = new ArrayList<Jugadores>();
	private static ArrayList<Equipos> listaEquipos = new ArrayList<Equipos>();
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
		
		// TODO se tienen dos ficheros csv y se tienen que juntar en un fichero xml
		leerJugadores();
		leerEquipos();
		crearXML();
		// TODO despues mostrar los jugadores del liceo
		buscarJugadoresLiceo();

	}

	private static void buscarJugadoresLiceo() {
		// TODO busqueda entre los equipos con nombre liceo y luego iguala con el id de equipo de los jugadores
		int idLiceo= 0;
		
		for(int i =0; i<listaEquipos.size();i++) {
			if(listaEquipos.get(i).getNombreEq().equals("Hockey Club Liceo") ) {
				idLiceo = listaEquipos.get(i).getCodigoEq();
			}
		}
		for(int i = 0; i<listaJugadores.size(); i++) {
			if(listaJugadores.get(i).getId_equipo() == idLiceo) {
				System.out.println("Los jugadores Liceo son: " + listaJugadores.get(i).getNombre());
			}
		}
		
	}

	private static void crearXML() throws ParserConfigurationException, TransformerException {
		// TODO creacion de un fichero XML en el que se juntan los dos arrays que tienen el csv
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factoria.newDocumentBuilder();
		Document documento = db.newDocument();
		
		
		Element okliga = documento.createElement("okliga");
		documento.appendChild(okliga);
		
		//	CREACION DE LA PRIMERA PARTE DEL XML (JUGADORES)
		Element jugadores = documento.createElement("Jugadores");
		okliga.appendChild(jugadores);
		for(int i = 0; i<listaJugadores.size(); i++) {
			Element jugador = documento.createElement("jugador");
			jugadores.appendChild(jugador);
			
			Element codigo = documento.createElement("codigo");
			codigo.setTextContent(String.valueOf(listaJugadores.get(i).getCodigo()));
			jugador.appendChild(codigo);
			
			Element nombre = documento.createElement("nombre");
			nombre.setTextContent(listaJugadores.get(i).getNombre());
			jugador.appendChild(nombre);
			
			Element id_equipo = documento.createElement("id_equipo");
			id_equipo.setTextContent(String.valueOf(listaJugadores.get(i).getId_equipo()));
			jugador.appendChild(id_equipo);
		}
		
		//	CREACION DE LA SEGUNDA PARTE DEL XML (EQUIPOS)
		Element equipos = documento.createElement("Equipos");
		okliga.appendChild(equipos);
		for(int i = 0; i<listaEquipos.size();i++) {
			Element equipo = documento.createElement("equipo");
			equipos.appendChild(equipo);
			
			Element codigoEq = documento.createElement("codigoEq");
			codigoEq.setTextContent(String.valueOf(listaEquipos.get(i).getCodigoEq()));
			equipo.appendChild(codigoEq);
			
			Element nombreEq = documento.createElement("nombreEq");
			nombreEq.setTextContent(listaEquipos.get(i).getNombreEq());
			equipo.appendChild(nombreEq);
			
			Element localidad = documento.createElement("localidad");
			localidad.setTextContent(listaEquipos.get(i).getLocalidad());
			equipo.appendChild(localidad);
		}
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource dom = new DOMSource(documento);
		StreamResult sr = new StreamResult(new File("C:\\PRUEBAS\\PRACTICA3\\okliga.xml"));
		transformer.transform(dom, sr);
		
		
	}

	private static void leerEquipos() throws IOException {
		// TODO lectura de fichero csv en el que se guarda en un arrayList
		Path ficheroEquipos = Paths.get("C:\\PRUEBAS\\PRACTICA3\\equipos.csv");
		BufferedReader br = Files.newBufferedReader(ficheroEquipos);
		String linea;
		String[] valores;
		//	ALMACENAMOS LA INFO DEL CSV EN EL ARRAY
		while ((linea = br.readLine())!= null) {
			valores = linea.split(",");
			Equipos equipo1 = new Equipos(Integer.parseInt(valores[0]), valores[1], valores[2]);
			listaEquipos.add(equipo1);
			System.out.println(linea);
		}
		
	}

	private static void leerJugadores() throws IOException {
		// TODO lectura de fichero csv en el que se guarda en un arrayList
		Path ficheroJugadores = Paths.get("C:\\PRUEBAS\\PRACTICA3\\jugadores.csv");
		BufferedReader br = Files.newBufferedReader(ficheroJugadores);
		String linea;
		String[] valores;
		//	SE ALMACENA EN EL ARRAY LA INFORMACION DEL CSV
		while((linea = br.readLine()) != null) {
			valores= linea.split(",");	//	SE DEBE MIRAR QUÉ SEPARA EN EL ARCHIVO
			Jugadores jugador1 = new Jugadores(Integer.parseInt(valores[0]),valores[1],Integer.parseInt(valores[2]));
			listaJugadores.add(jugador1);
			System.out.println(linea);
		}
		
		
	}

}
