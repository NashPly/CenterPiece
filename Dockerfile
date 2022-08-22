FROM openjdk:11
COPY . /src/java/com/CenterPiece
WORKDIR ./src/java/com/CenterPiece
RUN javac ./src/java/com/CenterPiece/CenterPiece.java
CMD ["java", "Main"]
