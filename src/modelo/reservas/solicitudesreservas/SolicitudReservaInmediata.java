package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import list.ArrayList;
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
		LocalDateTime tI = getTInicial();
		LocalDateTime tF = getTFinal();
		if (getHueco() == null) {
			boolean found = false;
			int distancia = 1;
			while (!found && distancia <= radio) {
				IList<GestorZona> gestoresAEvaluar = preciosOrdenados(gestor, distancia);
				int i;
				for (i = 0; i < gestoresAEvaluar.size()
						&& !gestoresAEvaluar.get(i).existeHueco(tI, tF); i++) {
					setGestorZona(gestoresAEvaluar.get(i));
					setHueco(gestoresAEvaluar.get(i).reservarHueco(tI, tF));
					found = true;
				}
				distancia++;
			}
		}
	}

	public IList<GestorZona> preciosOrdenados(GestorLocalidad gestor, int distancia) {
		IList<GestorZona> gestoresAEvaluar = new ArrayList<>();
		int i = getIZona();
		int j = getJZona();
		LocalDateTime tI = getTInicial();
		LocalDateTime tF = getTFinal();
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i - distancia + b, j + c, tI, tF)) {
				gestoresAEvaluar.add(gestoresAEvaluar.size(), gestor.getGestorZona(i - distancia + b, j + c));
			}
		}
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i + b, j + distancia - c, tI, tF)) {
				gestoresAEvaluar.add(gestoresAEvaluar.size(), gestor.getGestorZona(i + b, j + distancia - c));
			}
		}
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i + distancia - b, j - c, tI, tF)) {
				gestoresAEvaluar.add(gestoresAEvaluar.size(), gestor.getGestorZona(i + distancia - b, j - c));
			}
		}
		for (int b = 0, c = 0; b < distancia && c < distancia; b++, c++) {
			if (chequeoAux(gestor, i - b, j - distancia + c, tI, tF)) {
				gestoresAEvaluar.add(gestoresAEvaluar.size(), gestor.getGestorZona(i - b, j - distancia + c));
			}
		}
		ordenar(gestoresAEvaluar);
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

	public void ordenar(IList<GestorZona> gestoresAEvaluar) {
		int n = gestoresAEvaluar.size() - 1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n - i; j++) {
				if (gestoresAEvaluar.get(j).getPrecio() > gestoresAEvaluar.get(j + 1).getPrecio()) {
					GestorZona holder = gestoresAEvaluar.get(j);
					gestoresAEvaluar.set(j, gestoresAEvaluar.get(j + 1));
					gestoresAEvaluar.set(j + 1, holder);
				}
			}
		}
	}
}