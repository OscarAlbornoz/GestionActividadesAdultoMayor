package control;

import dao.AdultoMayorDAO;
import modelo.AdultoMayor;
import modelo.Usuario;
import utils.DateTimeUtils;
import utils.FormatoTablas;
import utils.Permisos;
import vista.Dialogos;
import vista.FormAdulto;
import vista.VistaAdulto;
import vista.VistaMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlAdultoMayor
{
    private final VistaAdulto vistaAdultos;
    private final Usuario usuarioActual;
    private final AdultoMayorDAO adultoMayorDAO = new AdultoMayorDAO();

    public ControlAdultoMayor(VistaAdulto vistaAdultos, Usuario usuarioActual) {
        this.vistaAdultos = vistaAdultos;
        this.usuarioActual = usuarioActual;

        inicializar();
    }

    private void inicializar() {
        vistaAdultos.getBtnNuevo().addActionListener(e -> crearAdulto());
        vistaAdultos.getBtnEditar().addActionListener(e -> editarAdulto());
        vistaAdultos.getBtnBorrar().addActionListener(e -> borrarAdulto());
        vistaAdultos.getBtnBuscar().addActionListener(e -> buscarAdultosMayores());
        vistaAdultos.getTxtFiltro().addActionListener(e -> buscarAdultosMayores());
        vistaAdultos.getBtnLimpiar().addActionListener(e -> limpiarFiltro());
        vistaAdultos.getBtnVolver().addActionListener(e -> volver());

        setPermisos();
        cargarTablaAdultosMayores();
        FormatoTablas.headersNegrita(vistaAdultos.getTbAdultos());
    }

    private void setPermisos() {
        Permisos.setPermisosAdmin(
                usuarioActual,
                vistaAdultos.getBtnEditar(),
                vistaAdultos.getBtnBorrar()
        );
    }

    private void crearAdulto() {
        FormAdulto formAdulto = new FormAdulto(vistaAdultos, true);
        new ControlFormAdulto(formAdulto, null);
        formAdulto.setLocationRelativeTo(null);
        formAdulto.setVisible(true);
        cargarTablaAdultosMayores();
    }

    private void editarAdulto() {
        int fila = vistaAdultos.getTbAdultos().getSelectedRow();
        if (fila == -1) {
            Dialogos.ADVERTENCIA("Debe seleccionar una fila.");
            return;
        }

        AdultoMayor adultoSeleccionado = getAdultoFila(fila);

        if (adultoSeleccionado != null) {
            FormAdulto formAdulto = new FormAdulto(vistaAdultos, true);
            new ControlFormAdulto(formAdulto, adultoSeleccionado);
            formAdulto.getTxtRUT().setEditable(false);
            formAdulto.setLocationRelativeTo(null);
            formAdulto.setVisible(true);

            cargarTablaAdultosMayores();
        }
    }

    private void borrarAdulto() {
        int fila = vistaAdultos.getTbAdultos().getSelectedRow();
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
                String rut = vistaAdultos.getTbAdultos().getValueAt(fila, 0).toString();
                adultoMayorDAO.delete(rut);

                Dialogos.INFORMACION("Adulto Mayor eliminado con éxito.", "Adulto Mayor eliminado");
                cargarTablaAdultosMayores();
            } catch (Exception e) {
                Dialogos.ERROR("Error al eliminar: ", e);
            }
        }
    }

    private void buscarAdultosMayores() {
        String texto = vistaAdultos.getTxtFiltro().getText().trim();

        try {
            ArrayList<AdultoMayor> listaResultados;

            if (texto.isEmpty()) {
                listaResultados = adultoMayorDAO.readAll();
            } else {
                listaResultados = adultoMayorDAO.readBy(texto);
            }
            llenarTablaAdultosMayores(listaResultados);

        } catch (Exception e) {
            Dialogos.ERROR("Error al realizar la búsqueda: ", e);
        }
    }

    private void volver() {
        vistaAdultos.dispose();

        VistaMenu vistaMenu = new VistaMenu();
        new ControlMenu(vistaMenu, usuarioActual);
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private void limpiarFiltro() {
        vistaAdultos.getTxtFiltro().setText("");
        cargarTablaAdultosMayores();
    }

    private void llenarTablaAdultosMayores(ArrayList<AdultoMayor> adultos) {
        DefaultTableModel m = (DefaultTableModel) vistaAdultos.getTbAdultos().getModel();
        m.setRowCount(0);

        for (AdultoMayor a : adultos) {
            m.addRow(new Object[]{
                    a.getRut(),
                    String.format("%s %s", a.getNombreAdultoMayor(), a.getApellidoAdultoMayor()),
                    a.getNacimiento().format(DateTimeUtils.DATE_CL),
                    DateTimeUtils.calcularEdad(a.getNacimiento()),
                    a.getNombreEncargado(),
                    (a.getContactoEncargado().length() == 9 ? "+56 " : "+41 ") + a.getContactoEncargado()
            });
        }
        FormatoTablas.ajustarAnchoColumnas(vistaAdultos.getTbAdultos());
    }

    private void cargarTablaAdultosMayores() {
        try {
            ArrayList<AdultoMayor> todos = adultoMayorDAO.readAll();
            llenarTablaAdultosMayores(todos);
        }
        catch (SQLException e) {
            Dialogos.ERROR("Error al cargar la tabla: ", e);
        }
    }

    private AdultoMayor getAdultoFila(int fila) {
        JTable tabla = vistaAdultos.getTbAdultos();
        String rut = tabla.getValueAt(fila, 0).toString();

        try {
            return adultoMayorDAO.readOne(rut);
        }
        catch (Exception e) {
            Dialogos.ERROR("Error al obtener el Adulto Mayor: ", e);
            return null;
        }
    }
}
