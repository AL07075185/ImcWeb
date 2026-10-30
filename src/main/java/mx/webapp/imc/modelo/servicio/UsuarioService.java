package mx.webapp.imc.modelo.servicio;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Reglas de negocio de los usuarios: validación del registro, cifrado de la
 * contraseña con PBKDF2 y autenticación.
 */
@Service
public class UsuarioService {

    public static final int EDAD_MINIMA = 15;
    public static final int EDAD_MAXIMA = 120;
    public static final double ESTATURA_MINIMA = 1.00;
    public static final double ESTATURA_MAXIMA = 2.50;
    public static final int LONGITUD_MINIMA_CONTRASENA = 8;

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final String PREFIJO_HASH = "pbkdf2";
    private static final int ITERACIONES = 210_000;
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;

    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[\\p{L}][\\p{L} '.-]{0,49}$");
    private static final Pattern PATRON_USUARIO = Pattern.compile("^[a-z0-9._-]{3,30}$");

    private static final SecureRandom ALEATORIO = new SecureRandom();

    /** Hash de referencia para igualar el tiempo de respuesta cuando el usuario no existe. */
    private static final String HASH_FICTICIO = generarHash("imcWeb-sin-usuario");

    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
    }

    /**
     * Valida los datos, cifra la contraseña y guarda al usuario.
     *
     * @return el usuario registrado con su idUsuario y sin el hash de la contraseña
     * @throws ValidacionException con todos los campos que no cumplen las reglas
     */
    public Usuario registrar(Usuario usuario, String contrasena, String confirmacion) throws ValidacionException {
        normalizar(usuario);
        validarDatos(usuario, contrasena, confirmacion);
        usuario.setContrasenaHash(generarHash(contrasena));
        usuario.setFechaRegistro(LocalDateTime.now().withNano(0));
        usuario.setIdUsuario(usuarioRepository.insertar(usuario));
        usuario.setContrasenaHash(null);
        return usuario;
    }

    /**
     * Comprueba el nombre de usuario y la contraseña.
     *
     * @return el usuario autenticado, sin el hash de la contraseña
     * @throws ValidacionException si las credenciales no son correctas
     */
    public Usuario autenticar(String nombreUsuario, String contrasena) throws ValidacionException {
        String nombre = nombreUsuario == null ? "" : nombreUsuario.trim().toLowerCase(Locale.ROOT);
        String clave = contrasena == null ? "" : contrasena;
        if (nombre.isEmpty() || clave.isEmpty()) {
            throw new ValidacionException("credenciales", "Captura tu nombre de usuario y tu contraseña.");
        }
        Usuario usuario = usuarioRepository.buscarPorNombreUsuario(nombre);
        boolean valida = verificarHash(clave, usuario != null ? usuario.getContrasenaHash() : HASH_FICTICIO);
        if (usuario == null || !valida) {
            throw new ValidacionException("credenciales", "Usuario o contraseña incorrectos.");
        }
        usuario.setContrasenaHash(null);
        return usuario;
    }

    /**
     * Regresa el usuario sin el hash de la contraseña.
     *
     * @throws RecursoNoEncontradoException si el id no existe
     */
    public Usuario obtenerUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
        if (usuario == null) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + idUsuario + ".");
        }
        usuario.setContrasenaHash(null);
        return usuario;
    }

    private void normalizar(Usuario usuario) {
        usuario.setNombre(recortar(usuario.getNombre()));
        usuario.setApellidoPaterno(recortar(usuario.getApellidoPaterno()));
        String materno = recortar(usuario.getApellidoMaterno());
        usuario.setApellidoMaterno(materno.isEmpty() ? null : materno);
        usuario.setNombreUsuario(recortar(usuario.getNombreUsuario()).toLowerCase(Locale.ROOT));
        usuario.setSexo(Character.toUpperCase(usuario.getSexo()));
    }

    private void validarDatos(Usuario usuario, String contrasena, String confirmacion) throws ValidacionException {
        Map<String, String> errores = new LinkedHashMap<>();

        if (usuario.getNombre().isEmpty()) {
            errores.put("nombre", "El nombre es obligatorio.");
        } else if (!PATRON_NOMBRE.matcher(usuario.getNombre()).matches()) {
            errores.put("nombre", "Usa solo letras y espacios (máximo 50 caracteres).");
        }
        if (usuario.getApellidoPaterno().isEmpty()) {
            errores.put("apellidoPaterno", "El apellido paterno es obligatorio.");
        } else if (!PATRON_NOMBRE.matcher(usuario.getApellidoPaterno()).matches()) {
            errores.put("apellidoPaterno", "Usa solo letras y espacios (máximo 50 caracteres).");
        }
        if (usuario.getApellidoMaterno() != null && !PATRON_NOMBRE.matcher(usuario.getApellidoMaterno()).matches()) {
            errores.put("apellidoMaterno", "Usa solo letras y espacios (máximo 50 caracteres).");
        }
        if (usuario.getEdad() < EDAD_MINIMA) {
            errores.put("edad", "Debes tener al menos 15 años.");
        } else if (usuario.getEdad() > EDAD_MAXIMA) {
            errores.put("edad", "Captura una edad válida (máximo 120 años).");
        }
        if (usuario.getSexo() != 'M' && usuario.getSexo() != 'F') {
            errores.put("sexo", "Selecciona el sexo.");
        }
        if (usuario.getEstatura() < ESTATURA_MINIMA || usuario.getEstatura() > ESTATURA_MAXIMA) {
            errores.put("estatura", "La estatura debe estar entre 1.00 y 2.50 m.");
        }
        if (usuario.getNombreUsuario().isEmpty()) {
            errores.put("nombreUsuario", "El nombre de usuario es obligatorio.");
        } else if (!PATRON_USUARIO.matcher(usuario.getNombreUsuario()).matches()) {
            errores.put("nombreUsuario", "Usa de 3 a 30 caracteres: letras, números, punto, guion o guion bajo.");
        } else if (usuarioRepository.existeNombreUsuario(usuario.getNombreUsuario())) {
            errores.put("nombreUsuario", "Este nombre de usuario ya está en uso.");
        }
        if (contrasena == null || contrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            errores.put("contrasena", "La contraseña debe tener al menos 8 caracteres.");
        } else if (!contrasena.equals(confirmacion)) {
            errores.put("confirmacion", "Las contraseñas no coinciden.");
        }

        if (!errores.isEmpty()) {
            throw new ValidacionException(errores);
        }
    }

    /** Genera el hash con el formato pbkdf2$iteraciones$sal$hash (Base64). */
    private static String generarHash(String contrasena) {
        byte[] sal = new byte[BYTES_SAL];
        ALEATORIO.nextBytes(sal);
        byte[] hash = derivar(contrasena.toCharArray(), sal, ITERACIONES);
        Base64.Encoder base64 = Base64.getEncoder();
        return PREFIJO_HASH + "$" + ITERACIONES + "$" + base64.encodeToString(sal) + "$" + base64.encodeToString(hash);
    }

    private boolean verificarHash(String contrasena, String hashGuardado) {
        if (hashGuardado == null) {
            return false;
        }
        String[] partes = hashGuardado.split("\\$");
        if (partes.length != 4 || !PREFIJO_HASH.equals(partes[0])) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = derivar(contrasena.toCharArray(), sal, iteraciones);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static byte[] derivar(char[] contrasena, byte[] sal, int iteraciones) {
        PBEKeySpec especificacion = new PBEKeySpec(contrasena, sal, iteraciones, BITS_HASH);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(especificacion).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("PBKDF2 no está disponible en la JVM.", ex);
        } finally {
            especificacion.clearPassword();
        }
    }

    private static String recortar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
}
