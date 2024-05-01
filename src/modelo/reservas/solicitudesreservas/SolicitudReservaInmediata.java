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
		boolean res = super.esValida(gestor);// Comprobamos por la clase padre si los atributos generales son válidos
		if (res && radio > 0) {
			if (radio <= gestor.getRadioMaxI() && radio <= gestor.getRadioMaxJ()) { // chequeamos si el radio esta en
																					// entre los radios maximos
				res = true;
			} else if (((i == 0 || i == gestor.getRadioMaxI()) && (j == 0 || j == gestor.getRadioMaxJ())
					&& (radio <= gestor.getRadioMaxI() + 1 && radio <= gestor.getRadioMaxJ() + 1))) {
				res = true;
			} else {
				res = false;
			}
		}
		return res;
	}

	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		GestorZona mejor = null; // zona candidata a ser escogida
		double mejorPrecio = buscarPrecioMaximo(gestor); // precio de zona candidata a ser escogida
		if (gestor.getGestorZona(i, j).existeHueco(tI, tF)) { // comprobamos si existe hueco en el centro
			mejor = gestor.getGestorZona(i, j);
			mejorPrecio = mejor.getPrecio();
		}
		for (int a = 1; a <= radio; a++) { // bucle que recorrera todas las zonas en el orden establecido
			for (int b = 0, c = 0; b < a && c < a; b++, c++) {
				if (i - a + b < 0 || i - a + b > gestor.getRadioMaxI() || j + c < 0 ||
						j + c > gestor.getRadioMaxJ()) { // comprobamos que estamos dentro del array de arrays que seria
															// el gestor localidad
					if (gestor.getGestorZona(i - a + b, j + c).existeHueco(tI, tF) &&
							gestor.getGestorZona(i - a + b, j + c).getPrecio() < mejorPrecio) {
						// comprobamos que exista hueco y que este sea mejor que el ya definido

						mejor = gestor.getGestorZona(i - a + b, j + c); // cambiamos a la mejor opcion
						mejorPrecio = mejor.getPrecio(); // definimos su precio como la barra a superar
					}
				}
			}
			for (int b = 0, c = 0; b < a && c < a; b++, c++) {
				if (i + b < 0 || i + b > gestor.getRadioMaxI() || j + a - c < 0 || j + a - c > gestor.getRadioMaxJ()) {
					if (gestor.getGestorZona(i + b, j + a - c).existeHueco(tI, tF) &&
							gestor.getGestorZona(i + b, j + a - c).getPrecio() < mejorPrecio) {
						mejor = gestor.getGestorZona(i + b, j + a - c);
						mejorPrecio = mejor.getPrecio();
					}
				}
			}
			for (int b = 0, c = 0; b < a && c < a; b++, c++) {
				if (i + a - b < 0 || i + a - b > gestor.getRadioMaxI() || j - c < 0 || j - c > gestor.getRadioMaxJ()) {
					if (gestor.getGestorZona(i + a - b, j - c).existeHueco(tI, tF) &&
							gestor.getGestorZona(i + a - b, j - c).getPrecio() < mejorPrecio) {
						mejor = gestor.getGestorZona(i + a - b, j - c);
						mejorPrecio = mejor.getPrecio();
					}
				}
			}
			for (int b = 0, c = 0; b < a && c < a; b++, c++) {
				if (i - b < 0 || i - b > gestor.getRadioMaxI() || j - a + c < 0 || j - a + c > gestor.getRadioMaxJ()) {
					if (gestor.getGestorZona(i - b, j - a + c).existeHueco(tI, tF) &&
							gestor.getGestorZona(i - b, j - a + c).getPrecio() < mejorPrecio) {
						mejor = gestor.getGestorZona(i - b, j - a + c);
						mejorPrecio = mejor.getPrecio();
					}
				}
			}
		}
		if (mejor == null) { // en el caso que no existian huecos en ninguno, escogemos el centro para
								// ponernos en lista de espera
			System.out.println(
					"No se pudo encontrar huecos disponibles en el centro determinado o en su radio de busqueda");
		}
		mejor.reservarHueco(tI, tF); // hacemos la reserva
	}

	public double buscarPrecioMaximo(GestorLocalidad gestor) {
		double res = gestor.getGestorZona(0, 0).getPrecio();
		for (int i = 0; i < gestor.getRadioMaxJ(); i++) {
			for (int j = 0; j < gestor.getRadioMaxJ(); j++) {
				if (res > gestor.getGestorZona(i, j).getPrecio()) {
					res = gestor.getGestorZona(i, j).getPrecio();
				}
			}
		}
		return res;
	}
}
