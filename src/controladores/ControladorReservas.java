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

	public ControladorReservas(int[][] plazas, double[][] precios) {
		//HECHO?
		registroReservas = new Reservas(); //INICIALIZAMOS registro vacío
		gestorLocalidad = new GestorLocalidad(plazas, precios); //INICIALIZAMOS gestor con las plazas y los precios
	}


	//PRE: la solicitud es válida
	public int hacerReserva(SolicitudReserva solicitud) throws SolicitudReservaInvalida {
		//HECHO?
		if(!solicitud.esValida(gestorLocalidad)) {//En caso de ser inválida la solicitud
			throw new SolicitudReservaInvalida ("Lo sentimos, su reserva es Inválida");//Lanzamos la excepción
		}
		solicitud.gestionarSolicitudReserva(gestorLocalidad);//GESTIONAMOS la solicitud
		if(solicitud.getHueco()!=null){//Una vez gestionada se comprueba si se ha podido ASIGNAR UN HUECO 
			return registroReservas.registrarReserva(solicitud);//Se registra ESTAMOS SEGUROS DE DEVOLVER ESTO?
		}else return -1;
	}

	public Reserva getReserva(int numReserva) {
		//HECHO?
		return registroReservas.obtenerReserva(numReserva);
	}

	//PRE: la plaza dada está libre y la reserva está validada
	public void ocuparPlaza(int i, int j, int numPlaza, int numReserva, Vehiculo vehiculo) throws PlazaOcupada, ReservaInvalida {
		//HECHO?
		if(!esValidaReserva(i,j,numPlaza,numReserva,vehiculo.getMatricula())) //Valoramos si la reserva existe
			throw new ReservaInvalida("Lo sentimos, su reserva es inválida");//En otro caso lanzamos excepción
		Plaza plaza = getReserva(numReserva).getHueco().getPlaza();
		if(plaza.getVehiculo()!=null)//Valoramos is la plaza está ocupada
			throw new PlazaOcupada("Lo sentimos, la plaza seleccionada está ocupada");//Y en ese caso lanzamos excepción
		plaza.setVehiculo(vehiculo);
	}


	//TO-DO alumno opcional

	public void desocuparPlaza(int numReserva) {
		//HECHO?
		getReserva(numReserva).liberarHuecoReservado();//Incovamos la reserva y liberamos el hueco
	}

	public void anularReserva(int numReserva) {
		//HECHO?
		desocuparPlaza(numReserva);//Desocupamos la plaza
		registroReservas.borrarReserva(numReserva);//Borramos la reserva del registro
	}

		
	// PRE (no es necesario comprobar): todas las solicitudes atendidas son válidas.
	public IList<Integer> getReservasRegistradasDesdeListaEspera(int i, int j){
		//HECHO?
		IList<Integer> lista = new ArrayList<>();
		return null;
	}
}
