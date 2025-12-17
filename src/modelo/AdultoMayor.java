package modelo;

import java.time.LocalDate;

import static utils.Validadores.validarRUT;

public class AdultoMayor
{
    private String rut;
    private String nombreAdultoMayor;
    private String apellidoAdultoMayor;
    private LocalDate nacimiento;
    private String nombreEncargado;
    private String contactoEncargado;

    public AdultoMayor() {
    }

    public AdultoMayor(String rut, String nombreAdultoMayor, String apellidoAdultoMayor, LocalDate nacimiento,
                       String nombreEncargado, String contactoEncargado)
    {
        setRut(rut);
        setNombreAdultoMayor(nombreAdultoMayor);
        setApellidoAdultoMayor(apellidoAdultoMayor);
        setNacimiento(nacimiento);
        setNombreEncargado(nombreEncargado);
        setContactoEncargado(contactoEncargado);
    }

    public void setRut(String rut) {
        if (!validarRUT(rut)) {
            throw new IllegalArgumentException("RUT inválido: " + rut);
        }
        this.rut = rut.toUpperCase();
    }

    public void setNombreAdultoMayor(String nombreAdultoMayor) {
        if (nombreAdultoMayor == null || nombreAdultoMayor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombreAdultoMayor = nombreAdultoMayor.trim();
    }

    public void setApellidoAdultoMayor(String apellidoAdultoMayor) {
        if (apellidoAdultoMayor == null || apellidoAdultoMayor.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        this.apellidoAdultoMayor = apellidoAdultoMayor.trim();
    }

    public void setNacimiento(LocalDate nacimiento) {
        if (nacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser null.");
        }
        if (nacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        this.nacimiento = nacimiento;
    }

    public void setNombreEncargado(String nombreEncargado) {
        if (nombreEncargado == null || nombreEncargado.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del encargado no puede estar vacío.");
        }
        this.nombreEncargado = nombreEncargado.trim();
    }

    public void setContactoEncargado(String contactoEncargado) {
        if (contactoEncargado == null || contactoEncargado.trim().isEmpty()) {
            throw new IllegalArgumentException("El contacto no puede estar vacío.");
        }
        this.contactoEncargado = contactoEncargado.trim();
    }

    public String getRut() {
        return rut;
    }

    public String getNombreAdultoMayor() {
        return nombreAdultoMayor;
    }

    public String getApellidoAdultoMayor() {
        return apellidoAdultoMayor;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public String getContactoEncargado() {
        return contactoEncargado;
    }

    @Override
    public String toString() {
        return nombreAdultoMayor + " " + apellidoAdultoMayor;
    }
}
