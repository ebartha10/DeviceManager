FROM maven:3.9-amazoncorretto-21 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /auth-microservice/pom.xml
WORKDIR /auth-microservice
RUN mvn dependency:go-offline
COPY ./src/ /auth-microservice/src
WORKDIR /auth-microservice
RUN mvn clean install -DskipTests

FROM amazoncorretto:25
WORKDIR /auth-microservice
COPY --from=build /auth-microservice/target/demo-0.0.1-SNAPSHOT.jar /auth-microservice/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
