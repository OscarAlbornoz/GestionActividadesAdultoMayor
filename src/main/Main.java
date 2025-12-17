package main;

import control.ControlLogin;
import vista.VistaLogin;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                UIManager.put("TextComponent.arc", 7);
                UIManager.put("Button.arc", 7);
                UIManager.put("Component.arc", 7);
                UIManager.put("ScrollPane.arc", 7);
            }
            catch (Exception e) {
                System.err.println("No se pudo iniciar FlatLaf.");
            }
            iniciarAplicacion();
        });
    }

    private static void iniciarAplicacion() {
        VistaLogin vistaLogin = new VistaLogin();
        new ControlLogin(vistaLogin);
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}