package cl.bluexpress.devolucion.controller;

import java.security.MessageDigest;

import javax.mail.MessagingException;

import cl.bluexpress.devolucion.service.DevolucionService;
import cl.bluexpress.devolucion.service.impl.DevolucionServiceImpl;

public class Test {

	public static void main(String[] args) throws MessagingException, Exception {
		// TODO Auto-generated method stub
		//DevolucionService service = new DevolucionServiceImpl();
		//System.out.println(service.obtieneUsuario());
		
		DevolucionService service = new DevolucionServiceImpl();
		MessageDigest md = MessageDigest.getInstance("MD5");
	       md.update("HAFE".getBytes());

	       byte byteData[] = md.digest();

	       StringBuffer sb = new StringBuffer();
	       for (int i = 0; i < byteData.length; i++) {
	           sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	       }

	       System.out.println("Pass MD5:: " + sb.toString());
	       
		   System.out.println(service.obtieneUsuario("HFARFAN",sb.toString()));
		   
		//enviarMail();
	}
	
	private static void enviarMail() throws MessagingException, Exception {
		DevolucionService service = new DevolucionServiceImpl();
		service.envioMail("a.castro.m@gmail.com", "andres", "123",4, null);
	}

}
