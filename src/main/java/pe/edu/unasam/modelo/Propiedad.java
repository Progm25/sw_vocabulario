/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.unasam.modelo;

/**
 *
 * @author PC
 */
public class Propiedad {
    private int idPropiedad;
    private String nombre;
    private String descripcion;
    private String patronRegex;
    private String tipoOperacion;

    // Constructor vacío
    public Propiedad() {
    }

    // Constructor completo
    public Propiedad(int idPropiedad, String nombre, String descripcion, String patronRegex, String tipoOperacion) {
        this.idPropiedad = idPropiedad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.patronRegex = patronRegex;
        this.tipoOperacion = tipoOperacion;
    }

    // Getters y Setters
    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getPatronRegex() { return patronRegex; }
    public void setPatronRegex(String patronRegex) { this.patronRegex = patronRegex; }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }
    
    // Sobrescribir toString para que el JComboBox muestre el nombre de la propiedad
    @Override
    public String toString() {
        return this.nombre;
    }
    
}
