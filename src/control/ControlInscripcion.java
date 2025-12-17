package control;

import dao.InscripcionDAO;
import modelo.Inscripcion;
import modelo.Usuario;
import utils.FormatoTablas;
import utils.Permisos;
import vista.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlInscripcion
{
    private final VistaInscripcion vistaInscripcion;
    private final Usuario usuarioActual;
    private final InscripcionDAO inscripcionDAO = new InscripcionDAO();

    public ControlInscripcion(VistaInscripcion vistaInscripcion, Usuario usuarioActual) {
        this.vistaInscripcion = vistaInscripcion;
        this.usuarioActual = usuarioActual;

        inicializar();
    }

    private void inicializar() {
        vistaInscripcion.getBtnNuevo().addActionListener(e -> crearInscripcion());
        vistaInscripcion.getBtnBorrar().addActionListener(e -> borrarInscripcion());
        vistaInscripcion.getBtnBuscar().addActionListener(e -> buscarInscripciones());
        vistaInscripcion.getTxtFiltro().addActionListener(e -> buscarInscripciones());
        vistaInscripcion.getBtnLimpiar().addActionListener(e -> limpiarFiltro());
        vistaInscripcion.getBtnVolver().addActionListener(e -> volver());

        setPermisos();
        cargarTablaInscripciones();
        FormatoTablas.headersNegrita(vistaInscripcion.getTbInscripciones());
    }

    private void crearInscripcion() {
        FormInscripcion formInscripcion = new FormInscripcion(vistaInscripcion, true);
        new ControlFormInscripcion(formInscripcion);
        formInscripcion.setLocationRelativeTo(null);
        formInscripcion.setVisible(true);
        cargarTablaInscripciones();
    }

    private void borrarInscripcion() {
        int fila = vistaInscripcion.getTbInscripciones().getSelectedRow();
        if (fila == -1) {
            Dialogos.ADVERTENCIA("Debe seleccionar una fila.");
            return;
        }

        int confirm = Dialogos.CONFIRMAR(
                "¿Seguro que desea eliminar este registro?",
                "Confirmar eliminación"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(vistaInscripcion.getTbInscripciones().getValueAt(fila, 0).toString());
                inscripcionDAO.delete(id);

                Dialogos.INFORMACION("Inscripción eliminada con éxito.", "Inscripción eliminada");
                cargarTablaInscripciones();
            } catch (Exception e) {
                Dialogos.ERROR("Error al eliminar: ", e);
            }
        }
    }

    private void buscarInscripciones() {
        String texto = vistaInscripcion.getTxtFiltro().getText().trim();

        try {
            ArrayList<Inscripcion> listaResultados;

            if (texto.isEmpty()) {
                listaResultados = inscripcionDAO.readAll();
            } else {
                listaResultados = inscripcionDAO.readBy(texto);
            }
            llenarTablaInscripciones(listaResultados);

        } catch (Exception e) {
            Dialogos.ERROR("Error al realizar la búsqueda: ", e);
        }
    }

    private void setPermisos() {
        Permisos.setPermisosAdmin(
                usuarioActual,
                vistaInscripcion,
                vistaInscripcion.getBtnBorrar()
        );
    }

    private void volver() {
        vistaInscripcion.dispose();

        VistaMenu vistaMenu = new VistaMenu();
        new ControlMenu(vistaMenu, usuarioActual);
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private void limpiarFiltro() {
        vistaInscripcion.getTxtFiltro().setText("");
        cargarTablaInscripciones();
    }

    private void cargarTablaInscripciones() {
        try {
            ArrayList<Inscripcion> todos = inscripcionDAO.readAll();
            llenarTablaInscripciones(todos);
        }
        catch (SQLException e) {
            Dialogos.ERROR("Error al cargar la tabla: ", e);
        }
    }

    private void llenarTablaInscripciones(ArrayList<Inscripcion> inscripciones) {
        DefaultTableModel m = (DefaultTableModel) vistaInscripcion.getTbInscripciones().getModel();
        m.setRowCount(0);

        for (Inscripcion i : inscripciones) {
            m.addRow(new Object[]{
                    i.getIdInscripcion(),
                    String.format("%s - %s %s",
                            i.getAdultoMayor().getRut(),
                            i.getAdultoMayor().getNombreAdultoMayor(),
                            i.getAdultoMayor().getApellidoAdultoMayor()
                    ),
                    String.format("%s (%s)",
                            i.getActividad().getNombreActividad(),
                            i.getActividad().getNombreProfesor())
            });
        }
        FormatoTablas.ajustarAnchoColumnas(vistaInscripcion.getTbInscripciones());
    }
}
