# FROM openjdk:11
# COPY . /src/main/java/com/CenterPiece
# WORKDIR ./src/main/java/com/CenterPiece
# RUN javac src/main/java/com/CenterPiece/CenterPiece.java
# CMD ["java", "CenterPiece"]

FROM maven:3.8.6-openjdk-18 AS build
ADD target/CenterPiece-1.0-SNAPSHOT.jar CenterPiece-1.0-SNAPSHOT.jar
COPY pom.xml ./
ENTRYPOINT ["mvn", "exec:java","-d ./src/main/java/com/CenterPiece/" ,"-Dexec.mainClass=com.CenterPiece.CenterPiece"]
#COPY --from=build target/CenterPiece-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

#ENTRYPOINT ["java", "-cp", "com.CenterPiece.CenterPiece"]
#CMD java -cp target/CenterPiece-1.0-SNAPSHOT.jar com.CenterPiece.CenterPiece
