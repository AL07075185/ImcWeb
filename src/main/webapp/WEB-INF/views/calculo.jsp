<%-- P_0003 Captura de peso (CU_0003) --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/TagLibs.jsp" %>
<c:set var="paginaActiva" value="calculo"/>
<c:set var="usuario" value="${sessionScope.usuarioSesion}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Calcular IMC · ImcWeb</title>
    <%@ include file="/WEB-INF/includes/Bootstrap.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">
<%@ include file="/WEB-INF/includes/Navbar.jsp" %>

<main class="flex-grow-1 d-flex align-items-center py-5">
    <div class="container">
        <div class="card tarjeta mx-auto" style="max-width: 560px;">
            <div class="card-body p-4">
                <h1 class="h3 mb-3">Calcula tu IMC</h1>
                <p class="text-secondary">Hola, <c:out value="${usuario.nombre}"/>. Captura tu peso actual para calcular tu índice de masa corporal.</p>

                <%@ include file="/WEB-INF/includes/AvisoAdolescente.jsp" %>

                <form method="post" action="${ctx}/calculo">
                    <div class="row g-3">
                        <div class="col-sm-6">
                            <label for="estatura" class="form-label">Estatura registrada</label>
                            <input type="text" id="estatura" class="form-control" readonly disabled
                                   value="<fmt:formatNumber value='${usuario.estatura}' minFractionDigits='2' maxFractionDigits='2'/> m">
                            <div class="form-text">Se registró al crear tu cuenta.</div>
                        </div>
                        <div class="col-sm-6">
                            <label for="peso" class="form-label">Peso (kg)</label>
                            <input type="number" id="peso" name="peso" step="0.01" required inputmode="decimal"
                                   placeholder="Ej. 76.50" autofocus
                                   class="form-control ${not empty error ? 'is-invalid' : ''}"
                                   value="${fn:escapeXml(peso)}">
                            <c:choose>
                                <c:when test="${not empty error}">
                                    <div class="invalid-feedback"><c:out value="${error}"/></div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-text">Usa punto decimal, por ejemplo 76.50.</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-primary w-100">Calcular IMC</button>
                            <p class="text-center small text-secondary mt-3 mb-0">IMC = peso (kg) ÷ estatura² (m²)</p>
                        </div>
                    </div>
                </form>

                <hr class="my-4">
                <h2 class="h6 mb-3">Tabla de referencia (OMS, adultos)</h2>
                <ul class="list-unstyled mb-0">
                    <c:forEach var="rango" items="${clasificaciones}">
                        <li class="d-flex justify-content-between align-items-center py-1">
                            <span class="badge rounded-pill imc-${rango.idClasificacion}"><c:out value="${rango.nombre}"/></span>
                            <span class="small text-secondary">
                                <c:choose>
                                    <c:when test="${rango.imcMinimo eq 0}">
                                        Menor a <fmt:formatNumber value="${rango.imcMaximo + 0.01}" minFractionDigits="1" maxFractionDigits="2"/> kg/m²
                                    </c:when>
                                    <c:when test="${rango.imcMaximo ge 999}">
                                        <fmt:formatNumber value="${rango.imcMinimo}" minFractionDigits="1" maxFractionDigits="2"/> o más kg/m²
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${rango.imcMinimo}" minFractionDigits="1" maxFractionDigits="2"/> –
                                        <fmt:formatNumber value="${rango.imcMaximo}" minFractionDigits="1" maxFractionDigits="2"/> kg/m²
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/includes/Footer.jsp" %>
<%@ include file="/WEB-INF/includes/BootstrapJS.jsp" %>
</body>
</html>
