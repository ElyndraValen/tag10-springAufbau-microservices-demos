# Tag 10 Spring Aufbau - Microservices Demos

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build](https://img.shields.io/badge/build-passing-success.svg)](https://github.com)

**Multi-Module Maven Projekt** mit 4 produktionsreifen Spring Boot Microservices!

> 💡 **Lernprojekt** aus dem Java Fleet - Spring Boot Aufbau Kurs Tag 10  
> 📚 Perfekt zum Lernen von Microservices-Architektur  
> ⭐ Wenn hilfreich, gib dem Projekt einen Stern!

---

## 🏗️ Multi-Module Maven Parent Structure

```
tag10-springAufbau-microservices-demos/
├── pom.xml                    ← PARENT POM (Multi-Module!)
├── eureka-server/             ← Service Discovery
├── user-service/              ← User Microservice
├── order-service/             ← Order Microservice (Feign Client)
└── api-gateway/               ← API Gateway
```

**Ein Befehl baut alles:** `mvn clean install` im Root-Verzeichnis!

---

## 📋 Inhaltsverzeichnis

- [Services](#-die-4-services)
- [Schnellstart](#-schnellstart)
- [Services testen](#-services-testen)
- [Architektur](#-architektur)
- [Was du lernst](#-was-du-lernst)
- [Übungen](#-übungen)
- [Docker Compose](#-docker-compose-details)
- [Production-Hinweise](#-production-hinweise)
- [FAQ](#-faq)
- [Contributing](#-contributing)
- [Lizenz](#-lizenz)

---

## 📦 Die 4 Services

| Service | Port | Beschreibung |
|---------|------|--------------|
| **Eureka Server** | 8761 | Service Discovery & Registry |
| **User Service** | 8081 | User-Verwaltung mit DataFaker (20 Users) |
| **Order Service** | 8082 | Order-Verwaltung mit Feign Client zu User Service |
| **API Gateway** | 8080 | Zentraler Einstiegspunkt für alle Services |

---

## 🚀 Schnellstart

### **Option 1: Services einzeln starten**

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
mvn spring-boot:run
```
Warte bis Eureka läuft: `http://localhost:8761`

**Terminal 2 - User Service:**
```bash
cd user-service
mvn spring-boot:run
```

**Terminal 3 - Order Service:**
```bash
cd order-service
mvn spring-boot:run
```

**Terminal 4 - API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
```

### **Option 2: Docker Compose (alles auf einmal)**

```bash
# Erst alle Services bauen
cd eureka-server && mvn clean package -DskipTests
cd ../user-service && mvn clean package -DskipTests
cd ../order-service && mvn clean package -DskipTests
cd ../api-gateway && mvn clean package -DskipTests

# Dann Docker Compose starten
cd ..
docker-compose up -d
```

---

## 🎯 Services testen

### **1. Eureka Dashboard**
```
http://localhost:8761
```
Hier siehst du alle registrierten Services!

### **2. User Service (direkt)**
```bash
# Alle Users
curl http://localhost:8081/api/users

# Ein User
curl http://localhost:8081/api/users/1
```

### **3. Order Service (direkt)**
```bash
# Alle Orders
curl http://localhost:8082/api/orders

# Order mit User-Info (Feign Client!)
curl http://localhost:8082/api/orders/1/with-user
```

### **4. Über API Gateway (empfohlen!)**
```bash
# Users über Gateway
curl http://localhost:8080/api/users

# Orders über Gateway
curl http://localhost:8080/api/orders

# Order mit User über Gateway
curl http://localhost:8080/api/orders/1/with-user
```

---

## 🏗️ Architektur

```
                    [Client]
                       |
                       ↓
              [API Gateway :8080]
                       |
        ┌──────────────┼──────────────┐
        |              |              |
        ↓              ↓              ↓
  [User Service] [Order Service]  [...]
      :8081          :8082
        |              |
        └──────┬───────┘
               ↓
        [Eureka Server :8761]
         (Service Registry)
```

### **Was passiert:**

1. **Eureka Server** startet und wartet auf Service-Registrierungen
2. **User Service** startet und registriert sich bei Eureka als `USER-SERVICE`
3. **Order Service** startet und:
   - Registriert sich bei Eureka als `ORDER-SERVICE`
   - Findet User Service über Eureka via Feign Client
4. **API Gateway** startet und:
   - Registriert sich bei Eureka
   - Routet Requests zu den richtigen Services
5. **Client** ruft nur Gateway auf → Gateway leitet weiter

---

## 📚 Was du lernst

### **1. Service Discovery mit Eureka**
- Services registrieren sich automatisch
- Services finden sich gegenseitig
- Health Checks & Monitoring

### **2. Feign Client (REST zwischen Services)**
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable Long id);
}
```
**Kein RestTemplate! Kein HttpClient!** Nur Interface → Spring macht REST-Call!

### **3. API Gateway (Spring Cloud Gateway)**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service  # lb = Load Balanced via Eureka!
          predicates:
            - Path=/api/users/**
```

### **4. Load Balancing**
Starte User Service 2x (verschiedene Ports) → Gateway/Order Service verteilen Requests automatisch!

---

## 🎓 Übungen

### **Übung 1: Zweite User Service Instanz**

```bash
# Terminal 5
cd user-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8083
```

Gehe zu Eureka Dashboard → Siehst du 2 Instanzen von USER-SERVICE?

Teste:
```bash
curl http://localhost:8080/api/orders/1/with-user
```
Mehrmals aufrufen → Load Balancing!

### **Übung 2: Service Resilience**

1. Stoppe User Service (Strg+C)
2. Rufe Order Service auf:
```bash
curl http://localhost:8080/api/orders/1/with-user
```
Was passiert? → Fehlermeldung!

3. Starte User Service wieder
4. Warte 30 Sekunden (Eureka Update)
5. Teste wieder → Funktioniert!

### **Übung 3: Neuer Service**

Erstelle einen **Product Service**:
- Port 8084
- Registriert sich bei Eureka
- API: `/api/products`
- Gateway routet `/api/products/**` zu Product Service

---

## 🐳 Docker Compose Details

### **docker-compose.yml:**
```yaml
version: '3.8'

services:
  eureka:
    build: ./eureka-server
    ports:
      - "8761:8761"
    networks:
      - microservices

  user-service:
    build: ./user-service
    ports:
      - "8081:8081"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8761/eureka/
    depends_on:
      - eureka
    networks:
      - microservices

  order-service:
    build: ./order-service
    ports:
      - "8082:8082"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8761/eureka/
    depends_on:
      - eureka
    networks:
      - microservices

  api-gateway:
    build: ./api-gateway
    ports:
      - "8080:8080"
    environment:
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8761/eureka/
    depends_on:
      - eureka
    networks:
      - microservices

networks:
  microservices:
    driver: bridge
```

### **Befehle:**
```bash
# Starten
docker-compose up -d

# Logs anschauen
docker-compose logs -f

# Einzelnen Service Logs
docker-compose logs -f user-service

# Stoppen
docker-compose down

# Neu bauen & starten
docker-compose up --build -d
```

---

## 🏢 Production-Hinweise

**Diese Demo ist für Lernen/Entwicklung!** Für Production brauchst du:

### **1. Hochverfügbarkeit**
- Mehrere Eureka Server (Peer Replication)
- Mehrere Instanzen jedes Service
- Load Balancer vor API Gateway

### **2. Security**
- Spring Security für alle Services
- JWT/OAuth2 für Authentication
- Service-to-Service Authentication

### **3. Monitoring & Logging**
- Spring Boot Actuator
- Prometheus + Grafana
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Distributed Tracing (Zipkin/Jaeger)

### **4. Resilience**
- Circuit Breaker (Resilience4j)
- Retry Logic
- Timeout Configuration
- Fallback Responses

### **5. Configuration Management**
- Spring Cloud Config Server
- Zentrale Konfiguration
- Secrets Management (Vault)

---

## ❓ FAQ

**Q: Warum Eureka statt Kubernetes Service Discovery?**  
A: Eureka funktioniert überall (lokal, Cloud, Kubernetes). Kubernetes hat eigene Service Discovery, aber Eureka ist Spring-nativ.

**Q: Was passiert wenn Eureka ausfällt?**  
A: Services cachen das Registry. Sie laufen weiter, aber neue Services werden nicht gefunden. Daher: Production → mehrere Eureka Server!

**Q: Feign vs RestTemplate?**  
A: Feign ist deklarativ (nur Interface), RestTemplate ist imperativ (Code). Feign ist moderner und einfacher!

**Q: API Gateway vs Direkt zu Services?**  
A: Gateway = Single Entry Point, Authentication, Rate Limiting, Load Balancing. Production = immer Gateway!

**Q: Ist das Production-Ready?**  
A: Nein! Es fehlt: Security, Monitoring, Resilience, Config Management. Aber das Fundament ist da!

---

## 📚 Weiterführende Links

- **Spring Cloud Netflix**: https://spring.io/projects/spring-cloud-netflix
- **Spring Cloud Gateway**: https://spring.io/projects/spring-cloud-gateway
- **Microservices Patterns**: https://microservices.io/
- **Netflix OSS**: https://netflixtechblog.com/

---

## 🤝 Contributing

Beiträge sind willkommen! Bitte lies [CONTRIBUTING.md](CONTRIBUTING.md) für Details.

1. Fork das Projekt
2. Erstelle deinen Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit deine Änderungen (`git commit -m 'feat: add AmazingFeature'`)
4. Push zum Branch (`git push origin feature/AmazingFeature`)
5. Öffne einen Pull Request

## 📜 Lizenz

Dieses Projekt ist unter der MIT Lizenz lizenziert - siehe [LICENSE](LICENSE) für Details.

## 🙏 Acknowledgments

- **Netflix OSS** für Eureka
- **Spring Team** für Spring Cloud
- **DataFaker** für Testdaten

## 📧 Kontakt

**Java Fleet** - Spring Boot Kurs  
- GitHub: [@java-fleet](https://github.com/java-fleet)
- Website: [java-fleet.de](https://java-fleet.de)

---

**Von Java Fleet** - Spring Boot Aufbau Kurs Tag 10

🚀 Viel Erfolg mit Microservices!
