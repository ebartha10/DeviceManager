FROM maven:3.9.9-eclipse-temurin-21 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /device-microservice/pom.xml
WORKDIR /device-microservice
RUN mvn dependency:go-offline
COPY ./src/ /device-microservice/src
WORKDIR /device-microservice
RUN mvn clean install -DskipTests

FROM openjdk:21-jdk
WORKDIR /device-microservice
COPY --from=build /device-microservice/target/demo-0.0.1-SNAPSHOT.jar /device-microservice/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
