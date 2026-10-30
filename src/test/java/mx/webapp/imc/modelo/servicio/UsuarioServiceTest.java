package mx.webapp.imc.modelo.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import mx.webapp.imc.modelo.entidad.Usuario;
import mx.webapp.imc.modelo.repositorio.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de UsuarioService con un repositorio en memoria: validaciones del
 * registro (RF_0001 a RF_0004), cifrado PBKDF2 y autenticación (RF_0005).
 */
class UsuarioServiceTest {

    /** Hash de "Imc2026!" del usuario jperez en database/imcDb.sql. */
    private static final String HASH_JPEREZ =
            "pbkdf2$210000$mcKzOKjiC+ZQDXbBGu+OVg==$TvnTqGq7CdlAUb6CtwxR20BiZT9UV1orhHr/M/wrvr0=";

    private UsuarioRepositoryEnMemoria repositorio;
    private UsuarioService usuarioService;

    @BeforeEach
    void preparar() {
        repositorio = new UsuarioRepositoryEnMemoria();
        usuarioService = new UsuarioService();
        usuarioService.setUsuarioRepository(repositorio);
    }

    @Test
    void registraUsuarioValidoConContrasenaCifrada() throws ValidacionException {
        Usuario registrado = usuarioService.registrar(crearUsuario("lmora", 20, 1.70), "Secreta123", "Secreta123");

        assertEquals(1, registrado.getIdUsuario());
        assertNull(registrado.getContrasenaHash());
        String hashGuardado = repositorio.buscarPorNombreUsuario("lmora").getContrasenaHash();
        assertTrue(hashGuardado.startsWith("pbkdf2$210000$"));
    }

    @Test
    void rechazaEdadMenorA15() {
        ValidacionException ex = assertThrows(ValidacionException.class,
                () -> usuarioService.registrar(crearUsuario("lmora", 14, 1.70), "Secreta123", "Secreta123"));
        assertEquals("Debes tener al menos 15 años.", ex.getErrores().get("edad"));
    }

    @Test
    void rechazaEstaturaFueraDeRango() {
        ValidacionException ex = assertThrows(ValidacionException.class,
                () -> usuarioService.registrar(crearUsuario("lmora", 20, 2.80), "Secreta123", "Secreta123"));
        assertEquals("La estatura debe estar entre 1.00 y 2.50 m.", ex.getErrores().get("estatura"));
    }

    @Test
    void reportaVariosErroresYUsuarioDuplicado() {
        repositorio.agregar(crearUsuarioConHash("jperez", HASH_JPEREZ));
        ValidacionException ex = assertThrows(ValidacionException.class,
                () -> usuarioService.registrar(crearUsuario("jperez", 14, 2.80), "Secreta123", "Otra12345"));
        assertTrue(ex.getErrores().containsKey("edad"));
        assertTrue(ex.getErrores().containsKey("estatura"));
        assertEquals("Este nombre de usuario ya está en uso.", ex.getErrores().get("nombreUsuario"));
        assertEquals("Las contraseñas no coinciden.", ex.getErrores().get("confirmacion"));
    }

    @Test
    void autenticaConElHashDelScriptSql() throws ValidacionException {
        repositorio.agregar(crearUsuarioConHash("jperez", HASH_JPEREZ));
        Usuario usuario = usuarioService.autenticar("JPerez ", "Imc2026!");
        assertEquals("jperez", usuario.getNombreUsuario());
        assertNull(usuario.getContrasenaHash());
    }

    @Test
    void rechazaCredencialesIncorrectas() {
        repositorio.agregar(crearUsuarioConHash("jperez", HASH_JPEREZ));
        ValidacionException contrasena = assertThrows(ValidacionException.class,
                () -> usuarioService.autenticar("jperez", "otraClave"));
        ValidacionException usuario = assertThrows(ValidacionException.class,
                () -> usuarioService.autenticar("noExiste", "Imc2026!"));
        assertEquals("Usuario o contraseña incorrectos.", contrasena.getMessage());
        assertEquals(contrasena.getMessage(), usuario.getMessage());
    }

    private Usuario crearUsuario(String nombreUsuario, int edad, double estatura) {
        Usuario usuario = new Usuario();
        usuario.setNombre("Luis");
        usuario.setApellidoPaterno("Mora");
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setEdad(edad);
        usuario.setSexo('M');
        usuario.setEstatura(estatura);
        return usuario;
    }

    private Usuario crearUsuarioConHash(String nombreUsuario, String hash) {
        Usuario usuario = crearUsuario(nombreUsuario, 28, 1.75);
        usuario.setContrasenaHash(hash);
        return usuario;
    }

    /** Sustituye la base de datos por un mapa para probar solo las reglas de negocio. */
    private static class UsuarioRepositoryEnMemoria extends UsuarioRepository {

        private final Map<String, Usuario> usuarios = new LinkedHashMap<>();

        void agregar(Usuario usuario) {
            insertar(usuario);
        }

        @Override
        public int insertar(Usuario usuario) {
            Usuario copia = new Usuario();
            copia.setIdUsuario(usuarios.size() + 1);
            copia.setNombreUsuario(usuario.getNombreUsuario());
            copia.setContrasenaHash(usuario.getContrasenaHash());
            usuarios.put(usuario.getNombreUsuario(), copia);
            return copia.getIdUsuario();
        }

        @Override
        public Usuario buscarPorNombreUsuario(String nombreUsuario) {
            Usuario guardado = usuarios.get(nombreUsuario);
            if (guardado == null) {
                return null;
            }
            Usuario copia = new Usuario();
            copia.setIdUsuario(guardado.getIdUsuario());
            copia.setNombreUsuario(guardado.getNombreUsuario());
            copia.setContrasenaHash(guardado.getContrasenaHash());
            return copia;
        }

        @Override
        public boolean existeNombreUsuario(String nombreUsuario) {
            return usuarios.containsKey(nombreUsuario);
        }
    }
}
