import java.util.LinkedList;

public abstract class Usuario {

	private static LinkedList<Usuario> usuarios = new LinkedList<Usuario>();
	
	
	private String mail;
	private String contr;
	private Rol rol;
	
	public Usuario(String mail, String contr, Rol rol) {
		super();
		this.mail = mail;
		this.contr = contr;
		this.rol = rol;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getContr() {
		return contr;
	}
	public void setContr(String contr) {
		this.contr = contr;
	}
	
	public static LinkedList<Usuario> getUsuarios() {
		return usuarios;
	}
	public static void setUsuarios(LinkedList<Usuario> usuarios) {
		Usuario.usuarios = usuarios;
	}
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	@Override
	public String toString() {
		return "Usuario [mail=" + mail + ", contr=" + contr + "]";
	}
	public static Usuario Login(String mail, String contr) {
		for (Usuario usuario : usuarios) {
			if (usuario.getMail().equals(mail) && usuario.getContr().equals(contr)) {
				return usuario;
			}
		}
		
		return null;
		}
		
	public static int getCantidadClientes() {
	    int count = 0;
	    for (Usuario usuario : usuarios) {
	        if (usuario instanceof Cliente) {
	            count++;
	       }
	    }
		    return count;
	}
	public abstract void Menu();
	
	public void Menu1() {
		// TODO Auto-generated method stub
		
	}
	
}
