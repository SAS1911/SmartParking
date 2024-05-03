package controladores;

import controladores.excepciones.PlazaOcupada;
import controladores.excepciones.ReservaInvalida;
import controladores.excepciones.SolicitudReservaInvalida;
import list.IList;
import list.ArrayList;//Importamos ArrayList
import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.huecos.Plaza;
import modelo.reservas.EstadoValidez;
import modelo.reservas.Reserva;
import modelo.reservas.Reservas;
import modelo.reservas.solicitudesreservas.SolicitudReserva;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;
import modelo.vehiculos.Vehiculo;
import anotacion.Programacion2;

@Programacion2 (
		nombreAutor1 = "Philip Daniel",
		apellidoAutor1 = "Salov Draganov",
		emailUPMAutor1 = "philip.salov@alumnos.upm.es",
		nombreAutor2 = "Samuel Andrés",
		apellidoAutor2 = "Sánchez Pérez",
		emailUPMAutor2 = "samuel.sanchez@alumnos.upm.es"
		)

public class ControladorReservas {
	private Reservas registroReservas;
	private GestorLocalidad gestorLocalidad;

	public GestorLocalidad getGestorLocalidad() {
		return gestorLocalidad;
	}

	public Reservas getRegistroReservas() {
		return registroReservas;
	}

	public boolean esValidaReserva(int i, int j, int numPlaza, int numReserva, String noMatricula) {
		Reserva reserva = this.registroReservas.obtenerReserva(numReserva);
		if (reserva == null)
			return false;
		reserva.validar(i, j, numPlaza, noMatricula, gestorLocalidad);
		return reserva.getEstadoValidez() == EstadoValidez.OK;
	}

	//TO-DO alumno obligatorio

	//CONSTRUCTOR
	public ControladorReservas(int[][] plazas, double[][] precios) {
		registroReservas = new Reservas(); //Inicializamos registro vacío
		gestorLocalidad = new GestorLocalidad(plazas, precios); //Inizcializamos gestor con las plazas y los precios
	}


	//PRE: la solicitud es válida
	public int hacerReserva(SolicitudReserva solicitud) throws SolicitudReservaInvalida {
		int numReserva = -1;
		if(!solicitud.esValida(gestorLocalidad)) {//En caso de ser inválida la solicitud
			throw new SolicitudReservaInvalida ("Lo sentimos, su solicitud de reserva es Inválida.\n"
					+ "Pruebe a seleccionar:\n"
					+ "1. Otra zona.\n"
					+ "2. Otro intervalo de tiempo.\n"
					+ "O asegúrese de que su vehículo no está sancionado.");//Lanzamos la excepción
		}
		solicitud.gestionarSolicitudReserva(gestorLocalidad);//Gestionamos la solicitud
		if(solicitud.getHueco()!=null)//Una vez gestionada se comprueba si se ha podido asignar un hueco
			numReserva = registroReservas.registrarReserva(solicitud);//Se registra devolviendo el número de la reserva
		return numReserva;
	}

	public Reserva getReserva(int numReserva) {
		return registroReservas.obtenerReserva(numReserva);//Empleamos el método correspondiente a la clase Reserva
	}

	//PRE: la plaza dada está libre y la reserva está validada
	public void ocuparPlaza(int i, int j, int numPlaza, int numReserva, Vehiculo vehiculo) throws PlazaOcupada, ReservaInvalida {
		if(!esValidaReserva(i,j,numPlaza,numReserva,vehiculo.getMatricula())) //Valoramos si la reserva existe
			throw new ReservaInvalida("Lo sentimos, su reserva es Inválida.\n"
					+ "Pruebe a seleccionar:\n"
					+ "1. Otra zona.\n"
					+ "2. Otro intervalo de tiempo.\n"
					+ "O asegúrese de que su vehículo no está sancionado.");//En otro caso lanzamos excepción
		Plaza plaza = getReserva(numReserva).getHueco().getPlaza();
		if(plaza.getVehiculo()!=null)//Valoramos is la plaza está ocupada
			throw new PlazaOcupada("Lo sentimos, la plaza seleccionada está ocupada.\n"
					+ "Pruebe a seleccionar otra:" + gestorLocalidad.getGestorZona(i, j).getPlazas());//Y en ese caso lanzamos excepción
		plaza.setVehiculo(vehiculo);//En otro caso ocupamos la plaza con el vehículo
	}


	//TO-DO alumno opcional

	public void desocuparPlaza(int numReserva) {
		getReserva(numReserva).liberarHuecoReservado();//Liberamos la reserva del registroReservas
		getReserva(numReserva).getGestorZona().liberarHueco(getReserva(numReserva).getHueco());//Libreamos el hueco del gestorHuecos y de los huecosReservados
		getReserva(numReserva).getHueco().getPlaza().setVehiculo(null);//Quitamos finalmente el vehículo poniéndolo como null
	}

	public void anularReserva(int numReserva) {
		desocuparPlaza(numReserva);//Desocupamos la plaza
		registroReservas.borrarReserva(numReserva);//Borramos la reserva del registro
	}

		
	// PRE (no es necesario comprobar): todas las solicitudes atendidas son válidas.
	public IList<Integer> getReservasRegistradasDesdeListaEspera(int i, int j){
		IList<SolicitudReservaAnticipada> solicitudesSerAtendidas = gestorLocalidad.getSolicitudesAtendidasListaEspera(i, j);
		IList<Integer> listaEnteros = new ArrayList<>();
		
		SolicitudReservaAnticipada solicitud;
		int numReserva;
		
		for(int k=0; k<solicitudesSerAtendidas.size(); k++) {
			solicitud = solicitudesSerAtendidas.get(k);
			try {
				numReserva = hacerReserva(solicitud);//Hacemos la reserva de la solicitud que puede ser atendida
				listaEnteros.add(listaEnteros.size(), numReserva);//Añadimos el número de esta misma reserva
			} catch (IndexOutOfBoundsException e) {
				System.err.println(e.getMessage());
			} catch (SolicitudReservaInvalida e) {
				System.err.println("Lo sentimos, su solicitud de reserva es Inválida.\n"
						+ "Pruebe a seleccionar:\n"
						+ "1. Otra zona.\n"
						+ "2. Otro intervalo de tiempo.\n"
						+ "O asegúrese de que su vehículo no está sancionado.");
			}
		}
			
		solicitudesSerAtendidas = null;//Una vez hecho todo el registro, declaramos null la lista de solicitudes que pueden ser atendidas
		
		return listaEnteros;
	}
}
