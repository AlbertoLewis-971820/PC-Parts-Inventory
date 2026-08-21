FROM maven:3.9-eclipse-temurin-21 as build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar pc-part-inventory-api.jar

EXPOSE 8080

CMD ["java", "-jar", "pc-part-inventory-api.jar"]