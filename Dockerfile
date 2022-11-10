# FROM openjdk:11
# COPY . /src/main/java/com/CenterPiece
# WORKDIR ./src/main/java/com/CenterPiece
# RUN javac src/main/java/com/CenterPiece/CenterPiece.java
# CMD ["java", "CenterPiece"]

FROM maven:3.8.6-openjdk-18 AS build

COPY src ./
COPY pom.xml ./
# RUN mvn -f pom.xml clean package
RUN mvn -f pom.xml install

FROM maven:3.8.6-openjdk-18 AS build
#FROM openjdk:18
COPY target/CenterPiece-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
#COPY --from=build target/CenterPiece-1.0-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

#ENTRYPOINT ["java", "-cp", "com.CenterPiece.CenterPiece"]
#CMD java -cp target/CenterPiece-1.0-SNAPSHOT.jar com.CenterPiece.CenterPiece
