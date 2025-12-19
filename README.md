# User Register API 🚀

Esta aplicación es una API RESTful desarrollada con Spring Boot para la gestión y registro de usuarios. Fue diseñada siguiendo una arquitectura en capas, buenas practicas de desarrollo (SOLID), y cumple con requisitos estrictos de validación y formato de respuesta segun el desafio establecido.

## 📋 Tabla de Contenidos
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Uso de la API](#-uso-de-la-api)
- [Configuración (Regex)](#-configuración)
- [Base de Datos (H2)](#-base-de-datos-h2)
- [Tests](#-tests)
- [Arquitectura y Diseño](#-arquitectura-y-diseño)

## 🛠 Tecnologías

* **Java 17** (Compatible con Java 8+)
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **H2 Database** (Base de datos en memoria)
* **Maven** (Gestor de dependencias)
* **Lombok** (Reducción de boilerplate)
* **JUnit 5 & Mockito** (Pruebas unitarias)
* **Spring Security** (Configuración básica de rutas)

## ⚙️ Requisitos Previos

* JDK 11 o superior instalado.
* Maven instalado (o usar el wrapper `mvnw` incluido).
* Git.

## 🚀 Instalación y Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/hernanprovoste/user_register_bci](https://github.com/hernanprovoste/user_register_bci)
    cd user_register_bci
    ```

2.  **Compilar y construir el proyecto:**
    ```bash
    mvn clean install
    ```

3.  **Ejecutar la aplicación:**
    ```bash
    mvn spring-boot:run
    ```

La aplicación iniciará en `http://localhost:8080`.

## 🔧 Configuración

Las reglas de validación son configurables desde el archivo `src/main/resources/application.properties.`

Validación de Contraseña (Regex)
Actualmente configurada para requerir: **1 Mayúscula, 1 Número, Mínimo 8 caracteres.**

Puedes cambiar esta expresión regular sin recompilar el código Java

```bash
    user.password.regex=^(?=.*[A-Z])(?=.*[0-9]).{8,}$
```

## 💾 Base de Datos (H2)

La aplicación utiliza una base de datos en memoria H2. Los datos se reinician cada vez que se detiene la aplicación.

Consola Web H2: http://localhost:8080/h2-console

* **JDBC URL:** `jdbc:h2:mem:userdb`
* **User:** `sa`
* **Password:** `(dejar vacío)`

Nota: El script de creación de tablas SQL se encuentra disponible en 
```src/main/resources/schema.sql``` (opcional, ya que Hibernate genera el esquema automáticamente).

## 🧪 Tests

El proyecto incluye pruebas unitarias utilizando JUnit 5 y Mockito para asegurar la lógica de negocio en la capa de servicios.

Para ejecutar las pruebas:
```bash
  mvn test
```

## 📡 Uso de la API

La API expone endpoints que aceptan y retornan exclusivamente JSON.

### 1. Registrar Usuario
Crea un nuevo usuario en el sistema. Valida formato de correo, contraseña y duplicidad de email.

* **URL:** `/api/users/register`
* **Método:** `POST`
* **Content-Type:** `application/json`

**Ejemplo de Request (Body):**

```json
{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Hunter2",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "contrycode": "57"
        },
        {
            "number": "9876543",
            "citycode": "2",
            "contrycode": "56"
        }
    ]
}
```

**Ejemplo de Response Exitoso:**

```json
{
  "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "created": "2025-12-18T20:30:00.123456",
  "modified": "2025-12-18T20:30:00.123456",
  "lastLogin": "2025-12-18T20:30:00.123456",
  "token": "456e4567-e89b-12d3-a456-426614174000",
  "isactive": true
}
```

**Ejemplo de Manejo de Error si el Email Existe:**

```json
{
  "mensaje": "El correo ya registrado"
}
```


## Diagrama de Flujo (Sequence Diagram)

```mermaid
sequenceDiagram
    participant Cliente as Postman/Frontend
    participant Controller as UserController
    participant Service as UserService
    participant Repo as UserRepository
    participant DB as H2 Database
    participant Handler as GlobalExceptionHandler

    Cliente->>Controller: POST /api/users/register (JSON)
    Controller->>Service: registerUser(UserRequestDTO)
    
    Service->>Repo: findByEmail(email)
    Repo-->>Service: (Empty Optional)
    
    alt Email ya existe
        Service-->>Handler: THROW RuntimeException
        Note over Handler: @ExceptionHandler<br/>captura el error
        Handler-->>Cliente: 400 Bad Request {"mensaje": "..."}
    else Email valido
        Service->>Service: Validar Regex (Password/Email)
        Service->>Service: Convertir DTO -> Entity
        Service->>Service: Generar Token UUID
        
        Service->>Repo: save(User)
        Repo->>DB: INSERT INTO users...
        DB-->>Repo: User (con ID)
        Repo-->>Service: User Created
        
        Service->>Service: Convertir Entity -> ResponseDTO
        Service-->>Controller: UserResponseDTO
        Controller-->>Cliente: 201 Created (JSON sin password)
    end