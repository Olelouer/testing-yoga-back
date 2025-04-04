# Backend Yoga App (testing-yoga-back)

Ce module constitue la partie backend de l'application Yoga App, développé avec Spring Boot.

## Présentation

Le backend fournit une API RESTful permettant de gérer les sessions de yoga, les utilisateurs et les inscriptions aux sessions.

## Prérequis

- Java 11
- Maven
- MySQL (sur le port par défaut 3306)

## Configuration de la base de données

Avant de démarrer l'application, vous devez configurer la base de données :

```sql
CREATE DATABASE yoga_app;
CREATE USER 'yoga_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON yoga_app.* TO 'yoga_user'@'localhost';
FLUSH PRIVILEGES;
```

Exécutez le script SQL fourni pour initialiser le schéma :

```sh
mysql -u yoga_user -p yoga_app < ressources/sql/script.sql
```

## Configuration de l'application

Modifiez le fichier `src/main/resources/application.properties` si nécessaire :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yoga_app?serverTimezone=UTC
spring.datasource.username=yoga_user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## Installation et démarrage

### Si vous utilisez ce module comme partie du projet principal :

Voir le README principal à la racine du projet pour les instructions de clonage avec sous-modules.

### Si vous utilisez ce module de façon autonome :

```sh
git clone https://github.com/Olelouer/testing-yoga-back
cd testing-yoga-back
mvn clean install
mvn spring-boot:run
```

Le serveur démarre sur [http://localhost:8080](http://localhost:8080).

## Tests

Exécution des tests unitaires et d'intégration :

```sh
mvn test
```

Le rapport de couverture JaCoCo sera généré dans :
```
target/site/jacoco/index.html
```

## Lien avec le frontend

Ce backend est conçu pour fonctionner avec le module frontend `testing-yoga-front`. Pour des instructions complètes sur l'installation et la configuration de l'ensemble de l'application, veuillez consulter le README principal à la racine du projet.