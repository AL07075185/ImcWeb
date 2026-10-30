<%--
    Página de bienvenida (web.xml). Redirige al inicio de sesión para que la
    primera petición pase por el DispatcherServlet de Spring.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% response.sendRedirect(request.getContextPath() + "/login"); %>
