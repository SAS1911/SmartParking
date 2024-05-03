package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import anotacion.Programacion2;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.vehiculos.Vehiculo;

@Programacion2 (
		nombreAutor1 = "Philip Daniel",
		apellidoAutor1 = "Salov Draganov",
		emailUPMAutor1 = "philip.salov@alumnos.upm.es",
		nombreAutor2 = "Samuel Andrés",
		apellidoAutor2 = "Sánchez Pérez",
		emailUPMAutor2 = "samuel.sanchez@alumnos.upm.es"
		)

public class SolicitudReservaAnticipada extends SolicitudReserva{
	
	private int i;
	private int j;
	private LocalDateTime tI;
	private LocalDateTime tF;
	private Vehiculo vehiculo;

	//CONSTRUCTOR
	public SolicitudReservaAnticipada(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo) {
		super(i, j, tI, tF, vehiculo);//Inicializamos los atributos mediante el constructor de la clase padre
	}
	
	@Override
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {//Sobreescribimos el método de la clase padre
		if(esValida(gestor)) {
			super.gestionarSolicitudReserva(gestor);//Primero gestionamos la solicitud mediante el método de la clase padre
			if(!gestor.getGestorZona(i,j).existeHuecoReservado(getHueco()))//En caso de no poder reservarlo
				gestor.getGestorZona(i,j).meterEnListaEspera(new SolicitudReservaAnticipada (i, j, tI, tF, vehiculo));//Metemos la solicitud en la lista de espera	
		}
	}
}
