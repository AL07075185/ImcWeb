<%-- RF_0014: aviso para usuarios de 15 a 17 años --%>
<c:if test="${sessionScope.usuarioSesion.edad lt 18}">
    <div class="alert alert-warning d-flex gap-2 small" role="alert">
        <i class="bi bi-exclamation-triangle"></i>
        <div>Tienes ${sessionScope.usuarioSesion.edad} años: esta clasificación corresponde a adultos. Para menores de 18 años
            la OMS evalúa el IMC con tablas por edad y sexo.</div>
    </div>
</c:if>
