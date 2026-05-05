# Aerocheck – Práctica XML (SAX, DOM, XPath, Excepciones, JUnit)

## Descripción
Proyecto de consola Java (Maven) que procesa un fichero XML de telemetría de inspecciones con drones.

Incluye:
- **SAX** (modo resumen): lectura secuencial sin cargar todo el XML en memoria.
- **DOM + XPath** (modo análisis): carga del XML como árbol (`Document`) y consultas con expresiones XPath.
- **Tratamiento de excepciones** típicas del parsing/consultas.
- **JUnit 4**: pruebas unitarias con `@Before`, `@After` y `@Test`.

## Estructura del proyecto
- `src/main/java/es/medac/soporte/aerocheck/`
  - `TelemetrySaxSummary.java` → resumen con SAX
  - `TelemetryDomLoader.java` → carga DOM del XML (desde resources)
  - `TelemetryXPathQueries.java` → consultas XPath
  - `TelemetryAnalysisApp.java` → ejecutable de análisis (DOM + XPath)
- `src/main/resources/`
  - `telemetria.xml` → XML de telemetría
- `src/test/java/es/medac/soporte/aerocheck/`
  - `TelemetryXPathQueriesTest.java` → tests JUnit 4

## Requisitos
- Java 21
- Maven
- NetBeans (opcional, para ejecutar cómodamente)

## Ejecución (modo resumen – SAX)
Ejecuta la clase:
- `es.medac.soporte.aerocheck.TelemetrySaxSummary`

Salida esperada (aprox.):
- Total drones: 2
- Total vuelos: 2
- Total eventos: 7
- Total alertas: 2
- Batería mínima: 19

## Ejecución (modo análisis – DOM + XPath)
Ejecuta la clase:
- `es.medac.soporte.aerocheck.TelemetryAnalysisApp`

Salida esperada (aprox.):
- Lista de alertas (timestamp + código)
- Drones con batería < 20 → `[DR-07]`
- Último GPS por drone

## Tests (JUnit 4)
Ejecuta los tests con Maven:
```bash
mvn test

