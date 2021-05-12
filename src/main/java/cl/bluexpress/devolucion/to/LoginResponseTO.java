package cl.bluexpress.devolucion.to;

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

@Produces("application/json")
@XmlRootElement
public class LoginResponseTO {

	String token;
	int codigoUsuario;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(int codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	@Override
	public String toString() {
		return "LoginResponseTO [token=" + token + ", codigoUsuario=" + codigoUsuario + "]";
	}
	
	
}
