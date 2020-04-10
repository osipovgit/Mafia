FROM openjdk:8
ADD build/libs/spring-rest-service-0.1.0.jar spring-rest-service-0.1.0.jar
COPY src/main/resources/ src/main/resources/
EXPOSE 3647
ENTRYPOINT ["java", "-jar", "mafia-online-1.0.jar"]