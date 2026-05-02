
package pe.edu.unasam.modelo;


public class propiedades {
    
    private String id;
    private String nombre;
    private String descripcion;
    private String patron;
    private String tipo_operacion;

    public propiedades() {
    }

    public propiedades(String id, String nombre, String descripcion, String patron, String tipo_operacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.patron = patron;
        this.tipo_operacion = tipo_operacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }

    public String getTipo_operacion() {
        return tipo_operacion;
    }

    public void setTipo_operacion(String tipo_operacion) {
        this.tipo_operacion = tipo_operacion;
    }
    
    
    
    
}
