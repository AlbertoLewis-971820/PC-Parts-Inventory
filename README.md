# PC Parts Inventory

A full-stack inventory management application for tracking PC components. The application provides a REST API built with Spring Boot, a PostgreSQL database for persistent storage, and an Angular frontend for managing inventory through a web interface.

The entire application can be run using Docker Compose.

![img.png](img.png)

## Features

- View all PC parts in inventory
- Add new PC parts
- Edit existing PC parts
- Delete PC parts
- Track part name, category, manufacturer, price, and quantity
- Backend validation for submitted data
- Error handling for invalid requests
- Persistent PostgreSQL storage
- Responsive Angular frontend
- Full-stack Docker Compose deployment

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- Maven

### Frontend
- Angular
- TypeScript
- HTML
- CSS
- Angular Forms
- Angular HttpClient
- Angular Signals

### Database
- PostgreSQL 17

### DevOps
- Docker
- Docker Compose
- Multi-stage Docker builds
- Nginx

## Architecture

```text
Browser
   |
   v
Angular Frontend
Nginx Container
Port 4200
   |
   | HTTP / REST
   v
Spring Boot REST API
Port 8080
   |
   | JDBC
   v
PostgreSQL
Port 5432
   |
   v
Docker Volume
```

Docker Compose creates an internal network that allows the Spring Boot backend to communicate with PostgreSQL using the `postgres` service name.

## PC Part Model

Each PC part contains:

| Field | Type | Description |
|---|---|---|
| `id` | Long | Unique database identifier |
| `name` | String | Name of the PC component |
| `category` | String | Component category |
| `manufacturer` | String | Component manufacturer |
| `price` | BigDecimal | Price of the component |
| `quantity` | Integer | Number currently in inventory |

Example:

```json
{
  "name": "Ryzen 7 7800X3D",
  "category": "CPU",
  "manufacturer": "AMD",
  "price": 349.99,
  "quantity": 3
}
```

## REST API

Base URL:

```text
http://localhost:8080/api/pcparts
```

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/pcparts` | Get all PC parts |
| GET | `/api/pcparts/{id}` | Get a PC part by ID |
| POST | `/api/pcparts` | Add a new PC part |
| PUT | `/api/pcparts/{id}` | Update an existing PC part |
| DELETE | `/api/pcparts/{id}` | Delete a PC part |

## Running with Docker Compose

### Prerequisites

Install:

- Docker
- Docker Compose

No local Java, Node.js, Angular CLI, or PostgreSQL installation is required when running the complete application through Docker.

### 1. Clone the repository

```bash
git clone https://github.com/AlbertoLewis-971820/Bill-Tracker-API.git
cd PC-Parts-Inventory
```

### 2. Configure the database password

Create a `.env` file in the project root:

```text
DB_PASSWORD=your_database_password
```

The `.env` file is ignored by Git and should not be committed.

### 3. Start the application

```bash
docker compose up --build
```

Docker Compose will:

1. Create the PostgreSQL database
2. Wait for PostgreSQL to become healthy
3. Start the Spring Boot backend
4. Build the Angular application
5. Serve the Angular frontend using Nginx

### 4. Open the application

Open the frontend in your browser:

```text
http://localhost:4200
```

The REST API is available at:

```text
http://localhost:8080/api/pcparts
```

### 5. Stop the application

```bash
docker compose down
```

The PostgreSQL data is stored in a Docker named volume, so inventory data persists when the containers are recreated.

To also remove the database volume and reset the database:

```bash
docker compose down -v
```

> **Warning:** `docker compose down -v` deletes the PostgreSQL volume and all inventory data stored in it.

## Docker Services

The Docker Compose environment contains three services:

### PostgreSQL

Uses PostgreSQL 17 and stores database files in a persistent Docker volume.

A health check using `pg_isready` ensures the database is ready before Spring Boot starts.

### Backend

The Spring Boot application is built using a multi-stage Docker build.

The backend connects to PostgreSQL over the Docker Compose network using:

```text
jdbc:postgresql://postgres:5432/pc_parts_inventory
```

### Frontend

The Angular application is compiled in a Node.js build stage.

The generated production files are copied into a lightweight Nginx container and served on port `80` inside the container, mapped to port `4200` on the host.

## Validation

The backend validates PC part data before saving it.

Examples of validation rules include:

- Name is required
- Category is required
- Manufacturer is required
- Price must be positive
- Quantity cannot be negative

Invalid requests return an HTTP `400 Bad Request` response.

## Project Structure

```text
PC-Parts-Inventory/
├── frontend/
│   ├── src/
│   ├── Dockerfile
│   ├── package.json
│   └── angular.json
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│
├── .dockerignore
├── .gitignore
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## Development

For local development, the applications can also be run separately.

### Backend

```bash
./mvnw spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

### Frontend

From the `frontend` directory:

```bash
npm install
npm start
```

The Angular development server runs at:

```text
http://localhost:4200
```

A PostgreSQL database must also be available and configured when running the backend outside Docker.

## What I Learned

This project was built to practice full-stack application development and deployment, including:

- Designing RESTful CRUD endpoints with Spring Boot
- Using Spring Data JPA and Hibernate with PostgreSQL
- Implementing backend request validation
- Connecting an Angular frontend to a REST API
- Using Angular services and HttpClient
- Managing component state with Angular Signals
- Communicating between Angular components
- Handling asynchronous API requests with Observables
- Building editable inventory tables
- Creating multi-stage Docker images
- Running multiple services with Docker Compose
- Configuring container-to-container networking
- Persisting database data using Docker volumes
- Using health checks to control container startup order

## Future Improvements

Possible future enhancements include:

- Search and filtering
- Inventory sorting
- Low-stock indicators
- Additional automated tests
- Authentication and authorization
- Production environment configuration

## Author

Alberto Lewis