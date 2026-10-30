# ImcWeb — Calculadora de Índice de Masa Corporal

Aplicación web en Java (Spring Framework + GlassFish + MySQL) para calcular y monitorear el Índice de Masa Corporal.

Proyecto de la Evidencia del curso **Computación Avanzada en Java** — Universidad Tecmilenio.

| Dato | Valor |
|---|---|
| Autor | AL07075185 |
| Estado | **Entrega final** · versión estable `v1.0.0` |
| Rama estable | `master` (integración en `develop`) |
| Documentación | [Wiki del proyecto](https://github.com/AL07075185/ImcWeb/wiki) |
| Prototipo navegable | [Abrir en Figma](https://www.figma.com/proto/Sz7yUP2wbm8Poz5xtyAutd/ImcWeb?node-id=3-6&starting-point-node-id=3%3A6) · [Archivo de diseño](https://www.figma.com/design/Sz7yUP2wbm8Poz5xtyAutd) |

## Descripción

ImcWeb permite que una persona se registre una sola vez (con su estatura), inicie sesión, capture su peso para calcular su IMC y consulte su IMC actual y su historial, obtenido mediante un servicio web REST con representaciones JSON y XML.

## Requerimientos principales

- Registro único con validación de estatura (1.00 – 2.50 m) y edad mínima de 15 años.
- Inicio de sesión obligatorio para calcular y consultar el historial.
- Captura del peso (mayor a 0) y cálculo del IMC (peso ÷ estatura²) con clasificación de la OMS y fecha de cada medición.
- Historial consultado desde la base de datos con un servicio REST (JSON y XML) con enlaces de hipermedia.
- Manejo de excepciones en ambas representaciones.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Framework | Spring Framework 7.0.10 (Spring Web MVC + Spring JDBC), JDK 21 |
| Servidor | GlassFish 8 (Jakarta EE 11) |
| IDE / construcción | NetBeans 31, Maven (WAR) |
| Base de datos | MySQL 8.0 (JDBC, recurso JNDI `jdbc/imcDb` o conexión directa) |
| JSON y XML | Jackson 3 |
| Interfaz | JSP + JSTL, Bootstrap 5.3.8 |
| Pruebas | JUnit 5 |
| Arquitectura | MVC + Controller-Service-Repository |

## Estructura del repositorio

```text
ImcWeb/
├── pom.xml                   dependencias, plugins y propiedades de Maven
├── database/imcDb.sql        creación de la base de datos y datos de prueba
├── src/main/java/mx/webapp/imc/
│   ├── controlador/          controladores web, interceptor y manejo de errores
│   ├── controlador/rest/     servicio REST (usuarios, mediciones, clasificaciones)
│   └── modelo/               entidad · dto · servicio · repositorio
├── src/main/resources/       bd.properties (datos de conexión)
├── src/main/webapp/          vistas JSP, Bootstrap, estilos e historial.js
├── src/test/java/            pruebas unitarias
├── docs/diagramas/           diagramas de clases y entidad-relación actualizados
├── Diagramas - Evidencia/    Avance 1: casos de uso, clases y navegación
├── Modelado - DB/            Avance 1: modelado de la base de datos
└── Prototipo_ImcWeb_Figma/   Avance 1: pantallas del prototipo
```

## Diseño

- **Entrega final:** [diagramas actualizados](docs/diagramas) (controlador web, REST, excepciones, modelo, DTO y entidad-relación).
- **Avance 1:** [Diagramas - Evidencia](Diagramas%20-%20Evidencia) · [Modelado - DB](Modelado%20-%20DB) · [Prototipo_ImcWeb_Figma](Prototipo_ImcWeb_Figma).

## Puesta en marcha

1. **Base de datos.** En MySQL Workbench: **File › Open SQL Script** › `database/imcDb.sql` › ejecutar con ⚡. O desde la terminal:
   ```bash
   mysql -u root -p < database/imcDb.sql
   ```
2. **Proyecto.** En NetBeans: **File › Open Project** › carpeta `ImcWeb`. Servidor GlassFish 8 y *Context Path* `/ImcWeb`.
3. **Ejecutar.** *Clean and Build* y *Run*. Abrir <http://localhost:8080/ImcWeb/> e iniciar sesión con `jperez` / `Imc2026!`.

La conexión se configura en `src/main/resources/bd.properties`. La guía completa está en la [Wiki](https://github.com/AL07075185/ImcWeb/wiki).

## Servicio REST

JSON por defecto; XML con `Accept: application/xml` o `?formato=xml`.

| Recurso | Métodos |
|---|---|
| `/api/usuarios/{idUsuario}` | GET |
| `/api/usuarios/{idUsuario}/mediciones` | GET, POST |
| `/api/usuarios/{idUsuario}/mediciones/actual` | GET |
| `/api/usuarios/{idUsuario}/mediciones/{idMedicion}` | GET, DELETE |
| `/api/clasificaciones` | GET |

## Estrategia de ramas

| Rama | Uso |
|---|---|
| `master` | Versión estable con el tag `v1.0.0`. |
| `develop` | Integración; contiene el Avance 1 y todos los commits del desarrollo. |
| `feature/usuarios` | Registro, inicio y cierre de sesión, recurso REST de usuarios. |
| `feature/mediciones` | Cálculo del IMC, historial y recurso REST de mediciones. |
| `feature/clasificaciones` | Catálogo de la OMS y recurso REST de clasificaciones. |

---
Universidad Tecmilenio · Computación Avanzada en Java · 2026
