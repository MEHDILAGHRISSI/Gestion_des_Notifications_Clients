# Système de Gestion des Notifications Clients - CDG Prévoyance

## 📋 Description du Projet

Ce système permet à la Caisse de Dépôt et de Gestion (CDG) Prévoyance de centraliser et automatiser la gestion des notifications clients via différents canaux de communication (Email, SMS, Courrier physique, Appels, Web).

### Fonctionnalités Principales

- ✅ **Gestion centralisée des notifications** avec interface unifiée
- ✅ **Templates dynamiques** avec variables personnalisables (`{{nomVariable}}`)
- ✅ **Multi-canal** : Email, SMS, Courrier, Appels, Notifications web
- ✅ **Envois groupés** avec planification et traçabilité
- ✅ **Import en masse** de destinataires (CSV/JSON)
- ✅ **Génération automatique de PDF** pour courriers officiels
- ✅ **Dashboard temps réel** avec statistiques par canal
- ✅ **Authentification utilisateur** et gestion des accès

## 🏗️ Architecture Technique

```
┌─────────────────┐    HTTP/HTTPS    ┌──────────────────┐    JPA/Hibernate    ┌─────────────────┐
│   Frontend      │ ◄──────────────► │    Backend       │ ◄─────────────────► │   Base de       │
│   (Angular)     │      REST API    │  (Spring Boot)   │       JDBC          │   Données       │
│   Port: 4200    │      JSON        │   Port: 8080     │                     │   (MySQL)       │
└─────────────────┘                  └──────────────────┘                     └─────────────────┘
```

### Stack Technologique

**Frontend :**
- Angular 17.3.12 (Standalone Components)
- Angular CLI 17.3.17
- PrimeNG (Composants UI)
- TypeScript
- RxJS (Programmation réactive)

**Backend :**
- Spring Boot 3.5.3
- Spring Data JPA
- Spring Security
- Java Mail Sender
- Flying Saucer (Génération PDF)

**Base de données :**
- MySQL 8.0+
- Port: 3306
- Scripts de migration Flyway inclus

## 🚀 Installation et Démarrage

### Prérequis

Assurez-vous d'avoir installé :
- **Java 17** ou supérieur
- **Node.js 18** ou supérieur  
- **npm** (inclus avec Node.js)
- **Angular CLI 17.3.17** : `npm install -g @angular/cli@17.3.17`
- **MySQL 8.0** ou supérieur
- **Git**

### 1. Cloner le Repository

```bash
git clone https://github.com/MEHDILAGHRISSI/Gestion_des_Notifications_Clients.git
cd Gestion_des_Notifications_Clients
```

### 2. Configuration de la Base de Données

1. **Démarrer MySQL** et créer une base de données :
```sql
CREATE DATABASE notification;
CREATE USER 'notif_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON notification.* TO 'notif_user'@'localhost';
FLUSH PRIVILEGES;
```

2. **Exécuter le script de création** :
```bash
mysql -u notif_user -p notification < notif_backend/src/main/resources/db/migration/V1__init_schema.sql
```

### 3. Configuration et Démarrage du Backend

1. **Naviguer vers le dossier backend** :
```bash
cd notif_backend
```

2. **Configurer la base de données** dans `src/main/resources/application.properties` :
```properties
# Configuration Base de données
spring.datasource.url=jdbc:mysql://localhost:3306/notification
spring.datasource.username=notif_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuration JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Configuration Flyway (optionnel - pour les migrations)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

3. **Installer les dépendances et démarrer** :
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Le backend sera accessible sur : `http://localhost:8080`

### 4. Configuration et Démarrage du Frontend

1. **Naviguer vers le dossier frontend** :
```bash
cd ../notification
```

2. **Installer les dépendances** :
```bash
npm install
```

3. **Configurer l'URL du backend** dans `src/environments/environment.ts` :
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

4. **Démarrer l'application** :
```bash
ng serve
```

L'application sera accessible sur : `http://localhost:4200`

## 👤 Utilisation


### Workflow Principal

1. **Créer des templates** réutilisables avec variables dynamiques
2. **Importer ou ajouter** des destinataires
3. **Générer des notifications** personnalisées
4. **Organiser en envois groupés** 
5. **Planifier et suivre** les campagnes via le dashboard

## 📁 Structure du Projet

```
Gestion_des_Notifications_Clients/
├── notif_backend/                    # API Spring Boot 3.5.3
│   ├── src/main/java/
│   │   ├── controller/              # Contrôleurs REST
│   │   ├── service/                 # Logique métier
│   │   ├── repository/              # Accès données
│   │   ├── model/                   # Entités JPA
│   │   └── dto/                     # Objects de transfert
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── db/migration/
│   │       └── V1__init_schema.sql  # Script initialisation DB
│   └── pom.xml
├── notification/                     # Interface Angular 17.3.12
│   ├── src/app/
│   │   ├── pages/                   # Composants pages
│   │   ├── services/                # Services HTTP
│   │   └── models/                  # Interfaces TypeScript
│   ├── angular.json
│   └── package.json
└── README.md
```

## 🔧 Fonctionnalités Techniques

### Design Patterns Implémentés
- **Factory Pattern** : Gestion multi-canal d'envois
- **Dependency Injection** : Architecture Spring
- **Repository Pattern** : Accès données avec Spring Data JPA
- **DTO Pattern** : Transfert de données sécurisé

### Fonctionnalités Avancées
- **Scheduler automatique** : Vérification envois en attente (60s)
- **Génération PDF** : Courriers officiels avec mise en forme
- **Upload de fichiers** : Pièces jointes et imports CSV
- **CORS configuré** : Communication frontend-backend sécurisée

## 🐛 Résolution de Problèmes

### Erreurs Communes

**Erreur de connexion à la base de données :**
```bash
# Vérifier que MySQL est démarré
sudo service mysql start
# ou
brew services start mysql
```

**Port déjà utilisé :**
```bash
# Changer le port dans application.properties
server.port=8081
```

**Problème CORS :**
- Vérifier que `CorsConfig.java` autorise l'origine du frontend


## 📝 License

Ce projet est développé dans le cadre du stage d'observation à CDG Prévoyance.

---

**Développé par :** MEHDI LAGHRISSI  
**Encadrant CDG :** M. TAJABRITE Nabil  
**Encadrant FST :** Pr. Ikram Ben abdelouahab  
**Période :** 03/07/2025 - 01/08/2025
