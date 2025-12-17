package dao;

import bd.ConexionSQLite;
import modelo.*;
import utils.DateTimeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class InscripcionDAO implements ISimpleDAO<Inscripcion, Integer>
{
    @Override
    public void create(Inscripcion i) throws SQLException {
        final String query =
                "INSERT INTO Inscripcion (rut_adulto, id_actividad) " +
                        "VALUES (?, ?)";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setString(1, i.getAdultoMayor().getRut());
            ps.setInt(2, i.getActividad().getIdActividad());
            ps.executeUpdate();
        }
    }

    @Override
    public Inscripcion readOne(Integer idInscripcion) throws SQLException {
        Inscripcion i = null;

        final String query =
                "SELECT " +
                        "i.id_inscripcion, " +
                        
                        "am.rut, am.nombre AS am_nombre, am.apellido, am.fecha_nacimiento, " +
                        "am.nombre_encargado, am.fono_encargado, " +
                        
                        "ac.id_actividad, ac.nombre AS ac_nombre, ac.descripcion, " +
                        "ac.dia, ac.hora_inicio, ac.hora_fin, ac.cupos, ac.nombre_profesor, " +
                        
                        "c.id_categoria, c.nombre AS c_nombre " +

                        "FROM Inscripcion i " +
                        "JOIN AdultoMayor am ON i.rut_adulto = am.rut " +
                        "JOIN Actividad ac ON i.id_actividad = ac.id_actividad " +
                        "JOIN Categoria c ON ac.id_categoria = c.id_categoria " +
                        "WHERE i.id_inscripcion = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setInt(1, idInscripcion);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                    i = mapInscripcion(rs);
            }
        }
        return i;
    }

    @Override
    public ArrayList<Inscripcion> readAll() throws SQLException {
        ArrayList<Inscripcion> inscripciones = new ArrayList<>();

        final String query =
                "SELECT " +
                        "i.id_inscripcion, " +

                        "am.rut, am.nombre AS am_nombre, am.apellido, am.fecha_nacimiento, " +
                        "am.nombre_encargado, am.fono_encargado, " +

                        "ac.id_actividad, ac.nombre AS ac_nombre, ac.descripcion, " +
                        "ac.dia, ac.hora_inicio, ac.hora_fin, ac.cupos, ac.nombre_profesor, " +

                        "c.id_categoria, c.nombre AS c_nombre " +

                        "FROM Inscripcion i " +
                        "JOIN AdultoMayor am ON i.rut_adulto = am.rut " +
                        "JOIN Actividad ac ON i.id_actividad = ac.id_actividad " +
                        "JOIN Categoria c ON ac.id_categoria = c.id_categoria";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
                inscripciones.add(mapInscripcion(rs));
        }
        return inscripciones;
    }

    @Override
    public ArrayList<Inscripcion> readBy(String filtro) throws SQLException {
        ArrayList<Inscripcion> inscripciones = new ArrayList<>();

        final String query =
                "SELECT " +
                        "i.id_inscripcion, " +

                        "am.rut, am.nombre AS am_nombre, am.apellido, am.fecha_nacimiento, " +
                        "am.nombre_encargado, am.fono_encargado, " +

                        "ac.id_actividad, ac.nombre AS ac_nombre, ac.descripcion, " +
                        "ac.dia, ac.hora_inicio, ac.hora_fin, ac.cupos, ac.nombre_profesor, " +

                        "c.id_categoria, c.nombre AS c_nombre " +

                        "FROM Inscripcion i " +
                        "JOIN AdultoMayor am ON i.rut_adulto = am.rut " +
                        "JOIN Actividad ac ON i.id_actividad = ac.id_actividad " +
                        "JOIN Categoria c ON ac.id_categoria = c.id_categoria " +
                        "WHERE am.rut LIKE ? OR am.nombre LIKE ? OR am.apellido LIKE ? " +
                        "OR ac.nombre LIKE ? OR ac.nombre_profesor LIKE ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            String like = "%" + filtro + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                    inscripciones.add(mapInscripcion(rs));
            }
        }
        return inscripciones;
    }

    @Override
    public void update(Inscripcion i) throws SQLException {
        final String query =
                "UPDATE Inscripcion SET rut_adulto = ?, id_actividad = ? " +
                        "WHERE id_inscripcion = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setString(1, i.getAdultoMayor().getRut());
            ps.setInt(2, i.getActividad().getIdActividad());
            ps.setInt(3, i.getIdInscripcion());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer idInscripcion) throws SQLException {
        final String query = "DELETE FROM Inscripcion WHERE id_inscripcion = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            ps.setInt(1, idInscripcion);
            ps.executeUpdate();
        }
    }

    public boolean existeInscripcion(String rutAdulto, int idActividad) throws SQLException {
        final String query = "SELECT COUNT(*) FROM Inscripcion WHERE rut_adulto = ? AND id_actividad = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setString(1, rutAdulto);
            ps.setInt(2, idActividad);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private Inscripcion mapInscripcion(ResultSet rs) throws SQLException
    {
        int idInscripcion = rs.getInt("id_inscripcion");

        AdultoMayor adulto = new AdultoMayor(
                rs.getString("rut"),
                rs.getString("am_nombre"),
                rs.getString("apellido"),
                DateTimeUtils.epochALocalDate(rs.getLong("fecha_nacimiento")),
                rs.getString("nombre_encargado"),
                rs.getString("fono_encargado")
        );

        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("c_nombre")
        );

        Actividad actividad = new Actividad(
                rs.getInt("id_actividad"),
                rs.getString("ac_nombre"),
                categoria,
                rs.getString("descripcion"),
                DayOfWeek.of(rs.getInt("dia")),
                DateTimeUtils.minutosALocalTime(rs.getInt("hora_inicio")),
                DateTimeUtils.minutosALocalTime(rs.getInt("hora_fin")),
                rs.getInt("cupos"),
                rs.getString("nombre_profesor")
        );

        return new Inscripcion(idInscripcion, adulto, actividad);
    }
}
