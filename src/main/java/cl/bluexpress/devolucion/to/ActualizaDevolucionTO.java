package cl.bluexpress.devolucion.to;

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

@Produces("application/json")
@XmlRootElement
public class ActualizaDevolucionTO {
	
	int codigoDevolucion;
	int motivoDevolucion;
	String descripcionDevolucion;
	String emailCliente;
	String nombreCliente;
	String numeroRequerimiento;
	public int getCodigoDevolucion() {
		return codigoDevolucion;
	}
	public void setCodigoDevolucion(int codigoDevolucion) {
		this.codigoDevolucion = codigoDevolucion;
	}
	public int getMotivoDevolucion() {
		return motivoDevolucion;
	}
	public void setMotivoDevolucion(int motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}
	public String getDescripcionDevolucion() {
		return descripcionDevolucion;
	}
	public void setDescripcionDevolucion(String descripcionDevolucion) {
		this.descripcionDevolucion = descripcionDevolucion;
	}
	public String getEmailCliente() {
		return emailCliente;
	}
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getNumeroRequerimiento() {
		return numeroRequerimiento;
	}
	public void setNumeroRequerimiento(String numeroRequerimiento) {
		this.numeroRequerimiento = numeroRequerimiento;
	}
	@Override
	public String toString() {
		return "ActualizaDevolucionTO [codigoDevolucion=" + codigoDevolucion + ", motivoDevolucion=" + motivoDevolucion
				+ ", descripcionDevolucion=" + descripcionDevolucion + ", emailCliente=" + emailCliente
				+ ", nombreCliente=" + nombreCliente + ", numeroRequerimiento=" + numeroRequerimiento + "]";
	}
	
	
	
	
	
	
}
