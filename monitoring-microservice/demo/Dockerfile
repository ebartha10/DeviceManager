FROM maven:3.9-amazoncorretto-21 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /monitoring-microservice/pom.xml
WORKDIR /monitoring-microservice
RUN mvn dependency:go-offline
COPY ./src/ /monitoring-microservice/src
WORKDIR /monitoring-microservice
RUN mvn clean install -DskipTests

FROM amazoncorretto:25
WORKDIR /monitoring-microservice
COPY --from=build /monitoring-microservice/target/demo-0.0.1-SNAPSHOT.jar /monitoring-microservice/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
