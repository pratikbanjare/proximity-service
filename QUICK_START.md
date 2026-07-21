# Proximity Service - Quick Start Guide

## Project Summary
A fully configured Java Maven project with Spring Boot and Spring Web for managing and searching nearby businesses using geohashing.

## Technology Stack
- **Java 17**
- **Spring Boot 2.7.x (LTS)**
- **Spring Data JPA**
- **H2 Database** (In-memory for development)
- **Maven Build Tool**

## Project Structure
```
proximity-service/
├── src/
│   ├── main/
│   │   ├── java/com/proximityservice/
│   │   │   ├── ProximityServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── BusinessController.java
│   │   │   │   └── NearbyController.java
│   │   │   ├── entity/
│   │   │   │   ├── Business.java
│   │   │   │   └── Geohash.java
│   │   │   ├── repository/
│   │   │   │   ├── BusinessRepository.java
│   │   │   │   └── GeohashRepository.java
│   │   │   ├── service/
│   │   │   │   └── BusinessService.java
│   │   │   └── dto/
│   │   │       └── BusinessDTO.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/proximityservice/
│           └── ProximityServiceApplicationTests.java
├── pom.xml
├── README.md
└── .gitignore
```

## Building the Project

### Build JAR
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Run Application
```bash
# Using Maven Spring Boot plugin
mvn spring-boot:run

# Or using the JAR file
java -jar target/proximity-service-1.0.0.jar
```

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### 1. Get Business
```
GET /business/{id}
Response: BusinessDTO (200 OK or 404 Not Found)
```

### 2. Add Business
```
POST /business/{id}
Request Body: { "businessName": "...", "latitude": ..., "longitude": ... }
Response: BusinessDTO (201 Created)
```

### 3. Update Business
```
PUT /business/{id}
Request Body: { "businessName": "...", "latitude": ..., "longitude": ... }
Response: BusinessDTO (200 OK)
```

### 4. Delete Business
```
DELETE /business/{id}
Response: No Content (204)
```

### 5. Search Nearby Businesses
```
GET /nearby/search/{latitude}/{longitude}?radius=5.0
Query Parameters:
  - radius: Search radius in km (optional, default: 5.0)
Response: List<BusinessDTO> (200 OK)
```

## Database

### H2 Console (Development)
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: (leave empty)

### Schema
- **Business Table**: Stores business information with coordinates
- **Geohash Table**: Stores geohash values for proximity calculations

## Next Steps

1. **Add Authentication**: Implement Spring Security for API authentication
2. **Add Validation**: Add validation annotations to DTOs and entities
3. **Add Error Handling**: Implement global exception handler
4. **Add Logging**: Implement comprehensive logging
5. **Switch Database**: Configure MySQL or PostgreSQL for production
6. **Add API Documentation**: Integrate Swagger/SpringFox
7. **Add Advanced Queries**: Implement more sophisticated geospatial queries
8. **Add Caching**: Implement Redis caching for frequent queries

## Troubleshooting

### Build Issues
- Ensure Java 17 is installed: `java -version`
- Clear Maven cache if issues persist: `mvn clean`

### Runtime Issues
- Check if port 8080 is available
- Verify application.properties configuration
- Check logs for startup errors

## Notes
- The project uses H2 in-memory database by default (data is lost on restart)
- Geohashing uses a simple implementation for demonstration
- For production, consider switching to a proper database and implementing advanced geospatial queries
- Lombok has been intentionally excluded to avoid Java 17 compatibility issues

