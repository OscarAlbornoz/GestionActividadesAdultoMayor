package dao;

import bd.ConexionSQLite;
import modelo.Actividad;
import modelo.Categoria;
import utils.DateTimeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class ActividadDAO implements ISimpleDAO<Actividad, Integer>
{
    @Override
    public void create(Actividad a) throws SQLException {
        final String query =
                "INSERT INTO Actividad " +
                        "(nombre, id_categoria, descripcion, dia, hora_inicio, hora_fin, cupos, nombre_profesor) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            prepararQuery(a, ps);
            ps.executeUpdate();
        }
    }

    @Override
    public Actividad readOne(Integer idActividad) throws SQLException {
        Actividad a = null;
        final String query =
                "SELECT a.*, c.id_categoria, c.nombre AS categoria_nombre " +
                        "FROM Actividad a " +
                        "JOIN Categoria c ON a.id_categoria = c.id_categoria " +
                        "WHERE a.id_actividad = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setInt(1, idActividad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    a = mapActividad(rs);
            }
        }
        return a;
    }

    @Override
    public ArrayList<Actividad> readAll() throws SQLException {
        ArrayList<Actividad> actividades = new ArrayList<>();
        final String query =
                "SELECT a.*, c.id_categoria, c.nombre AS categoria_nombre " +
                        "FROM Actividad a " +
                        "JOIN Categoria c ON a.id_categoria = c.id_categoria";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    actividades.add(mapActividad(rs));
            }
        }
        return actividades;
    }

    @Override
    public ArrayList<Actividad> readBy(String filtro) throws SQLException {
        ArrayList<Actividad> actividades = new ArrayList<>();

        int idDiaBuscado = DateTimeUtils.convertirTextoADia(filtro);
        final String query =
                "SELECT a.*, c.id_categoria, c.nombre AS categoria_nombre " +
                        "FROM Actividad a " +
                        "JOIN Categoria c ON a.id_categoria = c.id_categoria " +
                        "WHERE a.nombre LIKE ? " +
                        "   OR c.nombre LIKE ? " +
                        "   OR a.nombre_profesor LIKE ? " +
                        "   OR a.dia = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query))
        {
            String like = "%" + filtro + "%";

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setInt(4, idDiaBuscado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    actividades.add(mapActividad(rs));
            }
        }
        return actividades;
    }

    @Override
    public void update(Actividad a) throws SQLException {
        final String query =
                "UPDATE Actividad SET " +
                        "nombre = ?, id_categoria = ?, descripcion = ?, dia = ?, " +
                        "hora_inicio = ?, hora_fin = ?, cupos = ?, nombre_profesor = ? " +
                        "WHERE id_actividad = ?";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            prepararQuery(a, ps);
            ps.setInt(9, a.getIdActividad());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer idActividad) throws SQLException {
        final String query = "DELETE FROM Actividad WHERE id_actividad = ?";
        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setInt(1, idActividad);
            ps.executeUpdate();
        }
    }

    public boolean descontarCupo(int idActividad) throws SQLException {
        final String query =
                "UPDATE Actividad SET cupos = cupos - 1 " +
                        "WHERE id_actividad = ? AND cupos > 0";

        try (PreparedStatement ps = ConexionSQLite.getConexion().prepareStatement(query)) {
            ps.setInt(1, idActividad);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    private void prepararQuery(Actividad a, PreparedStatement ps) throws SQLException {
        ps.setString(1, a.getNombreActividad());
        ps.setInt(2, a.getCategoria().getIdCategoria());
        ps.setString(3, a.getDescripcion());
        ps.setInt(4, a.getDia().getValue());
        ps.setInt(5, DateTimeUtils.localTimeAMinutos(a.getHoraInicio()));
        ps.setInt(6, DateTimeUtils.localTimeAMinutos(a.getHoraFin()));
        ps.setInt(7, a.getCupos());
        ps.setString(8, a.getNombreProfesor());
    }

    private Actividad mapActividad(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("categoria_nombre")
        );

        return new Actividad(
                rs.getInt("id_actividad"),
                rs.getString("nombre"),
                categoria,
                rs.getString("descripcion"),
                DayOfWeek.of(rs.getInt("dia")),
                DateTimeUtils.minutosALocalTime(rs.getInt("hora_inicio")),
                DateTimeUtils.minutosALocalTime(rs.getInt("hora_fin")),
                rs.getInt("cupos"),
                rs.getString("nombre_profesor")
        );
    }
}