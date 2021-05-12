package cl.bluexpress.devolucion.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import cl.bluex.devolucion.exceptions.SimpleException;
import cl.bluex.devolucion.to.DashboardRequest;
import cl.bluexpress.devolucion.service.DevolucionService;
import cl.bluexpress.devolucion.service.impl.DevolucionServiceImpl;
import cl.bluexpress.devolucion.to.ActualizaDevolucionTO;
import cl.bluexpress.devolucion.to.FiltroFechasTO;
import cl.bluexpress.devolucion.to.IngresoDevolucionTo;
import cl.bluexpress.devolucion.to.LoginTO;



@Path("/logistica")
@Controller
public class DevolucionController {

	DevolucionService service = new DevolucionServiceImpl();

	@Path("/motivos")
    @GET
    @Produces("application/json")
    public String getMotivos() throws IOException, SimpleException {
    	return retornaObjToJson(service.retornaMotivos());
    }

	@Path("/eCommerces")
    @GET
    @Produces("application/json")
    public String getEcommerce() throws IOException, SimpleException {
    	return retornaObjToJson(service.retornaEmpresas());
    }

	@Path("/insertDevolutionClient/")
    @POST
    @Produces("application/json")
    public String putDevolution(@RequestBody IngresoDevolucionTo ingresoRequest, @Context HttpServletRequest request) throws MessagingException, Exception {
    	return retornaObjToJson(service.insertaDevolucionClient(ingresoRequest));
    }

	@Path("/devolucionClient/{nroDoc}")
    @GET
    @Produces("application/json")
    public String getDevolution(@PathParam("nroDoc") String nroDoc) throws IOException, SimpleException {
    	return retornaObjToJson(service.retornaDevolucionClient(nroDoc));
    }

	@Path("/validaUser/")
    @POST
    @Produces("application/json")
    public String getUsuario(@RequestBody LoginTO login, @Context HttpServletRequest request) throws IOException {
		HttpSession misession= request.getSession(true);
		String url="false";
		/**TODO validar usuario**/
		if(login.getUsuario().equalsIgnoreCase("andres")) {
			//respuesta = true;
			misession.setAttribute("login",login);
			misession.setAttribute("ideCommerce","1");
			url = "dashboard.html";

		}
    	return retornaObjToJson(url);
    }

	@Path("/buscarDevolucionxId/{nroDoc}")
    @GET
    @Produces("application/json")
    public String getDevolucionProducto(@PathParam("nroDoc") String nroDoc, @Context HttpServletRequest request) throws IOException, SimpleException {
		//HttpSession misession= request.getSession(true);
		//LoginTO login= (LoginTO) misession.getAttribute("login");
		//String nroDocAux =  (String) misession.getAttribute("nroDoc");
		//if(nroDocAux==null) {
		//	nroDocAux = nroDoc;
		//}
		String respuesta="false";
		/**TODO validar usuario**/

		//if(null != login) {
		//		if("undefined".equalsIgnoreCase(nroDoc)) {
		//			nroDoc = nroDocAux;
		//		}
		//	 	misession.setAttribute("nroDoc",nroDoc);
				respuesta = retornaObjToJson(service.retornaDevolucionClient(nroDoc));
		//}else {
		//	misession.setAttribute("url","/devolucionRest/new/consultaDevolucionXId.html");
		//	misession.setAttribute("nroDoc",nroDoc);
		//}
		return respuesta;
    }

	@Path("/consultarOS/{idOS}/{idEcommerce}")
    @GET
    @Produces("application/json")
    public String consultaOS(@PathParam("idOS") String idOS, @PathParam("idEcommerce") String idEcommerce, @Context HttpServletRequest request) throws IOException, SimpleException {
		//return retornaObjToJson(service.consultarOS(Long.parseLong(idOS), Integer.parseInt(idEcommerce)));
		return retornaObjToJson(service.consultarDocumento(idOS, Integer.parseInt(idEcommerce)));
    }

	@Path("/envioMail/{mail}/{nombre}/{ticket}")
    @GET
    @Produces("application/json")
    public String envioMail(@PathParam("mail") String mail,@PathParam("nombre") String nombre,@PathParam("ticket") String ticket, @Context HttpServletRequest request) throws IOException, MessagingException, Exception {
		return retornaObjToJson(service.envioMail(mail, nombre, ticket,1, null));
    }

	@Path("/dashboard/eCommerce")
    @POST
    @Produces("application/json")
    public String dashBoardeCommerce(@RequestBody FiltroFechasTO filtroFechas, @Context HttpServletRequest request) throws IOException, MessagingException, Exception {
		HttpSession misession= request.getSession(true);
		String idEcommerAux= (String) misession.getAttribute("ideCommerce");
		System.out.println("idEcommerAux: " + idEcommerAux);
		if(null != idEcommerAux) {
			List<DashboardRequest> dashRequest = new ArrayList<>();
			DashboardRequest dash = new DashboardRequest();
			String listadoCuentas = (String) misession.getAttribute("ideCommerce");
			System.out.println("largo de cuentas: "+listadoCuentas);
			if(null != listadoCuentas){
				String[] lsCuenta = listadoCuentas.split(";");
				for (String string : lsCuenta) {
					String[] cuenta = string.split("-");
					dash = new DashboardRequest();
					dash.setCodigo(Integer.parseInt(cuenta[0]));
					dash.setSucursal(Integer.parseInt(cuenta[1]));
					dash.setTipo(Integer.parseInt(cuenta[2]));
					dash.setFechaInicio(filtroFechas.getFechaInicio());
					dash.setFechaFin(filtroFechas.getFechaFin());
					dashRequest.add(dash);
				}
			}
			//TODO hasta aquí
			System.out.println("dashRequest: " + dashRequest.toString());
			return retornaObjToJson(service.dashboradEcommerce(dashRequest));
		}else {
			return retornaObjToJson("false");
		}
		
    }
	
	@Path("/dashboard/updateDevolucion")
    @POST
    @Produces("application/json")
	public String updateDevolucion(@RequestBody ActualizaDevolucionTO actualizaTO, @Context HttpServletRequest request) throws MessagingException, Exception {
		return retornaObjToJson(service.updateDevolucion(actualizaTO));
	}

	private String retornaObjToJson(Object objeto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(objeto);
        return jsonString;
    }

}
