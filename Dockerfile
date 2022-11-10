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

FROM maven:3.8.6-openjdk-18

COPY --from=build target/CenterPiece-1.0-SNAPSHOT.jar com.CenterPiece.CenterPiece
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "com.CenterPiece.CenterPiece" "com.CenterPiece.CenterPiece"]
#CMD java -cp target/CenterPiece-1.0-SNAPSHOT.jar com.CenterPiece.CenterPiece
