<%-- P_0001 Inicio de sesión (CU_0002) --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/TagLibs.jsp" %>
<c:set var="paginaActiva" value="login"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Iniciar sesión · ImcWeb</title>
    <%@ include file="/WEB-INF/includes/Bootstrap.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">
<%@ include file="/WEB-INF/includes/Navbar.jsp" %>

<main class="flex-grow-1 d-flex align-items-center py-5">
    <div class="container">
        <div class="card tarjeta mx-auto" style="max-width: 440px;">
            <div class="card-body p-4">
                <h1 class="h3 mb-3">Inicia sesión</h1>
                <p class="text-secondary">Ingresa con tu usuario para calcular y consultar tu índice de masa corporal.</p>

                <c:if test="${param.registro eq 'ok'}">
                    <div class="alert alert-success d-flex gap-2" role="alert">
                        <i class="bi bi-check-circle"></i> Tu cuenta se creó correctamente. Inicia sesión.
                    </div>
                </c:if>
                <c:if test="${param.salida eq 'ok'}">
                    <div class="alert alert-info d-flex gap-2" role="alert">
                        <i class="bi bi-box-arrow-right"></i> Cerraste tu sesión.
                    </div>
                </c:if>
                <c:if test="${param.sesion eq 'expirada'}">
                    <div class="alert alert-warning d-flex gap-2" role="alert">
                        <i class="bi bi-lock"></i> Inicia sesión para continuar.
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex gap-2" role="alert">
                        <i class="bi bi-x-circle"></i> <c:out value="${error}"/>
                    </div>
                </c:if>

                <form method="post" action="${ctx}/login">
                    <div class="mb-3">
                        <label for="nombreUsuario" class="form-label">Nombre de usuario</label>
                        <input type="text" class="form-control form-control-lg fs-6" id="nombreUsuario" name="nombreUsuario"
                               value="${fn:escapeXml(nombreUsuario)}" placeholder="Ej. jperez" maxlength="30"
                               autocomplete="username" required autofocus>
                    </div>
                    <div class="mb-3">
                        <label for="contrasena" class="form-label">Contraseña</label>
                        <input type="password" class="form-control form-control-lg fs-6" id="contrasena" name="contrasena"
                               placeholder="••••••••" autocomplete="current-password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Iniciar sesión</button>
                </form>

                <p class="text-center text-secondary mt-3 mb-0">
                    ¿No tienes cuenta? <a href="${ctx}/registro" class="fw-semibold text-decoration-none">Regístrate</a>
                </p>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/includes/Footer.jsp" %>
<%@ include file="/WEB-INF/includes/BootstrapJS.jsp" %>
</body>
</html>
