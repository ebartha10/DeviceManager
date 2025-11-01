FROM maven:3.9.9-eclipse-temurin-21 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /user-microservice/pom.xml
WORKDIR /user-microservice
RUN mvn dependency:go-offline
COPY ./src/ /user-microservice/src
WORKDIR /user-microservice
RUN mvn clean install -DskipTests

FROM openjdk:21-jdk
WORKDIR /user-microservice
COPY --from=build /user-microservice/target/demo-0.0.1-SNAPSHOT.jar /user-microservice/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
