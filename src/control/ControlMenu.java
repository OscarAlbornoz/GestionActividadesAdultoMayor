package control;

import dao.ActividadDAO;
import dao.AdultoMayorDAO;
import dao.InscripcionDAO;
import dao.UsuarioDAO;
import modelo.Actividad;
import modelo.AdultoMayor;
import modelo.Inscripcion;
import modelo.Usuario;
import utils.GeneradorReporte;
import vista.Dialogos;
import vista.VistaMenu;
import vista.VistaActividad;
import vista.VistaAdulto;
import vista.VistaInscripcion;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ControlMenu
{
    private final VistaMenu vistaMenu;
    private final Usuario usuarioActual;

    public ControlMenu(VistaMenu vista, Usuario usuario) {
        this.vistaMenu = vista;
        this.usuarioActual = usuario;

        inicializar();
    }

    private void inicializar() {
        vistaMenu.getBtnActividad().addActionListener(e -> abrirVistaActividad());
        vistaMenu.getBtnAdulto().addActionListener(e -> abrirVistaAdulto());
        vistaMenu.getBtnInscripcion().addActionListener(e -> abrirVistaInscripcion());
        vistaMenu.getBtnPDF().addActionListener(e -> generarReportePDF());
        vistaMenu.getBtnSalir().addActionListener(e -> salir());
        vistaMenu.setTitle("Menú");
        vistaMenu.getLblUsuario().setText(
                String.format("¡Bienvenido, %s (%s)!",
                        usuarioActual.getNombreUsuario(),
                        usuarioActual.getUsername()));
    }

    private void abrirVistaActividad() {
        vistaMenu.dispose();

        VistaActividad vistaActividad = new VistaActividad();
        new ControlActividad(vistaActividad, usuarioActual);
        vistaActividad.setLocationRelativeTo(null);
        vistaActividad.setVisible(true);
    }

    private void abrirVistaAdulto() {
        vistaMenu.dispose();

        VistaAdulto vistaAdulto = new VistaAdulto();
        new ControlAdultoMayor(vistaAdulto, usuarioActual);
        vistaAdulto.setLocationRelativeTo(null);
        vistaAdulto.setVisible(true);
    }

    private void abrirVistaInscripcion() {
        vistaMenu.dispose();

        VistaInscripcion vistaInscripcion = new VistaInscripcion();
        new ControlInscripcion(vistaInscripcion, usuarioActual);
        vistaInscripcion.setLocationRelativeTo(null);
        vistaInscripcion.setVisible(true);
    }

    private void generarReportePDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("Reporte_Maestro_CAM.pdf"));

        if (fileChooser.showSaveDialog(vistaMenu) == JFileChooser.APPROVE_OPTION) {
            try {
                 ActividadDAO actividadDAO = new ActividadDAO();
                 AdultoMayorDAO adultoMayorDAO = new AdultoMayorDAO();
                 InscripcionDAO inscripcionDAO = new InscripcionDAO();
                 UsuarioDAO usuarioDAO = new UsuarioDAO();

                ArrayList<Actividad> listaActividades = actividadDAO.readAll();
                ArrayList<AdultoMayor> listaAdultos = adultoMayorDAO.readAll();
                ArrayList<Inscripcion> listaInscripciones = inscripcionDAO.readAll();
                ArrayList<Usuario> listaUsuarios = usuarioDAO.readAll();

                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".pdf")) path += ".pdf";

                GeneradorReporte.generarReporteCompleto(
                        path,
                        listaActividades,
                        listaAdultos,
                        listaInscripciones,
                        listaUsuarios,
                        usuarioActual
                );

                Dialogos.INFORMACION("Reporte completo generado en:\n" + path, "Archivo guardado");

                Desktop.getDesktop().open(new File(path));

            }
            catch (Exception e) {
                Dialogos.ERROR("Error al generar reporte: ", e);
            }
        }
    }

    private void salir() {
        int res = Dialogos.CONFIRMAR("¿Está seguro que desea salir?", "Cerrar programa");
        if (res == JOptionPane.YES_OPTION)
            System.exit(0);
    }
}