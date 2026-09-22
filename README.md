# Prestamo Service - Loan Management Microservice

A Spring Boot microservice for managing loan applications and portfolio discounts for Banco Pascual Bravo.

## Features

- **Loan Application Processing**: Submit loan requests with client ID, amount, and client type
- **Automated Approval Logic**: Loans up to $50,000 are automatically approved
- **Interest Rate Calculation**: Different rates based on client type (Premium: 3.5%, Regular: 5.0%, Standard: 6.0%)
- **Portfolio Discount Calculation**: Calculate discounts for approved loan portfolios
- **H2 In-Memory Database**: For development and testing
- **H2 Console**: Accessible at `/h2-console` for database inspection

## Tech Stack

- Java 17
- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- JaCoCo for code coverage

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Running the Application

```bash
# Clone the repository
git clone <repository-url>
cd prestamo-service

# Build and run
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Accessing H2 Console

Navigate to `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:pascualbravodb`
- Username: `sa`
- Password: (empty)

## API Endpoints

### Submit Loan Application
```
POST /api/prestamos/solicitar
```

**Parameters:**
- `clienteId` (Long, required) - Client ID
- `monto` (Double, required) - Loan amount
- `tipoCliente` (String, required) - Client type: `PREMIUM`, `REGULAR`, or other

**Response:**
```json
{
  "id": 1,
  "clienteId": 123,
  "monto": 25000.0,
  "tasaInteres": 3.5,
  "estado": "APROBADO"
}
```

### Calculate Portfolio Discounts
```
POST /api/prestamos/descuentos
```

**Request Body:**
```json
[
  {"id": 1, "clienteId": 123, "monto": 15000.0, "tasaInteres": 3.5, "estado": "APROBADO"},
  {"id": 2, "clienteId": 456, "monto": 8000.0, "tasaInteres": 5.0, "estado": "APROBADO"}
]
```

**Response:**
```json
250.0
```

**Discount Rules:**
- Loans > $10,000: 5% discount
- Loans > $5,000: 2% discount
- Loans ≤ $5,000: No discount
- Only approved loans are considered

## Project Structure

```
src/main/java/co/edu/pascualbravo/banco/
├── BancoApplication.java          # Main application entry point
├── controller/
│   └── PrestamoController.java    # REST endpoints
├── model/
│   └── Prestamo.java              # Loan entity
├── repository/
│   └── PrestamoRepository.java    # Data access layer
└── service/
    └── PrestamoService.java       # Business logic
```

## Testing

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw test jacoco:report
```

Coverage report available at `target/site/jacoco/index.html`

## Business Rules

### Loan Approval
- Maximum approved amount: $50,000
- Amounts exceeding $50,000 are REJECTED with 0% interest rate
- Validates: clientId not null, amount > 0

### Interest Rates by Client Type
| Client Type | Interest Rate |
|-------------|---------------|
| PREMIUM     | 3.5%          |
| REGULAR     | 5.0%          |
| Standard    | 6.0%          |

### Portfolio Discounts
| Loan Amount | Discount Rate |
|-------------|---------------|
| > $10,000   | 5%            |
| > $5,000    | 2%            |
| ≤ $5,000    | 0%            |

---

# Servicio de Préstamos - Microservicio de Gestión de Préstamos

Un microservicio Spring Boot para gestionar solicitudes de préstamos y descuentos de cartera para el Banco Pascual Bravo.

## Características

- **Procesamiento de Solicitudes de Préstamo**: Envía solicitudes con ID de cliente, monto y tipo de cliente
- **Lógica de Aprobación Automatizada**: Préstamos hasta $50,000 se aprueban automáticamente
- **Cálculo de Tasas de Interés**: Diferentes tasas según tipo de cliente (Premium: 3.5%, Regular: 5.0%, Estándar: 6.0%)
- **Cálculo de Descuentos de Cartera**: Calcula descuentos para carteras de préstamos aprobados
- **Base de Datos H2 en Memoria**: Para desarrollo y pruebas
- **Consola H2**: Accesible en `/h2-console` para inspección de base de datos

## Tecnologías

- Java 17
- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- Base de datos H2
- Maven
- JaCoCo para cobertura de código

## Primeros Pasos

### Prerrequisitos

- Java 17+
- Maven 3.8+

### Ejecutar la Aplicación

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd prestamo-service

# Compilar y ejecutar
./mvnw spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`

### Acceder a la Consola H2

Navega a `http://localhost:8080/h2-console`
- URL JDBC: `jdbc:h2:mem:pascualbravodb`
- Usuario: `sa`
- Contraseña: (vacío)

## Endpoints de la API

### Solicitar Préstamo
```
POST /api/prestamos/solicitar
```

**Parámetros:**
- `clienteId` (Long, requerido) - ID del cliente
- `monto` (Double, requerido) - Monto del préstamo
- `tipoCliente` (String, requerido) - Tipo de cliente: `PREMIUM`, `REGULAR`, u otro

**Respuesta:**
```json
{
  "id": 1,
  "clienteId": 123,
  "monto": 25000.0,
  "tasaInteres": 3.5,
  "estado": "APROBADO"
}
```

### Calcular Descuentos de Cartera
```
POST /api/prestamos/descuentos
```

**Cuerpo de la Petición:**
```json
[
  {"id": 1, "clienteId": 123, "monto": 15000.0, "tasaInteres": 3.5, "estado": "APROBADO"},
  {"id": 2, "clienteId": 456, "monto": 8000.0, "tasaInteres": 5.0, "estado": "APROBADO"}
]
```

**Respuesta:**
```json
250.0
```

**Reglas de Descuento:**
- Préstamos > $10,000: 5% de descuento
- Préstamos > $5,000: 2% de descuento
- Préstamos ≤ $5,000: Sin descuento
- Solo se consideran préstamos aprobados

## Estructura del Proyecto

```
src/main/java/co/edu/pascualbravo/banco/
├── BancoApplication.java          # Punto de entrada principal
├── controller/
│   └── PrestamoController.java    # Endpoints REST
├── model/
│   └── Prestamo.java              # Entidad Préstamo
├── repository/
│   └── PrestamoRepository.java    # Capa de acceso a datos
└── service/
    └── PrestamoService.java       # Lógica de negocio
```

## Pruebas

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar pruebas con reporte de cobertura
./mvnw test jacoco:report
```

Reporte de cobertura disponible en `target/site/jacoco/index.html`

## Reglas de Negocio

### Aprobación de Préstamos
- Monto máximo aprobado: $50,000
- Montos superiores a $50,000 son RECHAZADOS con tasa de interés 0%
- Validaciones: clienteId no nulo, monto > 0

### Tasas de Interés por Tipo de Cliente
| Tipo de Cliente | Tasa de Interés |
|-----------------|-----------------|
| PREMIUM         | 3.5%            |
| REGULAR         | 5.0%            |
| Estándar        | 6.0%            |

### Descuentos de Cartera
| Monto del Préstamo | Tasa de Descuento |
|--------------------|-------------------|
| > $10,000          | 5%                |
| > $5,000           | 2%                |
| ≤ $5,000           | 0%                |