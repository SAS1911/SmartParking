package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.gestoresplazas.huecos.GestorHuecos;
import modelo.vehiculos.Vehiculo;

public class SolicitudReservaInmediata extends SolicitudReserva {
	
	private int i;
	private int j;
	private LocalDateTime tI;
	private LocalDateTime tF;
	private Vehiculo vehiculo;
	private int radio;
	
	public SolicitudReservaInmediata(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo, int radio) {
		super(radio, j, tI, tF, vehiculo);
		this.radio = radio;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean esValida(GestorLocalidad gestor) {
		boolean res = super.esValida(gestor);//Comprobamos por la clase padre si los atributos generales son válidos
		if(res && radio>0 && radio<=gestor.getRadioMaxI() && radio<=gestor.getRadioMaxJ()) {
			
		}
		return res;
	}

	public void gestionarSolicitudReserva(GestorLocalidad gestor){
		GestorZona mejor; //zona candidata a ser escogida
		double mejorPrecio = gestor.getGestorZona(i, j).getPrecio(); //precio de zona candidata a ser escogida
		for(int a = 1; i<=radio; i++){
			for(int b = 0; int c = 0; b<a && c<a; b++; c++){
				if(gestor.getGestorZona(i-a+b, j+c).getPrecio()<mejorPrecio){
					mejor = gestor.getGestorZona(i-a+b, j+c).getPrecio;
					mejorPrecio = mejor.getPrecio();
				} 
			}
			for(int b = 0; int c = 0; b<a && c<a; b++; c++){
				if(gestor.getGestorZona(i+b, j+a-c).getPrecio()<mejorPrecio){
					mejor = gestor.getGestorZona(i+b, j+a-c).getPrecio;
					mejorPrecio = mejor.getPrecio();
				} 
			}
			for(int b = 0; int c = 0; b<a && c<a; b++; c++){
				if(gestor.getGestorZona(i+a-b, j-c).getPrecio()<mejorPrecio){
					mejor = gestor.getGestorZona(i+a-b, j-c).getPrecio;
					mejorPrecio = mejor.getPrecio();
				} 
			}
			for(int b = 0; int c = 0; b<a && c<a; b++; c++){
				if(gestor.getGestorZona(i-b, j-a+c).getPrecio()<mejorPrecio){
					mejor = gestor.getGestorZona(i-b, j-a+c).getPrecio;
					mejorPrecio = mejor.getPrecio();
				} 
			}
		}
	}
}
