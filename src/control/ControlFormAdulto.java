package control;

import dao.AdultoMayorDAO;
import modelo.AdultoMayor;
import utils.DateTimeUtils;
import utils.Validadores;
import vista.Dialogos;
import vista.FormAdulto;
import java.time.LocalDate;

public class ControlFormAdulto
{
    private final FormAdulto vista;
    private final AdultoMayor adulto;
    private final AdultoMayorDAO adultoDAO = new AdultoMayorDAO();

    public ControlFormAdulto(FormAdulto vista, AdultoMayor adulto) {
        this.vista = vista;
        this.adulto = adulto;

        inicializar();
    }

    private void inicializar() {
        if (adulto != null) {
            vista.getTxtRUT().setText(adulto.getRut());
            vista.getTxtNombre().setText(adulto.getNombreAdultoMayor());
            vista.getTxtApellido().setText(adulto.getApellidoAdultoMayor());
            vista.getTxtNacimiento().setText(adulto.getNacimiento().format(DateTimeUtils.DATE_CL));
            vista.getTxtEncargado().setText(adulto.getNombreEncargado());
            vista.getTxtTelefono().setText(adulto.getContactoEncargado());
            vista.getTxtRUT().setEditable(false);
            vista.getTxtRUT().setFocusable(false);
            vista.setTitle("Editar Adulto");
        } else {
            vista.getTxtRUT().setEditable(true);
            vista.setTitle("Nuevo Adulto");
        }
        vista.getBtnConfirm().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    private void guardar() {
        String rut = vista.getTxtRUT().getText().trim();
        String nombre = vista.getTxtNombre().getText().trim();
        String apellido = vista.getTxtApellido().getText().trim();
        String nacimiento = vista.getTxtNacimiento().getText().trim();
        String encargado = vista.getTxtEncargado().getText().trim();
        String telefono = vista.getTxtTelefono().getText().trim();

        if (!Validadores.validarRUT(rut)) {
            Dialogos.INFORMACION("El RUT ingresado no es válido.", "Datos incorrectos"); return;
        }
        if (nombre.isBlank() || nombre.length() < 2) {
            Dialogos.INFORMACION("El nombre ingresado no es válido.", "Datos incorrectos"); return;
        }
        if (apellido.isBlank() || apellido.length() < 2) {
            Dialogos.INFORMACION("El apellido ingresado no es válido.", "Datos incorrectos"); return;
        }
        if (!Validadores.validarFecha(nacimiento)) {
            Dialogos.INFORMACION("La fecha ingresada no es válida.", "Datos incorrectos"); return;
        }
        if (encargado.isBlank() || encargado.length() < 2) {
            Dialogos.INFORMACION("El nombre del encargado no es válido.", "Datos incorrectos"); return;
        }
        if (telefono.isBlank() || telefono.length() < 7 || telefono.length() > 9) {
            Dialogos.INFORMACION("El número de teléfono ingresado no es válido.", "Datos incorrectos"); return;
        }

        try {
            if (adulto == null) {
                if (adultoDAO.existeRut(rut)) {
                    Dialogos.INFORMACION("El RUT " + rut + " ya se encuentra registrado en el sistema.", "Datos incorrectos"); return;
                }
                AdultoMayor nuevo =
                        new AdultoMayor(
                                rut,
                                nombre, apellido,
                                LocalDate.parse(nacimiento, DateTimeUtils.DATE_CL),
                                encargado,
                                telefono
                        );
                adultoDAO.create(nuevo);
                Dialogos.INFORMACION("Adulto Mayor creado con éxito.", "Adulto Mayor creado");
            }
            else {
                adulto.setRut(rut);
                adulto.setNombreAdultoMayor(nombre);
                adulto.setApellidoAdultoMayor(apellido);
                adulto.setNacimiento(LocalDate.parse(nacimiento, DateTimeUtils.DATE_CL));
                adulto.setNombreEncargado(encargado);
                adulto.setContactoEncargado(telefono);

                adultoDAO.update(adulto);
                Dialogos.INFORMACION("Adulto Mayor actualizado con éxito.", "Adulto Mayor actualizado");
            }
            vista.dispose();
        }
        catch (Exception ex) {
            Dialogos.ERROR("Error al guardad Adulto Mayor: ", ex);
        }
    }
}