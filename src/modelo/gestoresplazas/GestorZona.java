package modelo.gestoresplazas;

import java.time.LocalDateTime;
import java.util.Arrays;

import list.IList;
import list.ArrayList;

import modelo.gestoresplazas.huecos.GestorHuecos;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.gestoresplazas.huecos.Plaza;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

public class GestorZona {
	private int iZona;
	private int jZona;
	private Plaza[] plazas;
	private double precio;
	private IList<SolicitudReservaAnticipada> listaEspera;
	private GestorHuecos gestorHuecos;
	private IList<Hueco> huecosReservados;
	
	
	public int getI() {
		return iZona;
	}
	
	public int getJ() {
		return jZona;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public String getId() {
		return "z" + iZona + ":" + jZona;
	}
	
	public String getEstadoHuecosLibres() {
		return this.gestorHuecos.toString();
	}
	
	public String getEstadoHuecosReservados() {
		return this.huecosReservados.toString();
	}
	
	public String getListaEspera() {
		return this.listaEspera.toString();
	}
	
	public String getPlazas() {
		return Arrays.toString(this.plazas);
	}
	
	public String toString() {
		return getId() + ": " + getEstadoHuecosReservados();
	}
	
	//TO-DO alumno obligatorios
	
	//CONSTRUCTOR
	public GestorZona(int i, int j, int noPlazas, double precio){
		iZona = i;
		jZona = j;
		plazas = new Plaza[noPlazas];//Inicializamos el vector de plazas con el número de plazas indicado
		for(int k=0; k<plazas.length; k++) //Y a cada plaza le damos su número
			plazas[k] = new Plaza(k);
		this.precio = precio;
		listaEspera = new ArrayList<SolicitudReservaAnticipada>();//Inicializamos la lista de espera como ArrayList
		huecosReservados =  new ArrayList<Hueco>();//Al igual que huecos reservados
		gestorHuecos = new GestorHuecos(plazas);//Y el gestor de huecos mediante su constructor predeterminado
	}
	
	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		//Creamos la variable hueco, no hace falta comprobar si existe ya que ya lo hace el método reservarHueco(...)
		Hueco hueco = gestorHuecos.reservarHueco(tI, tF);//Seleccionamos el hueco que vamos a reservar, será null si no es posible
		if(hueco!=null)//En caso de existir:
			huecosReservados.add(huecosReservados.size(), hueco);//Lo añadimos a la lista de huecos reservados
		return hueco;
	}
	
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {//Método implementado en SolicitudReservaAnticipada
		listaEspera.add(listaEspera.size(), solicitud);//Si una solicitud no es posible admitirla, la metemos en la lista de espera
		
	}
	
	public boolean existeHuecoReservado(Hueco hueco) {
		boolean existe = false;
			for(int i=0; i<huecosReservados.size() && !existe; i++) {//Recorremos el array
//				if(huecosReservados.get(i).equals(hueco)) existe = true;//El equals parece que está sobreescrito incorrectamente, da error en el JUnit
				if(huecosReservados.get(i).getPlaza() == hueco.getPlaza() &&//Comparamos la plaza
						huecosReservados.get(i).gettF() == hueco.gettF() &&//El tiempo final
						huecosReservados.get(i).gettI() == hueco.gettI()) existe = true;//El tiempo inicial
			}
		return existe;
	}
	
	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		return gestorHuecos.existeHueco(tI, tF);
		}
	
	//TO-DO alumno opcionales
	
	public void liberarHueco(Hueco hueco) {
		gestorHuecos.liberarHueco(hueco);//Liberamos el hueco
		huecosReservados.remove(hueco);//Lo quitamos del array
	}

	//PRE (no es necesario comprobar): las solicitudes de la lista de espera son válidas
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		SolicitudReservaAnticipada solicitud;
		Hueco huecoReserva;
		
		IList<SolicitudReservaAnticipada> solicitudesSerAtendidas = new ArrayList<>();//Creamos la lista a devolver
		
		for (int i=0; i<listaEspera.size(); i++) {//La recorremos mediante un for
			solicitud = listaEspera.get(i);
			huecoReserva = gestorHuecos.reservarHueco(solicitud.getTInicial(), solicitud.getTFinal());//Vemos si el hueco de la solicitud está disponible
			if(huecoReserva!=null) {//En caso de estar disponible
				solicitud.setHueco(huecoReserva);//Cambiamos el hueco de la solicitud al obtenido
				/*
				 * IMPORTANTE
				 * El setter anterior lo empleamos ya que posteriormente, en la clase de ControladoresReservas,
				 * método getReservasRegistradasDesdeListaEspera(int i, int j), se emplea el método hacerReserva(SolicitudReserva solicitud),
				 * ahora bien, este ser realizará correctamente si el hueco de la solicitud está liberado, entonces, lo que hacemos es,
				 * arrastrar este hueco que hemos reservado con la solicitud y posteriormente lo liberamos otra vez para poder hacer
				 * la reserva correctamente. Esta liberación se hace una vez asignados todos las solicitudes que pueden ser atendidas
				 * para no crear bucles indefinidos.
				 */
				solicitudesSerAtendidas.add(solicitudesSerAtendidas.size(), solicitud);//Y añadimos la solicitud a la lista
			}
		}
		for (int j=0; j<solicitudesSerAtendidas.size();j++) { //Y al final todo lo que pueda ser atendido de la lista de espera
			solicitud = solicitudesSerAtendidas.get(j);
			listaEspera.remove(solicitud);
			gestorHuecos.liberarHueco(solicitud.getHueco());//Liberamos el hueco según lo previamente explicado
		}
		return solicitudesSerAtendidas;
	}
}