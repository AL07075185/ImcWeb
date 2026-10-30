<%-- P_0004 IMC actual e historial (CU_0004). Los datos se obtienen del servicio
     REST GET /api/usuarios/{idUsuario}/mediciones con js/historial.js (CU_0006). --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/TagLibs.jsp" %>
<c:set var="paginaActiva" value="historial"/>
<c:set var="usuario" value="${sessionScope.usuarioSesion}"/>
<c:set var="urlApi" value="${ctx}/api/usuarios/${usuario.idUsuario}/mediciones"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Mi IMC · ImcWeb</title>
    <%@ include file="/WEB-INF/includes/Bootstrap.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">
<%@ include file="/WEB-INF/includes/Navbar.jsp" %>

<main class="flex-grow-1 py-5">
    <div class="container-xl" id="historial" data-api="${urlApi}">
        <h1 class="display-6 fw-semibold mb-1">Mi IMC</h1>
        <p class="text-secondary mb-4">Resultado de tu última medición y el historial de tus cálculos.</p>

        <c:if test="${not empty medicionNueva}">
            <div class="alert alert-success alert-dismissible fade show d-flex gap-2" role="alert">
                <i class="bi bi-check-circle"></i>
                <div>
                    Se registró tu medición: IMC
                    <strong><fmt:formatNumber value="${medicionNueva.imc}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                    (<c:out value="${medicionNueva.clasificacion.nombre}"/>).
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>
        </c:if>

        <%@ include file="/WEB-INF/includes/AvisoAdolescente.jsp" %>

        <div id="mensajeError" class="alert alert-danger d-none" role="alert"></div>

        <div class="row g-4 align-items-start">
            <!-- IMC actual -->
            <div class="col-lg-4">
                <div class="card tarjeta">
                    <div class="card-body p-4">
                        <p class="small fw-semibold text-secondary mb-2">Tu IMC actual</p>

                        <div id="cargando" class="py-4 text-center text-secondary">
                            <div class="spinner-border spinner-border-sm me-2" role="status"></div> Consultando el servicio…
                        </div>

                        <div id="sinMediciones" class="d-none">
                            <p class="text-secondary">Aún no tienes mediciones registradas.</p>
                            <a href="${ctx}/calculo" class="btn btn-primary w-100">Calcular mi IMC</a>
                        </div>

                        <div id="actual" class="d-none">
                            <div class="d-flex align-items-baseline gap-2">
                                <span id="imcActual" class="imc-valor"></span>
                                <span class="text-secondary">kg/m²</span>
                            </div>
                            <span id="clasificacionActual" class="badge rounded-pill mt-2"></span>
                            <hr>
                            <dl class="row mb-3 datos-actual">
                                <dt class="col-6 fw-normal text-secondary">Peso</dt>
                                <dd class="col-6 text-end fw-semibold" id="pesoActual"></dd>
                                <dt class="col-6 fw-normal text-secondary">Estatura</dt>
                                <dd class="col-6 text-end fw-semibold">
                                    <fmt:formatNumber value="${usuario.estatura}" minFractionDigits="2" maxFractionDigits="2"/> m
                                </dd>
                                <dt class="col-6 fw-normal text-secondary">Fecha de medición</dt>
                                <dd class="col-6 text-end fw-semibold" id="fechaActual"></dd>
                            </dl>
                            <a href="${ctx}/calculo" class="btn btn-primary w-100">Nuevo cálculo</a>
                        </div>

                        <div class="alert alert-info d-flex gap-2 small mt-3 mb-0">
                            <i class="bi bi-info-circle"></i>
                            <div>El IMC es un indicador de referencia: no distingue entre masa muscular y grasa.</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Historial -->
            <div class="col-lg-8">
                <div class="card tarjeta">
                    <div class="card-body p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h2 class="h5 mb-0">Historial de cálculos</h2>
                            <span id="totalMediciones" class="small text-secondary"></span>
                        </div>
                        <div class="table-responsive border rounded">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light small">
                                <tr>
                                    <th scope="col">Fecha</th>
                                    <th scope="col">Peso (kg)</th>
                                    <th scope="col">IMC</th>
                                    <th scope="col">Clasificación</th>
                                </tr>
                                </thead>
                                <tbody id="tablaHistorial">
                                <tr><td colspan="4" class="text-center text-secondary py-4">Cargando…</td></tr>
                                </tbody>
                            </table>
                        </div>
                        <p class="small text-secondary mt-3 mb-0">
                            Datos obtenidos del servicio REST GET /api/usuarios/${usuario.idUsuario}/mediciones (JSON).
                            Ver representación:
                            <a href="${urlApi}?formato=json" target="_blank" rel="noopener">JSON</a> ·
                            <a href="${urlApi}?formato=xml" target="_blank" rel="noopener">XML</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/includes/Footer.jsp" %>
<%@ include file="/WEB-INF/includes/BootstrapJS.jsp" %>
<script src="${ctx}/js/historial.js"></script>
</body>
</html>
