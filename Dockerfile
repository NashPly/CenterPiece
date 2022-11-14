FROM maven:3.8.6-openjdk-18 AS build
COPY pom.xml ./
#COPY target/classes ./
RUN mvn clean install
ADD docker/CenterPiece-0.0.1-SNAPSHOT.jar center.jar
#ENTRYPOINT ["mvn", "exec:java"]
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/center.jar"]
