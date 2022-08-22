FROM openjdk:11
COPY . /src/main/java/com/CenterPiece
WORKDIR ./src/main/java/com/CenterPiece
RUN cd src/main/java/com/CenterPiece dir
# javac src/main/java/com/CenterPiece/CenterPiece.java
CMD ["java", "CenterPiece"]
