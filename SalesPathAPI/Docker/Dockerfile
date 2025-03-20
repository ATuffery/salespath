# Utiliser une image de base contenant Java (comme OpenJDK)
FROM openjdk:17-jdk-slim

# Copier le fichier .jar dans le container
COPY /SalesPathAPI-0.0.1-SNAPSHOT.jar /app/SalesPathAPI-0.0.1-SNAPSHOT.jar

# Commande pour exécuter le fichier .jar
ENTRYPOINT ["java", "-jar", "/app/SalesPathAPI-0.0.1-SNAPSHOT.jar"]