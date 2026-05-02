package pe.edu.unasam.modelo;

public class historialEjecucion {
    
    private String id;
    private String vocabulario;
    private String cantidad_lenguas;
    private String fecha_registro;
    
    public historialEjecucion() {
    
    }

    public historialEjecucion(String id, String vocabulario, String cantidad_lenguas, String fecha_registro) {
        this.id = id;
        this.vocabulario = vocabulario;
        this.cantidad_lenguas = cantidad_lenguas;
        this.fecha_registro = fecha_registro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVocabulario() {
        return vocabulario;
    }

    public void setVocabulario(String vocabulario) {
        this.vocabulario = vocabulario;
    }

    public String getCantidad_lenguas() {
        return cantidad_lenguas;
    }

    public void setCantidad_lenguas(String cantidad_lenguas) {
        this.cantidad_lenguas = cantidad_lenguas;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
    
}
