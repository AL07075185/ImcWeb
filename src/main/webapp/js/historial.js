/*
 * historial.js · Consume el servicio REST del historial (CU_0006) y llena la
 * vista historial.jsp: IMC actual y tabla de mediciones.
 * GET {data-api} con Accept: application/json y la cookie de sesión.
 */
(function () {
    'use strict';

    var contenedor = document.getElementById('historial');
    if (!contenedor) {
        return;
    }

    function $(id) {
        return document.getElementById(id);
    }

    function mostrar(id, visible) {
        $(id).classList.toggle('d-none', !visible);
    }

    function dosDecimales(valor) {
        return Number(valor).toFixed(2);
    }

    /* "2026-10-01T08:20:00" -> "01/10/2026 08:20" (RNF_0012) */
    function formatearFecha(iso) {
        var partes = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})/.exec(iso || '');
        return partes ? partes[3] + '/' + partes[2] + '/' + partes[1] + ' ' + partes[4] + ':' + partes[5] : '';
    }

    function crearInsignia(clasificacion) {
        var insignia = document.createElement('span');
        insignia.className = 'badge rounded-pill imc-' + clasificacion.idClasificacion;
        insignia.textContent = clasificacion.nombre;
        return insignia;
    }

    function crearCelda(texto, clase) {
        var celda = document.createElement('td');
        celda.textContent = texto;
        if (clase) {
            celda.className = clase;
        }
        return celda;
    }

    function mostrarActual(medicion) {
        $('imcActual').textContent = dosDecimales(medicion.imc);
        var insignia = $('clasificacionActual');
        insignia.className = 'badge rounded-pill mt-2 imc-' + medicion.clasificacion.idClasificacion;
        insignia.textContent = medicion.clasificacion.nombre;
        $('pesoActual').textContent = dosDecimales(medicion.peso) + ' kg';
        $('fechaActual').textContent = formatearFecha(medicion.fechaMedicion);
        mostrar('actual', true);
    }

    function mostrarTabla(mediciones) {
        var cuerpo = $('tablaHistorial');
        cuerpo.textContent = '';
        if (mediciones.length === 0) {
            var fila = document.createElement('tr');
            var celda = crearCelda('Aún no tienes mediciones registradas.', 'text-center text-secondary py-4');
            celda.colSpan = 4;
            fila.appendChild(celda);
            cuerpo.appendChild(fila);
            return;
        }
        mediciones.forEach(function (medicion) {
            var fila = document.createElement('tr');
            fila.appendChild(crearCelda(formatearFecha(medicion.fechaMedicion)));
            fila.appendChild(crearCelda(dosDecimales(medicion.peso)));
            fila.appendChild(crearCelda(dosDecimales(medicion.imc), 'fw-semibold'));
            var celdaClasificacion = document.createElement('td');
            celdaClasificacion.appendChild(crearInsignia(medicion.clasificacion));
            fila.appendChild(celdaClasificacion);
            cuerpo.appendChild(fila);
        });
    }

    function mostrarRespuesta(respuesta) {
        mostrar('cargando', false);
        var total = respuesta.totalMediciones;
        $('totalMediciones').textContent = total + (total === 1 ? ' medición' : ' mediciones');
        if (respuesta.imcActual) {
            mostrarActual(respuesta.imcActual);
        } else {
            mostrar('sinMediciones', true);
        }
        mostrarTabla(respuesta.mediciones || []);
    }

    function mostrarError(error) {
        mostrar('cargando', false);
        var mensaje = $('mensajeError');
        mensaje.textContent = 'No fue posible obtener el historial: ' +
            (error && error.mensaje ? error.mensaje : 'intenta de nuevo más tarde.');
        mostrar('mensajeError', true);
        $('tablaHistorial').textContent = '';
        if (error && error.codigo === 401) {
            window.location.href = contenedor.dataset.api.replace(/\/api\/.*$/, '/login?sesion=expirada');
        }
    }

    fetch(contenedor.dataset.api, {
        headers: {'Accept': 'application/json'},
        credentials: 'same-origin'
    })
        .then(function (respuesta) {
            return respuesta.json().catch(function () {
                return {codigo: respuesta.status};
            }).then(function (cuerpo) {
                if (!respuesta.ok) {
                    throw cuerpo;
                }
                return cuerpo;
            });
        })
        .then(mostrarRespuesta)
        .catch(mostrarError);
})();
