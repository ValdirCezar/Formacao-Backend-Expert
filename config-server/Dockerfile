FROM openjdk:21-jdk
ARG JAR_FILE=./build/libs/*.jar
EXPOSE 8888
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]