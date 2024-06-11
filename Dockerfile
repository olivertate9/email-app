FROM maven as build

WORKDIR /app

COPY pom.xml .

COPY .env .

COPY src ./src

RUN mvn clean package

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]