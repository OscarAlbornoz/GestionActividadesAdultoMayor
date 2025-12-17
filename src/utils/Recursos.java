package utils;

import org.openpdf.text.pdf.BaseFont;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public final class Recursos
{
    private Recursos() {}

    public static Image getIcono() {
        URL url = Recursos.class.getResource("/resources/img/icon.png");
        if (url != null) {
            return Toolkit.getDefaultToolkit().getImage(url);
        } else {
            System.err.println("No se encontró el ícono en resources/img/icon.png");
            return null;
        }
    }

    public static BaseFont getFuentePDF(String nombreArchivo) {
        try {
            URL url = Recursos.class.getResource("/resources/fonts/" + nombreArchivo);

            if (url == null) {
                System.err.println("ADVERTENCIA: No se encontró la fuente: " + nombreArchivo);
                return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            }
            return BaseFont.createFont(url.toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        } catch (Exception e) {
            System.err.println("Error cargando fuente " + nombreArchivo + ": " + e.getMessage());
            try {
                return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            }
            catch (Exception ex) {
                return null;
            }
        }
    }
}