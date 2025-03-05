FROM openjdk:17
ADD target/*.jar applicationjar.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "applicationjar.jar"]