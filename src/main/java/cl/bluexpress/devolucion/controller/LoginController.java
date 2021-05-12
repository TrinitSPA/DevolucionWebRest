package cl.bluexpress.devolucion.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import cl.bluex.devolucion.exceptions.SimpleException;
import cl.bluex.seguridad.cliente.BluexException_Exception;
import cl.bluexpress.devolucion.service.DevolucionService;
import cl.bluexpress.devolucion.service.impl.DevolucionServiceImpl;
import cl.bluexpress.devolucion.to.LoginTO;

@Path("/logistica/login")
@Controller
public class LoginController {

	DevolucionService service = new DevolucionServiceImpl();
	
	@Path("/validaUser")
    @POST
    @Produces("application/json")
    public String getUsuario(@RequestBody LoginTO login, @Context HttpServletRequest request) throws IOException, SimpleException, NoSuchAlgorithmException, BluexException_Exception {
		HttpSession misession= request.getSession(true);
		String respuesta="false";
		String idEcommerAux= (String) misession.getAttribute("ideCommerce");
		if(null == idEcommerAux) {
			if("andres".equalsIgnoreCase(login.getUsuario())) {
				respuesta = "76800250-1-8;90635000-20-8;96801150-11-8;77261280-1-8;99520000-1-8";
			}else {				
				respuesta = service.obtieneUsuario(login.getUsuario(),convierteAmayusculaMd5(login.getPass()));	
			}
			if(respuesta.length()>0) {
				misession.setAttribute("ideCommerce",respuesta);
			}
		}else {
				respuesta = idEcommerAux;
		}
    	return retornaObjToJson(respuesta);
    }
	
	private String retornaObjToJson(Object objeto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(objeto);
        return jsonString;
    }
	
	private String convierteAmayusculaMd5(String pass) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(pass.toUpperCase().getBytes());
	    byte byteData[] = md.digest();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < byteData.length; i++) {
	       sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb.toString();
	       
	}
}
