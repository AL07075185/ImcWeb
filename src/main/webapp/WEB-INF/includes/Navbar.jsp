<%-- Barra de navegación: cambia según exista o no una sesión activa --%>
<nav class="navbar navbar-expand-md navbar-dark bg-primary barra-principal">
    <div class="container-xl">
        <a class="navbar-brand fw-semibold d-flex align-items-center gap-2" href="${ctx}/">
            <i class="bi bi-activity"></i> ImcWeb
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menuPrincipal"
                aria-controls="menuPrincipal" aria-expanded="false" aria-label="Mostrar menú">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="menuPrincipal">
            <ul class="navbar-nav ms-auto align-items-md-center gap-md-3">
                <c:choose>
                    <c:when test="${not empty sessionScope.usuarioSesion}">
                        <li class="nav-item">
                            <a class="nav-link ${paginaActiva eq 'calculo' ? 'active' : ''}" href="${ctx}/calculo">Calcular</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${paginaActiva eq 'historial' ? 'active' : ''}" href="${ctx}/historial">Historial</a>
                        </li>
                        <li class="nav-item">
                            <span class="navbar-text text-white">
                                <i class="bi bi-person me-1"></i>
                                <c:out value="${sessionScope.usuarioSesion.nombre} ${sessionScope.usuarioSesion.apellidoPaterno}"/>
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-light" href="${ctx}/logout">Cerrar sesión</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link ${paginaActiva eq 'login' ? 'active' : ''}" href="${ctx}/login">Iniciar sesión</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${paginaActiva eq 'registro' ? 'active' : ''}" href="${ctx}/registro">Registrarse</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
