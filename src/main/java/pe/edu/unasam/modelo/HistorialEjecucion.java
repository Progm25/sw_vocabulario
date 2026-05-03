package pe.edu.unasam.modelo;

import java.sql.Timestamp;

public class HistorialEjecucion {
    
    private int idEjecucion;
    private String vocabulario;
    private int cantidadLenguajes;
    private Timestamp fechaRegistro;

    // Constructor vacío (Muy importante para instanciar en el DAO)
    public HistorialEjecucion() {
    }

    // Constructor completo
    public HistorialEjecucion(int idEjecucion, String vocabulario, int cantidadLenguajes, Timestamp fechaRegistro) {
        this.idEjecucion = idEjecucion;
        this.vocabulario = vocabulario;
        this.cantidadLenguajes = cantidadLenguajes;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getIdEjecucion() { return idEjecucion; }
    public void setIdEjecucion(int idEjecucion) { this.idEjecucion = idEjecucion; }

    public String getVocabulario() { return vocabulario; }
    public void setVocabulario(String vocabulario) { this.vocabulario = vocabulario; }

    public int getCantidadLenguajes() { return cantidadLenguajes; }
    public void setCantidadLenguajes(int cantidadLenguajes) { this.cantidadLenguajes = cantidadLenguajes; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Ejecución [" + fechaRegistro + "] - Vocabulario: {" + vocabulario + "} - Lenguajes: " + cantidadLenguajes;
    }
    
}
