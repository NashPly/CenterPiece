# FROM openjdk:11
# COPY . /src/main/java/com/CenterPiece
# WORKDIR ./src/main/java/com/CenterPiece
# RUN javac src/main/java/com/CenterPiece/CenterPiece.java
# CMD ["java", "CenterPiece"]

FROM maven:3.6.3-sdk-17

COPY ./ ./

RUN mvn clean package

CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
