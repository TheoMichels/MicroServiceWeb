FROM openjdk:11-jdk-slim
RUN addgroup --system spring 
RUN adduser --system --ingroup spring spring 
USER spring:spring
ARG JAR_FILE=target/authentifDemo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV SERVER_PORT=8082
ENTRYPOINT ["java","-jar","/app.jar", "--server.port=${SERVER_PORT}"]