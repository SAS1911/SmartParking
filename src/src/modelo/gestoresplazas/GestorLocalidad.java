package modelo.gestoresplazas;

import anotacion.Programacion2;
import list.IList;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

@Programacion2 (
		nombreAutor1 = "Philip Daniel",
		apellidoAutor1 = "Salov Draganov",
		emailUPMAutor1 = "philip.salov@alumnos.upm.es",
		nombreAutor2 = "Samuel Andrés",
		apellidoAutor2 = "Sánchez Pérez",
		emailUPMAutor2 = "samuel.sanchez@alumnos.upm.es"
		)

//TO-DO alumno obligatorio

public class GestorLocalidad {
	
	private GestorZona[][] gestoresZonas;

	//CONSTRUCTOR
	public GestorLocalidad(int[][] plazas, double[][] precios) {
		gestoresZonas = new GestorZona[precios.length][precios[0].length];//Inicializamos el tamaño 
		for(int i=0; i<precios.length; i++) {
			for(int j=0; j<precios[i].length; j++) {
				gestoresZonas[i][j] = new GestorZona (i, j, plazas[i][j], precios[i][j]);//Recorremos el array bidimensional e inicializamos cada GestorZona
			}
		}
	}
	
	public int getRadioMaxI() {
		return gestoresZonas.length-1;
	}
	
	public int getRadioMaxJ() {
		return gestoresZonas[0].length-1; 
	}
	
	public boolean existeZona(int i, int j) {
	
		return 0<=i && i<gestoresZonas.length && 0<=j && j<gestoresZonas[0].length; //Debe ser mayor o igual que 0 y menor o igual que el radio máximo
	}

	public boolean existeHuecoReservado(Hueco hueco, int i, int j) {
		return gestoresZonas[i][j].existeHuecoReservado(hueco);
	}

	public GestorZona getGestorZona(int i, int j) {
		return gestoresZonas[i][j];
	}
	
	//TO-DO alumno opcional
	
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera(int i, int j) {
		return gestoresZonas[i][j].getSolicitudesAtendidasListaEspera();//Se realiza la opreación en la clase hija GestorZona
	}
	
}
