FROM openjdk:11
COPY . /src/java/com/CenterPiece
WORKDIR /src/java/com/CenterPiece
RUN javac CenterPiece.java
CMD ["java", "Main"]
