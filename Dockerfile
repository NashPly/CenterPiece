FROM openjdk:11
COPY . /src/java/com/CenterPiece
WORKDIR ./src/java/com/CenterPiece
RUN cd src/java/com/CenterPiece dir
CMD ["java", "Main"]
