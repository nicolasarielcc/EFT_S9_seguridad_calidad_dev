# Reporte Inicial de Vulnerabilidades ZAP

Este documento resume los hallazgos de seguridad detectados por OWASP ZAP en los escaneos iniciales del **frontend** y **backend** de la aplicación.

---

## 1. Frontend (`localhost:8080`)

### Vulnerabilidades Detectadas

#### 1.1 CSP: Failure to Define Directive with No Fallback
- **Código:** ZAP-CSP
- **Riesgo:** Medio
- **OWASP Top 10:** A05:2021 - Security Misconfiguration
- **Descripción:** No se definieron directivas CSP (Content Security Policy) adecuadas, lo que puede permitir la ejecución de scripts maliciosos (XSS) si un atacante logra inyectar código.
- **Impacto:** Permite ataques de Cross-Site Scripting (XSS) y robo de información del usuario.
- **Recomendación:** Definir una política CSP estricta en los encabezados HTTP.

#### 1.2 GET para POST
- **Código:** ZAP-GET-POST
- **Riesgo:** Informativo
- **OWASP Top 10:** A01:2021 - Broken Access Control (potencial)
- **Descripción:** Se detectaron endpoints que aceptan métodos GET donde deberían aceptar solo POST, lo que puede llevar a exposición accidental de datos o acciones no intencionadas.
- **Impacto:** Puede permitir que usuarios no autorizados realicen acciones sensibles.
- **Recomendación:** Validar y restringir los métodos HTTP permitidos en cada endpoint.

#### 1.3 Petición de Autenticación Identificada
- **Código:** ZAP-AUTH-REQ
- **Riesgo:** Informativo
- **OWASP Top 10:** A07:2021 - Identification and Authentication Failures
- **Descripción:** Se identificaron peticiones relacionadas con autenticación. No es una vulnerabilidad por sí misma, pero requiere revisión para asegurar que no haya fugas de información.
- **Recomendación:** Revisar los flujos de autenticación y asegurar que no se exponga información sensible.

#### 1.4 User Agent Fuzzer
- **Código:** ZAP-UA-FUZZ
- **Riesgo:** Informativo
- **OWASP Top 10:** A05:2021 - Security Misconfiguration
- **Descripción:** Se detectaron respuestas diferentes ante cambios en el User-Agent, lo que puede indicar rutas de código no protegidas o comportamientos inesperados.
- **Recomendación:** Unificar el manejo de User-Agent y validar que no existan rutas inseguras.

---

## 2. Backend (`localhost:8081`)

### Vulnerabilidades Detectadas

#### 2.1 Cookie sin el atributo SameSite
- **Código:** ZAP-COOKIE-SAMESITE
- **Riesgo:** Bajo
- **OWASP Top 10:** A07:2021 - Identification and Authentication Failures
- **Descripción:** Se detectaron cookies sin el atributo `SameSite`, lo que puede permitir ataques de CSRF (Cross-Site Request Forgery).
- **Impacto:** Un atacante podría realizar acciones en nombre del usuario autenticado.
- **Recomendación:** Configurar todas las cookies con el atributo `SameSite=Strict` o `Lax`.

#### 2.2 Petición de Autenticación Identificada
- **Código:** ZAP-AUTH-REQ
- **Riesgo:** Informativo
- **OWASP Top 10:** A07:2021 - Identification and Authentication Failures
- **Descripción:** Se identificaron peticiones relacionadas con autenticación. Requiere revisión para asegurar que no haya fugas de información.
- **Recomendación:** Revisar los flujos de autenticación y asegurar que no se exponga información sensible.

#### 2.3 Respuesta de Gestión de Sesión Identificada
- **Código:** ZAP-SESSION-MGMT
- **Riesgo:** Informativo
- **OWASP Top 10:** A07:2021 - Identification and Authentication Failures
- **Descripción:** Se detectaron respuestas relacionadas con la gestión de sesión. No es una vulnerabilidad directa, pero requiere revisión.
- **Recomendación:** Validar que la gestión de sesión sea segura y no exponga tokens o identificadores sensibles.

---

## Referencias
- [OWASP Top 10 - 2021](https://owasp.org/Top10/)
- [OWASP ZAP Project](https://www.zaproxy.org/)

---

**Nota:** Las vulnerabilidades informativas requieren revisión manual para determinar si representan un riesgo real en el contexto de la aplicación.
