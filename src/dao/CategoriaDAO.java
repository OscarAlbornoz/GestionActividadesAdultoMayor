package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Categoria;
import bd.ConexionSQLite;

public class CategoriaDAO implements ISimpleDAO<Categoria, Integer>
{
    @Override
    public void create(Categoria c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public Categoria readOne(Integer idCategoria) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public ArrayList<Categoria> readAll() throws SQLException {
        ArrayList<Categoria> categorias = new ArrayList<>();
        String query = "SELECT id_categoria, nombre FROM Categoria";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                categorias.add(c);
            }
        }
        return categorias;
    }

    @Override
    public ArrayList<Categoria> readBy(String filtro) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public void update(Categoria c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }

    @Override
    public void delete(Integer idCategoria) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Está función no está implementada.");
    }
}