package bd;

import vista.Dialogos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite
{
    private static final String RUTA_ARCHIVO = "data/gestionActividadesV2.db";
    private static final String URL = "jdbc:sqlite:" + RUTA_ARCHIVO;

    private static Connection conn = null;

    private ConexionSQLite() {
    }

    public static Connection getConexion() throws SQLException {
        if (conn == null || conn.isClosed()) {
            verificarArchivoBD();
            try
            {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(URL);
                conn.createStatement().execute("PRAGMA foreign_keys = ON");

            }
            catch (ClassNotFoundException e) {
                throw new SQLException("No se encontró el driver JDBC de SQLite. Revisa que el .jar esté en la librería.", e);
            }
        }
        return conn;
    }

    private static void verificarArchivoBD() throws SQLException {
        File dbFile = new File(RUTA_ARCHIVO);

        if (!dbFile.exists()) {
            SQLException e = new SQLException("Base de datos no encontrada en \"" + dbFile.getAbsolutePath() + "\"");
            Dialogos.ERROR("No se puede iniciar el sistema:\n", e);
            throw e;
        }
    }
}