## Paso 1: Entrega el código de la Aplicación Web
Especificar backend, frontend y base de datos.
- Jerarquia de archivos
- Puertos y redes
- Frameworks
- Tests JUnit

Contexto: "A partir de un caso planteado, deberás desarrollar una aplicación basándote en frameworks y protocolos, herramientas de análisis y pruebas unitarias, con enfoque en la seguridad y la mejora continua, todo esto desarrollados durante las tres experiencias de aprendizaje. El producto de esta evaluación se dividirá en dos partes. Para el desarrollo y cumplimiento de la parte I, deberás entregar un informe que aborde todos los aspectos mencionados anteriormente."

## Paso 2. OWASP 10 con ZAP
Realizamos un análisis en la aplicación web utilizando la herramienta ZAP, verificando la ausencia de vulnerabilidades clasificadas dentro del OWASP 10. Se debe adjuntar como evidencia los reportes generados por ZAP.
- Entregar los reportes de antes y despues
  
## Paso 3: SonarQube
Realizaremos un análisis con SonarQube mediante un contenedor con Jenkins, para poder detectar vulnerabilidades. 
Corregiremos al menos 2 vulnerabilidades en el código detectadas por SonarQube (mostrar before/after), y en caso de no detectar vulnerabilidades crearemos 2 para corregirlas después.
- Entregar el Dashboard + lista de vulnerabilidades
 
## Paso 4. SCA Dependency check
Analizaremos las dependencias y en Dependency-Check adjuntar el suppression.xml si alguna lib no se puede actualizar.
- Entregar reporte inicial y reporte corregido
- 
## Paso 5: Jacoco
Nos aseguraremos de seguir creando pruebas unitarias para aumentar el % de cobertura en lógica de negocio (Service/Controller)
Utilizaremos JUnit y MockMVC para poder realizar pruebas controladas sin levantar el servidor completo.
- Entregar reporte HTML antes y después
 
## Paso 6. OpenVAS
Analizaremos el servidor donde corre la app.
Realizaremos escaneo contra IP.
Buscaremos puertos inseguros, SSL débiles o software obsoleto.
- Entregar pdf de la evidencia del escaneo del servidor


## Paso 7: Entrega del informe
Cada paso debe ser documentado con capturas de pantalla y una breve descripción.

## Paso 8: Presentación
Preparar guión de presentacion


## Puntos a completar

1. Valida correctamente las vulnerabilidades detectadas con ZAP, corrigiendo todas las fallas OWASP Top10
2. Analiza el código correctamente mediante herramienta estática y no presenta hallazgos críticos
3. Analiza el código y no detecta hallazgos críticos con la herramienta de escaneo.
4. Analiza las dependencias con herramienta SCA y no presenta hallazgos críticos.
5. Ejecuta las pruebas unitarias correctamente y alcanza una cobertura igual o mayor al 60% 
6. Entrega un informe detallado que documenta todos los procesos y contiene todas las evidencias requeridas. 
7. Presenta un video claro y completo que resume el desarrollo del proyecto y los resultados obtenidos.