# 🚀 GitHub Repository Setup

So pushst du dieses Projekt zu GitHub!

---

## 📋 Voraussetzungen

- Git installiert
- GitHub Account
- SSH-Key oder HTTPS Access

---

## 🎯 Option 1: Neues Repository erstellen (Empfohlen)

### **Schritt 1: Repository auf GitHub erstellen**

1. Gehe zu [github.com](https://github.com)
2. Klicke auf **"New Repository"**
3. Repository-Name: `microservices-demos`
4. Beschreibung: `Spring Boot Microservices Demo mit Eureka, Feign und API Gateway`
5. **Public** oder **Private** (deine Wahl)
6. ❌ **NICHT** initialisieren mit README, .gitignore oder License (haben wir schon!)
7. Klicke **"Create Repository"**

### **Schritt 2: Lokales Repository initialisieren**

```bash
cd microservices-demos

# Git initialisieren
git init

# Alle Dateien stagen
git add .

# Ersten Commit
git commit -m "feat: initial commit - microservices demo"
```

### **Schritt 3: Remote hinzufügen & pushen**

**HTTPS (einfacher):**
```bash
# Ersetze DEIN-USERNAME mit deinem GitHub-Username!
git remote add origin https://github.com/DEIN-USERNAME/microservices-demos.git

# Branch umbenennen (falls nötig)
git branch -M main

# Push!
git push -u origin main
```

**SSH (sicherer, braucht SSH-Key):**
```bash
# Ersetze DEIN-USERNAME mit deinem GitHub-Username!
git remote add origin git@github.com:DEIN-USERNAME/microservices-demos.git

git branch -M main
git push -u origin main
```

### **Schritt 4: Verifizieren**

Gehe zu: `https://github.com/DEIN-USERNAME/microservices-demos`

Du solltest sehen:
- ✅ Alle Dateien
- ✅ README mit Badges
- ✅ LICENSE
- ✅ CONTRIBUTING.md
- ✅ 4 Modul-Ordner

---

## 🎯 Option 2: In existierendes Repository pushen

Falls du schon ein Repository hast:

```bash
cd microservices-demos

git init
git add .
git commit -m "feat: initial commit"

# Remote hinzufügen
git remote add origin https://github.com/DEIN-USERNAME/DEIN-REPO.git

# Push zu spezifischem Branch
git push origin main

# Oder zu neuem Branch
git checkout -b microservices
git push origin microservices
```

---

## 📁 Projekt-Struktur (was gepusht wird)

```
microservices-demos/
├── .github/                      ← GitHub-spezifische Dateien
│   ├── workflows/ci.yml         ← CI/CD Pipeline
│   ├── ISSUE_TEMPLATE/          ← Bug & Feature Templates
│   └── pull_request_template.md ← PR Template
├── eureka-server/               ← Service Discovery
├── user-service/                ← User Microservice
├── order-service/               ← Order Microservice mit Feign
├── api-gateway/                 ← API Gateway
├── pom.xml                      ← Parent POM (Multi-Module!)
├── README.md                    ← Haupt-Dokumentation
├── CONTRIBUTING.md              ← Contributor Guide
├── LICENSE                      ← MIT License
├── .gitignore                   ← Git Ignore Rules
├── docker-compose.yml           ← Docker Orchestration
├── build-all.sh                 ← Build Script
├── start-all.sh                 ← Start Script
└── stop-all.sh                  ← Stop Script
```

---

## 🔧 Nützliche Git-Befehle

### **Status checken:**
```bash
git status
```

### **Änderungen committen:**
```bash
git add .
git commit -m "feat: deine änderung"
git push
```

### **Branch erstellen:**
```bash
git checkout -b feature/dein-feature
git push -u origin feature/dein-feature
```

### **Änderungen pullen:**
```bash
git pull origin main
```

### **Branches anzeigen:**
```bash
git branch -a
```

---

## 🎨 GitHub Features aktivieren

### **1. GitHub Actions (CI/CD)**

Bereits konfiguriert in `.github/workflows/ci.yml`!

Wird automatisch ausgeführt bei:
- Push zu `main` oder `develop`
- Pull Requests

Baut:
- ✅ Alle Maven Modules
- ✅ Führt Tests aus
- ✅ Erstellt Docker Images

### **2. Issues aktivieren**

Issues sind automatisch aktiviert! Templates vorhanden:
- 🐛 Bug Report
- ✨ Feature Request

### **3. Projects (optional)**

Für Projekt-Management:
1. Gehe zu **Projects** Tab
2. Erstelle neues Project
3. Nutze Kanban-Board

### **4. GitHub Pages (optional)**

Für Dokumentation:
1. Settings → Pages
2. Source: `main` Branch, `/docs` Ordner
3. Erstelle `/docs` Ordner mit Doku

---

## 📝 Conventional Commits

Wir nutzen Conventional Commits für klare History:

```bash
# Feature
git commit -m "feat: add product service"

# Bugfix
git commit -m "fix: resolve eureka timeout"

# Dokumentation
git commit -m "docs: update README with examples"

# Refactoring
git commit -m "refactor: improve feign client structure"

# Tests
git commit -m "test: add unit tests for user service"

# Chore (Build, Config)
git commit -m "chore: update spring boot to 3.2.1"
```

---

## 🌟 README-Badges anpassen

Öffne `README.md` und passe die Badges an:

```markdown
[![Build](https://github.com/DEIN-USERNAME/microservices-demos/actions/workflows/ci.yml/badge.svg)](https://github.com/DEIN-USERNAME/microservices-demos/actions)
```

Ersetze `DEIN-USERNAME` mit deinem echten GitHub-Username!

---

## 🔒 .gitignore erklärt

Diese Dateien werden NICHT gepusht:
- `target/` - Maven Build-Artifacts
- `*.iml`, `.idea/` - IntelliJ IDEA
- `.vscode/` - VS Code
- `*.log` - Log-Dateien
- `logs/` - Log-Ordner
- `.pids/` - Process IDs

---

## 🐛 Troubleshooting

### **Problem: "remote: Repository not found"**
```bash
# Remote neu setzen
git remote remove origin
git remote add origin https://github.com/DEIN-USERNAME/microservices-demos.git
```

### **Problem: "Updates were rejected"**
```bash
# Force push (VORSICHT!)
git push -f origin main

# Oder: Erst pullen
git pull origin main --rebase
git push origin main
```

### **Problem: "Permission denied (publickey)"**
→ SSH-Key fehlt. Nutze HTTPS stattdessen oder erstelle SSH-Key:
```bash
ssh-keygen -t ed25519 -C "deine-email@example.com"
cat ~/.ssh/id_ed25519.pub
# Kopiere den Key zu GitHub Settings → SSH Keys
```

---

## 🎯 Nach dem Push

### **1. README anpassen**

Ersetze in `README.md`:
- `[@java-fleet](https://github.com/java-fleet)` → Dein GitHub-Profil
- `[java-fleet.de](https://java-fleet.de)` → Deine Website (oder entfernen)

### **2. Repository beschreiben**

Auf GitHub:
- Settings → General
- About → Add description
- Topics: `spring-boot`, `microservices`, `eureka`, `feign`, `java`

### **3. Star dein eigenes Projekt** ⭐

Warum nicht? 😄

---

## 🚀 Fertig!

Dein Repository ist jetzt live auf GitHub!

**Nächste Schritte:**
1. ⭐ Star dein Projekt
2. 📝 Issue erstellen für nächstes Feature
3. 👥 Teile mit Community
4. 🔄 Weiterentwickeln!

**Share it:**
```
🚀 Check out my Spring Boot Microservices Demo!
https://github.com/DEIN-USERNAME/microservices-demos

#SpringBoot #Microservices #Java #OpenSource
```

---

**Happy Coding!** 🎉
