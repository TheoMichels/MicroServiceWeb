FROM openjdk:11-jdk-slim
RUN addgroup --system spring 
RUN adduser --system --ingroup spring spring 
USER spring:spring
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
#COPY production.properties /
ENV SERVER_PORT=8080
ENV PROFILE_URL=http://profile:8081
ENV AUTH_URL=http://auth:8082

ENTRYPOINT ["java","-jar","/app.jar", "--service.port=${SERVER_PORT}]", "--service.profile=${PROFILE_URL}", "--service.authentication=${AUTH_URL}"]