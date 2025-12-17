package dao;

import bd.ConexionSQLite;
import modelo.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioDAO implements ISimpleDAO<Usuario, Integer>
{
    public Usuario obtenerDatos(String username) throws SQLException {
        final String query = "SELECT * FROM Usuario WHERE username = ?";
        Usuario usuarioEncontrado = null;

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuarioEncontrado = new Usuario();

                    usuarioEncontrado.setIdUsuario(rs.getInt("id_usuario"));
                    usuarioEncontrado.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuarioEncontrado.setPasswordHash(rs.getString("password_hash"));
                    usuarioEncontrado.setEsAdministrador(rs.getBoolean("es_administrador"));
                    usuarioEncontrado.setUsername(username);
                }
            }
        }
        return usuarioEncontrado;
    }

    @Override
    public void create(Usuario u) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public Usuario readOne(Integer idUsuario) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public ArrayList<Usuario> readAll() throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        final String query = "SELECT * FROM Usuario";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                    usuarios.add(mapUsuario(rs));
            }
        }
        return usuarios;
    }

    @Override
    public ArrayList<Usuario> readBy(String filtro) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public void update(Usuario u) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public void delete(Integer idUsuario) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    private Usuario mapUsuario(ResultSet rs) throws SQLException {
        int idUsuario = rs.getInt("id_usuario");
        String nombreUsuario = rs.getString("nombre_usuario");
        String username = rs.getString("username");
        boolean es_administrador = rs.getBoolean("es_administrador");

        return new Usuario(idUsuario, nombreUsuario, username, es_administrador);
    }
}