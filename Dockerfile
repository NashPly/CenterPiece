FROM openjdk:11
COPY . /src/java/com/CenterPiece
WORKDIR ./src/java/com/CenterPiece
RUN dir
CMD ["java", "Main"]
