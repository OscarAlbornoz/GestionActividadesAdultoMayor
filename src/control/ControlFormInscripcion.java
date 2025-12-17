package control;

import dao.ActividadDAO;
import dao.AdultoMayorDAO;
import dao.InscripcionDAO;
import modelo.Actividad;
import modelo.AdultoMayor;
import modelo.Inscripcion;
import utils.DateTimeUtils;
import vista.FormInscripcion;
import vista.Dialogos;

import javax.swing.*;
import java.awt.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ControlFormInscripcion
{
    private final FormInscripcion vista;
    private final AdultoMayorDAO adultoMayorDAO = new AdultoMayorDAO();
    private final ActividadDAO actividadDAO = new ActividadDAO();
    private final InscripcionDAO inscripcionDAO = new InscripcionDAO();

    public ControlFormInscripcion(FormInscripcion vista) {
        this.vista = vista;
        inicializar();
    }

    private void inicializar() {
        vista.getTxtID().setEditable(false);
        vista.getTxtID().setFocusable(false);
        vista.getTxtID().setText("Autogenerado");

        if (!cargarDatosEnCombos()) return;

        configurarRenderers();

        vista.getBtnConfirm().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
        vista.setTitle("Crear Inscripcion");
    }

    private boolean cargarDatosEnCombos() {
        try {
            ArrayList<AdultoMayor> listaAdultos = adultoMayorDAO.readAll();
            ArrayList<Actividad> listaActividades = actividadDAO.readAll();

            if (listaAdultos.isEmpty()) {
                Dialogos.ADVERTENCIA("No existen Adultos Mayores registrados.\nDebe crear uno antes de realizar una inscripción.");
                vista.dispose();
                return false;
            }

            if (listaActividades.isEmpty()) {
                Dialogos.ADVERTENCIA("No existen Actividades registradas.\nDebe crear una antes de realizar una inscripción.");
                vista.dispose();
                return false;
            }

            DefaultComboBoxModel<AdultoMayor> modelAdultos = new DefaultComboBoxModel<>();
            for (AdultoMayor am : listaAdultos) {
                modelAdultos.addElement(am);
            }
            vista.getCmbAdulto().setModel(modelAdultos);

            DefaultComboBoxModel<Actividad> modelActividades = new DefaultComboBoxModel<>();
            for (Actividad act : listaActividades) {
                modelActividades.addElement(act);
            }
            vista.getCmbActividad().setModel(modelActividades);

            return true;

        } catch (Exception e) {
            Dialogos.ERROR("Error al cargar datos: ", e);
            vista.dispose();
            return false;
        }
    }

    private void configurarRenderers() {
        vista.getCmbAdulto().setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof AdultoMayor am) {
                    setText(am.getRut() + " - " + am.getNombreAdultoMayor() + " " + am.getApellidoAdultoMayor());
                }
                return this;
            }
        });

        vista.getCmbActividad().setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Actividad a) {
                    String horaIni = a.getHoraInicio().toString();
                    String horaFin = a.getHoraFin().toString();

                    String texto = String.format("%s (%s, %s - %s)",
                            a.getNombreActividad(),
                            DateTimeUtils.aTitleCase(a.getDia().getDisplayName(TextStyle.FULL, Locale.getDefault())),
                            horaIni,
                            horaFin);

                    setText(texto);
                }
                return this;
            }
        });
    }

    private void guardar() {
        AdultoMayor adultoSeleccionado = (AdultoMayor) vista.getCmbAdulto().getSelectedItem();
        Actividad actividadSeleccionada = (Actividad) vista.getCmbActividad().getSelectedItem();
        try {
            if (adultoSeleccionado == null || actividadSeleccionada == null) {
                Dialogos.ADVERTENCIA("Debe seleccionar un adulto mayor y una actividad."); return;
            }

            boolean yaInscrito = inscripcionDAO.existeInscripcion(adultoSeleccionado.getRut(), actividadSeleccionada.getIdActividad());
            if (yaInscrito) {
                Dialogos.ADVERTENCIA("Este adulto mayor ya está inscrito en esta actividad."); return;
            }

            if (actividadSeleccionada.getCupos() <= 0) {
                Dialogos.ADVERTENCIA("No quedan cupos disponibles para esta actividad."); return;
            }

            boolean cupoDescontado = actividadDAO.descontarCupo(actividadSeleccionada.getIdActividad());
            if (!cupoDescontado) {
                Dialogos.ADVERTENCIA("No quedan cupos disponibles para esta actividad."); return;
            }

            Inscripcion nuevaInscripcion = new Inscripcion(
                    adultoSeleccionado,
                    actividadSeleccionada
            );

            inscripcionDAO.create(nuevaInscripcion);
            Dialogos.INFORMACION("Inscripción realizada con éxito.", "Inscripción registrada");
            vista.dispose();

        } catch (Exception e) {
            Dialogos.ERROR("Error al guardar inscripción: ", e);
        }
    }
}