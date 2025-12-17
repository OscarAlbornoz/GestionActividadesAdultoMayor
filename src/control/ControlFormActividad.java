package control;

import dao.ActividadDAO;
import dao.CategoriaDAO;
import modelo.Actividad;
import modelo.Categoria;
import vista.FormActividad;
import utils.DateTimeUtils;
import vista.Dialogos;

import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class ControlFormActividad
{
    private final FormActividad vista;
    private final Actividad actividad;
    private final ActividadDAO actividadDAO = new ActividadDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private final String[] DIAS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    public ControlFormActividad(FormActividad vista, Actividad actividad) {
        this.vista = vista;
        this.actividad = actividad;

        inicializar();
    }

    private void inicializar() {
        configurarComponentes();

        vista.getTxtID().setEditable(false);
        vista.getTxtID().setFocusable(false);

        if (actividad != null) {
            vista.setTitle("Editar Actividad");
            vista.getTxtID().setText(String.valueOf(actividad.getIdActividad()));
            cargarDatos();
        } else {
            vista.setTitle("Crear Actividad");
            vista.getTxtID().setText("Autogenerado");
            setHorasDefault();
        }

        vista.getBtnConfirm().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    private void configurarComponentes() {
        DefaultComboBoxModel<String> modelDias = new DefaultComboBoxModel<>(DIAS);
        vista.getCmbDia().setModel(modelDias);

        try {
            DefaultComboBoxModel<Categoria> modelCat = new DefaultComboBoxModel<>();
            ArrayList<Categoria> listaCategorias = categoriaDAO.readAll();

            for (Categoria cat : listaCategorias) {
                modelCat.addElement(cat);
            }

            vista.getCmbCategoria().setModel(modelCat);

        } catch (Exception e) {
            Dialogos.ERROR("Error al cargar categorías: ", e);
        }

        vista.getSpinInicio().setModel(new SpinnerDateModel());
        vista.getSpinFin().setModel(new SpinnerDateModel());

        JSpinner.DateEditor editorIni = new JSpinner.DateEditor(vista.getSpinInicio(), "HH:mm");
        JSpinner.DateEditor editorFin = new JSpinner.DateEditor(vista.getSpinFin(), "HH:mm");

        vista.getSpinInicio().setEditor(editorIni);
        vista.getSpinFin().setEditor(editorFin);

        vista.getSpinCupos().setModel(new SpinnerNumberModel(10, 1, 1000, 1));
    }

    private void cargarDatos() {
        vista.getTxtNombre().setText(actividad.getNombreActividad());
        vista.getTxtDesc().setText(actividad.getDescripcion());
        vista.getTxtProfesor().setText(actividad.getNombreProfesor());
        vista.getSpinCupos().setValue(actividad.getCupos());

        if (actividad.getDia() != null) {
            int indexDia = actividad.getDia().getValue() - 1;
            vista.getCmbDia().setSelectedIndex(indexDia);
        }

        Categoria catActual = actividad.getCategoria();
        if (catActual != null) {
            DefaultComboBoxModel<Categoria> model = (DefaultComboBoxModel<Categoria>) vista.getCmbCategoria().getModel();

            for (int i = 0; i < model.getSize(); i++) {
                Categoria c = model.getElementAt(i);
                if (c.getIdCategoria() == catActual.getIdCategoria()) {
                    vista.getCmbCategoria().setSelectedIndex(i);
                    break;
                }
            }
        }

        LocalTime ltInicio = actividad.getHoraInicio();
        LocalTime ltFin = actividad.getHoraFin();
        vista.getSpinInicio().setValue(DateTimeUtils.localTimeToDate(ltInicio));
        vista.getSpinFin().setValue(DateTimeUtils.localTimeToDate(ltFin));
    }

    private void guardar() {
        String nombre = vista.getTxtNombre().getText().trim();
        String desc = vista.getTxtDesc().getText().trim();
        String profesor = vista.getTxtProfesor().getText().trim();
        int cupos = (int) vista.getSpinCupos().getValue();

        Categoria categoriaSeleccionada = (Categoria) vista.getCmbCategoria().getSelectedItem();

        int indexDia = vista.getCmbDia().getSelectedIndex();
        DayOfWeek diaEnum = DayOfWeek.of(indexDia + 1);

        Date dInicio = (Date) vista.getSpinInicio().getValue();
        Date dFin = (Date) vista.getSpinFin().getValue();

        LocalTime ltInicio = DateTimeUtils.dateALocalTime(dInicio);
        LocalTime ltFin = DateTimeUtils.dateALocalTime(dFin);

        int minInicio = DateTimeUtils.localTimeAMinutos(ltInicio);
        int minFin = DateTimeUtils.localTimeAMinutos(ltFin);

        if (nombre.isBlank() || nombre.length() < 2) {
            Dialogos.INFORMACION("El nombre ingresado no es válido.", "Datos incorrectos"); return;
        }

        if (minFin <= minInicio) {
            Dialogos.INFORMACION("La hora de término debe ser posterior a la de inicio.", "Datos incorrectos"); return;
        }

        try {
            if (actividad == null) {
                Actividad nueva = new Actividad(
                        nombre,
                        categoriaSeleccionada,
                        desc,
                        diaEnum,
                        ltInicio,
                        ltFin,
                        cupos,
                        profesor
                );
                actividadDAO.create(nueva);
                Dialogos.INFORMACION("Actividad creada con éxito.", "Éxito");
            }
            else {
                actividad.setNombreActividad(nombre);
                actividad.setCategoria(categoriaSeleccionada);
                actividad.setDescripcion(desc);
                actividad.setDia(diaEnum);
                actividad.setHoraInicio(ltInicio);
                actividad.setHoraFin(ltFin);
                actividad.setCupos(cupos);
                actividad.setNombreProfesor(profesor);

                actividadDAO.update(actividad);
                Dialogos.INFORMACION("Actividad actualizada con éxito.", "Éxito");
            }
            vista.dispose();
        }
        catch (Exception e) {
            Dialogos.ERROR("Error al guardar actividad: ", e);
        }
    }

    private void setHorasDefault() {
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(10, 0);
        vista.getSpinInicio().setValue(DateTimeUtils.localTimeToDate(inicio));
        vista.getSpinFin().setValue(DateTimeUtils.localTimeToDate(fin));
    }
}