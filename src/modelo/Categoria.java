package modelo;

public class Categoria
{
    private int idCategoria;
    private String nombre;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre) {
        setIdCategoria(idCategoria);
        setNombre(nombre);
    }

    public void setIdCategoria(int idCategoria) {
        if (idCategoria < 0) {
            throw new IllegalArgumentException("El ID de la categoría no debe ser negativo.");
        }
        this.idCategoria = idCategoria;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no debe estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria other = (Categoria) obj;
        return idCategoria == other.idCategoria;
    }
}
