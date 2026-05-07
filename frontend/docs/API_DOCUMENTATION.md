# Documentación de Frontend - Pet Adoption System

Documentación técnica del cliente web (frontend) que consume la API del backend.

## Información General

- **Aplicación**: SeguridadCalidad Frontend
- **Framework**: Spring Boot 3.x
- **Lenguaje**: Java + Thymeleaf (templates HTML)
- **Puerto**: 8080
- **URL Base**: `http://localhost:8080`
- **Backend conectado**: `http://localhost:8081`
- **Autenticación**: JWT (JWT Bearer Token en cookies)

---

## 📋 Tabla de Contenidos

1. [Arquitectura](#arquitectura)
2. [Autenticación y Seguridad](#autenticación-y-seguridad)
3. [Controllers Web](#controllers-web)
4. [Controllers REST](#controllers-rest)
5. [Servicios](#servicios)
6. [Modelos](#modelos)
7. [Configuración](#configuración)
8. [Flujo de Autenticación](#flujo-de-autenticación)

---

## Arquitectura

### Componentes Principales

```
Frontend (Spring Boot - 8080)
    ├── Controllers Web (Thymeleaf)
    │   ├── HomeController
    │   ├── LoginController
    │   ├── PetController
    │   ├── PatientController
    │   └── InvoicePageController
    │
    ├── Controllers REST (JSON)
    │   ├── AppointmentRestController
    │   ├── PatientRestController
    │   ├── PetRestController
    │   └── AuthController
    │
    ├── Servicios
    │   └── BackendService (comunica con backend)
    │
    ├── Seguridad
    │   ├── JwtAuthenticationFilter
    │   ├── JwtCookieService
    │   ├── JwtUtil
    │   └── WebSecurityConfig
    │
    └── Repositorios (Base de datos local)
        ├── PatientRepository
        └── AppointmentRepository
```

---

## Autenticación y Seguridad

### JWT en Cookies

El frontend utiliza **JWT en cookies seguras** para mantener la sesión del usuario.

**Configuración de Seguridad**:
```properties
app.security.jwt-cookie.secure=true          # Solo HTTPS en producción
app.security.jwt-cookie.max-age-seconds=3600 # 1 hora
app.security.jwt-cookie.same-site=Strict     # CSRF protection
```

### Componentes de Seguridad

#### JwtUtil
- Genera y valida tokens JWT
- Extrae información del token (username, claims)
- Gestiona expiración de tokens

#### JwtCookieService
- Almacena JWT en cookies seguras
- HttpOnly: no accesible desde JavaScript
- Secure: solo se envía por HTTPS en producción
- SameSite: protección contra CSRF

#### JwtAuthenticationFilter
- Intercepta todas las peticiones
- Extrae JWT de la cookie
- Valida y autentica el usuario
- Permite acceso público a `/login` y `/pets/**`

#### WebSecurityConfig
- Configura las rutas públicas y protegidas
- Habilita CORS
- Configura la cadena de filtros de seguridad

---

## Controllers Web

### HomeController
Controlador principal que maneja la página de inicio.

**Rutas**:
- `GET /` - Página principal

**Respuesta**: Retorna vista HTML `home.html`

---

### LoginController
Controlador de autenticación y login.

**Rutas**:

#### GET /login
Muestra el formulario de login.

**Respuesta**: Vista HTML `login.html`

#### POST /login
Autentica usuario y genera sesión con JWT.

**Request Body**:
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response** (302 Redirect):
- Redirige a `/` si login exitoso
- Redirige a `/login?error` si falla

**Headers de Respuesta**:
- `Set-Cookie`: Token JWT en cookie segura

---

#### GET /logout
Cierra la sesión eliminando la cookie JWT.

**Response**: Redirige a `/login`

---

### PetController
Controlador para gestión de mascotas (MVC).

**Rutas**:

#### GET /pets
Lista todas las mascotas disponibles.

**Template**: `pets/list.html`
**Respuesta**: Página HTML con tabla de mascotas

#### GET /pets/{id}
Detalle de una mascota específica.

**Path Parameters**:
- `id` (integer): ID de la mascota

**Template**: `pets/detail.html`
**Respuesta**: Página HTML con información de la mascota

#### GET /pets/create
Formulario para crear nueva mascota.

**Template**: `pets/form.html`
**Requerido**: Autenticación JWT

#### POST /pets
Crea una nueva mascota.

**Request Body** (Form data):
- `name`: Nombre de la mascota
- `species`: Especie
- `breed`: Raza
- `age`: Edad
- `gender`: Género (Macho/Hembra)
- `location`: Ubicación
- `photos`: URLs de fotos

**Requerido**: Autenticación JWT
**Response**: Redirige a `/pets`

#### GET /pets/{id}/edit
Formulario para editar mascota.

**Path Parameters**:
- `id` (integer): ID de la mascota

**Template**: `pets/form.html`
**Requerido**: Autenticación JWT

#### PUT /pets/{id}
Actualiza información de una mascota.

**Path Parameters**:
- `id` (integer): ID de la mascota

**Request Body** (Form data): Campos actualizables
**Requerido**: Autenticación JWT
**Response**: Redirige a `/pets/{id}`

#### DELETE /pets/{id}
Elimina una mascota.

**Path Parameters**:
- `id` (integer): ID de la mascota

**Requerido**: Autenticación JWT
**Response**: Redirige a `/pets`

---

### PatientController
Controlador para gestión de pacientes (MVC).

**Rutas**:

#### GET /patients
Lista todos los pacientes.

**Template**: `patients/list.html`
**Requerido**: Autenticación JWT
**Response**: Página HTML con tabla de pacientes

#### GET /patients/{id}
Detalle de un paciente específico.

**Path Parameters**:
- `id` (integer): ID del paciente

**Template**: `patients/detail.html`
**Requerido**: Autenticación JWT

#### GET /patients/create
Formulario para crear nuevo paciente.

**Template**: `patients/form.html`
**Requerido**: Autenticación JWT

#### POST /patients
Crea un nuevo paciente.

**Request Body** (Form data):
- `name`: Nombre del paciente
- `species`: Especie
- `breed`: Raza
- `age`: Edad
- `owner`: Dueño del paciente

**Requerido**: Autenticación JWT
**Response**: Redirige a `/patients`

---

### InvoicePageController
Controlador para gestión de facturas (MVC).

**Rutas**:

#### GET /invoices
Lista todas las facturas.

**Template**: `invoices/list.html`
**Requerido**: Autenticación JWT
**Response**: Página HTML con tabla de facturas

#### GET /invoices/{id}
Detalle de una factura específica.

**Path Parameters**:
- `id` (integer): ID de la factura

**Template**: `invoices/detail.html`
**Requerido**: Autenticación JWT

#### GET /invoices/appointment/{appointmentId}/create
Formulario para crear factura de visita.

**Path Parameters**:
- `appointmentId` (integer): ID de la visita

**Template**: `invoices/form.html`
**Requerido**: Autenticación JWT

---

## Controllers REST

### AuthController
API REST para autenticación.

**Rutas**:

#### POST /api/auth/login
Autentica usuario y retorna token JWT.

**Request Body**:
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "expiresIn": 3600
}
```

**Response** (401 Unauthorized):
```json
{
  "error": "Invalid credentials"
}
```

---

### AppointmentRestController
API REST para gestión de citas.

**Base Path**: `/api/appointments`

**Rutas**:

#### GET /api/appointments
Obtiene todas las citas.

**Requerido**: Autenticación JWT
**Response** (200 OK):
```json
[
  {
    "id": 1,
    "patientId": 1,
    "appointmentDate": "2026-05-15T10:30:00",
    "description": "Consulta general",
    "status": "scheduled"
  }
]
```

#### POST /api/appointments
Crea una nueva cita.

**Requerido**: Autenticación JWT
**Request Body**:
```json
{
  "patientId": 1,
  "appointmentDate": "2026-05-15T10:30:00",
  "description": "Consulta general"
}
```

**Response** (201 Created): Cita creada

#### GET /api/appointments/{id}
Obtiene cita por ID.

**Path Parameters**:
- `id` (integer): ID de la cita

**Requerido**: Autenticación JWT
**Response** (200 OK): Cita encontrada

#### PUT /api/appointments/{id}
Actualiza una cita.

**Path Parameters**:
- `id` (integer): ID de la cita

**Requerido**: Autenticación JWT
**Request Body**: Campos actualizables
**Response** (200 OK): Cita actualizada

#### DELETE /api/appointments/{id}
Elimina una cita.

**Path Parameters**:
- `id` (integer): ID de la cita

**Requerido**: Autenticación JWT
**Response** (204 No Content): Cita eliminada

---

### PatientRestController
API REST para pacientes.

**Base Path**: `/api/patients`

Similar al PatientController pero retorna JSON en lugar de HTML.

**Rutas**:
- `GET /api/patients` - Lista de pacientes
- `POST /api/patients` - Crear paciente
- `GET /api/patients/{id}` - Detalle del paciente
- `PUT /api/patients/{id}` - Actualizar paciente
- `DELETE /api/patients/{id}` - Eliminar paciente

---

### PetRestController
API REST para mascotas.

**Base Path**: `/api/pets`

Similar al PetController pero retorna JSON.

**Rutas**:
- `GET /api/pets` - Lista de mascotas
- `POST /api/pets` - Crear mascota
- `GET /api/pets/{id}` - Detalle de mascota
- `PUT /api/pets/{id}` - Actualizar mascota
- `DELETE /api/pets/{id}` - Eliminar mascota
- `GET /api/pets/search` - Buscar mascotas

---

## Servicios

### BackendService
Servicio que encapsula la comunicación con el backend.

**URL Base del Backend**: Configurable vía `backend.base-url`

**Métodos principales**:

#### Authentication
```java
// Login y obtener token
AuthResponse login(String username, String password)
```

#### Pets
```java
// Obtener todas las mascotas
List<Pet> getAllPets()

// Obtener mascota por ID
Pet getPetById(Long id)

// Crear nueva mascota
Pet createPet(Pet pet)

// Actualizar mascota
Pet updatePet(Long id, Pet pet)

// Eliminar mascota
void deletePet(Long id)

// Buscar mascotas
List<Pet> searchPets(String species, String gender, String location, Integer age)
```

#### Patients
```java
// Obtener todos los pacientes
List<Patient> getAllPatients()

// Obtener paciente por ID
Patient getPatientById(Long id)

// Crear nuevo paciente
Patient createPatient(Patient patient)

// Actualizar paciente
Patient updatePatient(Long id, Patient patient)

// Eliminar paciente
void deletePatient(Long id)
```

#### Invoices
```java
// Obtener todas las facturas
List<Invoice> getAllInvoices()

// Obtener factura por ID
Invoice getInvoiceById(Long id)

// Obtener factura por visita
Invoice getInvoiceByAppointmentId(Long appointmentId)

// Crear factura
Invoice createInvoice(Long appointmentId, InvoiceCreateRequest request)
```

---

## Modelos

### Pet
```java
class Pet {
    Long id;
    String name;
    String species;
    String breed;
    Integer age;
    String gender;        // Macho, Hembra
    String location;
    List<String> photos;
    String status;        // available, adopted, unavailable
}
```

### Patient
```java
class Patient {
    Long id;
    String name;
    String species;
    String breed;
    Integer age;
    String owner;
}
```

### Appointment
```java
class Appointment {
    Long id;
    Long patientId;
    LocalDateTime appointmentDate;
    String description;
    String status;        // scheduled, completed, cancelled
}
```

### Invoice
```java
class Invoice {
    Long id;
    Long appointmentId;
    LocalDate issueDate;
    Double subtotal;
    Double vatAmount;
    Double total;
    List<InvoiceLineItem> items;
    String notes;
}
```

### InvoiceLineItem
```java
class InvoiceLineItem {
    Long id;
    InvoiceLineItemType type;  // SERVICE, MEDICATION, ADDITIONAL_CHARGE
    String description;
    Integer quantity;
    Double unitPrice;
    Double lineTotal;
}
```

### AuthRequest
```java
class AuthRequest {
    String username;
    String password;
}
```

### AuthResponse
```java
class AuthResponse {
    String token;
    String username;
    Long expiresIn;
}
```

---

## Configuración

### application.properties

```properties
# Nombre de la aplicación
spring.application.name=seguridadcalidad

# URL del backend (configurable por variable de entorno)
backend.base-url=${BACKEND_URL:http://localhost:8081}

# Logging
logging.level.com.duoc.seguridadcalidad=INFO
logging.level.org.springframework.web=INFO
logging.level.org.springframework.http=INFO
logging.file.path=./logs

# JWT Cookie Configuration
app.security.jwt-cookie.secure=${JWT_COOKIE_SECURE:true}
app.security.jwt-cookie.max-age-seconds=${JWT_COOKIE_MAX_AGE_SECONDS:3600}
app.security.jwt-cookie.same-site=${JWT_COOKIE_SAME_SITE:Strict}

# Puerto del servidor
server.port=8080
```

### Variables de Entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| `BACKEND_URL` | `http://localhost:8081` | URL del backend |
| `JWT_COOKIE_SECURE` | `true` | Cookie solo por HTTPS |
| `JWT_COOKIE_MAX_AGE_SECONDS` | `3600` | Expiración en segundos (1 hora) |
| `JWT_COOKIE_SAME_SITE` | `Strict` | Protección CSRF |

---

## Flujo de Autenticación

### 1. Usuario accede a /login

```
Usuario → GET /login → Frontend → Muestra formulario
```

### 2. Usuario ingresa credenciales

```
Usuario → POST /login (credentials) → Frontend → BackendService
```

### 3. Frontend autentica con Backend

```
Frontend → POST /login → Backend → Backend retorna JWT
```

### 4. Frontend almacena JWT en cookie

```
JwtCookieService → Almacena token en cookie HttpOnly, Secure, SameSite
```

### 5. Usuario redirigido a inicio

```
Frontend → 302 Redirect → / → Usuario autenticado
```

### 6. Peticiones posteriores incluyen JWT

```
Usuario → GET /pets → JwtAuthenticationFilter → Extrae JWT de cookie
                    → Valida token
                    → Continúa si es válido
```

### 7. Logout

```
Usuario → GET /logout → Frontend → Elimina cookie JWT → Redirige a /login
```

---

## Rutas Públicas vs Protegidas

### Rutas Públicas (sin autenticación)
- `GET /login` - Formulario de login
- `POST /login` - Procesamiento de login
- `GET /pets` - Lista pública de mascotas
- `GET /pets/{id}` - Detalle público de mascota
- `GET /pets/search` - Búsqueda pública de mascotas
- `GET /` - Página de inicio

### Rutas Protegidas (requieren JWT)
- `GET /logout` - Cerrar sesión
- `POST /pets` - Crear mascota
- `PUT /pets/{id}` - Actualizar mascota
- `DELETE /pets/{id}` - Eliminar mascota
- `GET /patients` - Ver pacientes
- `POST /patients` - Crear paciente
- `GET /patients/{id}` - Ver detalle de paciente
- `PUT /patients/{id}` - Actualizar paciente
- `DELETE /patients/{id}` - Eliminar paciente
- `GET /invoices` - Ver facturas
- `POST /invoices` - Crear factura
- `GET /api/**` - Todos los endpoints REST (excepto /api/auth/login)

---

## Flujo de Comunicación Frontend-Backend

```
┌─────────────────────────────────────────────────────────────┐
│                        Usuario Browser                       │
└────────────────────────────┬────────────────────────────────┘
                             │ (HTTP/HTTPS)
                             │
┌────────────────────────────▼────────────────────────────────┐
│            Frontend (Spring Boot - Puerto 8080)             │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │         Thymeleaf + REST Controllers                │   │
│  │                                                     │   │
│  │  - HomeController                                  │   │
│  │  - LoginController                                 │   │
│  │  - PetController / PetRestController              │   │
│  │  - PatientController / PatientRestController      │   │
│  │  - InvoicePageController                          │   │
│  └────────────────────┬────────────────────────────────┘   │
│                       │                                      │
│  ┌────────────────────▼────────────────────────────────┐   │
│  │       BackendService (HTTP Client)                  │   │
│  │                                                     │   │
│  │  - Encapsula comunicación con backend              │   │
│  │  - Maneja JWT en headers                           │   │
│  │  - Gestiona errores y reintentos                  │   │
│  └────────────────────┬────────────────────────────────┘   │
│                       │ (HTTP/REST)                         │
└───────────────────────┼─────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────────┐
│            Backend (Spring Boot - Puerto 8081)              │
│                                                              │
│  - REST API Endpoints                                        │
│  - JWT Validation                                            │
│  - Business Logic                                            │
│  - Database                                                  │
└────────────────────────────────────────────────────────────┘
```

---

## Notas de Desarrollo

### Manejo de JWT
- El JWT se almacena en una **cookie HttpOnly** (no accesible desde JavaScript)
- Se envía automáticamente en cada petición
- El backend valida el JWT antes de procesar la solicitud

### CORS
- Frontend y Backend están en puertos diferentes (8080 y 8081)
- CORS debe estar habilitado en el backend
- Las cookies se envían automáticamente por el navegador

### Seguridad
- Las contraseñas se envían al backend (que las valida)
- El JWT tiene expiración configurable (default: 1 hora)
- Las cookies son `HttpOnly` (protección contra XSS)
- Las cookies son `Secure` en producción (solo HTTPS)
- Las cookies tienen `SameSite=Strict` (protección contra CSRF)

---

## Ejemplos de Uso

### Obtener lista de mascotas (público)
```bash
curl http://localhost:8080/pets
```

### Crear mascota (requiere autenticación)
```bash
# 1. Login y obtener JWT
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# 2. Crear mascota (el JWT se envía automáticamente en cookies)
curl -X POST http://localhost:8080/api/pets \
  -H "Content-Type: application/json" \
  -H "Cookie: jwt=<token>" \
  -d '{
    "name": "Max",
    "species": "Perro",
    "breed": "Golden Retriever",
    "age": 3,
    "gender": "Macho",
    "location": "Santiago"
  }'
```

---

Generado para: DUOC UC - CDY2203
Versión: 1.0.0
Fecha: 2026-05-06
