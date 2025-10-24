# User Service

Microservice zur Verwaltung von Benutzerdaten mit automatisch generierten Testdaten.

---

## 📋 Übersicht

Der User Service ist ein einfacher RESTful Microservice der:
- 20 zufällige User beim Start generiert (DataFaker)
- Sich automatisch bei Eureka registriert
- Eine REST-API zur Abfrage von Usern bereitstellt

**Port:** 8081

---

## 🚀 Schnellstart

### **Standalone starten:**
```bash
cd user-service
mvn spring-boot:run
```

### **Mit anderem Port:**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8083
```

### **Vom Root:**
```bash
cd ..
./build-all.sh
./start-all.sh
```

---

## 📡 API Endpoints

### **Alle User abrufen**
```bash
GET http://localhost:8081/api/users

# Response
[
  {
    "id": 1,
    "username": "max.mustermann",
    "email": "max@example.de",
    "firstName": "Max",
    "lastName": "Mustermann",
    "city": "München"
  },
  ...
]
```

### **Einzelnen User abrufen**
```bash
GET http://localhost:8081/api/users/{id}

# Beispiel
curl http://localhost:8081/api/users/1

# Response
{
  "id": 1,
  "username": "max.mustermann",
  "email": "max@example.de",
  "firstName": "Max",
  "lastName": "Mustermann",
  "city": "München"
}
```

---

## 🏗️ Projekt-Struktur

```
user-service/
├── src/main/java/com/javafleet/userservice/
│   ├── UserServiceApplication.java     # Main Class mit @EnableDiscoveryClient
│   ├── model/
│   │   └── User.java                   # User Entity (Lombok)
│   ├── service/
│   │   └── UserService.java            # Business Logic + DataFaker
│   └── controller/
│       └── UserController.java         # REST Controller
├── src/main/resources/
│   └── application.yml                 # Konfiguration
├── pom.xml                             # Maven Dependencies
└── Dockerfile                          # Docker Image
```

---

## 🔧 Technologie-Stack

- **Spring Boot 3.2.0**
- **Spring Web** - REST API
- **Spring Cloud Netflix Eureka Client** - Service Discovery
- **DataFaker 2.0.2** - Zufällige Testdaten (deutsche Namen!)
- **Lombok** - Boilerplate-Code reduzieren
- **Java 21**

---

## 📦 Dependencies

```xml
<dependencies>
    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Eureka Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    
    <!-- DataFaker -->
    <dependency>
        <groupId>net.datafaker</groupId>
        <artifactId>datafaker</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

---

## ⚙️ Konfiguration

**application.yml:**
```yaml
server:
  port: 8081

spring:
  application:
    name: user-service  # Name für Eureka Registry

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

---

## 🎯 DataFaker Integration

**UserService.java:**
```java
@Service
public class UserService {
    
    private final Faker faker = new Faker(Locale.GERMAN);  // Deutsche Namen!
    private final List<User> users;
    
    public UserService() {
        this.users = generateUsers(20);  // 20 User beim Start
    }
    
    private List<User> generateUsers(int count) {
        List<User> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername(faker.internet().username());
            user.setEmail(faker.internet().emailAddress());
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setCity(faker.address().city());
            list.add(user);
        }
        return list;
    }
}
```

**Resultat:** Bei jedem Start werden 20 neue zufällige User generiert!

---

## 🔍 Eureka Integration

### **Service registriert sich automatisch**

**UserServiceApplication.java:**
```java
@SpringBootApplication
@EnableDiscoveryClient  // ← Aktiviert Eureka Client
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

### **Registrierung beobachten:**

1. Starte Eureka Server: `http://localhost:8761`
2. Starte User Service
3. Warte 30 Sekunden
4. Refresh Eureka Dashboard
5. User Service erscheint unter "USER-SERVICE" ✅

---

## 🧪 Testen

### **Lokal testen:**
```bash
# Service starten
mvn spring-boot:run

# In anderem Terminal
curl http://localhost:8081/api/users
curl http://localhost:8081/api/users/1
```

### **Über API Gateway testen:**
```bash
# Erst Gateway starten, dann:
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users/1
```

### **Von Order Service aufgerufen:**
```bash
# Order Service nutzt Feign Client
curl http://localhost:8082/api/orders/1/with-user

# Order Service ruft User Service automatisch auf!
```

---

## 🐳 Docker

### **Image bauen:**
```bash
mvn clean package -DskipTests
docker build -t javafleet/user-service:latest .
```

### **Container starten:**
```bash
docker run -p 8081:8081 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8761/eureka/ \
  javafleet/user-service:latest
```

### **Mit Docker Compose:**
```bash
cd ..
docker-compose up user-service
```

---

## 🔥 Mehrere Instanzen (Load Balancing)

### **Zweite Instanz starten:**
```bash
# Terminal 1
mvn spring-boot:run

# Terminal 2 (anderer Port!)
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8083
```

### **Eureka zeigt dann:**
```
USER-SERVICE:
- localhost:8081
- localhost:8083
```

### **Feign Client verteilt Requests automatisch!**

```bash
# Order Service wählt abwechselnd:
curl http://localhost:8082/api/orders/1/with-user  # → 8081
curl http://localhost:8082/api/orders/2/with-user  # → 8083
curl http://localhost:8082/api/orders/3/with-user  # → 8081
```

**Round-Robin Load Balancing ohne Konfiguration!** 🎉

---

## 📝 User Model

**User.java:**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
}
```

**Lombok generiert automatisch:**
- Getter & Setter
- toString()
- equals() & hashCode()
- Konstruktoren

---

## 🎓 Lernziele

Nach dem Durcharbeiten verstehst du:
- ✅ REST Controller mit Spring Boot
- ✅ Service Layer Pattern
- ✅ Eureka Client Integration
- ✅ DataFaker für Testdaten
- ✅ Lombok für cleanen Code
- ✅ Application Properties (YAML)

---

## 🔧 Erweiterungsideen

### **1. Datenbank hinzufügen**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```

### **2. Pagination hinzufügen**
```java
@GetMapping
public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
}
```

### **3. Validierung**
```java
@PostMapping
public User createUser(@Valid @RequestBody User user) {
    return userService.save(user);
}
```

### **4. Exception Handling**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }
}
```

---

## 🐛 Troubleshooting

**Problem:** Port 8081 already in use
```bash
# Finde Prozess
lsof -i :8081

# Töte Prozess
kill -9 <PID>
```

**Problem:** Service registriert sich nicht bei Eureka
- Check: Eureka Server läuft auf 8761?
- Check: `spring.application.name` gesetzt?
- Warte 30 Sekunden (Eureka Heartbeat)

**Problem:** DataFaker generiert keine deutschen Namen
- Check: `new Faker(Locale.GERMAN)` richtig?
- Alternative: `new Faker(new Locale("de", "DE"))`

---

## 📚 Weiterführende Links

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Eureka Docs**: https://spring.io/projects/spring-cloud-netflix
- **DataFaker Docs**: https://www.datafaker.net/
- **Lombok Docs**: https://projectlombok.org/

---

**Teil der Microservices Demo Collection** - [Zurück zum Haupt-README](../README.md)
