package control;

import dao.ActividadDAO;
import modelo.Actividad;
import modelo.Usuario;
import utils.DateTimeUtils;
import utils.FormatoTablas;
import utils.Permisos;
import vista.Dialogos;
import vista.FormActividad;
import vista.VistaActividad;
import vista.VistaMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ControlActividad
{
    private final VistaActividad vistaActividades;
    private final Usuario usuarioActual;
    private final ActividadDAO actividadDAO = new ActividadDAO();

    public ControlActividad(VistaActividad vistaActividades, Usuario usuarioActual) {
        this.vistaActividades = vistaActividades;
        this.usuarioActual = usuarioActual;

        inicializar();
    }

    private void inicializar() {
        vistaActividades.getBtnNuevo().addActionListener(e -> crearActividad());
        vistaActividades.getBtnEditar().addActionListener(e -> editarActividad());
        vistaActividades.getBtnBorrar().addActionListener(e -> borrarActividad());
        vistaActividades.getBtnBuscar().addActionListener(e -> buscarActividades());
        vistaActividades.getTxtFiltro().addActionListener(e -> buscarActividades());
        vistaActividades.getBtnLimpiar().addActionListener(e -> limpiarFiltro());
        vistaActividades.getBtnVolver().addActionListener(e -> volver());

        setPermisos();
        cargarTablaActividades();
        FormatoTablas.headersNegrita(vistaActividades.getTbActividades());
    }

    private void setPermisos() {
        Permisos.setPermisosAdmin(
                usuarioActual,
                vistaActividades,
                vistaActividades.getBtnEditar(),
                vistaActividades.getBtnBorrar()
        );
    }

    private void crearActividad() {
        FormActividad formActividad = new FormActividad(vistaActividades, true);
        new ControlFormActividad(formActividad, null);
        formActividad.setLocationRelativeTo(null);
        formActividad.setVisible(true);
        cargarTablaActividades();
    }

    private void editarActividad() {
        int fila = vistaActividades.getTbActividades().getSelectedRow();
        if (fila == -1) {
            Dialogos.ADVERTENCIA("Debe seleccionar una fila.");
            return;
        }

        Actividad actividadSeleccionada = getActividadFila(fila);

        if (actividadSeleccionada != null) {
            FormActividad formActividad = new FormActividad(vistaActividades, true);
            new ControlFormActividad(formActividad, actividadSeleccionada);
            formActividad.setLocationRelativeTo(null);
            formActividad.getTxtID().setEditable(false);
            formActividad.setVisible(true);

            cargarTablaActividades();
        }
    }

    private void borrarActividad() {
        int fila = vistaActividades.getTbActividades().getSelectedRow();
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
                Integer id = (Integer) vistaActividades.getTbActividades().getValueAt(fila, 0);
                actividadDAO.delete(id);

                Dialogos.INFORMACION("Actividad eliminada con éxito.", "Actividad eliminada");
                cargarTablaActividades();
            }
            catch (Exception e) {
                Dialogos.ERROR("Error al eliminar: ", e);
            }
        }
    }

    private void buscarActividades() {
        String filtro = vistaActividades.getTxtFiltro().getText().trim();

        try {
            ArrayList<Actividad> listaResultados;

            if (filtro.isEmpty()) {
                listaResultados = actividadDAO.readAll();
            } else {
                listaResultados = actividadDAO.readBy(filtro);
            }
            llenarTablaActividades(listaResultados);

        } catch (Exception e) {
            Dialogos.ERROR("Error al realizar la búsqueda: ", e);
        }
    }

    private void limpiarFiltro() {
        vistaActividades.getTxtFiltro().setText("");
        cargarTablaActividades();
    }

    private void llenarTablaActividades(ArrayList<Actividad> actividades) {
        DefaultTableModel m = (DefaultTableModel) vistaActividades.getTbActividades().getModel();
        m.setRowCount(0);

        for (Actividad a : actividades) {
            m.addRow(new Object[]{
                    a.getIdActividad(),
                    a.getNombreActividad(),
                    a.getCategoria().getNombre(),
                    DateTimeUtils.aTitleCase(a.getDia().getDisplayName(TextStyle.FULL, Locale.getDefault())),
                    String.format("%s - %s",
                            a.getHoraInicio().format(DateTimeUtils.HORA_MIN),
                            a.getHoraFin().format(DateTimeUtils.HORA_MIN)
                    ),
                    a.getCupos(),
                    a.getNombreProfesor(),
                    a.getDescripcion()
            });
        }
        FormatoTablas.ajustarAnchoColumnas(vistaActividades.getTbActividades());
    }

    private void cargarTablaActividades() {
        try {
            ArrayList<Actividad> todas = actividadDAO.readAll();
            llenarTablaActividades(todas);
        }
        catch (SQLException e) {
            Dialogos.ERROR("Error al cargar la tabla: ", e);
        }
    }

    private void volver() {
        vistaActividades.dispose();

        VistaMenu vistaMenu = new VistaMenu();
        new ControlMenu(vistaMenu, usuarioActual);
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private Actividad getActividadFila(int fila) {
        JTable tabla = vistaActividades.getTbActividades();
        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        try {
            return actividadDAO.readOne(id);
        }
        catch (Exception e) {
            Dialogos.ERROR("Error al obtener la Actividad: ", e);
            return null;
        }
    }
}
