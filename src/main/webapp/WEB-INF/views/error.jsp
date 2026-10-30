<%-- P_0005 Página de error. La usan ManejadorErroresWeb (Spring MVC) y las
     páginas de error de web.xml (errores fuera de Spring). --%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/TagLibs.jsp" %>
<c:set var="codigoError" value="${not empty codigo ? codigo : requestScope['jakarta.servlet.error.status_code']}"/>
<c:if test="${empty codigoError}">
    <c:set var="codigoError" value="500"/>
</c:if>
<c:if test="${empty titulo}">
    <c:choose>
        <c:when test="${codigoError eq 404}">
            <c:set var="titulo" value="Página no encontrada"/>
            <c:set var="mensaje" value="La página que buscas no existe o fue movida. Regresa al inicio para continuar."/>
        </c:when>
        <c:otherwise>
            <c:set var="titulo" value="Error del servidor"/>
            <c:set var="mensaje" value="Ocurrió un error inesperado. Intenta de nuevo en unos minutos."/>
        </c:otherwise>
    </c:choose>
</c:if>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>${codigoError} · ImcWeb</title>
    <%@ include file="/WEB-INF/includes/Bootstrap.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100">
<%@ include file="/WEB-INF/includes/Navbar.jsp" %>

<main class="flex-grow-1 d-flex align-items-center py-5">
    <div class="container text-center" style="max-width: 480px;">
        <p class="codigo-error text-primary mb-2">${codigoError}</p>
        <h1 class="h3 mb-3"><c:out value="${titulo}"/></h1>
        <p class="text-secondary mb-4"><c:out value="${mensaje}"/></p>
        <a href="${ctx}/" class="btn btn-primary">Volver al inicio</a>
    </div>
</main>

<%@ include file="/WEB-INF/includes/Footer.jsp" %>
<%@ include file="/WEB-INF/includes/BootstrapJS.jsp" %>
</body>
</html>
