package utils;

import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import modelo.*;
import vista.Dialogos;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

// pero que *** más difícil ***
public final class GeneradorReporte
{
    private GeneradorReporte() {}

    private static final BaseFont bfRegular = Recursos.getFuentePDF("Roboto-Regular.ttf");
    private static final BaseFont bfBold = Recursos.getFuentePDF("Roboto-Bold.ttf");

    private static final Font FONT_TITULO = new Font(bfBold, 18, Font.NORMAL, Color.DARK_GRAY);
    private static final Font FONT_SUBTITULO = new Font(bfBold, 14, Font.NORMAL, Color.DARK_GRAY);
    private static final Font FONT_HEADER = new Font(bfBold, 10, Font.NORMAL, Color.WHITE);
    private static final Font FONT_DATA = new Font(bfRegular, 10, Font.NORMAL, Color.BLACK);

    public static void generarReporteCompleto(
            String destPath,
            ArrayList<Actividad> listaActividades,
            ArrayList<AdultoMayor> listaAdultos,
            ArrayList<Inscripcion> listaInscripciones,
            ArrayList<Usuario> listaUsuarios,
            Usuario usuario)
    {
        Document doc = new Document(PageSize.LETTER, 30, 30, 30, 30);

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(destPath));
            writer.setPageEvent(new PieDePagina());
            doc.open();

            Paragraph titulo = new Paragraph("Reporte General del Sistema CAM", FONT_TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(
                    String.format("Emisor: %s ([ID: %d] %s - Admin: %s)",
                            usuario.getNombreUsuario(),
                            usuario.getIdUsuario(),
                            usuario.getUsername(),
                            usuario.isAdministrador() ? "SÍ" : "NO"),
                            FONT_DATA));
            doc.add(new Paragraph("Fecha de emisión: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), FONT_DATA));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Resumen del Centro:", FONT_SUBTITULO));
            doc.add(new Paragraph("- Total Actividades: " + listaActividades.size(), FONT_DATA));
            doc.add(new Paragraph("- Total Adultos Mayores: " + listaAdultos.size(), FONT_DATA));
            doc.add(new Paragraph("- Total Inscripciones Vigentes: " + listaInscripciones.size(), FONT_DATA));
            doc.add(new Paragraph("- Total Usuarios Sistema: " + listaUsuarios.size(), FONT_DATA));
            doc.add(new Paragraph(" "));

            agregarSeccion(doc, "1. Listado de Actividades");
            PdfPTable tablaAct = crearTablaActividades(listaActividades);
            doc.add(tablaAct);

            doc.newPage();

            agregarSeccion(doc, "2. Nómina de Adultos Mayores");
            PdfPTable tablaAdultos = crearTablaAdultos(listaAdultos);
            doc.add(tablaAdultos);

            doc.newPage();

            agregarSeccion(doc, "3. Detalle de Inscripciones");
            PdfPTable tablaInscripciones = crearTablaInscripciones(listaInscripciones);
            doc.add(tablaInscripciones);

            doc.newPage();

            agregarSeccion(doc, "4. Usuarios Registrados (Staff)");
            PdfPTable tablaUsuarios = crearTablaUsuarios(listaUsuarios);
            doc.add(tablaUsuarios);

            doc.close();
        }
        catch (Exception e) {
            Dialogos.ERROR("Error al generar reporte: ", e);
        }
    }

    private static void agregarSeccion(Document doc, String titulo) throws DocumentException {
        Paragraph p = new Paragraph(titulo, FONT_SUBTITULO);
        p.setSpacingAfter(10);
        doc.add(p);
    }

    private static PdfPTable crearTablaActividades(ArrayList<Actividad> actividades) {
        PdfPTable tabla = new PdfPTable(new float[]{1, 3, 2, 2, 4});
        tabla.setWidthPercentage(100);

        setHeaders(tabla, "ID", "Actividad", "Día", "Profesor", "Categoría");

        for (Actividad a : actividades) {
            tabla.addCell(celda(String.valueOf(a.getIdActividad())));
            tabla.addCell(celda(a.getNombreActividad()));
            tabla.addCell(celda(DateTimeUtils.aTitleCase(a.getDia().getDisplayName(TextStyle.FULL, Locale.getDefault()))));
            tabla.addCell(celda(a.getNombreProfesor()));
            tabla.addCell(celda(a.getCategoria().getNombre()));
        }
        return tabla;
    }

    private static PdfPTable crearTablaAdultos(ArrayList<AdultoMayor> adultos) {
        PdfPTable tabla = new PdfPTable(new float[]{2, 3, 3, 2, 3});
        tabla.setWidthPercentage(100);

        setHeaders(tabla, "RUT", "Nombre", "Apellido", "Nacimiento", "Contacto Enc.");

        for (AdultoMayor am : adultos) {
            tabla.addCell(celda(am.getRut()));
            tabla.addCell(celda(am.getNombreAdultoMayor()));
            tabla.addCell(celda(am.getApellidoAdultoMayor()));
            tabla.addCell(celda(am.getNacimiento().format(DateTimeUtils.DATE_CL)));
            tabla.addCell(celda(am.getContactoEncargado()));
        }
        return tabla;
    }

    private static PdfPTable crearTablaInscripciones(ArrayList<Inscripcion> inscripciones) {
        PdfPTable tabla = new PdfPTable(new float[]{1, 4, 4});
        tabla.setWidthPercentage(100);

        setHeaders(tabla, "ID", "Adulto Mayor", "Actividad Inscrita");

        for (Inscripcion i : inscripciones) {
            tabla.addCell(celda(String.valueOf(i.getIdInscripcion())));

            String nombreAdulto = i.getAdultoMayor().getNombreAdultoMayor() + " " + i.getAdultoMayor().getApellidoAdultoMayor();
            String nombreActividad = i.getActividad().getNombreActividad();

            tabla.addCell(celda(nombreAdulto));
            tabla.addCell(celda(nombreActividad));
        }
        return tabla;
    }

    private static PdfPTable crearTablaUsuarios(ArrayList<Usuario> usuarios) {
        PdfPTable tabla = new PdfPTable(new float[]{1, 3, 2});
        tabla.setWidthPercentage(100);

        setHeaders(tabla, "ID", "Username", "Rol");

        for (Usuario u : usuarios) {
            tabla.addCell(celda(String.valueOf(u.getIdUsuario())));
            tabla.addCell(celda(u.getUsername()));
            tabla.addCell(celda(u.isAdministrador() ? "ADMINISTRADOR" : "ESTÁNDAR"));
        }
        return tabla;
    }

    private static void setHeaders(PdfPTable tabla, String... titulos) {
        for (String t : titulos) {
            PdfPCell c = new PdfPCell(new Phrase(t, FONT_HEADER));
            c.setBackgroundColor(Color.GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setPadding(5);
            tabla.addCell(c);
        }
    }

    private static PdfPCell celda(String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto, FONT_DATA));
        c.setPadding(4);
        return c;
    }

    static class PieDePagina extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document doc) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase pie = new Phrase(String.valueOf(writer.getPageNumber()), FONT_DATA);

            float x = (doc.right() - doc.left()) / 2 + doc.leftMargin();
            float y = doc.bottom() - 20;

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, pie, x, y, 0);
        }
    }
}