package cl.bluexpress.devolucion.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.mail.MessagingException;

import cl.bluex.devolucion.exceptions.SimpleException;
import cl.bluex.devolucion.to.DashboardRequest;
import cl.bluex.seguridad.cliente.BluexException_Exception;
import cl.bluexpress.devolucion.to.ActualizaDevolucionTO;
import cl.bluexpress.devolucion.to.EtiquetaTO;
import cl.bluexpress.devolucion.to.IngresoDevolucionTo;
import cl.bluexpress.devolucion.to.ListaEcommerceTO;
import cl.bluexpress.devolucion.to.ListaMotivosTO;
import cl.bluexpress.devolucion.to.RespuestaDevolucionClient;
import cl.bluexpress.devolucion.to.RespuestaIngresoDevolucionTo;
import cl.bluexpress.devolucion.to.RetornaDashBoardeCommerce;

public interface DevolucionService {

	RespuestaIngresoDevolucionTo insertaDevolucionClient(IngresoDevolucionTo ingresoRequest) throws IOException, SimpleException, MessagingException, Exception;

	RespuestaDevolucionClient retornaDevolucionClient(String nroDoc) throws IOException, SimpleException;

	List<ListaEcommerceTO> retornaEmpresas() throws IOException, SimpleException;

	List<ListaMotivosTO> retornaMotivos() throws IOException, SimpleException;

	boolean consultarOS(long idOs, int idEcommerce) throws IOException, SimpleException;
	
	/**
	 * 
	 * @param numeroDocumento
	 * @param idEcommerce
	 * @return
	 * @throws IOException
	 * @throws SimpleException
	 */
	boolean consultarDocumento(String numeroDocumento, int idEcommerce) throws IOException, SimpleException;

	boolean envioMail(String destinatario, String nombreUsuario, String idRequerimiento, int tipoUpd, EtiquetaTO etiquetaTO) throws MessagingException, Exception;

	RetornaDashBoardeCommerce dashboradEcommerce(List<DashboardRequest> dashRequest) throws IOException, SimpleException;

	String obtieneUsuario(String nickname, String pass) throws BluexException_Exception, NoSuchAlgorithmException, IOException, SimpleException;
	
	String updateDevolucion(ActualizaDevolucionTO actualizaTO) throws IOException, SimpleException, MessagingException, Exception;
}
