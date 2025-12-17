package utils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import modelo.Usuario;

public final class Permisos
{
    private Permisos() {}

    public static void setPermisosAdmin(Usuario u, Object ventana, JButton... botones) {
        boolean esAdmin = u.isAdministrador();

        for (JButton btn : botones) {
            if (btn != null) {
                btn.setVisible(esAdmin);
                btn.setEnabled(esAdmin);
            }
        }

        String rol = esAdmin ? "Administrador" : "Usuario";
        String titulo = "Gestión Centro Adulto Mayor - " + rol + ": " + u.getUsername();

        if (ventana instanceof JFrame)((JFrame) ventana).setTitle(titulo);
        else if (ventana instanceof JInternalFrame) ((JInternalFrame) ventana).setTitle(titulo);
    }
}