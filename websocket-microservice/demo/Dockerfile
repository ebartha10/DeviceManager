FROM maven:3.9-amazoncorretto-21 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /websocket-microservice/pom.xml
WORKDIR /websocket-microservice
RUN mvn dependency:go-offline
COPY ./src/ /websocket-microservice/src
WORKDIR /websocket-microservice
RUN mvn clean install -DskipTests

FROM amazoncorretto:25
WORKDIR /websocket-microservice
COPY --from=build /websocket-microservice/target/demo-0.0.1-SNAPSHOT.jar /websocket-microservice/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
