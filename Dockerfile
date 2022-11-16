FROM maven:3.8.6-openjdk-18 AS build
COPY pom.xml ./
RUN mvn install
ADD target/CenterPiece-0.0.1-SNAPSHOT.jar CenterPiece-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["mvn", "exec:java"]
#ENTRYPOINT ["java", "-jar", "target\CenterPiece-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["java", "-jar", "CenterPiece-0.0.1-SNAPSHOT.jar"]
#RUN java -jar target/CenterPiece-0.0.1-SNAPSHOT.jar
