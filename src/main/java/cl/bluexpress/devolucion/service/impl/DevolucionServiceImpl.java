package cl.bluexpress.devolucion.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;

import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;

import cl.bluex.devolucion.exceptions.SimpleException;
import cl.bluex.devolucion.sp.SPInsert;
import cl.bluex.devolucion.sp.SPSelect;
import cl.bluex.devolucion.sp.SPUpdate;
import cl.bluex.devolucion.to.ConsultaDevolucionResponse;
import cl.bluex.devolucion.to.DashboardRequest;
import cl.bluex.devolucion.to.DevolucionResponse;
import cl.bluex.devolucion.to.ECommerceResponse;
import cl.bluex.devolucion.to.InsertDevolucionRequest;
import cl.bluex.devolucion.to.InsertaDevolucionResponse;
import cl.bluex.devolucion.to.MotivoResponse;
import cl.bluex.devolucion.to.SolicitudEtiquetaTO;
import cl.bluex.devolucion.to.UpdateDevolucionRequest;
import cl.bluex.devolucion.to.UpdateDevolucionResponse;
import cl.bluex.emisionintegracion.cliente.BXEMS001EmisionIntegracion_Service;
import cl.bluex.emisionintegracion.cliente.ContactoCanal;
import cl.bluex.emisionintegracion.cliente.EmisionEmbalajeCrearReq;
import cl.bluex.emisionintegracion.cliente.ListaEmisionEmbalajesCrearReq;
import cl.bluex.emisionintegracion.cliente.ListaNumerosReferenciaCrearReq;
import cl.bluex.emisionintegracion.cliente.Macroestado;
import cl.bluex.emisionintegracion.cliente.ObjetoListaContactos;
import cl.bluex.emisionintegracion.cliente.ObjetoListaMacroestados;
import cl.bluex.emisionintegracion.cliente.OrdenServicioCrearReq;
import cl.bluex.emisionintegracion.cliente.RequestEmisionIntegracion;
import cl.bluex.emisionintegracion.cliente.ResponseEmisionIntegracion;
import cl.bluex.emisionintegracion.cliente.ValidationException_Exception;
import cl.bluex.envio.EnvioEmail;
import cl.bluex.opcionesservicio.cliente.BXAUX002OpcionesServicio_Service;
import cl.bluex.opcionesservicio.cliente.CuentaCorriente;
import cl.bluex.opcionesservicio.cliente.HeaderRequest;
import cl.bluex.opcionesservicio.cliente.RequestCuentasCorrientesFiltrar;
import cl.bluex.opcionesservicio.cliente.ResponseCuentasCorrientesFiltrar;
import cl.bluex.seguridad.cliente.BluexException_Exception;
import cl.bluex.seguridad.cliente.NewUsuario;
import cl.bluex.seguridad.cliente.RequestDatosUsuario;
import cl.bluex.seguridad.cliente.ResponseDatosUsuario;
import cl.bluex.seguridad.cliente.SeguridadService;
import cl.bluexpress.devolucion.service.DevolucionService;
import cl.bluexpress.devolucion.to.ActualizaDevolucionTO;
import cl.bluexpress.devolucion.to.EtiquetaTO;
import cl.bluexpress.devolucion.to.IngresoDevolucionTo;
import cl.bluexpress.devolucion.to.ListaEcommerceTO;
import cl.bluexpress.devolucion.to.ListaMotivosTO;
import cl.bluexpress.devolucion.to.LoginResponseTO;
import cl.bluexpress.devolucion.to.RespuestaDevolucionClient;
import cl.bluexpress.devolucion.to.RespuestaIngresoDevolucionTo;
import cl.bluexpress.devolucion.to.RetornaDashBoardeCommerce;

public class DevolucionServiceImpl implements DevolucionService {


	SPSelect select = new SPSelect();
	SPInsert insert = new SPInsert();

	@Override
	public RespuestaIngresoDevolucionTo insertaDevolucionClient(IngresoDevolucionTo ingresoRequest) throws MessagingException, Exception {
		InsertDevolucionRequest request = new InsertDevolucionRequest();
		BeanUtils.copyProperties(ingresoRequest, request);
		InsertaDevolucionResponse response = insert.insertaDevolucion(request);
		RespuestaIngresoDevolucionTo salida = new RespuestaIngresoDevolucionTo();
		BeanUtils.copyProperties(response, salida);
		envioMail(ingresoRequest.getEmail(), ingresoRequest.getNombre(), salida.getIdNumeroRequerimiento(), 1, null);
		return salida;
	}
	@Override
	public String updateDevolucion(ActualizaDevolucionTO actualizaTO) throws MessagingException, Exception {
		UpdateDevolucionRequest updrequest = new UpdateDevolucionRequest();
		EtiquetaTO etiquetaTO = null;
		BeanUtils.copyProperties(actualizaTO, updrequest);
		SPUpdate upd = new SPUpdate();
		UpdateDevolucionResponse response = upd.actualizaDeolucion(updrequest); 
		if(actualizaTO.getMotivoDevolucion()==4) {
			etiquetaTO = generaEtiqueta(actualizaTO.getCodigoDevolucion());			
		}
		envioMail(actualizaTO.getEmailCliente(),actualizaTO.getNombreCliente(),actualizaTO.getNumeroRequerimiento(), actualizaTO.getMotivoDevolucion(), etiquetaTO);
		return response.getDescripcion();
	}
	
	private EtiquetaTO generaEtiqueta(Integer codigoSolicitud) throws cl.bluex.emisionintegracion.cliente.BluexException_Exception, ValidationException_Exception, IOException, SimpleException{
		EtiquetaTO etiquetaTO = new EtiquetaTO();
		BXEMS001EmisionIntegracion_Service service = new BXEMS001EmisionIntegracion_Service();
		RequestEmisionIntegracion requestEmisionIntegracion = new RequestEmisionIntegracion();
		List<SolicitudEtiquetaTO> etiquetaTOs = select.consultaSolicitudEtiqueta(codigoSolicitud);
		for(int i = 0; i < etiquetaTOs.size(); i++) {
			// Tipo Etiqueta
			requestEmisionIntegracion.setCodigoFormatoImpresion(3);
			
			// Cabecera
			cl.bluex.emisionintegracion.cliente.HeaderRequest headerRequest = new cl.bluex.emisionintegracion.cliente.HeaderRequest();
			headerRequest.setCodigoUsuario(etiquetaTOs.get(i).getCodigoUsuario());
			headerRequest.setIdToken(etiquetaTOs.get(i).getIdToken());
			
			
			OrdenServicioCrearReq ordenServicioCrearReq = new OrdenServicioCrearReq();
			// Cuenta Corriente
			ordenServicioCrearReq.setCuentaCliente(etiquetaTOs.get(i).getCodigoCliente().toString() + '-' + etiquetaTOs.get(i).getCodigoSucursal().toString() + '-' +  etiquetaTOs.get(i).getTipoCliente());
			
			// Origen
			ordenServicioCrearReq.setNombreEmbalador(etiquetaTOs.get(i).getNombreDestinatario());
			ordenServicioCrearReq.setCodigoPaisEmbalador("CL");
			ordenServicioCrearReq.setCodigoRegionEmbalador(etiquetaTOs.get(i).getRegionDestino());
			ordenServicioCrearReq.setCodigoComunaEmbalador(etiquetaTOs.get(i).getComunaDestino());
			ordenServicioCrearReq.setCodigoLocalidadOrigen(etiquetaTOs.get(i).getLocalidadDestino());
			ordenServicioCrearReq.setDireccionCompletaEmbalador(etiquetaTOs.get(i).getDireccionDestinatario());
			ordenServicioCrearReq.setNumeroTelefonoEmbalador(etiquetaTOs.get(i).getTelefonoDestinatario());
			ordenServicioCrearReq.setPrefijoTelefonoEmbalador("");
			
			// Destino
			ordenServicioCrearReq.setNombreDestinatario(etiquetaTOs.get(i).getNombreEmbalador());
			ordenServicioCrearReq.setCodigoPaisDestinatario("CL");
			ordenServicioCrearReq.setCodigoRegionDestinatario(etiquetaTOs.get(i).getRegionOrigen());
			ordenServicioCrearReq.setCodigoComunaDestinatario(etiquetaTOs.get(i).getComunaOrigen());
			ordenServicioCrearReq.setCodigoLocalidadDestino(etiquetaTOs.get(i).getLocalidadOrigen());
			ordenServicioCrearReq.setDireccionCompletaDestinatario(etiquetaTOs.get(i).getDireccionEmbalador());
			ordenServicioCrearReq.setNumeroTelefonoDestinatario(etiquetaTOs.get(i).getTelefonoEmbalador());
			ordenServicioCrearReq.setPrefijoTelefonoDestinatario("");
			
			ordenServicioCrearReq.setValorDeclarado(BigDecimal.ZERO);
			ordenServicioCrearReq.setCodigoProducto("P");
			ordenServicioCrearReq.setCodigoMoneda("CLP");
			ordenServicioCrearReq.setCodigoEmpresa(etiquetaTOs.get(i).getCodigoEmpresa());
			ordenServicioCrearReq.setCodigoTipoServicio("EX");
			ordenServicioCrearReq.setCodigoPersona(etiquetaTOs.get(i).getCodigoPersona());
			ordenServicioCrearReq.setCodigoFamiliaProducto("PAQU");
			ordenServicioCrearReq.setObservaciones("DEVOLUCION DE PRODUCTO");
			ordenServicioCrearReq.setSwitchNotificar(false);
			ordenServicioCrearReq.setCodigoAgencia("0");
			
			// Embalaje
			ListaEmisionEmbalajesCrearReq listaEmisionEmbalajesCrearReq = new ListaEmisionEmbalajesCrearReq();
			EmisionEmbalajeCrearReq emisionEmbalajeCrearReq = new EmisionEmbalajeCrearReq();
			emisionEmbalajeCrearReq.setCodigoUnidadLongitud("CM");
			emisionEmbalajeCrearReq.setCodigoUnidadMasa("KG");
			emisionEmbalajeCrearReq.setMasa(etiquetaTOs.get(i).getPesoFisico());
			emisionEmbalajeCrearReq.setAlto(Float.valueOf("21"));
			emisionEmbalajeCrearReq.setLargo(Float.valueOf("22"));
			emisionEmbalajeCrearReq.setAncho(Float.valueOf("23"));			
			listaEmisionEmbalajesCrearReq.getEmisionEmbalajeCrearReq().add(emisionEmbalajeCrearReq);			
			ordenServicioCrearReq.setListaEmisionEmbalajesCrearReq(listaEmisionEmbalajesCrearReq);
			
			// Referencia
			ListaNumerosReferenciaCrearReq listaNumerosReferenciaCrearReq = new ListaNumerosReferenciaCrearReq();
			listaNumerosReferenciaCrearReq.getNumeroReferencia().add(etiquetaTOs.get(i).getSolicitud());
			ordenServicioCrearReq.setListaNumerosReferenciaCrearReq(listaNumerosReferenciaCrearReq);
			
			// Lista Contactos
			
			ObjetoListaContactos objetoListaContactos = new ObjetoListaContactos();
			ContactoCanal contactoCanal = new ContactoCanal();
			ObjetoListaMacroestados objetoListaMacroestados = new ObjetoListaMacroestados();
			Macroestado macroestado = new Macroestado();
			macroestado.setCodigo(0);
			objetoListaMacroestados.getMacroestado().add(macroestado);
			
			contactoCanal.setCodigoTipoContacto("D");
			contactoCanal.setCodigoTipoCanal(2);
			contactoCanal.setValorCanal(etiquetaTOs.get(i).getEmail());
			contactoCanal.setListaMacroestados(objetoListaMacroestados);						
			objetoListaContactos.getContactoCanal().add(contactoCanal);			
			ordenServicioCrearReq.setListaContactosCanal(objetoListaContactos);
			
			requestEmisionIntegracion.setOrdenServicio(ordenServicioCrearReq);
			ResponseEmisionIntegracion responseEmisionIntegracion = service.getBXEMS001EmisionIntegracionPort().emitir(requestEmisionIntegracion, headerRequest);
			
			etiquetaTO.setCodigoEmpresa(etiquetaTOs.get(i).getCodigoEmpresa());
			etiquetaTO.setCodigoEspecieValorada(responseEmisionIntegracion.getIdEspecieValorada());
			etiquetaTO.setNumeroFolio(responseEmisionIntegracion.getNroFolio().longValue());
			etiquetaTO.setContenido(responseEmisionIntegracion.getListaImpresiones().getImpresion().get(0).getContenido());
			etiquetaTO.setCodigoUsuario(etiquetaTOs.get(i).getCodigoUsuario());
			etiquetaTO.setIdToken(etiquetaTOs.get(i).getIdToken());
			
			
		}
		return etiquetaTO;
	}

	@Override
	public RespuestaDevolucionClient retornaDevolucionClient(String nroDocto) throws IOException, SimpleException {
		ConsultaDevolucionResponse consulta = select.selectDevolucion(nroDocto);
		return convertDevolucionClient(consulta);
	}

	private RespuestaDevolucionClient convertDevolucionClient(ConsultaDevolucionResponse response) {
		RespuestaDevolucionClient respuestaConsulta = new RespuestaDevolucionClient();
		if(null != response) {
			BeanUtils.copyProperties(response, respuestaConsulta);
		}
		return respuestaConsulta;
	}

	@Override
	public List<ListaEcommerceTO> retornaEmpresas() throws IOException, SimpleException {
		List<ListaEcommerceTO> response = new ArrayList<>();
		List<ECommerceResponse> listaEmpresas = select.selectEcommerce();
		for (ECommerceResponse eCommerceResponse : listaEmpresas) {
			ListaEcommerceTO eCommerce = new ListaEcommerceTO();
			eCommerce.setCodigoEmpresa(eCommerceResponse.getSegEmpresa());
			eCommerce.setNombreEmpresa(eCommerceResponse.getNombreEmpresa());
			response.add(eCommerce);
		}
		return response;
	}

	@Override
	public List<ListaMotivosTO> retornaMotivos() throws IOException, SimpleException{
		List<ListaMotivosTO> response = new ArrayList<>();
		List<MotivoResponse> listaMotivos = select.selectMotivo();
		for (MotivoResponse motivoResponse : listaMotivos) {
			ListaMotivosTO motivo = new ListaMotivosTO();
			motivo.setCodigoMotivo(motivoResponse.getCodigoMotivo());
			motivo.setNombreMotivo(motivoResponse.getNombreMotivo());
			response.add(motivo);
		}
		return response;
	}

	@Override
	public boolean consultarOS(long idOs, int idEcommerce) throws IOException, SimpleException {
		boolean respuesta=false;
		if(null != select.consultarOS(idOs, idEcommerce).getOser_nmbr_emba()) {
			respuesta=true;
		}
		return respuesta;
	}
	
	@Override
	public boolean consultarDocumento(String numeroDocumento, int idEcommerce) throws IOException, SimpleException {
		boolean respuesta = false;
		if(null != select.consultarDocumento(numeroDocumento, idEcommerce).getOser_nmbr_emba()) {
			respuesta=true;
		}
		return respuesta;
	}	
	

	@Override
	public boolean envioMail(String destinatario, String nombreUsuario, String idRequerimiento, int tipoUpd, EtiquetaTO etiquetaTO) throws MessagingException, Exception {
		EnvioEmail envio = new EnvioEmail();
	      String pais = "CL";
	      String asunto = "";
	      String nombreArchivo="";
	      if(tipoUpd==1) {	    	  
	    	  asunto = "Ingreso de Devolucion";
	    	  nombreArchivo = "ingreso.txt";
	      }else if(tipoUpd==2) {	    	  
	    	  asunto = "Rechazo de Devolucion";
	    	  nombreArchivo = "rechazo.txt";
	      }else if(tipoUpd==4) {	    	  
	    	  asunto = "Aprobación de Devolucion";
	    	  nombreArchivo = "aprueba.txt";
	      }
	      
	      String mensaje = leerArchivo(nombreUsuario, idRequerimiento, tipoUpd, nombreArchivo, etiquetaTO);
	      String[] names =new String[1];
	      names[0] = destinatario;
	      return envio.enviaEmailHTML(pais.toUpperCase(), names, asunto, mensaje);
	}

	private String leerArchivo(String nombreUsuario, String idRequerimiento, int tipoUpd, String nombreArchivo, EtiquetaTO etiquetaTO) {
		String content = "";
		try {
			ClassPathResource resource = new ClassPathResource(nombreArchivo);
			Properties p = new Properties();
			InputStream in = resource.getInputStream();
			p.load(new ClassPathResource("devolucion.properties").getInputStream());
			String codigoFormato = p.getProperty("cl.bluex.etiqueta.formato");
			String host = p.getProperty("host")+":"+p.getProperty("port");
			
			InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			if(tipoUpd==1) {				
				for (String line; (line = reader.readLine()) != null;) { // Process line
					if(line.contains("Hola (Nombre destinatario),")) {
						line = "Hola "+nombreUsuario+", ";
					}
					if(line.equalsIgnoreCase("41344245")) {
						line = idRequerimiento;
					}
					if(line.contains("localhost:8080")) {
						line = line.replace("localhost:8080", "www.bluexQA.cl:8080");
					}
					content = content + line;
				}
			}else if(tipoUpd==2 || tipoUpd==4){
				String linkEtiqueta=null;
				if(tipoUpd==4) {					
					linkEtiqueta = armarUrl(p.getProperty("cl.bluex.etiqueta.servlet"), etiquetaTO, codigoFormato);
				}
				for (String line; (line = reader.readLine()) != null;) { // Process line
					if(line.contains("Hola (Nombre destinatario),")) {
						line = "Hola "+nombreUsuario+", ";
					}
					if(line.equalsIgnoreCase("422545") || line.equalsIgnoreCase("24552")) {
						line = idRequerimiento;
					}
					if(line.equalsIgnoreCase("--aqui--")) {
						line = linkEtiqueta;
					}
					
					content = content + line;
				}
			}
			  in.close();
		} catch (IOException ioe){
			  ioe.printStackTrace();
		}
		return content;
	}
	
	private String armarUrl(String url, EtiquetaTO etiquetaTo, String codigoFormato) {
		url = url.replace("{0}", codigoFormato);
		url = url.replace("{1}", String.valueOf(etiquetaTo.getCodigoEmpresa()));
		url = url.replace("{2}", String.valueOf(etiquetaTo.getCodigoEspecieValorada()));
		url = url.replace("{3}", String.valueOf(etiquetaTo.getCodigoUsuario()));
		url = url.replace("{4}", String.valueOf(etiquetaTo.getIdToken()));
		//url.replace("10.30.60.20", "www.bluexQA.cl");
		System.out.println("nueva url: " + url);
		return url;
	}

	@Override
	public RetornaDashBoardeCommerce dashboradEcommerce(List<DashboardRequest> dashRequest) throws IOException, SimpleException {
		RetornaDashBoardeCommerce retornaDevolucion = new RetornaDashBoardeCommerce();
		List<RespuestaDevolucionClient> listado = new ArrayList<>();
		List<DevolucionResponse> salida = select.consultaDevolucionesDasheCommerce(dashRequest);
		int pendientes=0;
		int aceptadas=0;
		int rechazadas=0;
		for (DevolucionResponse devolucionResponse : salida) {
			if(devolucionResponse.getCodEstadoDevolucion()==1) {
				pendientes++;
			}else if(devolucionResponse.getCodEstadoDevolucion()==2) {
				rechazadas++;
			}else if(devolucionResponse.getCodEstadoDevolucion()==4) {
				aceptadas++;
			}
			RespuestaDevolucionClient objeto = new RespuestaDevolucionClient();
			objeto.setCdgSeqReq(devolucionResponse.getCodDevolucion());
			objeto.setEstadoDevolucion(devolucionResponse.getNombreDevolucion());
			objeto.setFechaIngresoDevolucion(devolucionResponse.getFechaDevolucion());
			objeto.setMotivoDevolucion(devolucionResponse.getNombreMotivo());
			objeto.setNombreCliente(devolucionResponse.getNombreUsuario());
			objeto.setNombreProducto(devolucionResponse.getDescripProducto());
			objeto.setNumeroRequerimiento(devolucionResponse.getNroRequerimiento());
			objeto.setNumeroOrdenServicio(String.valueOf(devolucionResponse.getNroOrdenServicio()));
			objeto.setNombreEmpresa(devolucionResponse.geteCommerce());
			objeto.setEmailCliente(devolucionResponse.getEmailUsuario());
			objeto.setTelefonoCliente(devolucionResponse.getTelefono());			
			objeto.setRutCliente(devolucionResponse.getIdentificador());
			listado.add(objeto);
		}
		retornaDevolucion.setLstDevoluciones(listado);
		retornaDevolucion.setPendientes(pendientes);
		retornaDevolucion.setRechazados(rechazadas);
		retornaDevolucion.setAceptados(aceptadas);
		return retornaDevolucion;
	}

	@Override
	public String obtieneUsuario(String nickname, String pass) throws BluexException_Exception, NoSuchAlgorithmException, IOException, SimpleException {
		RequestDatosUsuario req = new RequestDatosUsuario();
		NewUsuario usuario = new NewUsuario();
		usuario.setUsername(nickname.toUpperCase());
	    usuario.setPassword(pass);
		req.setParametrosEntrada(usuario);
		SeguridadService seguridad = new SeguridadService();
		LoginResponseTO loginResponse=new LoginResponseTO();
		String nLista ="";
		try {
			ResponseDatosUsuario response = seguridad.getSeguridadServicePort().getValidarUsuario(req);
			loginResponse.setCodigoUsuario(Integer.parseInt(response.getParametrosSalida().getCodigoUsuario()));
			loginResponse.setToken(response.getParametrosSalida().getToken());
			BXAUX002OpcionesServicio_Service servicioCuentas = new BXAUX002OpcionesServicio_Service();
			HeaderRequest header = new HeaderRequest();
			header.setCodigoUsuario(loginResponse.getCodigoUsuario());
			header.setIdToken(loginResponse.getToken());
			RequestCuentasCorrientesFiltrar reqCtaCte = new RequestCuentasCorrientesFiltrar();
			reqCtaCte.setCodigoEmpresa(2000);
			reqCtaCte.setMantenedor(false);
			ResponseCuentasCorrientesFiltrar responseCta = servicioCuentas.getBXAUX002OpcionesServicioPort().obtenerCuentasCorrientes(reqCtaCte, header);
			responseCta.getListaCuentasCorrientes().getCuentaCorriente();

			for (CuentaCorriente elemento : responseCta.getListaCuentasCorrientes().getCuentaCorriente()) {
				/*String[] numeroCuenta = elemento.getNumeroCuenta().split("-");
				DashboardRequest dashboard = new DashboardRequest();
				dashboard.setCodigo(Integer.parseInt(numeroCuenta[0]));
				dashboard.setSucursal(Integer.parseInt(numeroCuenta[1]));
				dashboard.setTipo(Integer.parseInt(numeroCuenta[2]));
				listdashboard.add(dashboard);*/
				nLista = nLista + elemento.getNumeroCuenta()+";";
			}
		} catch (BluexException_Exception e) {
			e.printStackTrace();
			System.out.println("Error: "+e.getMessage());
		} catch (cl.bluex.opcionesservicio.cliente.BluexException_Exception e) {
			e.printStackTrace();
		}
		System.out.println("login service response: " + loginResponse.toString());
		return nLista.substring(0, nLista.length()-1);
	}

}
