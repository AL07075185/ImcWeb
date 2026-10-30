<%-- P_0002 Registro (CU_0001) --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/TagLibs.jsp" %>
<c:set var="paginaActiva" value="registro"/>
<c:set var="f" value="${formularioRegistro}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Crear cuenta · ImcWeb</title>
    <%@ include file="/WEB-INF/includes/Bootstrap.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">
<%@ include file="/WEB-INF/includes/Navbar.jsp" %>

<main class="flex-grow-1 d-flex align-items-center py-5">
    <div class="container">
        <div class="card tarjeta mx-auto" style="max-width: 680px;">
            <div class="card-body p-4">
                <h1 class="h3 mb-3">Crea tu cuenta</h1>
                <p class="text-secondary">Tu estatura se registra una sola vez y se usará en todos tus cálculos de IMC.</p>

                <c:if test="${not empty errores}">
                    <div class="alert alert-danger d-flex gap-2" role="alert">
                        <i class="bi bi-x-circle"></i> Revisa los campos marcados para continuar.
                    </div>
                </c:if>

                <form method="post" action="${ctx}/registro">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="nombre" class="form-label">Nombre(s)</label>
                            <input type="text" id="nombre" name="nombre" maxlength="50" required autocomplete="given-name"
                                   class="form-control ${not empty errores.nombre ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(f.nombre)}">
                            <div class="invalid-feedback"><c:out value="${errores.nombre}"/></div>
                        </div>
                        <div class="col-md-6">
                            <label for="apellidoPaterno" class="form-label">Apellido paterno</label>
                            <input type="text" id="apellidoPaterno" name="apellidoPaterno" maxlength="50" required
                                   autocomplete="family-name"
                                   class="form-control ${not empty errores.apellidoPaterno ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(f.apellidoPaterno)}">
                            <div class="invalid-feedback"><c:out value="${errores.apellidoPaterno}"/></div>
                        </div>
                        <div class="col-md-6">
                            <label for="apellidoMaterno" class="form-label">Apellido materno (opcional)</label>
                            <input type="text" id="apellidoMaterno" name="apellidoMaterno" maxlength="50"
                                   class="form-control ${not empty errores.apellidoMaterno ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(f.apellidoMaterno)}">
                            <div class="invalid-feedback"><c:out value="${errores.apellidoMaterno}"/></div>
                        </div>
                        <div class="col-md-6">
                            <label for="edad" class="form-label">Edad</label>
                            <input type="number" id="edad" name="edad" step="1" required inputmode="numeric"
                                   class="form-control ${not empty errores.edad ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(f.edad)}">
                            <div class="invalid-feedback"><c:out value="${errores.edad}"/></div>
                        </div>
                        <div class="col-md-6">
                            <label for="sexo" class="form-label">Sexo</label>
                            <select id="sexo" name="sexo" required
                                    class="form-select ${not empty errores.sexo ? 'is-invalid' : ''}">
                                <option value="" ${empty f.sexo ? 'selected' : ''}>Selecciona…</option>
                                <option value="M" ${f.sexo eq 'M' ? 'selected' : ''}>Masculino</option>
                                <option value="F" ${f.sexo eq 'F' ? 'selected' : ''}>Femenino</option>
                            </select>
                            <div class="invalid-feedback"><c:out value="${errores.sexo}"/></div>
                        </div>
                        <div class="col-md-6">
                            <label for="estatura" class="form-label">Estatura (m)</label>
                            <input type="number" id="estatura" name="estatura" step="0.01" required inputmode="decimal"
                                   placeholder="Ej. 1.75"
                                   class="form-control ${not empty errores.estatura ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(f.estatura)}">
                            <div class="invalid-feedback"><c:out value="${errores.estatura}"/></div>
                        </div>
                        <div class="col-12">
                            <label for="nombreUsuario" class="form-label">Nombre de usuario</label>
                            <input type="text" id="nombreUsuario" name="nombreUsuario" maxlength="30" required
                                   autocomplete="username" placeholder="Ej. jperez"
                                   class="form-control ${not empty errores.nombreUsuario ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(f.nombreUsuario)}">
                            <div class="invalid-feedback"><c:out value="${errores.nombreUsuario}"/></div>
                        </div>
                        <div class="col-md-6">
                            <label for="contrasena" class="form-label">Contraseña</label>
                            <input type="password" id="contrasena" name="contrasena" required autocomplete="new-password"
                                   class="form-control ${not empty errores.contrasena ? 'is-invalid' : ''}">
                            <div class="invalid-feedback"><c:out value="${errores.contrasena}"/></div>
                            <div class="form-text">Mínimo 8 caracteres.</div>
                        </div>
                        <div class="col-md-6">
                            <label for="confirmacion" class="form-label">Confirmar contraseña</label>
                            <input type="password" id="confirmacion" name="confirmacion" required autocomplete="new-password"
                                   class="form-control ${not empty errores.confirmacion ? 'is-invalid' : ''}">
                            <div class="invalid-feedback"><c:out value="${errores.confirmacion}"/></div>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-primary w-100">Crear cuenta</button>
                        </div>
                    </div>
                </form>

                <p class="text-center text-secondary mt-3 mb-0">
                    ¿Ya tienes cuenta? <a href="${ctx}/login" class="fw-semibold text-decoration-none">Inicia sesión</a>
                </p>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/includes/Footer.jsp" %>
<%@ include file="/WEB-INF/includes/BootstrapJS.jsp" %>
</body>
</html>
