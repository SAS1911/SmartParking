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

	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		return gestorHuecos.existeHueco(tI, tF);
	}
	
	//TO-DO alumno obligatorios
	
	public GestorZona(int i, int j, int noPlazas, double precio){
		//HECHO?
		iZona = i;
		jZona = j;
		plazas = new Plaza[noPlazas];//Inicializamos el vector de plazas
		for(int k=0; k<plazas.length; k++) {//Y a cada plaza le damos su número
			plazas[k] = new Plaza(k);
		}
		this.precio = precio;
		listaEspera = new ArrayList<SolicitudReservaAnticipada>();//Inicializamos la lista de espera como ArrayList
		huecosReservados =  new ArrayList<Hueco>();//Al igual que huecos reservados
		gestorHuecos = new GestorHuecos(plazas);//Y el gestor de huecos mediante su constructor predeterminado
	}
	
	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		//HECHO?
		//Creamos la variable hueco, no hace falta comprobar si existe ya que ya lo hace el método reservarHueco(...)
		Hueco hueco = gestorHuecos.reservarHueco(tI, tF);//Creamos el hueco que vamos a reservar, será null si no es posible
		if(hueco!=null)//En caso de existir:
			huecosReservados.add(huecosReservados.size(), hueco);//Lo añadimos a la lista de huecos reservados
		return hueco;
	}
	
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {//Método implementado en SolicitudReservaAnticipada
		//HECHO?
		listaEspera.add(listaEspera.size(), solicitud);//Si una solicitud no es posible admitirla, la metemos en la lista de espera
		
	}
	
	public boolean existeHuecoReservado(Hueco hueco) {
		//HECHO? 
		boolean existe = false;
			for(int i=0; i<huecosReservados.size() && !existe; i++) {//Recorremos el array
	//			if(huecosReservados.get(i).equals(hueco)) existe = true;//El equals parece que está sobreescrito incorrectamente
				if(huecosReservados.get(i).getPlaza() == hueco.getPlaza() &&//Comparamos la plaza
						huecosReservados.get(i).gettF() == hueco.gettF() &&//El tiempo final
						huecosReservados.get(i).gettI() == hueco.gettI()) existe = true;//El tiempo inicial
			}
		return existe;
	}
	
	
	//TO-DO alumno opcionales
	
	public void liberarHueco(Hueco hueco) {
		//HECHO?
		if(hueco!=null) {//En caso de no ser nulo
			gestorHuecos.liberarHueco(hueco);//Liberamos el hueco
			huecosReservados.remove(hueco);//Lo quitamos del array
		}
	}

	//PRE (no es necesario comprobar): las solicitudes de la lista de espera son válidas
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		//HECHO?
		Hueco hueco = null;
		IList<SolicitudReservaAnticipada> solicitudesSerAtendidas = new ArrayList<>();
		for (int i=0; i<listaEspera.size(); i++) {
			hueco = listaEspera.get(i).getHueco();
			if(hueco!=null && !existeHuecoReservado(hueco)) {
				solicitudesSerAtendidas.add(solicitudesSerAtendidas.size(), listaEspera.get(i));
			}
		}
		for (int j=0; j<solicitudesSerAtendidas.size();j++) 
			listaEspera.remove(solicitudesSerAtendidas.get(j));
		return solicitudesSerAtendidas;
	}

	


}
