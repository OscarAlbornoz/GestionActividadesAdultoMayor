package dao;

import bd.ConexionSQLite;
import modelo.AdultoMayor;
import utils.DateTimeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdultoMayorDAO implements ISimpleDAO<AdultoMayor, String>
{
    @Override
    public void create(AdultoMayor a) throws SQLException {
        final String query =
                "INSERT INTO AdultoMayor (rut, nombre, apellido, fecha_nacimiento, nombre_encargado, fono_encargado) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setString(1, a.getRut());
            ps.setString(2, a.getNombreAdultoMayor());
            ps.setString(3, a.getApellidoAdultoMayor());
            ps.setLong(4, DateTimeUtils.localDateAEpoch(a.getNacimiento()));
            ps.setString(5, a.getNombreEncargado());
            ps.setString(6, a.getContactoEncargado());

            ps.executeUpdate();
        }
    }


    @Override
    public AdultoMayor readOne(String rut) throws SQLException {
        AdultoMayor a = null;
        final String query = "SELECT * FROM AdultoMayor WHERE rut = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                    a = mapAdultoMayor(rs);
            }
        }
        return a;
    }

    @Override
    public ArrayList<AdultoMayor> readAll() throws SQLException {
        ArrayList<AdultoMayor> adultos = new ArrayList<>();
        final String query = "SELECT * FROM AdultoMayor";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                    adultos.add(mapAdultoMayor(rs));
            }
        }
        return adultos;
    }

    @Override
    public ArrayList<AdultoMayor> readBy(String filtro) throws SQLException {
        ArrayList<AdultoMayor> adultos = new ArrayList<>();

        final String query = "SELECT * FROM AdultoMayor WHERE rut LIKE ? OR nombre LIKE ? OR apellido LIKE ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            String like = "%" + filtro + "%";

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    adultos.add(mapAdultoMayor(rs));
                }
            }
        }
        return adultos;
    }

    @Override
    public void update(AdultoMayor a) throws SQLException {
        final String query =
                "UPDATE AdultoMayor SET " +
                "nombre = ?, apellido = ?, fecha_nacimiento = ?, nombre_encargado = ?, fono_encargado = ? " +
                "WHERE rut = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setString(1, a.getNombreAdultoMayor());
            ps.setString(2, a.getApellidoAdultoMayor());
            ps.setLong(3, DateTimeUtils.localDateAEpoch(a.getNacimiento()));
            ps.setString(4, a.getNombreEncargado());
            ps.setString(5, a.getContactoEncargado());
            ps.setString(6, a.getRut());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String rut) throws SQLException {
        final String query = "DELETE FROM AdultoMayor WHERE rut = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setString(1, rut);
            ps.executeUpdate();
        }
    }

    public boolean existeRut(String rut) throws SQLException {
        final String query = "SELECT COUNT(*) FROM AdultoMayor WHERE rut = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setString(1, rut);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private AdultoMayor mapAdultoMayor(ResultSet rs) throws SQLException
    {
        String rut = rs.getString("rut");
        String nombreAdultoMayor = rs.getString("nombre");
        String apellidoAdultoMayor = rs.getString("apellido");
        LocalDate fechaNacimiento = DateTimeUtils.epochALocalDate(rs.getLong("fecha_nacimiento"));
        String nombreEncargado = rs.getString("nombre_encargado");
        String contactoEncargado = rs.getString("fono_encargado");

        return new AdultoMayor(rut, nombreAdultoMayor, apellidoAdultoMayor, fechaNacimiento, nombreEncargado, contactoEncargado);
    }
}
