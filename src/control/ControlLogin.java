package control;

import dao.UsuarioDAO;
import modelo.Usuario;
import utils.Hashing;
import vista.Dialogos;
import vista.VistaLogin;
import vista.VistaMenu;

import javax.swing.*;
import java.sql.SQLException;

public class ControlLogin
{
    private final VistaLogin vista;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ControlLogin(VistaLogin vista) {
        this.vista = vista;

        inicializar();
    }

    private void inicializar() {
        vista.getBtnLogin().addActionListener(e -> iniciarSesion());
        vista.getTxtPassword().addActionListener(e -> iniciarSesion());
        vista.getBtnSalir().addActionListener(e -> salir());
        vista.setTitle("Login");
    }

    public void iniciarSesion() {
        Usuario usuarioLogueado = validarCredenciales();

        if (usuarioLogueado != null) {
            vista.dispose();

            VistaMenu vistaMenu = new VistaMenu();
            new ControlMenu(vistaMenu, usuarioLogueado);
            vistaMenu.setLocationRelativeTo(null);
            vistaMenu.setVisible(true);
        }
    }

    private Usuario validarCredenciales() {
        String username = vista.getTxtUsuario().getText();
        String passwordIngresada = String.valueOf(vista.getTxtPassword().getPassword());

        try {
            Usuario usuarioBD = usuarioDAO.obtenerDatos(username);

            if (usuarioBD == null) {
                Dialogos.ADVERTENCIA("Usuario no encontrado."); return null;
            }

            String hashIngresado = Hashing.getHashPassword(passwordIngresada);

            if (usuarioBD.getPasswordHash().equals(hashIngresado)) {
                return usuarioBD;
            }

            Dialogos.ADVERTENCIA("Contraseña incorrecta."); return null;

        } catch (SQLException e) {
            Dialogos.ERROR("Error de conexión: ", e);
            return null;
        }
    }

    private void salir() {
        int res = Dialogos.CONFIRMAR("¿Está seguro que desea salir?", "Cerrar programa");
        if (res == JOptionPane.YES_OPTION)
            System.exit(0);
    }
}