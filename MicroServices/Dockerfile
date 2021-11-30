FROM openjdk:11-jdk-slim
RUN addgroup --system spring 
RUN adduser --system --ingroup spring spring 
USER spring:spring
ARG JAR_FILE=target/MicroServices-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY production.properties /
ENV SERVER_PORT=8080
ENTRYPOINT ["java","-jar","/app.jar", "--spring.config.name=production", "--server.port=${SERVER_PORT}"]