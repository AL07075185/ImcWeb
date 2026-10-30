package mx.webapp.imc.controlador;

import mx.webapp.imc.modelo.entidad.Usuario;

/**
 * Datos del formulario de registro tal como los envía el navegador. Se
 * conservan como texto para volver a mostrarlos si hay errores.
 */
public class FormularioRegistro {

    private String nombre = "";
    private String apellidoPaterno = "";
    private String apellidoMaterno = "";
    private String edad = "";
    private String sexo = "";
    private String estatura = "";
    private String nombreUsuario = "";
    private String contrasena = "";
    private String confirmacion = "";

    public FormularioRegistro() {
    }

    /**
     * Crea el usuario con los valores convertidos. Un número inválido se deja
     * en 0 para que UsuarioService lo rechace con el mensaje de su rango.
     */
    public Usuario crearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setEdad(convertirEntero(edad));
        usuario.setSexo(sexo == null || sexo.isBlank() ? ' ' : sexo.trim().charAt(0));
        usuario.setEstatura(convertir(estatura));
        usuario.setNombreUsuario(nombreUsuario);
        return usuario;
    }

    /** Limpia las contraseñas para no regresarlas a la vista. */
    public void limpiarContrasenas() {
        this.contrasena = "";
        this.confirmacion = "";
    }

    private int convertirEntero(String texto) {
        if (texto == null || texto.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private double convertir(String texto) {
        if (texto == null || texto.isBlank()) {
            return 0;
        }
        try {
            double valor = Double.parseDouble(texto.trim().replace(',', '.'));
            return Double.isFinite(valor) ? valor : 0;
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstatura() {
        return estatura;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }
}
