FROM maven:3.9-amazoncorretto-21 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /chat-microservice/pom.xml
WORKDIR /chat-microservice
RUN mvn dependency:go-offline
COPY ./src/ /chat-microservice/src
WORKDIR /chat-microservice
RUN mvn clean install -DskipTests

FROM amazoncorretto:25
WORKDIR /chat-microservice
COPY --from=build /chat-microservice/target/demo-0.0.1-SNAPSHOT.jar /chat-microservice/demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
