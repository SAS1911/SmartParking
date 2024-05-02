package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import list.IList;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.vehiculos.Vehiculo;

public class SolicitudReservaInmediata extends SolicitudReserva {

	private int radio;

	public SolicitudReservaInmediata(int i, int j, LocalDateTime tI,
			LocalDateTime tF, Vehiculo vehiculo, int radio) {
		super(radio, j, tI, tF, vehiculo);
		this.radio = radio;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean esValida(GestorLocalidad gestor) {
		int ZonaI = getIZona();
		int ZonaJ = getJZona();
		boolean res = super.esValida(gestor) && radio > 0;// Comprobamos por la clase padre si los atributos generales
															// son válidos
		if (res) {
			int maxRI = (gestor.getRadioMaxI() - (ZonaI) > ZonaI) ? gestor.getRadioMaxI() - (ZonaI) : ZonaI;
			int maxRJ = (gestor.getRadioMaxJ() - (ZonaJ) > ZonaJ) ? gestor.getRadioMaxJ() - (ZonaJ) : ZonaJ;
			if (radio <= maxRI + maxRJ)
				res = true;
			else
				res = false;
		}
		return res;
	}

	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		super.gestionarSolicitudReserva(gestor);
		if (getHueco() == null) {
			boolean encontrado = false;
			int distancia = 1;
			while (!encontrado && distancia <= radio) {
				IList<GestorZona> gestoresAEvaluar = preciosOrdenados(gestor, distancia);
				int i;
				for (i = 0; i < gestoresAEvaluar.size()
						&& !gestoresAEvaluar.get(i).existeHueco(getTInicial(), getTFinal()); i++)
					;
				if (i < gestoresAEvaluar.size()) {
					setGestorZona(gestoresAEvaluar.get(i));
					setHueco(gestoresAEvaluar.get(i).reservarHueco(getTInicial(), getTFinal()));
					encontrado = true;
				}
				distancia++;
			}
		}
	}

	public IList<GestorZona> preciosOrdenados(GestorLocalidad gestor, int distancia) {
		IList<GestorZona> gestoresAEvaluar = null;
		int i = getIZona();
		int j = getJZona();
		LocalDateTime tI = getTInicial();
		LocalDateTime tF = getTFinal();
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i - distancia + b, j + c, tI, tF)) {
				ordenar(gestor, gestoresAEvaluar);
			}
		}
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i + b, j + distancia - c, tI, tF)) {
				ordenar(gestor, gestoresAEvaluar);
			}
		}
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i + distancia - b, j - c, tI, tF)) {
				ordenar(gestor, gestoresAEvaluar);
			}
		}
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i - b, j - distancia + c, tI, tF)) {
				ordenar(gestor, gestoresAEvaluar);
			}
		}
		return gestoresAEvaluar;
	}

	public boolean chequeoAux(GestorLocalidad gestor, int coordX, int coordY, LocalDateTime tI, LocalDateTime tF) {
		boolean res = false;
		if (coordX < 0 || coordX >= gestor.getRadioMaxI() || coordY < 0 || coordY >= gestor.getRadioMaxJ()) {
			// comprobamos que estamos dentro de la matriz que seria el gestor localidad
			res = true;
		}
		return res;
	}

	public IList<GestorZona> ordenar(GestorLocalidad gestor, IList<GestorZona> gestoresAEvaluar) {
		int pos;
		int i = getIZona();
		int j = getJZona();
		for (pos = 0; pos < gestoresAEvaluar.size() &&
				gestoresAEvaluar.get(pos).getPrecio() < gestor.getGestorZona(i, j).getPrecio(); pos++)
			;
		gestoresAEvaluar.add(pos, gestor.getGestorZona(i, j));
		return gestoresAEvaluar;
	}

	public int distanciaManhattan(int x, int y) {
		return Math.abs(getIZona() - x) + Math.abs(getJZona() + y);
	}
}
/*
 * package modelo.reservas.solicitudesreservas;
 * 
 * import java.time.LocalDateTime;
 * 
 * import list.IList;
 * import list.ArrayList;
 * import modelo.gestoresplazas.GestorLocalidad;
 * import modelo.gestoresplazas.GestorZona;
 * import modelo.vehiculos.Vehiculo;
 * 
 * public class SolicitudReservaInmediata extends SolicitudReserva{
 * private int radio;
 * 
 * public SolicitudReservaInmediata(int i, int j, LocalDateTime tI,
 * LocalDateTime tF, Vehiculo vehiculo, int radio){
 * super(i, j, tI, tF, vehiculo);
 * this.radio = radio;
 * }
 * 
 * public boolean esValida(GestorLocalidad gestorLocalidad){
 * return gestorLocalidad.getGestorZona(getIZona(), getJZona()) != null &&
 * getTInicial().compareTo(getTFinal()) < 0 && !getVehiculo().getSancionado() &&
 * radio > 0 && distanciaValida(gestorLocalidad);
 * }
 * 
 * //Es un metodo para comprobar si el radio esta dentro del rango de la
 * localidad
 * private boolean distanciaValida(GestorLocalidad gestorLocalidad){
 * int distancia = 0;
 * 
 * //Para comprobar si el radio es valido primero buscamos si la fila y la
 * columna de la zona estan por encima de la mitad o no
 * //Para poder calcular la esquina mas lejana y ver si queda dentro del radio
 * 
 * if(getIZona() > gestorLocalidad.getRadioMaxI() / 2){
 * if(getJZona() > gestorLocalidad.getRadioMaxJ() / 2)
 * distancia = getIZona() + getJZona(); //caso esquina izquierda arriba
 * else
 * distancia = getIZona() + gestorLocalidad.getRadioMaxJ() - getJZona(); // caso
 * esquina izquierda abajo
 * }
 * else{
 * if(getJZona() > gestorLocalidad.getRadioMaxJ() / 2)
 * distancia = gestorLocalidad.getRadioMaxI() - getIZona() + getJZona(); //caso
 * esquina derecha arriba
 * else
 * distancia = gestorLocalidad.getRadioMaxI() - getIZona() +
 * gestorLocalidad.getRadioMaxJ() - getJZona(); //caso esquina derecha abajo
 * }
 * return radio <= distancia;
 * }
 * //comprueba los diferentes casos para calcular la distancia de Manhattan
 * entre dos zonas
 * private int distanciaManhattan(int i, int j){
 * int distancia = 0;
 * if(i > getIZona()){
 * if(j > getJZona())
 * distancia = i - getIZona() + j - getJZona();
 * else
 * distancia = i - getIZona() + getJZona() - j;
 * }
 * else
 * if(j > getJZona())
 * distancia = getIZona() - i + j - getJZona();
 * else
 * distancia = getIZona() - i + getJZona() - j;
 * return distancia;
 * }
 * 
 * 
 * public void gestionarSolicitudReserva(GestorLocalidad gestor){
 * super.gestionarSolicitudReserva(gestor);
 * if(getHueco() == null){
 * boolean encontrado = false;
 * int ronda = 1;
 * while(!encontrado && ronda <= radio){
 * IList<GestorZona> gestoresRonda = ordenPrecios(gestor, ronda);
 * int i;
 * for(i=0; i < gestoresRonda.size() &&
 * !gestoresRonda.get(i).existeHueco(getTInicial(), getTFinal()); i++);
 * if(i < gestoresRonda.size()){
 * setGestorZona(gestoresRonda.get(i));
 * setHueco(gestoresRonda.get(i).reservarHueco(getTInicial(), getTFinal()));
 * encontrado = true;
 * }
 * ronda++;
 * }
 * }
 * }
 * 
 * //ordena las zonas con el mismo radio de menor a mayor precio
 * private IList<GestorZona> ordenPrecios(GestorLocalidad gestor, int radio){
 * IList<GestorZona> zonas = new ArrayList<>(); //zonas ordenadas de menor a
 * mayor precio
 * for(int i=0; i < gestor.getRadioMaxI(); i++){
 * for(int j=0; j < gestor.getRadioMaxJ(); j++){
 * if(gestor.existeZona(i, j) && distanciaManhattan(i,j) == radio){
 * //Buscamos la posicion en la que añadimos la zona
 * int k;
 * for(k=0; k < zonas.size() && zonas.get(k).getPrecio() <
 * gestor.getGestorZona(i, j).getPrecio(); k++);
 * zonas.add(k, gestor.getGestorZona(i, j));
 * }
 * }
 * }
 * //Si dos o mas zonas tienen el mismo precio las ordenamos con una funcion
 * auxliar
 * for(int l=0; l < zonas.size() - 1; l++){
 * if(zonas.get(l).getPrecio() == zonas.get(l+1).getPrecio()){
 * IList<GestorZona> zonasOrdenadas= ordenar(zonas, zonas.get(l).getPrecio(),
 * radio);
 * int m=0;
 * for(; m < zonasOrdenadas.size() ; l++){
 * zonas.removeElementAt(l);
 * zonas.add(l, zonasOrdenadas.get(m++));
 * }
 * }
 * }
 * return zonas;
 * }
 * 
 * //Es un metodo que ordena zonas con el mismo precio en una ronda segun el
 * orden dado
 * private IList<GestorZona> ordenar(IList<GestorZona> zonas, double precio, int
 * radio){
 * IList<GestorZona> zonasPrecio = new ArrayList<>(); //Un array con las zonas
 * del mismo precio y del mismo radio
 * int l;
 * for(l=0; l < zonas.size(); l++){ //Añade las zonas con el mismo precio al
 * ArrayList
 * if(zonas.get(l).getPrecio() == precio)
 * zonasPrecio.add(zonasPrecio.size(), zonas.get(l));
 * }
 * int k = 0;
 * //El punto de partida
 * int i = getIZona();
 * int j = getJZona() - radio;
 * IList<GestorZona> zonasPrecioOrdenadas = new ArrayList<>(); //Un array con
 * las zonas del mismo precio ordenadas
 * 
 * //Creamos un while que nos va a ordenar las zonas segun el orden de busqueda
 * while (k < 4 /&& zonasPrecio.size() > zonasPrecioOrdenadas.size()/){
 * for(int n=0; n < zonasPrecio.size(); n++){ //Comprobamos si hemos encontrado
 * alguna de las zonas
 * if(zonasPrecio.get(n).getI() == i && zonasPrecio.get(n).getJ() == j)
 * zonasPrecioOrdenadas.add(zonasPrecioOrdenadas.size(), zonasPrecio.get(n));
 * }
 * 
 * //En el swicth vamos revisando en orden cada localidad para ver si alguna es
 * una de las que buscamos
 * //y dividimos en cuatro zonas en funcion de la direccion y el senetido en el
 * que hacemos la busqueda
 * switch(k){
 * case 0:
 * i++;
 * j++;
 * if(j == getJZona())
 * k++;
 * break;
 * case 1:
 * i--;
 * j++;
 * if(i == getIZona())
 * k++;
 * break;
 * case 2:
 * i--;
 * j--;
 * if(j == getJZona())
 * k++;
 * break;
 * default:
 * if(i == getIZona() - 1){
 * k++;
 * }
 * else{
 * i++;
 * j--;
 * }
 * }
 * }
 * return zonasPrecioOrdenadas;
 *     }
 * }
 */