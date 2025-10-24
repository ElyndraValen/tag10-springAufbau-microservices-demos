# 🎯 Quick Reference

Schnelle Befehls-Übersicht für die Microservices Demo

---

## 🚀 Build & Start

```bash
# Alles bauen
./build-all.sh

# Oder einzeln
mvn clean install

# Alle Services starten
./start-all.sh

# Alle Services stoppen
./stop-all.sh
```

---

## 🐳 Docker

```bash
# Bauen
cd eureka-server && mvn clean package -DskipTests && cd ..
cd user-service && mvn clean package -DskipTests && cd ..
cd order-service && mvn clean package -DskipTests && cd ..
cd api-gateway && mvn clean package -DskipTests && cd ..

# Starten
docker-compose up -d

# Logs
docker-compose logs -f

# Stoppen
docker-compose down
```

---

## 📡 Service URLs

| Service | URL | Port |
|---------|-----|------|
| **Eureka Dashboard** | http://localhost:8761 | 8761 |
| **User Service** | http://localhost:8081/api/users | 8081 |
| **Order Service** | http://localhost:8082/api/orders | 8082 |
| **API Gateway** | http://localhost:8080/api/* | 8080 |

---

## 🧪 Test Endpoints

```bash
# User Service
curl http://localhost:8081/api/users
curl http://localhost:8081/api/users/1

# Order Service
curl http://localhost:8082/api/orders
curl http://localhost:8082/api/orders/1
curl http://localhost:8082/api/orders/1/with-user  # Feign Client!

# Über Gateway (empfohlen)
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users/1
curl http://localhost:8080/api/orders
curl http://localhost:8080/api/orders/1/with-user
```

---

## 📦 Maven Befehle

```bash
# Alle Module bauen
mvn clean install

# Nur Parent
mvn clean install -N

# Einzelnes Modul
cd user-service
mvn clean install

# Tests überspringen
mvn clean install -DskipTests

# Einzelnes Modul starten
cd user-service
mvn spring-boot:run

# Mit anderem Port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8083
```

---

## 🔧 Git Workflow

```bash
# Status
git status

# Änderungen stagen
git add .

# Commit
git commit -m "feat: deine änderung"

# Push
git push origin main

# Neuer Branch
git checkout -b feature/mein-feature

# Branch pushen
git push -u origin feature/mein-feature
```

---

## 🐛 Debugging

```bash
# Logs anschauen (wenn mit ./start-all.sh gestartet)
tail -f logs/eureka.log
tail -f logs/user-service.log
tail -f logs/order-service.log
tail -f logs/api-gateway.log

# Port-Konflikte prüfen
lsof -i :8761
lsof -i :8081
lsof -i :8082
lsof -i :8080

# Prozess killen
kill -9 <PID>

# Oder mit ./stop-all.sh
./stop-all.sh
```

---

## 📝 Projekt-Struktur

```
microservices-demos/
├── pom.xml                 # Parent POM (Multi-Module)
├── eureka-server/          # Service Discovery
├── user-service/           # User Microservice
├── order-service/          # Order Microservice (mit Feign)
├── api-gateway/            # API Gateway
├── docker-compose.yml      # Docker Orchestration
├── build-all.sh            # Build Script
├── start-all.sh            # Start Script
├── stop-all.sh             # Stop Script
└── GITHUB_SETUP.md         # GitHub Push Anleitung
```

---

## 🎓 Key Concepts

### **Eureka Service Discovery**
```yaml
# In application.yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### **Feign Client**
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable Long id);
}
```

### **API Gateway Routing**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service  # Load Balanced!
          predicates:
            - Path=/api/users/**
```

---

## 🔍 Häufige Probleme

**Problem:** Port schon in Verwendung
```bash
# Finde Prozess
lsof -i :8081

# Töte Prozess
kill -9 <PID>
```

**Problem:** Eureka zeigt Service nicht
- Warte 30 Sekunden (Eureka Heartbeat)
- Check Service-Logs
- Check `spring.application.name` in application.yml

**Problem:** Feign Client findet Service nicht
- Check: Eureka läuft?
- Check: Service registriert in Eureka?
- Check: `@FeignClient(name = "service-name")` korrekt?

---

## 📚 Dokumentation

- **Haupt-README**: `README.md`
- **Eureka Details**: `eureka-server/README.md`
- **GitHub Setup**: `GITHUB_SETUP.md`
- **Contributing**: `CONTRIBUTING.md`

---

## 🌟 Nächste Schritte

1. ✅ Services starten und testen
2. ✅ Eureka Dashboard erkunden
3. ✅ Feign Client verstehen
4. ✅ Zweite User-Service Instanz starten (Load Balancing testen)
5. ✅ Eigenen Service hinzufügen
6. ✅ Auf GitHub pushen

---

**Happy Coding!** 🚀
