package cl.bluexpress.devolucion.to;

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

@Produces("application/json")
@XmlRootElement
public class FiltroFechasTO {

	String fechaInicio;
	String fechaFin;
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	@Override
	public String toString() {
		return "FiltroFechasTO [fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "]";
	}
	
	
}
