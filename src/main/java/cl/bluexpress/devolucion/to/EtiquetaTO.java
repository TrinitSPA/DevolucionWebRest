package cl.bluexpress.devolucion.to;

public class EtiquetaTO {

	private int codigoEmpresa;
	private long codigoEspecieValorada;
	private long numeroFolio;
	private String contenido;
	private Integer codigoUsuario;
	private String idToken;
	
	public EtiquetaTO() {
		super();
	}

	public EtiquetaTO(int codigoEmpresa, long codigoEspecieValorada, long numeroFolio, String contenido,
			Integer codigoUsuario, String idToken) {
		super();
		this.codigoEmpresa = codigoEmpresa;
		this.codigoEspecieValorada = codigoEspecieValorada;
		this.numeroFolio = numeroFolio;
		this.contenido = contenido;
		this.codigoUsuario = codigoUsuario;
		this.idToken = idToken;
	}

	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public long getCodigoEspecieValorada() {
		return codigoEspecieValorada;
	}

	public void setCodigoEspecieValorada(long codigoEspecieValorada) {
		this.codigoEspecieValorada = codigoEspecieValorada;
	}

	public long getNumeroFolio() {
		return numeroFolio;
	}

	public void setNumeroFolio(long numeroFolio) {
		this.numeroFolio = numeroFolio;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Integer getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Integer codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	@Override
	public String toString() {
		return "EtiquetaTO [codigoEmpresa=" + codigoEmpresa + ", codigoEspecieValorada=" + codigoEspecieValorada
				+ ", numeroFolio=" + numeroFolio + ", contenido=" + contenido + ", codigoUsuario=" + codigoUsuario
				+ ", idToken=" + idToken + "]";
	}
	
}
