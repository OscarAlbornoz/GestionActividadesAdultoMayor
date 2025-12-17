package modelo;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Actividad
{
    private int idActividad;
    private String nombreActividad;
    private Categoria categoria;
    private String descripcion;
    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cupos;
    private String nombreProfesor;

    public Actividad() {
    }

    public Actividad(int idActividad, String nombreActividad, Categoria categoria, String descripcion, DayOfWeek dia,
                     LocalTime horaInicio, LocalTime horaFin, int cupos, String nombreProfesor)
    {
        setIdActividad(idActividad);
        setNombreActividad(nombreActividad);
        setCategoria(categoria);
        setDescripcion(descripcion);
        setDia(dia);
        setHoraInicio(horaInicio);
        setHoraFin(horaFin);
        setCupos(cupos);
        setNombreProfesor(nombreProfesor);
    }

    // Constructor sin ID
    public Actividad(String nombre, Categoria cat, String desc, DayOfWeek dia, LocalTime inicio, LocalTime fin, int cupos, String profesor) {
        this.nombreActividad = nombre;
        this.categoria = cat;
        this.descripcion = desc;
        this.dia = dia;
        this.horaInicio = inicio;
        this.horaFin = fin;
        this.cupos = cupos;
        this.nombreProfesor = profesor;
    }

    public void setIdActividad(int idActividad) {
        if (idActividad < 0) {
            throw new IllegalArgumentException("El id de la actividad no puede ser negativo.");
        }
        this.idActividad = idActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        if (nombreActividad == null || nombreActividad.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la actividad no puede estar vacío.");
        }
        this.nombreActividad = nombreActividad.trim();
    }

    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser null.");
        }
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = (descripcion != null && !descripcion.trim().isEmpty()) ? descripcion.trim() : null;
    }

    public void setDia(DayOfWeek dia) {
        if (dia == null) {
            throw new IllegalArgumentException("El día no puede ser null.");
        }
        this.dia = dia;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        if (horaInicio == null) {
            throw new IllegalArgumentException("La hora de inicio no puede ser null.");
        }
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        if (horaFin == null) {
            throw new IllegalArgumentException("La hora de fin no puede ser null.");
        }
        if (horaInicio != null && horaFin.isBefore(horaInicio)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        this.horaFin = horaFin;
    }

    public void setCupos(int cupos) {
        if (cupos < 0) {
            throw new IllegalArgumentException("Los cupos no pueden ser negativos.");
        }
        this.cupos = cupos;
    }

    public void setNombreProfesor(String nombreProfesor) {
        if (nombreProfesor == null || nombreProfesor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del profesor no puede estar vacío.");
        }
        this.nombreProfesor = nombreProfesor.trim();
    }

    public int getIdActividad() {
        return idActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public int getCupos() {
        return cupos;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    @Override
    public String toString() {
        return nombreActividad;
    }
}
