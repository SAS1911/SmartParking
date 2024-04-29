package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import modelo.gestoresplazas.GestorLocalidad;
import modelo.vehiculos.Vehiculo;

public class SolicitudReservaAnticipada extends SolicitudReserva{
	
	private int i;
	private int j;
	private LocalDateTime tI;
	private LocalDateTime tF;
	private Vehiculo vehiculo;

	
	public SolicitudReservaAnticipada(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo) {
		// HECHO?
		super(i, j, tI, tF, vehiculo);
	}
	
	@Override
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		if(esValida(gestor)) {
			super.gestionarSolicitudReserva(gestor);
			if(!gestor.getGestorZona(i,j).existeHuecoReservado(getHueco()))
				gestor.getGestorZona(i,j).meterEnListaEspera(new SolicitudReservaAnticipada (i, j, tI, tF, vehiculo));	
		}
	}

}
