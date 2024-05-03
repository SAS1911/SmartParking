package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import anotacion.Programacion2;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.vehiculos.Vehiculo;

@Programacion2 (
		nombreAutor1 = "Philip Daniel",
		apellidoAutor1 = "Salov Draganov",
		emailUPMAutor1 = "philip.salov@alumnos.upm.es",
		nombreAutor2 = "Samuel Andrés",
		apellidoAutor2 = "Sánchez Pérez",
		emailUPMAutor2 = "samuel.sanchez@alumnos.upm.es"
		)

public class SolicitudReserva {
	private int iZona;
	private int jZona;
	private LocalDateTime tInicial;
	private LocalDateTime tFinal;
	private Vehiculo vehiculo;
	private GestorZona gestorZona; // se inicializa al gestionar la solicitud
	private Hueco hueco; // se deja a null hasta que se completa la reserva

	//CONSTRUCTOR
	protected SolicitudReserva(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo) {
		this.iZona = i;
		this.jZona = j;
		this.tInicial = tI;
		this.tFinal = tF;
		this.vehiculo = vehiculo;
		this.hueco = null;
		this.gestorZona = null;
	}

	public String toString() {
		return "(Sol:" + iZona + " " + jZona + " " + tInicial.toLocalTime() + " " + tFinal.toLocalTime() 
		+ " " + this.vehiculo.getMatricula() +  ")";
	}
	
	public void setHueco(Hueco hueco) {
		this.hueco = hueco;		
	}

	public Hueco getHueco() {
		return hueco;
	}
	
	public void setGestorZona(GestorZona gestor) {
		this.gestorZona = gestor;		
	}
	
	public GestorZona getGestorZona() {
		return this.gestorZona;
	}
	
	public int getIZona() {
		return iZona;
	}

	public int getJZona() {
		return jZona;
	}

	public LocalDateTime getTInicial() {
		return tInicial;
	}

	public LocalDateTime getTFinal() {
		return tFinal;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}
	
	//TO-DO alumno obligatorio
	
	public boolean esValida(GestorLocalidad gestorLocalidad) {
		
		return gestorLocalidad.existeZona(iZona, jZona) && //Comprobaoms si la zona existe
				tInicial.compareTo(tFinal)<0 && !vehiculo.getSancionado();//Y si el tiempo de reserva es válido y el vehiculo no está sancionado
	}
	
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		setGestorZona(gestor.getGestorZona(iZona, jZona));//Inicializamos el GestorZona
		setHueco(gestorZona.reservarHueco(tInicial, tFinal));//Según la zona dada, intentamos reservar y guardamos el hueco en la solicitud
	}
}
