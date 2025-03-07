FROM openjdk:17
ADD target/*.jar applicationjar.jar
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar 
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-javaagent:/opentelemetry-javaagent.jar", "applicationjar.jar"]
