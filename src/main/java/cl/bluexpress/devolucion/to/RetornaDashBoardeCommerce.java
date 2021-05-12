package cl.bluexpress.devolucion.to;

import java.util.List;

public class RetornaDashBoardeCommerce {
	int pendientes;
	int rechazados;
	int aceptados;
	List<RespuestaDevolucionClient> lstDevoluciones;
	public int getPendientes() {
		return pendientes;
	}
	public void setPendientes(int pendientes) {
		this.pendientes = pendientes;
	}
	public int getRechazados() {
		return rechazados;
	}
	public void setRechazados(int rechazados) {
		this.rechazados = rechazados;
	}
	public int getAceptados() {
		return aceptados;
	}
	public void setAceptados(int aceptados) {
		this.aceptados = aceptados;
	}
	public List<RespuestaDevolucionClient> getLstDevoluciones() {
		return lstDevoluciones;
	}
	public void setLstDevoluciones(List<RespuestaDevolucionClient> lstDevoluciones) {
		this.lstDevoluciones = lstDevoluciones;
	}
	@Override
	public String toString() {
		return "RetornaDashBoardeCommerce [pendientes=" + pendientes + ", rechazados=" + rechazados + ", aceptados="
				+ aceptados + ", lstDevoluciones=" + lstDevoluciones + "]";
	}
	
	
}
