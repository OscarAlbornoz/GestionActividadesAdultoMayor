package vista;

import javax.swing.*;
import static java.awt.Toolkit.getDefaultToolkit;

public final class Dialogos
{
    private Dialogos() {}

    public static void ERROR(String mensaje, Exception ex) {
        getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(
                null, mensaje + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, null);
    }

    public static void ADVERTENCIA(String mensaje) {
        getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(
                null, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE, null);
    }

    public static void INFORMACION(String mensaje, String titulo) {
        getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE, null);
    }

    public static int CONFIRMAR(String mensaje, String titulo) {
        getDefaultToolkit().beep();
        return JOptionPane.showConfirmDialog(
                null, mensaje, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
    }
}