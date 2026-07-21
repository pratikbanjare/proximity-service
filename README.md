# Proximity Service

A Spring Boot web application to identify nearby businesses on a map using geohashing.

## Project Overview

This application provides REST APIs to manage businesses and search for nearby businesses based on geographic coordinates. It uses geohashing for efficient proximity searches.

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 2.7.x (LTS)
- **Database**: H2 (In-memory database for development/testing)
- **Build Tool**: Maven
- **Libraries**: 
  - Spring Data JPA
  - Lombok (for reducing boilerplate code)
  - Geohash Core (for proximity calculations)

## Project Structure

```
proximity-service/
├── src/
│   ├── main/
│   │   ├── java/com/proximityservice/
│   │   │   ├── ProximityServiceApplication.java  (Main Spring Boot application class)
│   │   │   ├── controller/
│   │   │   │   ├── BusinessController.java       (REST endpoints for business CRUD)
│   │   │   │   └── NearbyController.java         (REST endpoints for proximity search)
│   │   │   ├── entity/
│   │   │   │   ├── Business.java                 (Business entity)
│   │   │   │   └── Geohash.java                  (Geohash entity)
│   │   │   ├── repository/
│   │   │   │   ├── BusinessRepository.java       (Business data access layer)
│   │   │   │   └── GeohashRepository.java        (Geohash data access layer)
│   │   │   ├── service/
│   │   │   │   └── BusinessService.java          (Business logic layer)
│   │   │   └── dto/
│   │   │       └── BusinessDTO.java              (Data Transfer Object)
│   │   └── resources/
│   │       └── application.properties             (Spring Boot configuration)
│   └── test/
│       └── java/com/proximityservice/
│           └── ProximityServiceApplicationTests.java
├── pom.xml                                        (Maven configuration)
└── README.md

```

## Database Schema

### Business Table
```sql
CREATE TABLE business (
    business_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_name VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);
```

### Geohash Table
```sql
CREATE TABLE geohash (
    geohash_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL,
    geohash_value VARCHAR(255)
);
```

## API Endpoints

### 1. Business Management Endpoints

#### Get Business by ID
```
GET /api/business/{id}
```
Returns details of a business by its ID.

**Response:**
```json
{
    "businessId": 1,
    "businessName": "Iyengers Bakery",
    "latitude": 45.677,
    "longitude": 234.234324
}
```

#### Add New Business
```
POST /api/business/{id}
```
Adds a new business to the database.

**Request Body:**
```json
{
    "businessName": "Iyengers Bakery",
    "latitude": 45.677,
    "longitude": 234.234324
}
```

#### Update Business
```
PUT /api/business/{id}
```
Updates an existing business or adds a new entry if it doesn't exist.

**Request Body:**
```json
{
    "businessName": "Updated Business Name",
    "latitude": 45.678,
    "longitude": 234.234325
}
```

#### Delete Business
```
DELETE /api/business/{id}
```
Deletes an existing business from the database.

### 2. Proximity Search Endpoint

#### Search Nearby Businesses
```
GET /api/nearby/search/{latitude}/{longitude}?radius=5.0
```
Finds all businesses within the specified radius (in kilometers) from the given coordinates.

**Query Parameters:**
- `radius` (optional, default: 5.0 km): Search radius in kilometers

**Response:**
```json
[
    {
        "businessId": 1,
        "businessName": "Iyengers Bakery",
        "latitude": 45.677,
        "longitude": 234.234324
    },
    {
        "businessId": 2,
        "businessName": "Another Business",
        "latitude": 45.680,
        "longitude": 234.235000
    }
]
```

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6.0 or higher

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/proximity-service-1.0.0.jar
```

The application will start on `http://localhost:8080`

### Access H2 Console (Development)

Once the application is running, you can access the H2 console at:
```
http://localhost:8080/h2-console
```

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## Testing

Run tests with Maven:

```bash
mvn test
```

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port
- Database connection details
- JPA/Hibernate settings
- Context path

## Future Enhancements

- Add authentication and authorization
- Implement caching for frequently accessed data
- Add support for different database systems (MySQL, PostgreSQL)
- Add business categories and ratings
- Implement advanced geospatial queries
- Add API documentation with Swagger/SpringFox

## License

This project is licensed under the MIT License.

## Author

Proximity Service Development Team
