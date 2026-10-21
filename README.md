# ImcWeb — Calculadora de Índice de Masa Corporal

Aplicación web en Java (Jakarta EE + GlassFish + MySQL) para calcular y monitorear el Índice de Masa Corporal.

Proyecto de la Evidencia del curso **Computación Avanzada en Java** — Universidad Tecmilenio.

| Dato | Valor |
|---|---|
| Autor | AL07075185 |
| Estado | **Avance 1: análisis y diseño** |
| Rama de trabajo | `develop` |
| Prototipo navegable | [Abrir en Figma](https://www.figma.com/proto/Sz7yUP2wbm8Poz5xtyAutd/ImcWeb?node-id=3-6&starting-point-node-id=3%3A6) · [Archivo de diseño](https://www.figma.com/design/Sz7yUP2wbm8Poz5xtyAutd) |

## Descripción

ImcWeb permite que una persona se registre una sola vez (con su estatura), inicie sesión, capture su peso para calcular su IMC y consulte su IMC actual y su historial, obtenido mediante un servicio web REST con representaciones JSON y XML.

## Requerimientos principales

- Registro único con validación de estatura (1.00 – 2.50 m) y edad mínima de 15 años.
- Inicio de sesión obligatorio para calcular y consultar el historial.
- Cálculo del IMC (peso ÷ estatura²) con clasificación de la OMS y fecha de cada medición.
- Historial consultado desde la base de datos con un servicio REST (JSON y XML) con enlaces de hipermedia.
- Manejo de excepciones en ambas representaciones.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Plataforma | Jakarta EE (Servlets, JSP + JSTL, JAX-RS), JDK 21 |
| Servidor | GlassFish 8 |
| IDE / construcción | NetBeans, Maven (WAR) |
| Base de datos | MySQL 8.4 (JDBC, recurso JNDI `jdbc/imcDb`) |
| Interfaz | Bootstrap 5.3 |
| Arquitectura | MVC + Controller-Service-Repository |

## Estructura del repositorio

```text
ImcWeb/
├── .gitignore
├── Diagramas - Evidencia/
│   ├── casosUso.png
│   ├── clasesControladorModelo.png
│   ├── clasesDto.png
│   ├── clasesEntidades.png
│   ├── clasesRepositorio.png
│   ├── clasesRest.png
│   ├── clasesServicioMedicion.png
│   ├── clasesServicioUsuario.png
│   ├── clasesServlets.png
│   ├── clasesVistaControlador.png
│   ├── diagramaEr.png
│   ├── flujoNavegacion.png
├── Modelado - DB/
│   ├── BD-EER.png
│   ├── imcdb.png
│   ├── imcdb.sql
├── Prototipo_ImcWeb_Figma/
│   ├── P_0001_Credenciales_invalidas.png
│   ├── P_0001_Inicio_de_sesion.png
│   ├── P_0002_Registro.png
│   ├── P_0002_Validacion_de_datos.png
│   ├── P_0003_Captura_de_peso.png
│   ├── P_0003_Peso_invalido.png
│   ├── P_0004_IMC_actual_e_historial.png
│   ├── P_0005_Pagina_de_error.png
├── README.md
```

## Contenido

**Diagramas - Evidencia/**

- [casosUso.png](Diagramas%20-%20Evidencia/casosUso.png)
- [clasesControladorModelo.png](Diagramas%20-%20Evidencia/clasesControladorModelo.png)
- [clasesDto.png](Diagramas%20-%20Evidencia/clasesDto.png)
- [clasesEntidades.png](Diagramas%20-%20Evidencia/clasesEntidades.png)
- [clasesRepositorio.png](Diagramas%20-%20Evidencia/clasesRepositorio.png)
- [clasesRest.png](Diagramas%20-%20Evidencia/clasesRest.png)
- [clasesServicioMedicion.png](Diagramas%20-%20Evidencia/clasesServicioMedicion.png)
- [clasesServicioUsuario.png](Diagramas%20-%20Evidencia/clasesServicioUsuario.png)
- [clasesServlets.png](Diagramas%20-%20Evidencia/clasesServlets.png)
- [clasesVistaControlador.png](Diagramas%20-%20Evidencia/clasesVistaControlador.png)
- [diagramaEr.png](Diagramas%20-%20Evidencia/diagramaEr.png)
- [flujoNavegacion.png](Diagramas%20-%20Evidencia/flujoNavegacion.png)

**Modelado - DB/**

- [BD-EER.png](Modelado%20-%20DB/BD-EER.png)
- [imcdb.png](Modelado%20-%20DB/imcdb.png)
- [imcdb.sql](Modelado%20-%20DB/imcdb.sql)

**Prototipo_ImcWeb_Figma/**

- [P_0001_Credenciales_invalidas.png](Prototipo_ImcWeb_Figma/P_0001_Credenciales_invalidas.png)
- [P_0001_Inicio_de_sesion.png](Prototipo_ImcWeb_Figma/P_0001_Inicio_de_sesion.png)
- [P_0002_Registro.png](Prototipo_ImcWeb_Figma/P_0002_Registro.png)
- [P_0002_Validacion_de_datos.png](Prototipo_ImcWeb_Figma/P_0002_Validacion_de_datos.png)
- [P_0003_Captura_de_peso.png](Prototipo_ImcWeb_Figma/P_0003_Captura_de_peso.png)
- [P_0003_Peso_invalido.png](Prototipo_ImcWeb_Figma/P_0003_Peso_invalido.png)
- [P_0004_IMC_actual_e_historial.png](Prototipo_ImcWeb_Figma/P_0004_IMC_actual_e_historial.png)
- [P_0005_Pagina_de_error.png](Prototipo_ImcWeb_Figma/P_0005_Pagina_de_error.png)

## Cómo crear la base de datos

```bash
mysql -u root -p < "Modelado - DB/imcdb.sql"
```

O en MySQL Workbench: **File › Open SQL Script** › `Modelado - DB/imcdb.sql` › ejecutar con ⚡.

## Estrategia de ramas

| Rama | Uso |
|---|---|
| `master` | Versión estable (tag `v1.0.0` al finalizar). |
| `develop` | Integración; contiene este avance. |
| `feature/usuario` | Registro, inicio y cierre de sesión. |
| `feature/medicion` | Cálculo y guardado del IMC. |
| `feature/historial` | Servicio REST JSON/XML, hipermedia y excepciones. |

---
Universidad Tecmilenio · Computación Avanzada en Java · 2026
