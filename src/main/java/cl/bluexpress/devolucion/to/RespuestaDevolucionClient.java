package cl.bluexpress.devolucion.to;

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

@Produces("application/json")
@XmlRootElement
public class RespuestaDevolucionClient {

	private long cdgSeqReq;
	private String numeroRequerimiento;
	private String nombreCliente;
	private String rutCliente;
	private String telefonoCliente;
	private String emailCliente;
	private String nombreProducto;
	private String nombreEmpresa;
	private String estadoDevolucion;
	private String motivoDevolucion;
	private String fechaIngresoDevolucion;
	private String numeroOrdenServicio;

	
	public long getCdgSeqReq() {
		return cdgSeqReq;
	}
	public void setCdgSeqReq(long cdgSeqReq) {
		this.cdgSeqReq = cdgSeqReq;
	}
	public String getNumeroRequerimiento() {
		return numeroRequerimiento;
	}
	public void setNumeroRequerimiento(String numeroRequerimiento) {
		this.numeroRequerimiento = numeroRequerimiento;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getRutCliente() {
		return rutCliente;
	}
	public void setRutCliente(String rutCliente) {
		this.rutCliente = rutCliente;
	}
	public String getTelefonoCliente() {
		return telefonoCliente;
	}
	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}
	public String getEmailCliente() {
		return emailCliente;
	}
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
	public String getNombreProducto() {
		return nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	public String getEstadoDevolucion() {
		return estadoDevolucion;
	}
	public void setEstadoDevolucion(String estadoDevolucion) {
		this.estadoDevolucion = estadoDevolucion;
	}
	public String getMotivoDevolucion() {
		return motivoDevolucion;
	}
	public void setMotivoDevolucion(String motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}
	public String getFechaIngresoDevolucion() {
		return fechaIngresoDevolucion;
	}
	public void setFechaIngresoDevolucion(String fechaIngresoDevolucion) {
		this.fechaIngresoDevolucion = fechaIngresoDevolucion;
	}
	
	public String getNumeroOrdenServicio() {
		return numeroOrdenServicio;
	}
	public void setNumeroOrdenServicio(String numeroOrdenServicio) {
		this.numeroOrdenServicio = numeroOrdenServicio;
	}
	@Override
	public String toString() {
		return "RespuestaDevolucionClient [cdgSeqReq=" + cdgSeqReq + ", numeroRequerimiento=" + numeroRequerimiento
				+ ", nombreCliente=" + nombreCliente + ", rutCliente=" + rutCliente + ", telefonoCliente="
				+ telefonoCliente + ", emailCliente=" + emailCliente + ", nombreProducto=" + nombreProducto
				+ ", nombreEmpresa=" + nombreEmpresa + ", estadoDevolucion=" + estadoDevolucion + ", motivoDevolucion="
				+ motivoDevolucion + ", fechaIngresoDevolucion=" + fechaIngresoDevolucion + ", numeroServicio="
				+ numeroOrdenServicio + "]";
	}
	

}
