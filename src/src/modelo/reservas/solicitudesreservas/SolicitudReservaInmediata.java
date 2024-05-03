package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import anotacion.Programacion2;
import list.ArrayList;
import list.IList;

import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.vehiculos.Vehiculo;

@Programacion2 (
		nombreAutor1 = "Philip Daniel",
		apellidoAutor1 = "Salov Draganov",
		emailUPMAutor1 = "philip.salov@alumnos.upm.es",
		nombreAutor2 = "Samuel Andrés",
		apellidoAutor2 = "Sánchez Pérez",
		emailUPMAutor2 = "samuel.sanchez@alumnos.upm.es"
		)

public class SolicitudReservaInmediata extends SolicitudReserva {
	private int radio;

	public SolicitudReservaInmediata(int i, int j, LocalDateTime tI,
			LocalDateTime tF, Vehiculo vehiculo, int radio) {
		super(i, j, tI, tF, vehiculo);
		this.radio = radio;
	}

	@Override
	public boolean esValida(GestorLocalidad gestor) {
		int ZonaI = getIZona();
		int ZonaJ = getJZona();
		boolean res = super.esValida(gestor) && radio > 0;// Comprobamos por la clase padre si los atributos generales son válidos y radio mayor que cero 
		if (res) {//En el caso de cumplir ambas condiciones
			//Para calcular el radio máximo, hacemos una suma de dos recorridos: 
			//1. maxRI = es la máxima distancia vertical de la zona inicial a uno de los bordes
			//2. maxRJ = es la máxima distancia horizonal de la zona inicial a uno de los bordes
			//Se elegirá o bien la diferencia entre el radio máximo (I ó J) y la coordenada, o bien la propia coordenada
			int maxRI = (gestor.getRadioMaxI()-(ZonaI) > ZonaI)? gestor.getRadioMaxI()-(ZonaI) : ZonaI;
			int maxRJ = (gestor.getRadioMaxJ()-(ZonaJ) > ZonaJ)? gestor.getRadioMaxJ()-(ZonaJ) : ZonaJ;
			if (radio > maxRI+maxRJ)//En el caso de que el radio sea mayor, lo ponemos como falso
				res = false;
		}
		return res;
	}

	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		LocalDateTime tI = getTInicial();
		LocalDateTime tF = getTFinal();
		IList<GestorZona> gestoresAEvaluar;//Serán los gestores zona que comprobaremos que se pueden reservar o no
		super.gestionarSolicitudReserva(gestor);//Primero gestionamos la solicitud en la zona inicial
		if (getHueco() == null) {//En el caso de no poderla reservar
			boolean found = false;//Asignamos el hueco como no encontrado
			for(int d=1; !found && d<=radio; d++){//Mientras que no lo encontremos o lleguemos al radio propuesto
				gestoresAEvaluar = preciosOrdenados(gestor, d);//Evaluamos cada uno de las zonas a la distancia correspondiente
				for (int i = 0; !found && i < gestoresAEvaluar.size(); i++){//Después de ordenar las zonas según el precio de cada zona
					if(gestoresAEvaluar.get(i).existeHueco(tI, tF)){//Evaluamos si existe un hueco para reservarlo en dicha zona
						setGestorZona(gestoresAEvaluar.get(i));//En ese caso setteamos la zona
						setHueco(gestoresAEvaluar.get(i).reservarHueco(tI, tF));//Y el hueco a esta misma, reservándolo
						found = true;//Anulando los bucles anteriores asignando true a "found"
					}
				}
			}
		}
	}

	private IList<GestorZona> preciosOrdenados(GestorLocalidad gestor, int distancia) {
		IList<GestorZona> gestoresAEvaluar = new ArrayList<>();
		int i = getIZona();
		int j = getJZona();
		
		//PASO 1: MOVIMIENTO DIAGONAL DE IZQUIERDA A ABAJO
		for (int w = 0, l = -distancia; w < distancia; w++, l++) //l (length) comienza con valor -distancia para situarse en la casilla de la izquierda de la zona inicial
			agregarGestor(gestoresAEvaluar, gestor, i + w, j + l);//Hacemos una llamada a la función auxiliar
		
		//PASO 2: MOVIMIENTO DIAGONAL DE ABAJO A DERECHA
		for (int w = distancia, l = 0; l < distancia; w--, l++) //w (width) comienza con valor distancia para situarse en la casilla inferior de la zona inicial
			agregarGestor(gestoresAEvaluar, gestor, i + w, j + l);

		//PASO 3: MOVIMIENTO DIAGONAL DE DERECHA A ARRIBA
		for (int w = 0, l = distancia; Math.abs(w) < distancia; w--, l--) //l comienza con valor distancia para situarse en la casilla derecha de la zona inicial
			agregarGestor(gestoresAEvaluar, gestor, i + w, j + l);
		
		//PASO 4: MOVIMIENTO DIAGONAL DE ARRIBA A IZQUIERDA
		for (int w = -distancia, l = 0 ; Math.abs(l) < distancia; w++, l--) //w comienza con valor -distancia para situarse en la casilla superior de la zona inicial
			agregarGestor(gestoresAEvaluar, gestor, i + w, j + l);
		
		ordenar(gestoresAEvaluar);//Ordenamos mediante un método auxiliar los gestores zona a evaluar según el precio de cada uno

		return gestoresAEvaluar;
	}

	//Comprueba que las coordenadas seleccionadas están dentro del rango
	private boolean chequeoAux(GestorLocalidad gestor, int coordX, int coordY) {
		boolean res = false;
		if (coordX >= 0 && coordX <= gestor.getRadioMaxI() && coordY >= 0 && coordY <= gestor.getRadioMaxJ()) {
			res = true;
		}
		return res;
	}
	
	//Realiza la comprobación con el método anterior, y en caso de ser válidas las coordenadas, lo mete dentro de los gestores a evaluar
	private void agregarGestor(IList<GestorZona> gestoresAEvaluar, GestorLocalidad gestor, int coordX, int coordY) {
		if(chequeoAux(gestor, coordX, coordY))
			gestoresAEvaluar.add(gestoresAEvaluar.size(), gestor.getGestorZona(coordX, coordY));
	}

	//Como mencionado anteriormente, ordena los gestores zona del IList según el precio de cada uno, de MENOR a MAYOR
	private void ordenar(IList<GestorZona> gestoresAEvaluar) {
		for (int i = 0; i < gestoresAEvaluar.size()-1; i++) {
			if (gestoresAEvaluar.get(i+1).getPrecio() < gestoresAEvaluar.get(i).getPrecio()) {//En el caso de que uno de las zonas tenga un precio menor al del anterior
				gestoresAEvaluar.add(i, gestoresAEvaluar.get(i+1));//Lo ponemos en esa posición
				gestoresAEvaluar.removeElementAt(i+2);//Y eliminamos la zona duplicada que había en su posición antigua
				i = -1;//Reseteamos el bucle for con un -1 ya que comenzará con un incremento haciéndolo valer 0
			}
		}
	}

}