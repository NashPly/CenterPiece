FROM openjdk:11
COPY . /src/main/java/com/CenterPiece
WORKDIR ./src/main/java/com/CenterPiece
RUN dir src/main/java/com/CenterPiece
# javac src/main/java/com/CenterPiece/CenterPiece.java
CMD ["java", "CenterPiece"]
