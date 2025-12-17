package modelo;

public class Usuario
{
    private int idUsuario;
    private String nombreUsuario;
    private String username;
    private String passwordHash;
    private boolean esAdministrador;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String username, boolean esAdministrador) {
        this.setIdUsuario(idUsuario);
        this.setNombreUsuario(nombreUsuario);
        this.setUsername(username);
        this.setEsAdministrador(esAdministrador);
        this.passwordHash = "";
    }

    public void setIdUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("El nombre no debe estar vacío.");
        }
        this.nombreUsuario = nombreUsuario;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("El username no debe estar vacío.");
        }
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isEmpty()) {
            throw new IllegalArgumentException("El password hash no debe estar vacío.");
        }
        this.passwordHash = passwordHash;
    }

    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {return passwordHash;}

    public boolean isAdministrador() {
        return esAdministrador;
    }
}
