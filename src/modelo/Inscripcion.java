package modelo;

public class Inscripcion
{
    private int idInscripcion;
    private AdultoMayor adultoMayor;
    private Actividad actividad;

    public Inscripcion() {
    }

    public Inscripcion(AdultoMayor adultoMayor, Actividad actividad) {
        setAdultoMayor(adultoMayor);
        setActividad(actividad);
    }

    public Inscripcion(int idInscripcion, AdultoMayor adultoMayor, Actividad actividad) {
        setIdInscripcion(idInscripcion);
        setAdultoMayor(adultoMayor);
        setActividad(actividad);
    }

    public void setIdInscripcion(int idInscripcion) {
        if (idInscripcion < 0) {
            throw new IllegalArgumentException("El ID de la inscripción no puede ser negativo.");
        }
        this.idInscripcion = idInscripcion;
    }

    public void setAdultoMayor(AdultoMayor adultoMayor) {
        if (adultoMayor == null) {
            throw new IllegalArgumentException("El adulto mayor no puede ser null.");
        }
        this.adultoMayor = adultoMayor;
    }

    public void setActividad(Actividad actividad) {
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser null.");
        }
        this.actividad = actividad;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public AdultoMayor getAdultoMayor() {
        return adultoMayor;
    }

    public Actividad getActividad() {
        return actividad;
    }

    @Override
    public String toString() {
        return adultoMayor + " -> " + actividad;
    }
}
