FROM openjdk:17
EXPOSE 8080
ADD target/spring-tickets.jar spring-tickets.jar
ENTRYPOINT ["java","-jar","/spring-tickets.jar"]